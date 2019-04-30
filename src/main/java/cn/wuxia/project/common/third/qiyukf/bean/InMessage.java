/*
* Created on :2017年6月14日
* Author     :songlin
* Change History
* Version       Date         Author           Reason
* <Ver.No>     <date>        <who modify>       <reason>
* Copyright 2014-2020 wuxia.gd.cn All right reserved.
*/
package cn.wuxia.project.common.third.qiyukf.bean;

import java.util.Map;

public class InMessage {
    String userid;

    String userName;

    InMessageType msgType;

    byte[] content;

    Map<String, String> addition;

    public enum InMessageType {
        text, voice, image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Map<String, String> getAddition() {
        return addition;
    }

    public void setAddition(Map<String, String> addition) {
        this.addition = addition;
    }

    public InMessageType getMsgType() {
        return msgType;
    }

    public void setMsgType(InMessageType msgType) {
        this.msgType = msgType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
