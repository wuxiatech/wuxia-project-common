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

public class DiseaseDetail {
    String id;

    String name;

    String summary;

    String typeId;

    String typeName;

    String subTypeId;

    String subTypeName;

    /**
     * 创建时间
     */
    String ct;

    List<DiseaseTag> tagList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSubTypeId() {
        return subTypeId;
    }

    public void setSubTypeId(String subTypeId) {
        this.subTypeId = subTypeId;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    public void setSubTypeName(String subTypeName) {
        this.subTypeName = subTypeName;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public List<DiseaseTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<DiseaseTag> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
