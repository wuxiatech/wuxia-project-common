package cn.wuxia.project.common.third.fenci.bean;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.ArrayList;

public class NerBean implements Serializable {
    ArrayList<String> tag;

    ArrayList<String> word;

    ArrayList<ArrayList<Object>> entity;

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public ArrayList<String> getWord() {
        return word;
    }

    public void setWord(ArrayList<String> word) {
        this.word = word;
    }

    public ArrayList<ArrayList<Object>> getEntity() {
        return entity;
    }

    public void setEntity(ArrayList<ArrayList<Object>> entity) {
        this.entity = entity;
    }

    @Override
    public String toString(){
        return ToStringBuilder.reflectionToString(this);
    }
}
