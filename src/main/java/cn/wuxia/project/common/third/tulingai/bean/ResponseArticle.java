package cn.wuxia.project.common.third.tulingai.bean;

public class ResponseArticle {
	/**
	 * 文章标题
	 */
	String article;
	/**
	 * 文章来源
	 */
	String source;

	/**
	 * 名字（当是菜谱的时候）
	 */
	String name;

	/**
	 * 当是菜谱时,返回的信息内容
	 */
	String info;
	/**
	 * 文章，菜谱图片
	 */
	String icon;
	/**
	 * 文章，菜谱链接
	 */
	String detailurl;

	public String getArticle() {
		return article;
	}

	public void setArticle(String article) {
		this.article = article;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDetailurl() {
		return detailurl;
	}

	public void setDetailurl(String detailurl) {
		this.detailurl = detailurl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
