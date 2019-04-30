package cn.wuxia.project.common.third.tulingai.bean.v2resp;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResultValue {
	String url;
	String text;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
