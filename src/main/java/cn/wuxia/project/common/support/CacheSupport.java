package cn.wuxia.project.common.support;

import cn.wuxia.common.spring.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;

/**
 * <pre>
 * 缓存工具类
 * 基于spring cache的公用方法
 * 使用memcacheClient也可达到相同效果
 * </pre>
 *
 * @author songlin.li
 * @since 2015-9-28
 */
public class CacheSupport {

    final static Logger logger = LoggerFactory.getLogger(CacheSupport.class);

    /**
     * 根据名称获取缓存
     *
     * @return
     * @author songlin
     */
    public static Cache getCache(String cacheName) {
        CacheManager ehcacheManager = null;
        try {
            ehcacheManager = getCacheManager();
        } catch (Exception e) {
            logger.warn("", e);
        }
        if (ehcacheManager != null) {
            return ehcacheManager.getCache(cacheName);
        }
        return null;
    }

    public static void cleanCache(String cacheName) {
        CacheManager ehcacheManager = null;
        try {
            ehcacheManager = getCacheManager();
        } catch (Exception e) {
            logger.warn("", e);
        }
        if (ehcacheManager != null) {
            Cache cache = ehcacheManager.getCache(cacheName.toString());
            cache.clear();
        }
    }

    /**
     * @return
     * @author songlin
     */
    private static CacheManager getCacheManager() throws Exception {
        return SpringContextHolder.getBean("cacheManager");
    }

    /**
     * 获取缓存对象（缓存2H）
     *
     * @param key
     * @return
     * @author songlin
     */
    public static Object get(String key) {
        return get(getCache(CacheConstants.CACHED_VALUE_2_HOUR), key);
    }

    /**
     * 获取缓存对象
     *
     * @param cacheName
     * @param key
     * @return
     * @author songlin
     */
    public static Object get(String cacheName, String key) {
        return get(getCache(cacheName), key);
    }

    /**
     * 获取缓存对象
     *
     * @param cache
     * @param key
     * @return
     * @author songlin
     */
    public static Object get(Cache cache, String key) {
        logger.debug("调用读取缓存：{},{}", cache.getName(), key);

        if (null != cache) {
            /**
             * 限制key的长度为250个字符
             */
            ValueWrapper element = cache.get(key);
            if (element == null) {
                return null;
            }
            Object value = element.get();
            logger.debug("get Out Cache and key is :" + key + " , value is {}", value);
            return value;
        } else {
            logger.error("无法获取baseCache或其他名字的Cache。Cache失效！！！！");
            return null;
        }
    }

    /**
     * 将对象写入内存(2小时)
     *
     * @param key
     * @param value
     * @author songlin
     */
    public static void set(String key, Object value) {
        set(getCache(CacheConstants.CACHED_VALUE_2_HOUR), key, value);
    }

    /**
     * 将对象写入内存
     *
     * @param cacheName
     * @param key
     * @param value
     * @author songlin
     */
    public static void set(String cacheName, String key, Object value) {
        set(getCache(cacheName), key, value);
    }

    /**
     * 将对象写入内存
     *
     * @param cache
     * @param key
     * @param value
     * @author songlin
     */
    public static void set(Cache cache, String key, Object value) {
        logger.debug("调用写入缓存：{}, {}", cache.getName(), key);

        if (cache != null && value != null) {
            //将旧的删除再新增
            cache.evict(key);
            cache.put(key, value);
            logger.debug("put In Cache and key is :" + key + " , value is :" + value);
        } else {
            logger.error("无法获取baseCache或其他名字的Cache。Cache失效！！！！");
        }
    }

    /**
     * 移除某个缓存对象
     *
     * @param key
     * @author songlin
     */
    public static void del(String key) {
        del(getCache(CacheConstants.CACHED_VALUE_2_HOUR), key);
    }

    /**
     * 移除某个缓存对象
     *
     * @param cacheName
     * @param key
     * @author songlin
     */
    public static void del(String cacheName, String key) {
        del(getCache(cacheName), key);
    }

    /**
     * 移除某个缓存对象
     *
     * @param key
     * @author songlin
     */
    public static void del(Cache cache, String key) {
        logger.debug("调用删除缓存：{},{}", cache.getName(), key);

        if (cache != null) {
            logger.debug("delete Cache and key is :" + key);
            cache.evict(key);
        } else {
            logger.error("无法获取baseCache或其他名字的Cache。Cache失效！！！！");
        }
    }
}
