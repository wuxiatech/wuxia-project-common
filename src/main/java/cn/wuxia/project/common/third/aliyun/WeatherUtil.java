package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.aliyun.components.ApiAccount;
import cn.wuxia.common.util.MapUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpAction;
import cn.wuxia.common.web.httpclient.HttpClientMethod;
import cn.wuxia.project.common.open.AppApiException;
import cn.wuxia.project.common.third.aliyun.bean.WeatherCity;
import cn.wuxia.project.common.third.aliyun.bean.WeatherInfo;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 天气预报
 */
public class WeatherUtil extends BaseMarketApi {

    public WeatherUtil(ApiAccount apiAccount) {
        super(apiAccount);
    }

    public WeatherUtil(String appCode) {
        super(appCode);
    }

    public static WeatherUtil build(String appcode) {
        return new WeatherUtil(appcode);
    }


    public HttpAction queryAction = HttpAction.Action("http://jisutqybmf.market.alicloudapi.com/weather/query", HttpClientMethod.GET);

    /**
     * 全国3000多个省市的实时天气预报，未来7天、未来24小时天气，穿衣、运动、洗车、感冒、空气污染扩散、紫外线等指数查询接口，
     * 可按地名、经纬度、IP查询，30分钟更新一次，中国气象局权威数据。（接口流量限制情况（每个用户ID）：30次流控/分钟，接口还有总流控限制。）
     */
    public WeatherInfo query(WeatherCity weatherCity) throws AppApiException {

        String resultJson = super.send(queryAction, new HashMap<String, String>(1) {{
            if (StringUtil.isNotBlank(weatherCity.getCitycode())) {
                put("citycode", weatherCity.getCitycode());
            } else if (StringUtil.isNotBlank(weatherCity.getCity())) {
                put("city", weatherCity.getCity());
            } else if (StringUtil.isNotBlank(weatherCity.getCityid())) {
                put("cityid", weatherCity.getCityid());
            }
        }});
        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        if (!StringUtil.equalsIgnoreCase("ok", MapUtil.getString(jsonObject, "msg"))) {
            throw new AppApiException(MapUtil.getString(jsonObject, "msg"));
        }
        jsonObject = jsonObject.getJSONObject("result");
        return jsonObject.toJavaObject(WeatherInfo.class);
    }

    public WeatherInfo query(String ip) throws AppApiException {
        String resultJson = super.send(queryAction, new HashMap<String, String>(1) {{
            put("ip", ip);
        }});
        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        if (!StringUtil.equalsIgnoreCase("ok", MapUtil.getString(jsonObject, "msg"))) {
            throw new AppApiException(MapUtil.getString(jsonObject, "msg"));
        }
        jsonObject = jsonObject.getJSONObject("result");
        return jsonObject.toJavaObject(WeatherInfo.class);
    }


    public HttpAction queryCityAction = HttpAction.Action("http://jisutqybmf.market.alicloudapi.com/weather/city", HttpClientMethod.GET);

    /**
     * 获取城市列表
     *
     * @return
     */
    public List<WeatherCity> queryCity() throws AppApiException {
        String resultJson = super.send(queryCityAction, new HashMap<>(0));
        JSONObject jsonObject = JSONObject.parseObject(resultJson);
        if (!StringUtil.equalsIgnoreCase("ok", MapUtil.getString(jsonObject, "msg"))) {
            throw new AppApiException(MapUtil.getString(jsonObject, "msg"));
        }
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        return jsonArray.toJavaList(WeatherCity.class);
    }


}
