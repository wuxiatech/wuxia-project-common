package cn.wuxia.project.common.support;

import cn.wuxia.common.lock.RedissonDistributedLock;
import cn.wuxia.common.util.PropertiesUtils;
import cn.wuxia.common.util.StringUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.Properties;

@Configuration
//@EnableRedissonHttpSession
@Conditional(RedissonSpringCacheManager.InitRedissonCondition.class)
@EnableCaching
@Slf4j
public class RedissonSpringCacheManager {

    @Value("${redis.hosts:127.0.0.1:6379}")
    private String hosts;
    @Value("${redis.auth:}")
    private String password;
    @Value("${redis.dbIndex:0}")
    private int dbIndex;


    @Bean(destroyMethod = "shutdown")
    public RedissonClient getSingleRedisson() {
        Config config = new Config();
        //实例化redisson
        config.useSingleServer().setAddress("redis://" + hosts).setPassword(password)
                .setDatabase(dbIndex)
                .setConnectTimeout(100)
                .setTimeout(200)
                .setConnectionPoolSize(8)
                .setConnectionMinimumIdleSize(5)
                .setTcpNoDelay(true)
                .setPingConnectionInterval(30000)
                .setKeepAlive(true)
                .setRetryInterval(50);
        //得到redisson对象
        RedissonClient redisson = null;
        try {
            redisson = Redisson.create(config);
            log.warn("初始化Redisson成功， {}", hosts);
        } catch (Exception e) {
            log.warn("初始化Redisson出错，{}, {}", hosts, e.getMessage());
        }
        //可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
        return redisson;
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient getClusterRedisson() {
        String[] nodes = hosts.split(",");
        //redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = "redis://" + nodes[i];
        }
        RedissonClient redisson = null;
        Config config = new Config();
        config.useClusterServers() //这是用的集群server
                .setScanInterval(2000) //设置集群状态扫描时间
                .addNodeAddress(nodes)
                .setPassword(password)
                .setConnectTimeout(100)
                .setTimeout(200)
                .setTcpNoDelay(true)
                .setPingConnectionInterval(30000)
                .setKeepAlive(true)
                .setRetryInterval(50);
        try {
            redisson = Redisson.create(config);
            log.warn("初始化ClusterRedisson成功， {}", nodes);
        } catch (Exception e) {
            log.warn("初始化ClusterRedisson出错，{}, {}", nodes, e.getMessage());
        }
        //可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
        return redisson;
    }


    @Bean
    CacheManager cacheManager() {
        Map<String, CacheConfig> config = Maps.newHashMap();
        config.put(CacheConstants.CACHED_VALUE_1_DAY, new CacheConfig(24 * 60 * 60 * 1000, 12 * 60 * 60 * 1000));
        config.put(CacheConstants.CACHED_VALUE_4_HOUR, new CacheConfig(4 * 60 * 60 * 1000, 2 * 60 * 60 * 1000));
        config.put(CacheConstants.CACHED_VALUE_2_HOUR, new CacheConfig(2 * 60 * 60 * 1000, 60 * 60 * 1000));
        config.put(CacheConstants.CACHED_VALUE_1_HOUR, new CacheConfig(1 * 60 * 60 * 1000, 30 * 60 * 1000));
        config.put(CacheConstants.CACHED_VALUE_30_MINUTES, new CacheConfig(30 * 60 * 1000, 15 * 60 * 1000));
        config.put(CacheConstants.CACHED_VALUE_10_MINUTES, new CacheConfig(10 * 60 * 1000, 5 * 60 * 1000));
        config.put(CacheConstants.CACHED_VALUE_2_MINUTES, new CacheConfig(2 * 60 * 1000, 1 * 60 * 1000));
        return new org.redisson.spring.cache.RedissonSpringCacheManager(getSingleRedisson(), config);
    }


    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     *
     * @return
     */
    @Bean
    public RedissonDistributedLock distributedLocker() {
        RedissonDistributedLock locker = new RedissonDistributedLock(getSingleRedisson());
        return locker;
    }

    public static class InitRedissonCondition implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            try {
                Properties properties = PropertiesUtils.loadProperties("classpath:properties/redis.properties", "classpath:redis.properties");
                if (properties.isEmpty()) {
                    return false;
                } else if (StringUtil.isNotBlank(properties.getProperty("redis.host"))) {
                    return true;
                } else {
                    return false;
                }
//                Class clazz = ClassLoaderUtil.loadClass("");
            } catch (Exception e) {
                return false;
            }
        }
    }
}
