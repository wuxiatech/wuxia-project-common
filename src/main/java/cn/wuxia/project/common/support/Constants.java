/*
 * Created on :Jun 25, 2013 Author :PL Change History Version Date Author Reason
 * <Ver.No> <date> <who modify> <reason>
 */
package cn.wuxia.project.common.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.wuxia.common.util.StringUtil;

public class Constants extends cn.wuxia.common.Constants {

    public final static String USER_DIR = "user.dir";

    public final static String UTF_8 = "UTF-8";

    public final static String IMG_GIF = "GIF";

    public final static String IMG_gif = "gif";

    /**
     *
     */
    public final static String HEADER_APPID_NAME = "wx-appid";

    /**
     *
     */
    public final static String HEADER_PLATFORM_NAME = "X-PLATFORM-NAME";

    /**
     * 在application-*cache.xml定义，为1个小时缓存
     * @see {@link CacheConstants}
     */
    @Deprecated
    public final static String CACHED_VALUE_BASE = CacheConstants.CACHED_VALUE_1_HOUR;

    /**
     *  @see {@link CacheConstants}
     */
    @Deprecated
    public final static String CACHED_KEY_DEFAULT = CacheConstants.CACHED_KEY_DEFAULT;

    /**
     * 邮箱正则表达式
     * 
     * @author songlin
     */
    public final static String EMAIL_PATTERN = "([a-zA-Z0-9]+[_|_|.|-]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.|-]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,3}";

    /**
     * 电话正则表达式
     * 
     * @author songlin
     */
    public final static String TELEPHONE_PATTERN = "((\\d{3,4})|\\d{3,4}-)?\\d{7,8}(-\\d+)*";

    /**
     * 手机正则表达式
     * 
     * @author songlin
     */
    public final static String MOBILE_PATTERN = "^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";

    /**
     * 密码正则表达式
     * 
     * @author songlin
     */

    public final static String PASSWORD_PATTERN = "[a-zA-z0-9\u4E00-\u9FA5]*$";

    /**
     * 管理员用户id
     */
    public final static Long ADMINUSERID = 1l;

    /**
     * 登录验证码的key
     */
    public final static String LOGIN_VERIFY_KEY = "login_captcha";

    /**
     * 登录错误次数key
     */
    public final static String LOGIN_SEESION_COUNT = "loginErrorNotFoundCount";

    /**
     * 登录时保存用户名
     */
    public final static String LOGIN_SESSION_NAME = "login_failure_name";

    public static void main(String[] args) {
        Pattern p = Pattern.compile(Constants.MOBILE_PATTERN);
        Matcher match = p.matcher(StringUtil.trimBlank("13003891917"));
        System.out.println(match.matches());
    }
}
