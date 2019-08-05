package cn.wuxia.project.common.third.ip.bean;


import cn.wuxia.common.util.StringUtil;

public class IpAdress {
    String ip;
    String country;
    String region;
    String city;
    String area;
    String isp;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }


    public boolean isEmpty() {
        return StringUtil.isBlank(city) && StringUtil.isBlank(region) && StringUtil.isBlank(country);
    }
}
