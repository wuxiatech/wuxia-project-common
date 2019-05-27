package cn.wuxia.project.common.open.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import cn.wuxia.project.common.api.ApiResponseBean;
import cn.wuxia.project.common.api.RequestBean;
import cn.wuxia.project.common.open.AppApiException;
import cn.wuxia.common.mapper.JacksonMapper;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.common.web.sign.SignHttpUtil;

/**
 * 本util用户请求 api接口的参数签名
 * 
 * @author songlin
 *
 */
public class ApiUtil {
    protected final static Logger logger = LoggerFactory.getLogger(ApiUtil.class);

    /**
     * 推送到api拿到, 注意:该方法只接受json为参数
     * 
     * @author songlin
     * @param bean
     * @return
     * @throws Exception
     */
    public static ApiResponseBean post2Gateway(RequestBean requestBean, String gateway, Map<String, String> headers, String appkey, String secret)
            throws AppApiException {
        Assert.notNull(gateway, "gateway地址不能为空");
        Assert.notNull(appkey, "APP_KEY不能为空");
        Assert.notNull(secret, "APP_SECRET不能为空");
        logger.info("========" + gateway);
        String[] url = StringUtil.split(gateway, "/", 3);
        String host = url[0] + "//" + url[1];
        String path = "";
        if (url.length >= 3) {
            path = "/" + url[2];
        }

        HttpClientResponse repos;
        try {
            repos = SignHttpUtil.httpPost(host, path, headers, null, requestBean.toMap(), null, appkey, secret);
            ApiResponseBean callbackBean = JacksonMapper.nonEmptyMapper().getMapper().readValue(repos.getByteResult(), ApiResponseBean.class);
            if (callbackBean.isok()) {
                return callbackBean;
            } else {
                throw new AppApiException(callbackBean.getMessage());
            }
        } catch (Exception e) {
            logger.error("接口异常", e);
            throw new AppApiException(e);
        }
    }

}
