package cn.wuxia.project.common.third.aliyun.bean;

import cn.wuxia.common.util.reflection.BeanUtil;

import java.util.Map;

public class RjrzCommonParam {
    String Format;//		否	返回值的类型，支持JSON与XML。默认为XML
    String Version;//	是	API版本号，为日期形式：YYYY-MM-DD 取值：2016-11-23
    String AccessKeyId;        // 是	阿里云颁发给用户的访问服务所用的密钥ID
    String Signature;//	是	签名结果串，关于签名的计算方法，请参见签名机制。
    String SignatureMethod;//	是	签名方式，目前支持HMAC-SHA1
    String Timestamp;//	是	请求的时间戳。日期格式按照ISO8601标准表示，并需要使用UTC时间。格式为：YYYY-MM-DDThh:mm:ssZ;例如，2013-08-15T12:00:00Z（为北京时间2013年8月15日20点0分0秒）
    String SignatureVersion;//		是	签名算法版本，目前版本是1.0
    String SignatureNonce;//	是	唯一随机数，用于防止网络重放攻击。用户在不同请求间要使用不同的随机数值

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getAccessKeyId() {
        return AccessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        AccessKeyId = accessKeyId;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    public String getSignatureMethod() {
        return SignatureMethod;
    }

    public void setSignatureMethod(String signatureMethod) {
        SignatureMethod = signatureMethod;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public void setTimestamp(String timestamp) {
        Timestamp = timestamp;
    }


    public Map<String, Object> getMap() {
        return BeanUtil.beanToMap(this);
    }

    public String getSignatureVersion() {
        return SignatureVersion;
    }

    public void setSignatureVersion(String signatureVersion) {
        SignatureVersion = signatureVersion;
    }

    public String getSignatureNonce() {
        return SignatureNonce;
    }

    public void setSignatureNonce(String signatureNonce) {
        SignatureNonce = signatureNonce;
    }
}
