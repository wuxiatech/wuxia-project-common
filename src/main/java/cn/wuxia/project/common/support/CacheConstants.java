/*
 * Created on :Jun 25, 2013 Author :PL Change History Version Date Author Reason
 * <Ver.No> <date> <who modify> <reason>
 */
package cn.wuxia.project.common.support;

import cn.wuxia.common.spring.enums.CacheNameEnum;

/**
 * @author songlin
 */
public class CacheConstants extends cn.wuxia.common.Constants {
    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_30_SECONDS}
     */
    public final static String CACHED_VALUE_30_SECONDS = CacheNameEnum.CACHE_30_SECONDS.name();
    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_1_MINUTES}
     */
    public final static String CACHED_VALUE_1_MINUTES = CacheNameEnum.CACHE_1_MINUTES.name();

    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_2_MINUTES}
     */
    public final static String CACHED_VALUE_2_MINUTES = "2MinutesData";

    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_10_MINUTES}
     */
    public final static String CACHED_VALUE_10_MINUTES = "10MinutesData";

    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_30_MINUTES}
     */
    public final static String CACHED_VALUE_30_MINUTES = "30MinutesData";
    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_1_HOUR}
     */

    public final static String CACHED_VALUE_1_HOUR = "1HourData";
    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_2_HOUR}
     */
    public final static String CACHED_VALUE_2_HOUR = "2HourData";
    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_4_HOUR}
     */
    public final static String CACHED_VALUE_4_HOUR = "4HourData";
    /**
     * @see {@link cn.wuxia.common.spring.enums.CacheNameEnum#CACHE_1_DAY}
     */
    public final static String CACHED_VALUE_1_DAY = "1DayData";

    /**
     * 带全路径包名
     */
    public final static String CACHED_KEY_LONG = "#root.targetClass +'.'+ #root.methodName";
    /**
     * 避免key过长
     */
    public final static String CACHED_KEY_DEFAULT = "#root.target.getClass().getSimpleName()+':'+ #root.methodName";
    /**
     * 带全路径包名
     */
    public final static String CACHED_KEY_LONG_CLASS = "#root.targetClass +'.'+";
    /**
     * 避免key过长
     */
    public final static String CACHED_KEY_CLASS = "#root.target.getClass().getSimpleName()+':'+";
}
