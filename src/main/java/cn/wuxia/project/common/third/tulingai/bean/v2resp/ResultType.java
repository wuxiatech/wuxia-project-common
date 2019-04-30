package cn.wuxia.project.common.third.tulingai.bean.v2resp;

public enum ResultType {
	text("文本"), url("连接"), news("图文");

	String displayName;

	ResultType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

}
