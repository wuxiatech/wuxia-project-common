package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.MapUtil;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.project.common.third.aliyun.bean.IpAdress;
import cn.wuxia.project.common.third.ip.IPSeeker;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

public class IpSeekerUtil extends ApiConstants {

    public IpSeekerUtil() {
        super("5f68fba5e70843f6961d70f34f6b1f5b");
    }

    public static IpAdress getAdress(@NotBlank String ip) {
        String url = "http://api01.aliyun.venuscn.com/ip";
        String[] ips = StringUtil.split(ip, ",");
        if (ArrayUtils.isNotEmpty(ips) & ips.length > 1) {
            ip = StringUtil.trimBlank(ips[1]);
        }
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("ip", ip);
        String result = new IpSeekerUtil().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "ret") == 200) {
                map = MapUtil.getMap(map, "data");
                logger.debug("{}", map);
                if (MapUtil.isNotEmpty(map)) {
                    return MapUtil.mapToBean(map, IpAdress.class);
                }
            }
        }
        return null;
    }

    public static IpAdress ip2location(@NotBlank String ip) {
        try {
            String[] ips = StringUtil.split(ip, ",");
            if (ArrayUtils.isNotEmpty(ips) & ips.length > 1) {
                ip = StringUtil.trimBlank(ips[1]);
            }
            String url = "http://ip.ws.126.net/ipquery?ip=" + ip;
            HttpClientResponse response = HttpClientRequest.get(url).setConnectionTimeout(2000).execute();
            String responseString = response.getStringResult();
            responseString = StringUtil.substringBetween(responseString, "{", "}");
            if (StringUtil.isNotBlank(responseString)) {
                responseString = "{" + responseString + "}";
                JSONObject  jsonObject = JSONObject.parseObject(responseString);
                IpAdress ipAdress = new IpAdress();
                ipAdress.setCity(MapUtils.getString(jsonObject, "city"));
                ipAdress.setRegion(MapUtils.getString(jsonObject, "province"));
                if (ipAdress.isEmpty())
                    return null;
                return ipAdress;
            }
        } catch (Exception e) {
            logger.error("获取ip={}地理位置失败, {}", ip, e.getMessage());
        }
        return getAdress(ip);
    }

    public static IpAdress getIpAdressByLocal(String ip) {
        try {
            String[] ips = StringUtil.split(ip, ",");
            if (ArrayUtils.isNotEmpty(ips) & ips.length > 1) {
                ip = StringUtil.trimBlank(ips[1]);
            }
            IPSeeker seeker = IPSeeker.getInstance();
            String s = seeker.getAddress(ip);
            System.out.println(s);
            if (StringUtil.isNotBlank(s)) {
                String[] address = StringUtil.split(s, " ");
                IpAdress ipLocationBean = new IpAdress();
                if (StringUtil.indexOf(s, "市") > 0) {
                    if (StringUtil.indexOf(s, "省") > 0) {
                        ipLocationBean.setRegion(StringUtil.substringBefore(s, "省") + "省");
                        ipLocationBean.setCity(StringUtil.substringBetween(s, "省", "市") + "市");
                    } else {
                        ipLocationBean.setCity(StringUtil.substringBefore(s, "市") + "市");
                    }
                } else {
                    ipLocationBean.setCountry(StringUtil.substringBefore(s, " "));
                }
                if (address.length > 0) {
                    ipLocationBean.setIsp(StringUtil.substringAfter(s, " "));
                }
                ipLocationBean.setArea(s);
                return ipLocationBean;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getAdress("10.43.18.146, 58.22.117.10").getCity());
//        System.out.println(ip2location("10.43.18.146, 58.22.117.10").getCity());
//        System.out.println(getIpAdressByLocal("10.43.18.146, 58.22.117.10"));
        //healthDetail("20951");
    }
}
