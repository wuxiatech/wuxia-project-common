package cn.wuxia.project.common.third.tulingai.bean.v2resp;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class IntentParameter {
	String nearby_place;
	String name_output;
	String news_code;

	public IntentParameter() {
	}

	public IntentParameter(String nearby_place) {
		this.nearby_place = nearby_place;
	}

	public String getNearby_place() {
		return nearby_place;
	}

	public void setNearby_place(String nearby_place) {
		this.nearby_place = nearby_place;
	}

	public String getName_output() {
		return name_output;
	}

	public void setName_output(String name_output) {
		this.name_output = name_output;
	}

	public String getNews_code() {
		return news_code;
	}

	public void setNews_code(String news_code) {
		this.news_code = news_code;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
