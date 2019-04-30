/*
 * Created on :Sep 10, 2012 Author :songlin.li
 */
package cn.wuxia.project.common.support;

import cn.wuxia.common.spring.SpringContextHolder;
import cn.wuxia.common.spring.support.LogAspect;
import cn.wuxia.common.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * statistics service method spend time
 * 
 * @author songlin.li @ Version : V<Ver.No> <Sep 10, 2012>
 */
public class CustomLogAspect extends LogAspect {

    public Object invoke(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = super.invoke(joinPoint);
        long costTime = System.currentTimeMillis() - startTime;
        if (costTime >= 10 * 1000) {
            saveSystemStatistics(joinPoint.toShortString(), costTime / 1000, joinPoint.toLongString());
        }
        return result;
    }

    private void saveSystemStatistics(final String methodName, final long costTime, final String remark) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String name = StringUtil.substringAfter(methodName, "execution(");
                    name = StringUtil.substringBeforeLast(name, ")");
                    String rm = StringUtil.substringAfter(remark, "execution(");
                    rm = StringUtil.substringBeforeLast(rm, ")");
                    JdbcTemplate jdbcTemplate = SpringContextHolder.getBean("jdbcTemplate");
                    String sql = "INSERT INTO sys_cost_time_statistics ( NAME, cost_time, ip, access_datetime, type, remark ) VALUES (?, ?, ?, ?, ?, ?);";
                    jdbcTemplate.update(sql, new Object[] { name, costTime, getIp(), new Date(), 1, rm });
                } catch (Exception e) {
                }
            }
        });
        thread.start();
    }

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
