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

public class DiseaseType {

    private String typeId;

    private String typeName;

    private List<DiseaseSubType> subList;

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

    public List<DiseaseSubType> getSubList() {
        return subList;
    }

    public void setSubList(List<DiseaseSubType> subList) {
        this.subList = subList;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
