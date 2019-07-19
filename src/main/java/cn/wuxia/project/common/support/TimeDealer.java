package cn.wuxia.project.common.support;

import cn.wuxia.common.util.DateUtil;
import cn.wuxia.common.util.DateUtil.DateFormatter;

import java.util.Date;

/**
 * 时间处理类
 * 
 * @author YangRenZhi
 */
public class TimeDealer {

    /**
     * 返回某时刻距当前时间（例如：15天前）
     */
    public static String getDistance(Date paramTime) {
        Long distance = System.currentTimeMillis() - paramTime.getTime();
        if (distance < 60 * 1000) {
            return distance / 1000 + "秒前";
        } else if (60 * 1000L <= distance && distance < 60 * 60 * 1000L) {
            return (int) Math.floor(distance / 60000) + "分钟前";
        } else if (60 * 60 * 1000L <= distance && distance < 24 * 60 * 60 * 1000L) {
            return (int) Math.floor(distance / 60 / 60000) + "小时前";
        } else if (24 * 60 * 60 * 1000L <= distance && distance < 30 * 24 * 60 * 60 * 1000L) {
            return (int) Math.floor(distance / 60 / 60000 / 24) + "天前";
        } else {
            return DateUtil.dateToString(paramTime, DateFormatter.FORMAT_YYYY_MM_DD_CN_HH_MM);
        }
    }

}