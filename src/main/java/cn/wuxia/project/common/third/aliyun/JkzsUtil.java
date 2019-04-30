/*
* Created on :2017年7月14日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.common.third.aliyun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.wuxia.project.common.third.aliyun.bean.HealthCategory;
import org.hibernate.validator.constraints.NotBlank;

import cn.wuxia.project.common.third.aliyun.bean.HealthDetail;
import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.MapUtil;
import cn.wuxia.common.util.StringUtil;

/**
 * 
 * [ticket id]
 * 健康知识
 * @author songlin
 * @ Version : V<Ver.No> <2017年7月14日>
 * @see https://market.aliyun.com/products/57126001/cmapi013637.html?spm=5176.2020520132.101.27.QFjeap#sku=yuncode763700001
 */
public class JkzsUtil extends ApiConstants {

    /**
     * 最新健康知识列表
     * @author songlin
     * 名称   类型  是否必须    描述
    classify    STRING  可选  分类ID，取得该分类下的最新数据
    id  STRING  可选  当前最新的知识的id
    rows    STRING  可选  返回最新关键词的条数，默认rows=20
     */
    public static List<HealthDetail> healthInfo(String categoryId, Integer rows) {
        String url = "http://health-ali.juheapi.com/health_knowledge/infoNews";

        Map<String, String> querys = new HashMap<String, String>();
        if (StringUtil.isNotBlank(categoryId)) {
            querys.put("classify", categoryId);
        }
        if (rows != null) {
            querys.put("rows", "" + rows);
        }
        String result = new JkzsUtil().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "error_core") == 0) {
                map = MapUtil.getMap(map, "result");
                if (MapUtil.isNotEmpty(map)) {
                    ArrayList list = (ArrayList) MapUtil.getObject(map, "data");
                    logger.debug("{}", list);
                    return ListUtil.copyProperties(HealthDetail.class, list);
                }
            }
        }
        return null;
    }

    /**
     * 健康知识信息列表
     * @author songlin
        名称  类型  是否必须    描述
        id  STRING  可选  这里是指知识分类的ID 默认为 null ，也就是全部
        limit   STRING  可选  每页返回的条数，默认是20
        page    STRING  可选  请求页数，默认是1
     */
    public static List<HealthDetail> healthList(String categoryId, Integer limit, Integer page) {
        String url = "http://health-ali.juheapi.com/health_knowledge/infoList";

        Map<String, String> querys = new HashMap<String, String>();
        if (StringUtil.isNotBlank(categoryId)) {
            querys.put("id", categoryId);
        }
        if (null != limit) {
            querys.put("limit", "" + limit);
        }
        if (null != page) {
            querys.put("page", "" + page);
        }
        String result = new JkzsUtil().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "error_core") == 0) {
                map = MapUtil.getMap(map, "result");
                logger.debug("{}", map);
                if (MapUtil.isNotEmpty(map)) {
                    ArrayList list = (ArrayList) MapUtil.getObject(map, "data");
                    return ListUtil.copyProperties(HealthDetail.class, list);
                }
            }
        }
        return null;
    }

    /**
     * 健康知识详细明细
     * <pre>
     * 
     * { 
     *      "error_code" : 0 , 
     *      "reason" : "Success!" , 
     *      "result" : { 
     *          "count" : 2368 , 
     *          "description" : "在其他的女同事眼里，那女孩又会装可爱又很会向男人献媚，对她无法产生好感，但她在男人这边很受欢迎" , 
     *          "fcount" : 0 , 
     *          "id" : 1 , 
     *          "img" : "http://imgs.juheapi.com/health_knowledge/256a975b77439772669a89a518a550a1.jpg" , 
     *          "keywords" : "男人 做作 男性 在一起 这样 " , 
     *          "loreclass" : 4 , 
     *          "rcount" : 0 , 
     *          "time" : 1438305196000 , 
     *          "title" : "男性比女性想象的更为单纯 对矫情女毫无招架之力" , 
     *          "message" : "<p>  </p> \n<p> 日媒此前公布一项心理学研究报告称，男性比女性想象的更为单纯。大多数男人对于那种非常明显的“矫情女”毫无招架之力。为什么会这样呢？日本网站livedoor4月14日刊文分析了男人们迷上“做作女”的理由，并读者们介绍了一窥男人心理的3个关键词。让我们来看一下吧! </p> \n<p> </p> \n<span></span> \n<span></span> \n<p> 接受采访的男性介绍了他身边这样一个现象：“最近觉得很不错的那个女孩子，是很明显的‘矫情女’，有种职场偶像的感觉。在其他的女同事眼里，那女孩又会装可爱又很会向男人献媚，对她无法产生好感，但她在男人这边很受欢迎。首先她非常有所谓的‘服务精神’，很会判断气氛、会看眼色。而且如果和她在一起会很愉快，也能衬托自己。还有呢就是，做作女一般很容易就能约出来，男人觉得比较好下手吧。”以上话语中，男人迷上“矫情女”的理由里出现了3个关键词： </p> \n<p> <strong>1.服务精神</strong> </p> \n<p> 做作女因为服务精神旺盛，所以受男性欢迎。女人眼里这样的女生是以可爱作为武器，所以产生厌恶之情，而做作女本身就是靠对男性露出可爱笑容来取胜的。 </p> \n<p> <strong>2.在一起很快乐</strong> </p> \n<p> 做作女很了解怎么取悦男人，所以和这样的女生在一起会很有趣。仅仅是轻浮地打情骂俏，互相开开玩笑什么的，就能使男人陷入情网。这一点非常重要，以此就能决定女人是否受欢迎哦。 </p> \n<p> <strong>3.容易答应约会</strong> </p> \n<p> 做作女比较容易答应约会。就算是拒绝，也会回答得比较圆滑而不伤害对方。尤其是喜欢被奉承，爱听甜言蜜语的女性，在男人眼中看起来非常有机可乘，所以受男性欢迎。 </p> \n<br>"
     * </pre>
     * @author songlin
     */
    public static HealthDetail healthDetail(@NotBlank String id) {
        String url = "http://health-ali.juheapi.com/health_knowledge/infoDetail";

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("id", id);
        String result = new JkzsUtil().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "error_code") == 0) {
                map = MapUtil.getMap(map, "result");
                logger.debug("{}", map);
                if (MapUtil.isNotEmpty(map)) {
                    return MapUtil.mapToBean(map, HealthDetail.class);
                }
            }
        }
        return null;
    }

    /**
     * 健康知识分类列表
     * @author songlin
     */
    public static List<HealthCategory> healthCategory() {
        String url = "http://health-ali.juheapi.com/health_knowledge/categoryList";
        Map<String, String> querys = new HashMap<String, String>();
        String result = new JkzsUtil().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            logger.debug("{}", map);
            if (MapUtil.getIntValue(map, "error_code") == 0) {
                ArrayList list = (ArrayList) MapUtil.getObject(map, "result");
                if (ListUtil.isNotEmpty(list)) {
                    return ListUtil.copyProperties(HealthCategory.class, list);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(healthCategory());
        //System.out.println(healthList());
        //healthDetail("20951");
    }
}
