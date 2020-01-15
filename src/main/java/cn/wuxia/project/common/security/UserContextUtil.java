package cn.wuxia.project.common.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class UserContextUtil {
    private static Logger logger = LoggerFactory.getLogger(UserContextUtil.class);

    protected final static String CURRENT_SESSION_WX_USER = "CURRENT_SESSION_USER:";

    public static void saveUserContext(UserContext uc) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            logger.warn("非浏览器请求");
            return;
        }
        ra.setAttribute(CURRENT_SESSION_WX_USER, uc, RequestAttributes.SCOPE_SESSION);

        logger.debug("登录{}", uc);
    }

    public static UserContext getUserContext() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            logger.warn("非浏览器请求");
            return null;
        }
        UserContext uc = (UserContext) ra.getAttribute(CURRENT_SESSION_WX_USER, RequestAttributes.SCOPE_SESSION);
        return uc;
    }

    public static void removeUserContext() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            logger.warn("非浏览器请求");
            return;
        }
        ra.removeAttribute(CURRENT_SESSION_WX_USER, RequestAttributes.SCOPE_SESSION);
        if (getUserContext() == null) {
            logger.info("清除成功");
        }
    }

    public static String getName() {
        return getUserContext() == null ? "" : getUserContext().getName();
    }

    public static String getId() {
        return getUserContext() == null ? "" : getUserContext().getId();
    }



    public static String getMobile() {
        return getUserContext() == null ? "" : getUserContext().getMobile();
    }


}
