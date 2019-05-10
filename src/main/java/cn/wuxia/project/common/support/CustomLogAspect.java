/*
 * Created on :Sep 10, 2012 Author :songlin.li
 */
package cn.wuxia.project.common.support;

import cn.wuxia.common.spring.SpringContextHolder;
import cn.wuxia.common.spring.support.LogAspect;
import cn.wuxia.common.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * statistics service method spend time
 *
 * @author songlin.li @ Version : V<Ver.No> <Sep 10, 2012>
 */
public class CustomLogAspect extends LogAspect {

    final AsyncTaskManager asyncTaskManager = AsyncTaskManager.build("logtime");

    JdbcTemplate jdbcTemplate;

    MongoTemplate mongoTemplate;

    final String sql = "INSERT INTO sys_cost_time_statistics ( NAME, cost_time, ip, access_datetime, type, remark ) VALUES (?, ?, ?, ?, ?, ?);";

    @Override
    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = super.invoke(joinPoint);
        long costTime = System.currentTimeMillis() - startTime;
        if (costTime >= 2 * 1000) {
            saveSystemStatistics(joinPoint.toShortString(), costTime / 1000, joinPoint.toLongString());
        }
        return result;
    }

    private void saveSystemStatistics(final String methodName, final long costTime, final String remark) {
        String ip = getIp();
        if (mongoTemplate == null) {
            try {
                mongoTemplate = SpringContextHolder.getBean("mongoTemplate");
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        if (jdbcTemplate == null && mongoTemplate == null) {
            try {
                jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }
        asyncTaskManager.getExecutor().execute(() -> {
            try {
                String name = StringUtil.substringAfter(methodName, "execution(");
                name = StringUtil.substringBeforeLast(name, ")");
                String rm = StringUtil.substringAfter(remark, "execution(");
                rm = StringUtil.substringBeforeLast(rm, ")");
                if (mongoTemplate != null) {
                    Map map = new HashMap<String, Object>(5);
                    map.put("name", name);
                    map.put("cost_time", costTime);
                    map.put("ip", ip);
                    map.put("access_datetime", new Date());
                    map.put("method", rm);
                    mongoTemplate.insert(map, "statistics_cost_time");
                } else if (jdbcTemplate != null) {
                    jdbcTemplate.update(sql, new Object[]{name, costTime, ip, new Date(), 1, rm});
                }
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        });
    }

    @Override
    public String getIp() {
        try {
            if (RequestContextHolder.getRequestAttributes() != null) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                if (request != null) {
                    return request.getRemoteAddr();
                }
            }
            // Class<?> springSecurityUtils =
            // ClassLoaderUtil.loadClass("cn.daoming.common.security.SpringSecurityUtils");
            // Object obj = springSecurityUtils.newInstance();
            // Object ipObj = ClassUtil.invokeMethod(obj, "getCurrentUserIp",
            // null);
            // return "[" + ipObj + "]";
            return "";
        } catch (Exception e) {

        }
        return "";
    }
}
