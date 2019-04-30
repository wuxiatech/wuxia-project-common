package cn.wuxia.project.common.third.tulingai.bean.v2resp;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class V2ResponseBean {

	/**
	 * 输入的信息
	 */
	@NotNull
	RespIntent intent;
	/**
	 * 用户参数
	 */
	@NotNull
	ArrayList<RespResult> results;

	public RespIntent getIntent() {
		return intent;
	}

	public void setIntent(RespIntent intent) {
		this.intent = intent;
	}

	public ArrayList<RespResult> getResults() {
		return results;
	}

	public void setResults(ArrayList<RespResult> results) {
		this.results = results;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
