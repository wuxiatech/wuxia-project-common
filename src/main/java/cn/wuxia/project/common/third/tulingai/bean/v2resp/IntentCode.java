package cn.wuxia.project.common.third.tulingai.bean.v2resp;

public enum IntentCode {
/**
 * 异常码	说明
 */
	_5000("暂不支持该功能"),
	_6000("暂不支持该功能"),
	_4000("请求参数格式错误"),
	_4001("加密方式错误"),
	_4002("无功能权限"),
	_4003("该apikey没有可用请求次数"),
	_4005("无功能权限"),
	_4007("apikey不合法"),
	_4100("userid获取失败"),
	_4200("上传格式错误"),
	_4300("批量操作超过限制"),
	_4400("没有上传合法userid"),
	_4500("userid申请个数超过限制"),
	_7002("上传信息失败"),
	_8008("服务器错误"),
	_0("上传成功");
	
	private String displayName;
	IntentCode(String displayName){
		this.displayName = displayName;
	}
	public String getDisplayName() {
		return displayName;
	}
	
	
}
