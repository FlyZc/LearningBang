package com.zhouchi.learningbang;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

@AVClassName(Answer.Answer_CLASS)
public class Answer extends AVObject {

  static final String Answer_CLASS = "Answer";
  
  //内容
  private static final String CONTENT_KEY = "content";
  //回答者
  private static final String ANSWER_USER_KEY = "answer_user";
  //答案对应的问题
  private static final String QUESTION_KEY = "question_id";
  //答案照片
  private static final String ANSWER_PIC_KEY = "answer_pic";
  
  public String getAnswerPic() {
	  return this.getString(ANSWER_PIC_KEY);
  }
  public void setAnswerPic(String answerPic) {
	  this.put(ANSWER_PIC_KEY, answerPic);
  }
  public String getAnswerUser() {
	    return this.getString(ANSWER_USER_KEY);
	  }

  public void setAnswerUser(String answer_user) {
	    this.put(ANSWER_USER_KEY, answer_user);
	  }
  public String getContent() {
    return this.getString(CONTENT_KEY);
  }

  public void setContent(String content) {
    this.put(CONTENT_KEY, content);
  }
  
  public String getQuestionKey() {
	  return this.getString(QUESTION_KEY);
  }
  
  public void setPutQuestionKey(String question_key) {
	  this.put(QUESTION_KEY, question_key);
  }
  
  
}