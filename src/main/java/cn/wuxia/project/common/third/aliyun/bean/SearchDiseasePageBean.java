/*
* Created on :2017年7月17日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.common.third.aliyun.bean;

import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class SearchDiseasePageBean {
    Integer allPages;

    Integer currentPage;

    Integer allNum;

    Integer maxResult;

    List<DiseaseDetail> contentlist;

    public Integer getAllPages() {
        return allPages;
    }

    public void setAllPages(Integer allPages) {
        this.allPages = allPages;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getAllNum() {
        return allNum;
    }

    public void setAllNum(Integer allNum) {
        this.allNum = allNum;
    }

    public Integer getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }

    public List<DiseaseDetail> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<DiseaseDetail> contentlist) {
        this.contentlist = contentlist;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
