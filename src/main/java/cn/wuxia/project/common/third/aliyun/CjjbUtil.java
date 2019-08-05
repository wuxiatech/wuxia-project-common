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

import cn.wuxia.project.common.third.aliyun.bean.DiseaseDetail;
import cn.wuxia.project.common.third.aliyun.bean.DiseaseType;
import cn.wuxia.project.common.third.aliyun.bean.SearchDiseasePageBean;
import org.hibernate.validator.constraints.NotBlank;

import cn.wuxia.common.util.JsonUtil;
import cn.wuxia.common.util.ListUtil;
import cn.wuxia.common.util.MapUtil;
import cn.wuxia.common.util.StringUtil;

/**
 * 
 * [ticket id]
 * 常见疾病
 * @author songlin
 * @ Version : V<Ver.No> <2017年7月14日>
 *  @see https://market.aliyun.com/products/57126001/cmapi011522.html?spm=5176.2020520132.101.29.QFjeap#sku=yuncode552200000
 */
public class CjjbUtil extends BaseMarketApi {

    public CjjbUtil() {
        super("b915a1e017d04bd48c5b6d64bc9ace7c");
    }

    public static CjjbUtil getInstance(){
        return new  CjjbUtil();
    }
    /**
     * 根据关键字查找疾病
     *  key  STRING  可选  关键词
        page    STRING  可选  第几页，每页最多20条
        subTypeId   STRING  可选  二级科目id
        typeId  STRING  可选  一级科目id
     * @author songlin
     * 
     */
    public static SearchDiseasePageBean searchDisease(String key) {
        String url = "http://ali-disease.showapi.com/search-disease";

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("key", key);
        String result = getInstance().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "showapi_res_code") == 0) {
                map = MapUtil.getMap(map, "showapi_res_body");
                if (MapUtil.isNotEmpty(map)) {
                    map = MapUtil.getMap(map, "pagebean");
                    logger.debug("{}", map);
                    return MapUtil.mapToBean(map, SearchDiseasePageBean.class);
                }
            }
        }
        return null;
    }

    /**
     * 查询疾病明细
     * @author songlin
     */
    public static DiseaseDetail diseaseDetail(@NotBlank String id) {
        String url = "http://ali-disease.showapi.com/disease-detail";

        Map<String, String> querys = new HashMap<String, String>();
        querys.put("id", id);
        String result = getInstance().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "showapi_res_code") == 0) {
                map = MapUtil.getMap(map, "showapi_res_body");
                if (MapUtil.isNotEmpty(map)) {
                    map = MapUtil.getMap(map, "item");
                    logger.debug("{}", map);
                    return MapUtil.mapToBean(map, DiseaseDetail.class);
                }
            }
        }
        return null;
    }

    /**
     * 查询疾病科目
     * @author songlin
     */
    public static List<DiseaseType> searchDiseaseType() {
        String url = "http://ali-disease.showapi.com/disease-type-list";
        Map<String, String> querys = new HashMap<String, String>();
        String result = getInstance().get(url, querys);
        if (StringUtil.isNotBlank(result)) {
            Map<String, Object> map = JsonUtil.fromJson(result);
            if (MapUtil.getIntValue(map, "showapi_res_code") == 0) {
                map = MapUtil.getMap(map, "showapi_res_body");
                if (MapUtil.isNotEmpty(map)) {
                    ArrayList list = (ArrayList) MapUtil.getObject(map, "list");
                    logger.debug("{}", list);
                    return ListUtil.copyProperties(DiseaseType.class, list);
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(searchDisease("肺癌"));
        System.out.println(searchDiseaseType());
    }
}
