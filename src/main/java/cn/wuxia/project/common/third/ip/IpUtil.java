package cn.wuxia.project.common.third.ip;

import cn.wuxia.common.util.StringUtil;
import cn.wuxia.common.web.httpclient.HttpClientRequest;
import cn.wuxia.common.web.httpclient.HttpClientResponse;
import cn.wuxia.project.common.third.ip.bean.IpAdress;
import cn.wuxia.project.common.third.ip.chunzhen.IPSeeker;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import javax.validation.constraints.NotBlank;

@Slf4j
public class IpUtil {

    public static IpAdress by126(@NotBlank String ip) {
        try {
            String[] ips = StringUtil.split(ip, ",");
            if (ArrayUtils.isNotEmpty(ips) & ips.length > 1) {
                ip = StringUtil.trimBlank(ips[1]);
            }
            String url = "http://ip.ws.126.net/ipquery?ip=" + ip;
            HttpClientResponse response = HttpClientRequest.get(url)
                    .setConnectionTimeout(2000).execute();
            String responseString = response.getStringResult();
            responseString = StringUtil.substringBetween(responseString, "{", "}");
            if (StringUtil.isNotBlank(responseString)) {
                responseString = "{" + responseString + "}";
                JSONObject jsonObject = JSONObject.parseObject(responseString);
                IpAdress ipAdress = new IpAdress();
                ipAdress.setCity(MapUtils.getString(jsonObject, "city"));
                ipAdress.setRegion(MapUtils.getString(jsonObject, "province"));
                if (!ipAdress.isEmpty()) {
                    return ipAdress;
                }
            }
        } catch (Exception e) {
            log.error("获取ip={}地理位置失败, {}", ip, e.getMessage());
        }
        return byLocal(ip);
    }

    /**
     * 20190909不能继续访问，放弃该方法
     * @param ip
     * @return
     */
    @Deprecated
    public static IpAdress bytool(@NotBlank String ip) {
        try {
            String[] ips = StringUtil.split(ip, ",");
            if (ArrayUtils.isNotEmpty(ips) & ips.length > 1) {
                ip = StringUtil.trimBlank(ips[1]);
            }
            String url = "https://tool.lu/ip/ajax.html?ip=" + ip;
            HttpClientResponse response = HttpClientRequest.post(url).setConnectionTimeout(2000).execute();
            String responseString = response.getStringResult();
            /**
             * {"text":{"tb_location":"","ip":"103.27.26.245","location":"广东省 广电网络","l":1729829621,"ip2region_location":"中国 广东省 广州市 广东广电","ipip_location":"中国 广东  -"},"message":"","status":true}
             */
            JSONObject jsonObject = JSONObject.parseObject(responseString);
            if (jsonObject != null && MapUtils.getBooleanValue(jsonObject, "status")) {
                jsonObject = jsonObject.getJSONObject("text");
                IpAdress ipAdress = new IpAdress();
                String tb_location = MapUtils.getString(jsonObject, "tb_location");
                String location = MapUtils.getString(jsonObject, "location");
                String ip2region_location = MapUtils.getString(jsonObject, "ip2region_location");
                String ipip_location = MapUtils.getString(jsonObject, "ipip_location");

                String province = "", city = "", isp = "";
                if (StringUtil.isNotBlank(ip2region_location)) {
                    String[] datas = StringUtil.split(ip2region_location, " ");
                    province = datas.length > 1 ? datas[1] : "";
                    city = datas.length > 1 ? datas[2] : "";
                    isp = datas.length > 2 ? datas[3] : "";
                }
                if (StringUtil.isBlank(city) && StringUtil.isNotBlank(ipip_location)) {
                    String[] datas = StringUtil.split(ipip_location, " ");
                    province = datas.length > 1 ? datas[1] : province;
                    city = datas.length > 1 ? datas[2] : city;
                    isp = datas.length > 2 ? datas[3] : isp;
                }
                if (StringUtil.isBlank(city) && StringUtil.isNotBlank(location)) {
                    String[] datas = StringUtil.split(location, " ");
                    province = datas.length > 1 ? StringUtil.substringBefore(datas[0], "省") : province;
                    city = datas.length > 1 ? StringUtil.substringBetween(datas[0], "省", "市") : city;
                    isp = datas.length > 1 ? datas[1] : isp;
                }
                ipAdress.setCity(city);
                ipAdress.setIp(ip);
                ipAdress.setRegion(province);
                ipAdress.setIsp(isp);
                if (!ipAdress.isEmpty()) {
                    return ipAdress;
                }
            }
        } catch (Exception e) {
            log.error("获取ip={}地理位置失败, {}", ip, e.getMessage());
        }
        return by126(ip);
    }


    /**
     * 最后的备份使用方法，使用本地纯真数据库
     *
     * @param ip
     * @return
     */
    public static IpAdress byLocal(String ip) {
        try {
            String[] ips = StringUtil.split(ip, ",");
            if (ArrayUtils.isNotEmpty(ips) & ips.length > 1) {
                ip = StringUtil.trimBlank(ips[1]);
            }
            IPSeeker seeker = new IPSeeker("");
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
        System.out.println(by126("58.22.117.10").getCity());
        System.out.println(bytool("58.22.117.10").getCity());
//        System.out.println(getIpAdressByLocal("10.43.18.146, 58.22.117.10"));
        //healthDetail("20951");
    }
}
