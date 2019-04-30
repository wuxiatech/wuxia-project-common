package cn.wuxia.project.common.third.tulingai.bean;

import java.util.List;

public class RobotResponse {
	/**
	 * 状态码
	 */
	String code;
	/**
	 * 回复的文本
	 */
	String text;
	/**
	 * 回复的链接
	 */
	String url;

	List<ResponseArticle> list;
}
