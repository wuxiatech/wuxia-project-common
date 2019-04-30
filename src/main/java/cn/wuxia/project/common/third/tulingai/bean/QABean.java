package cn.wuxia.project.common.third.tulingai.bean;

public class QABean {
	String id;
	String question;
	String answer;

	public QABean() {
	}

	public QABean(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

}
