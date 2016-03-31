package com.zhouchi.learningbang;

import android.util.Log;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;

@AVClassName(Todo.TODO_CLASS)
public class Todo extends AVObject {

  static final String TODO_CLASS = "Todo";
  //����
  private static final String CONTENT_KEY = "content";
  //������
  private static final String PUT_USER_KEY = "put_user";
  //���ͻ���
  private static final String QUESTION_SCORE_KEY = "question_score";
  //������Ƭ
  private static final String QUESTION_PIC_KEY = "question_pic";
  //��ʾ���Ƿ�õ����
  private static final String QUESTION_FINISHED = "state";
 

  public String getContent() {
    return this.getString(CONTENT_KEY);
  }

  public void setContent(String content) {
    this.put(CONTENT_KEY, content);
  }
  
  public String getPutUser() {
	  return this.getString(PUT_USER_KEY);
  }
  
  public void setPutUser(String putUser) {
	  this.put(PUT_USER_KEY, putUser);
  }
  
  public int getQuestionScore() {
	  return this.getInt(QUESTION_SCORE_KEY);
  }
  
  public void setQuestionScore(int questionScore) {
	  this.put(QUESTION_SCORE_KEY, questionScore);
  }
  public void setQuestionState() {
	  this.put(QUESTION_FINISHED,true);
  }
  
  public boolean getQuestionState() {
	  return this.getBoolean(QUESTION_FINISHED);
  }
  
  public String getQuestionPic() {
	  
	return this.getString(QUESTION_PIC_KEY);
  }
  public void setQuestionPic(String questionPic) {
	  this.put(QUESTION_PIC_KEY, questionPic);
  }
}
