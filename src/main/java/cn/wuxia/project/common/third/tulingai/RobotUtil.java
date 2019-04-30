package cn.wuxia.project.common.third.tulingai;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import cn.wuxia.project.common.third.tulingai.bean.QABean;
import cn.wuxia.project.common.third.tulingai.bean.QAQueryResultBean;
import cn.wuxia.project.common.third.tulingai.bean.v2requ.V2RequestBean;
import cn.wuxia.project.common.third.tulingai.bean.v2resp.V2ResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.MapUtil;
import cn.wuxia.common.util.PropertiesUtils;
import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.common.web.httpclient.HttpClientUtil;

public class RobotUtil {
    protected static Logger logger = LoggerFactory.getLogger(RobotUtil.class);

    static Properties prop = PropertiesUtils.loadProperties("classpath:/tulingai.config.properties");

    static String address = prop.getProperty("api.address");

    static String secret = prop.getProperty("api.secret");

    static String key = prop.getProperty("api.key");

    static String addQAAddr = "http://www.tuling123.com/v1/setting/importfaq";

    static String queryQAAddr = "http://www.tuling123.com/v1/setting/selectfaq";

    static String modifyQAAddr = "http://www.tuling123.com/v1/setting/updatefaq";

    public static void post(String info, String userid) {
        Map<String, String> param = Maps.newHashMap();
        param.put("key", key);
        param.put("info", info);
        param.put("userid", userid);

        post(address, param);
    }

    /**
     * 根据一个问题，得到相应的回答
     * 
     * @param question
     * @param userid
     * @return
     */
    public static V2ResponseBean getAnswer(String question, String userid) {
        HttpClientRequest request = new HttpClientRequest(address);
        V2RequestBean reqBean = new V2RequestBean(key, userid, question);

        Map<String, Object> map = post(address, reqBean);
        V2ResponseBean respBean = cn.wuxia.common.util.reflection.BeanUtil.mapToBean(map, V2ResponseBean.class);
        logger.debug("转换为对象结果:{}", respBean);
        return respBean;
    }

    public static void addQA(QABean qa) {
        addQA(Lists.newArrayList(qa));
    }

    public static void addQA(List<QABean> qas) {
        if (ListUtil.isEmpty(qas))
            return;
        Map<String, Object> param = Maps.newHashMap();
        param.put("apikey", key);
        param.put("data", qas);

        Map<String, Object> map = post(addQAAddr, param);
        if (MapUtil.getInteger(map, "code") != 0) {
            throw new TulingAIException("新增问题不成功：" + map.get("data").toString());
        }
    }

    /**
     * <pre>
     * 
     *              
    	"code": 0,
    	"data": {
    	"totalCount": 2,
    	"knowledgeCount": 2,
    	"knowledgeList": [
    	{
    	"id": "2",
    	"question": "测试问题2",
    	"answer": "新的答案2",
    	"time": "2015-12-03 14:34:36"
    	},
    	{
    	"id": "1",
    	"question": "测试问题1",
    	"answer": "新的答案1",
    	"time": "2015-12-03 14:34:35"
    	}
    	]
    	}
    	}
     * </pre>
     * 
     * @param keyword
     */
    public static List<QAQueryResultBean> queryQA(String keyword) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("apikey", key);

        Map<String, Object> page = Maps.newHashMap();
        page.put("pageNumber", 1);
        page.put("pageSize", 10);
        page.put("searchBy", keyword);

        Map<String, Object> p = Maps.newHashMap();
        p.put("pages", page);

        param.put("data", p);
        Map<String, Object> result = post(queryQAAddr, param);
        List<QAQueryResultBean> returnResult = Lists.newArrayList();
        if (MapUtil.getInteger(result, "code") == 0) {
            Map r = MapUtil.getMap(result, "data");
            List<Map> l = (List<Map>) MapUtil.getObject(r, "knowledgeList");
            returnResult = ListUtil.copyProperties(QAQueryResultBean.class, l);
        }
        logger.debug("转换对象结果为:{}", returnResult);
        return returnResult;
    }

    /**
     * 修改一条问题
     * 
     * @param qa
     */
    public static void modifyQA(QABean qa) {
        modifyQA(Lists.newArrayList(qa));
    }

    /**
     * 修改多条问题
     * 
     * @param qas
     */
    public static void modifyQA(List<QABean> qas) {
        Map<String, Object> param = Maps.newHashMap();
        param.put("apikey", key);
        param.put("data", qas);

        Map<String, Object> result = post(modifyQAAddr, param);
        if (MapUtil.getInteger(result, "code") != 0) {
            throw new TulingAIException("修改问题结果错误：" + result.get("data").toString());
        }
    }

    /**
     * 
     * @param queryAddress
     * @param param
     * @return
     */
    private static Map post(String queryAddress, Object param) {
        HttpClientResponse response;
        try {
            response = HttpClientUtil.postJson(queryAddress, JsonUtil.toJson(param));
        } catch (HttpClientException e) {
            throw new RuntimeException(e);
        }
        response.setCharset("UTF-8");
        Map map = JsonUtil.fromJson(response.getStringResult());
        logger.debug("执行返回结果为：{}", map);
        return map;
    }
}
