package cn.wuxia.project.common.third.baidu;

/**
 * 百度api账号  开发者配置
 * [ticket id]
 * Description of the class
 *
 * @author songlin.li
 * @ Version : V<Ver.No> <2016年3月31日>
 */
public class OpenAccount {

    private String appid;

    private String appSecret;

    private String appKey;

    public OpenAccount(String appid, String appKey, String appSecret) {
        this.appid = appid;
        this.appKey = appKey;
        this.appSecret = appSecret;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }


    public String getAccessToken() {
        return TokenUtil.getAccessToken(this);
    }


}
