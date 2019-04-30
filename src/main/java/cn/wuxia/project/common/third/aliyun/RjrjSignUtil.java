/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.aliyun.components.gateway.constant.Constants;
import cn.wuxia.common.util.StringUtil;
import cn.wuxia.project.common.third.aliyun.bean.RjrzCommonParam;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.TreeMap;

/**
 * 签名工具
 */
public class RjrjSignUtil {

    /**
     * 计算签名
     *
     * @param secret APP密钥
     * @param method HttpMethod
     * @param param
     * @return 签名后的字符串
     */
    public static String sign(String secret, String method, RjrzCommonParam param) {
        try {
            byte[] keyBytes = (secret+Constants.SPE3).getBytes(Constants.ENCODING);
//            HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_1, keyBytes);
//            HmacUtils.updateHmac()
            Mac hmacSha256 = Mac.getInstance("HmacSHA1");
            hmacSha256.init(new SecretKeySpec(keyBytes, "HmacSHA1"));
           return Base64.encodeBase64String(hmacSha256.doFinal(buildStringToSign(method, param)
                    .getBytes(Constants.ENCODING)));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建待签名字符串
     *
     * @param param
     * @return
     */
    private static String buildStringToSign(String method, RjrzCommonParam param) throws Exception {
        String stringToSign = method + Constants.SPE3 + percentEncode("/") + Constants.SPE3 + percentEncode(buildCanonicalizedQueryStringToSign(param));
        return stringToSign;
    }

    /**
     * 构建待签名字符串
     *
     * @param param
     * @return
     */
    private static String buildCanonicalizedQueryStringToSign(RjrzCommonParam param) throws Exception {

        Map<String, String> sortMap = getSortMap(param);

        StringBuilder sbParam = new StringBuilder();
        for (Map.Entry<String, String> item : sortMap.entrySet()) {
            if (!StringUtils.isBlank(item.getKey())) {
                if (0 < sbParam.length()) {
                    sbParam.append(Constants.SPE3);
                }
                sbParam.append(item.getKey());
                if (!StringUtils.isBlank(item.getValue())) {
                    sbParam.append(Constants.SPE4).append(item.getValue());
                }
            }
        }
        return sbParam.toString();
    }


    private static Map<String, String> getSortMap(RjrzCommonParam param) throws Exception {
        Map<String, Object> map = param.getMap();
        Map<String, String> sortMap = new TreeMap<String, String>();

        if (null != map) {
            for (Map.Entry<String, Object> body : map.entrySet()) {
                if (!StringUtils.isBlank(body.getKey()) && !StringUtil.equals("Signature", body.getKey())) {
                    sortMap.put(percentEncode(body.getKey()), percentEncode(body.getValue() + ""));
                }
            }
        }
        return sortMap;
    }


    private static String percentEncode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
    }

}
