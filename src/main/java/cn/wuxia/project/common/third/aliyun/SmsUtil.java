package cn.wuxia.project.common.third.aliyun;

import cn.wuxia.common.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SmsUtil {

	/**
	 * 模板ID枚举
	 */
	enum Sms_model{
		MODEL_SHEN_FEN_YAN_ZHENG,//身份验证
		MODEL_DENG_LU_QUE_REN,	//登录确认
		MODEL_DENG_LU_YI_CHANG,	//登录异常
		MODEL_YONG_HU_ZHU_CE,	//用户注册
		MODEL_HUO_DONG_QUE_REN,	//活动确认
		MODEL_XIU_GAI_MI_MA,	//修改密码
		MODEL_XIN_XI_BIAN_GENG,	//信息变更
		MODEL_YAN_ZHENG_MA;	
	}
	private static final String[] sms_models = {
			"SMS_60300041",	//身份验证
			"SMS_60300039",	//登录确认
			"SMS_60300038",	//登录异常
			"SMS_60300037",	//用户注册
			"SMS_60300036",	//活动确认
			"SMS_60300035",	//修改密码
			"SMS_60300034",	//信息变更
			"SMS_60300040"
	};

	private static String appKey = "";
	private static String appSecret = "";
	private static String url = "https://ca.aliyuncs.com/gw/alidayu/sendSms";
	private static String sms_type = "normal";

	//发送短信验证码
	public static String sendCodeSms(String phone,String code) {
		String resp = "";
		String arr[] = phone.split(",");
		if(arr.length>0){
			List<String> tels = new ArrayList<String>();
			for(int i=0;i<arr.length;i++){
				tels.add(arr[i]);
			}
			Map params = new HashMap();
			params.put("code", code);
			resp = sendSms(tels, params, Sms_model.MODEL_YAN_ZHENG_MA.ordinal(), "");
		}
		return resp;
	}

	/**
	 * 发送阿里大鱼的短信
	 */
	public static String sendSms(List<String> phones,Map params,int model_index,String sms_sign){
		String responseBody = "";
		String tel = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try{
			for(int i=0;i<phones.size();i++){
				if(!tel.equals("")){
					tel += ",";
				}
				tel += phones.get(i).toString();
			}			


			HttpUriRequest httpget = RequestBuilder.get()
					.setUri(url)
					.addHeader("X-Ca-Key", appKey)
					.addHeader("X-Ca-Secret", appSecret)
					.addParameter("rec_num", tel)	//电话号码
					.addParameter("sms_template_code", SmsUtil.sms_models[model_index])	//模板编码
					.addParameter("sms_free_sign_name", sms_sign)	//短信签名
					.addParameter("sms_type", sms_type)
					.addParameter("sms_param", JsonUtil.toJson(params))
					.build();
			// 处理请求结果
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				@Override
				public String handleResponse(final HttpResponse response) throws IOException {
					int status = response.getStatusLine().getStatusCode();
					System.out.println(status);
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				}
			};
			// 发起API 调用
			responseBody = httpClient.execute(httpget, responseHandler);
			System.out.println(responseBody);

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				httpClient.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return responseBody;
	}

}
