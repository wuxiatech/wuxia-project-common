package cn.wuxia.project.common.third.tulingai.bean.v2resp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RespIntent {
	private static Logger logger = LoggerFactory.getLogger(RespIntent.class);
	IntentCode code;
	String intentName;
	String actionName;
	/**
	 * 地理位置信息
	 */
	IntentParameter parameters;

	public RespIntent() {
	}

	public IntentCode getCode() {
		return code;
	}

	public void setCode(IntentCode code) {
		this.code = code;
	}

	public void setCode(Integer code) {
		try {
			this.code = IntentCode.valueOf("_" + code);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
	}

	public String getIntentName() {
		return intentName;
	}

	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public IntentParameter getParameters() {
		return parameters;
	}

	public void setParameters(IntentParameter parameters) {
		this.parameters = parameters;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
