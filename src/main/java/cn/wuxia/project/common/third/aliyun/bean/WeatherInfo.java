package cn.wuxia.project.common.third.aliyun.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


/**
 * @author songlin
 */
@Setter
@Getter
public class WeatherInfo implements Serializable {

    private static final long serialVersionUID = 8421146696011121879L;
    /**
     * city : 安顺
     * cityid : 111
     * citycode : 101260301
     * date : 2015-12-22
     * week : 星期二
     * weather : 多云
     * temp : 16
     * temphigh : 18
     * templow : 9
     * img : 1
     * humidity : 55
     * pressure : 879
     * windspeed : 14.0
     * winddirect : 南风
     * windpower : 2级
     * updatetime : 2015-12-22 15:37:03
     * index : [{"iname":"空调指数","ivalue":"较少开启","detail":"您将感到很舒适，一般不需要开启空调。"},{"iname":"运动指数","ivalue":"较适宜","detail":"天气较好，无雨水困扰，较适宜进行各种运动，但因气温较低，在户外运动请注意增减衣物。"}]
     * aqi : {"so2":"37","so224":"43","no2":"24","no224":"21","co":"0.647","co24":"0.675","o3":"26","o38":"14","o324":"30","pm10":"30","pm1024":"35","pm2_5":"23","pm2_524":"24","iso2":"13","ino2":"13","ico":"7","io3":"9","io38":"7","ipm10":"35","ipm2_5":"35","aqi":"35","primarypollutant":"PM10","quality":"优","timepoint":"2015-12-09 16:00:00","aqiinfo":{"level":"一级","color":"#00e400","affect":"空气质量令人满意，基本无空气污染","measure":"各类人群可正常活动"}}
     * daily : [{"date":"2015-12-22","week":"星期二","sunrise":"07:39","sunset":"18:09","night":{"weather":"多云","templow":"9","img":"1","winddirect":"无持续风向","windpower":"微风"},"day":{"weather":"多云","temphigh":"18","img":"1","winddirect":"无持续风向","windpower":"微风"}}]
     * hourly : [{"time":"16:00","weather":"多云","temp":"14","img":"1"},{"time":"17:00","weather":"多云","temp":"13","img":"1"}]
     */

    private String city;
    private String cityid;
    private String citycode;
    private String date;
    private String week;
    private String weather;
    private String temp;
    private String temphigh;
    private String templow;
    private String img;
    private String humidity;
    private String pressure;
    private String windspeed;
    private String winddirect;
    private String windpower;
    private String updatetime;
    private AqiBean aqi;
    private List<IndexBean> index;
    private List<DailyBean> daily;
    private List<HourlyBean> hourly;


    @Getter
    @Setter
    public static class AqiBean implements Serializable {
        private static final long serialVersionUID = 7078463868546235086L;
        /**
         * so2 : 37
         * so224 : 43
         * no2 : 24
         * no224 : 21
         * co : 0.647
         * co24 : 0.675
         * o3 : 26
         * o38 : 14
         * o324 : 30
         * pm10 : 30
         * pm1024 : 35
         * pm2_5 : 23
         * pm2_524 : 24
         * iso2 : 13
         * ino2 : 13
         * ico : 7
         * io3 : 9
         * io38 : 7
         * ipm10 : 35
         * ipm2_5 : 35
         * aqi : 35
         * primarypollutant : PM10
         * quality : 优
         * timepoint : 2015-12-09 16:00:00
         * aqiinfo : {"level":"一级","color":"#00e400","affect":"空气质量令人满意，基本无空气污染","measure":"各类人群可正常活动"}
         */

        private String so2;
        private String so224;
        private String no2;
        private String no224;
        private String co;
        private String co24;
        private String o3;
        private String o38;
        private String o324;
        private String pm10;
        private String pm1024;
        private String pm2_5;
        private String pm2_524;
        private String iso2;
        private String ino2;
        private String ico;
        private String io3;
        private String io38;
        private String ipm10;
        private String ipm2_5;
        private String aqi;
        private String primarypollutant;
        private String quality;
        private String timepoint;
        private AqiinfoBean aqiinfo;


        @Setter
        @Getter
        public static class AqiinfoBean implements Serializable {
            private static final long serialVersionUID = 8017553830368819591L;
            /**
             * level : 一级
             * color : #00e400
             * affect : 空气质量令人满意，基本无空气污染
             * measure : 各类人群可正常活动
             */

            private String level;
            private String color;
            private String affect;
            private String measure;


        }
    }

    @Setter
    @Getter
    public static class IndexBean implements Serializable {
        private static final long serialVersionUID = -5049903021549329930L;
        /**
         * iname : 空调指数
         * ivalue : 较少开启
         * detail : 您将感到很舒适，一般不需要开启空调。
         */

        private String iname;
        private String ivalue;
        private String detail;


    }

    @Getter
    @Setter
    public static class DailyBean implements Serializable {
        private static final long serialVersionUID = -4752762793321502190L;
        /**
         * date : 2015-12-22
         * week : 星期二
         * sunrise : 07:39
         * sunset : 18:09
         * night : {"weather":"多云","templow":"9","img":"1","winddirect":"无持续风向","windpower":"微风"}
         * day : {"weather":"多云","temphigh":"18","img":"1","winddirect":"无持续风向","windpower":"微风"}
         */

        private String date;
        private String week;
        private String sunrise;
        private String sunset;
        private NightBean night;
        private DayBean day;


        @Getter
        @Setter
        public static class NightBean implements Serializable {
            private static final long serialVersionUID = 1489315871777270951L;
            /**
             * weather : 多云
             * templow : 9
             * img : 1
             * winddirect : 无持续风向
             * windpower : 微风
             */

            private String weather;
            private String templow;
            private String img;
            private String winddirect;
            private String windpower;

        }

        @Getter
        @Setter
        public static class DayBean implements Serializable {
            private static final long serialVersionUID = 8235671717396744789L;
            /**
             * weather : 多云
             * temphigh : 18
             * img : 1
             * winddirect : 无持续风向
             * windpower : 微风
             */

            private String weather;
            private String temphigh;
            private String img;
            private String winddirect;
            private String windpower;


        }
    }

    @Getter
    @Setter
    public static class HourlyBean implements Serializable {
        private static final long serialVersionUID = 6380966348049704289L;
        /**
         * time : 16:00
         * weather : 多云
         * temp : 14
         * img : 1
         */

        private String time;
        private String weather;
        private String temp;
        private String img;
    }
}
