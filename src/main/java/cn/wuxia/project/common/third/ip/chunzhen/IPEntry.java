/*
* Created on :8 Oct, 2015
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 武侠科技 All right reserved.
*/
package cn.wuxia.project.common.third.ip.chunzhen;

//一条IP范围记录，不仅包括国家和区域，也包括起始IP和结束IP
public class IPEntry {
    public String beginIp;

    public String endIp;

    public String country;

    public String area;

    public IPEntry() {
        beginIp = "";
        endIp = "";
        country = "";
        area = "";
    }

    @Override
    public String toString() {
        return this.area + "  " + this.country + "  IP范围:" + this.beginIp + "-" + this.endIp;
    }
}
