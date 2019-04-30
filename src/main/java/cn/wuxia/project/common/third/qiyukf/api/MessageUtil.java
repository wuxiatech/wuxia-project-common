package cn.wuxia.project.common.third.qiyukf.api;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import cn.wuxia.project.common.third.qiyukf.session.model.ApplyStaffInfo;
import cn.wuxia.project.common.third.qiyukf.session.model.ApplyStaffResult;
import cn.wuxia.project.common.third.qiyukf.session.model.CommonResult;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.wuxia.project.common.third.qiyukf.bean.InMessage;
import cn.wuxia.common.util.PropertiesUtils;

/**
 * Created by zhoujianghua on 2016/10/20.
 */
public class MessageUtil {
    protected static Logger logger = LoggerFactory.getLogger(MessageUtil.class);

    static Properties prop = PropertiesUtils.loadProperties("classpath:/qiyukf.config.properties");

    public static final String QIYU_APP_SECRET = prop.getProperty("api.appsecret");

    private static final String QIYU_APP_KEY = prop.getProperty("api.appkey");

    private static MessageClient client;
    
    //ExecutorService cachedThreadPool = Executors.newCachedThreadPool(); 
    static {
        client = new MessageClient(MessageUtil.QIYU_APP_KEY, MessageUtil.QIYU_APP_SECRET);
    }

    public static ApplyStaffResult applyStaff(ApplyStaffInfo staffInfo) throws IOException {
        return client.applyStaff(staffInfo);
    }

    public static CommonResult send(InMessage inMessage) throws IOException {
        return client.send(inMessage);
    }

    /**
     * 更新用户信息
     * @author songlin
     * @param openId
     * @param wxUser
     */
    public static void updateUser(JSONObject wxUser) {
        JSONArray crm = new JSONArray();
        String openId = wxUser.getString("openid");
        crm.add(item(null, "real_name", wxUser.getString("nickname")));
        crm.add(item("性别", "sex", wxUser.getString("sex")));
        crm.add(item("地址", "addr", wxUser.getString("province") + "-" + wxUser.getString("city")));
        crm.add(item("备注", "remark", wxUser.getString("remark")));
        int i = 0;
        for (Map.Entry<String, Object> k : wxUser.entrySet()) {
            JSONObject item = new JSONObject();
            item.put("index", i++);
            item.put("key", k.getKey());
            item.put("value", k.getValue());
            item.put("label", k.getKey());
            crm.add(item);
        }
        try {
            CommonResult result = client.updateCrmInfo(openId, crm);
            logger.debug("update crm " + openId + " result: " + result);
        } catch (Exception e) {
            logger.debug("update crm error: " + e);
        }
    }

    private static JSONObject item(String label, String key, String value) {
        JSONObject item = new JSONObject();
        item.put("key", key);
        item.put("value", value);
        if (!TextUtils.isEmpty(label)) {
            item.put("label", label);
        }
        return item;
    }
}
