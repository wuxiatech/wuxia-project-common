package cn.wuxia.project.common.support;

import cn.wuxia.common.mapper.JacksonMapper;
import cn.wuxia.common.util.*;
import cn.wuxia.common.util.reflection.BeanUtil;
import cn.wuxia.common.web.httpclient.HttpClientException;
import cn.wuxia.project.common.bean.CallbackBean;
import cn.wuxia.project.common.bean.CallbackResultType;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CallbackBeanHandler {
    final static Logger logger = LoggerFactory.getLogger(CallbackBeanHandler.class);

    /**
     * @param callbackBean
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static String toString(CallbackBean callbackBean) throws IOException {
        return (String) handler(callbackBean, String.class);
    }

    /**
     * @param callbackBean
     * @param clazz
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static <T> List<T> toList(CallbackBean callbackBean, Class<T> clazz) throws IOException {
        return (List<T>) handler(callbackBean, clazz);
    }

    /**
     * @param callbackBean
     * @param clazz
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static <T> T toBean(CallbackBean callbackBean, Class<T> clazz) throws IOException {
        return (T) handler(callbackBean, clazz);

    }

    /**
     * @param callbackBean
     * @return
     * @throws HttpClientException
     * @author songlin
     */
    public static Map toMap(CallbackBean callbackBean) throws IOException {
        return (Map) handler(callbackBean, HashMap.class);
    }

    public static Object handler(CallbackBean callbackBean, Class clazz) throws IOException {

        if (ArrayUtil.isEmpty(callbackBean.getByteResult()))
            return null;

        Object o = null;
        try {
            o = BytesUtil.bytesToObject(callbackBean.getByteResult());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
            throw new IOException(e);
        }
        CallbackResultType resultType = callbackBean.getResultType();
        logger.info("result type: 需要转换为类型{}，读取的类型为{}", o.getClass().getName(), resultType);

        /**
         * 返回的数据结构类型
         */
        switch (resultType.getMsgType()) {

            case BYTES:

                Assert.notNull(clazz, "参数clazz不能为空");

                Class contentType = null;

                if (StringUtil.isNotBlank(resultType.getContentType())) {
                    try {
                        contentType = ClassLoaderUtil.loadClass(resultType.getContentType());
                    } catch (Exception e) {
                        logger.warn(e.getMessage());
                    }
                }
                logger.info("返回的结果为：{}，类型为：{}，希望转换结果为：{}", o.getClass().getName(), resultType.getContentType(), clazz.getName());
                /**
                 * 如果返回的数据为集合
                 */
                if (o instanceof Collection) {
                    logger.info("orign size:{}, conversion from:{}", ListUtil.size(o), resultType.getContentType());
                    Collection list = ListUtil.copyProperties(clazz, (Collection) o);
                    if (ListUtil.isEmpty(list)) {
                        return null;
                    }
                    logger.info("target size:{}, conversion to:{}", ListUtil.size(list), clazz.getName());
                    return Lists.newArrayList(list);
                } else if (o instanceof Map && !clazz.equals(HashMap.class) && !clazz.equals(Map.class)) {
                    /**
                     * 如果返回的数据为Map， 但希望转换结果不是map
                     */
                    return BeanUtil.mapToBean((Map) o, clazz);
                } else if (o instanceof Object[]) {
                    /**
                     *  先转为list再转换
                     */
                    o = ListUtil.arrayToList((Object[]) o);
                    logger.info("orign size:{}, conversion from:{}", ListUtil.size(o), resultType.getContentType());
                    Collection list = ListUtil.copyProperties(clazz, (Collection) o);
                    logger.info("target size:{}, conversion to:{}", ListUtil.size(list), clazz.getName());
                    return Lists.newArrayList(list);
                } else if (contentType != null && !clazz.equals(contentType) && !clazz.equals(HashMap.class) && !clazz.equals(Map.class)) {
                    /**
                     * 当要转换的结果类型和数据返回的类型不同，并且返回的类型不是Map，HashMap，则经行bean copy
                     */
                    Object target = null;
                    try {
                        target = clazz.newInstance();
                        BeanUtil.copyProperties(target, o);
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    return target;
                } else {
                    /**
                     * 其它情况直接返回o对象本身
                     */
                    return o;

                }
            case JSON:
                if (clazz == null) {
                    clazz = HashMap.class;
                } else {
                    logger.info("conversion to:{}", clazz.getName());
                }
                return JsonUtil.fromJson((String) o, clazz);
            case LIST_JSON:
                if (clazz == null || Map.class.isAssignableFrom(clazz)) {
                    return JacksonMapper.nonEmptyMapper().fromJson(o.toString(), Collection.class);
                } else {
                    logger.info("conversion to:{}", clazz.getName());
                    Collection list = JsonUtil.fromJsonToCollection((String) o, clazz);
                    logger.info("target size: {}", ListUtil.size(list));
                    return Lists.newArrayList(list);
                }
            case TEXT:
                return callbackBean.getStringResult();
        }
        /**
         * 其它情况直接返回o对象本身
         */
        return o;
    }
}
