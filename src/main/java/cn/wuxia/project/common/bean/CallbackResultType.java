package cn.wuxia.project.common.bean;

import java.io.Serializable;

public class CallbackResultType implements Serializable{
    private static final long serialVersionUID = 6594809787675651623L;

    public enum MsgType {
        TEXT,BYTES, JSON, LIST_JSON;
    }

    MsgType msgType;

    String contentType;

    public CallbackResultType() {
    }

    public CallbackResultType(MsgType msgType, String contentType) {
        this.msgType = msgType;
        this.contentType = contentType;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "{" + "msgType=" + msgType + ", contentType=" + contentType + '}';
    }
}
