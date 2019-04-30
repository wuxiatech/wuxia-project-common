package cn.wuxia.project.common.third.tulingai.bean.v2resp;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RespResult {
	Integer groupType;

	ResultType resultType;

	ResultValue values;

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public void setResultType(String resultType) {
		try {
			this.resultType = ResultType.valueOf(resultType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultValue getValues() {
		return values;
	}

	public void setValues(ResultValue values) {
		this.values = values;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
