package com.zhouchi.learningbang;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;

@AVClassName(Experience.Experience_CLASS)
public class Experience extends AVObject {

  static final String Experience_CLASS = "Experience";
  //标题
  private static final String Title_KEY = "title";
  //内容
  private static final String CONTENT_KEY = "content";
  //提问者
  private static final String PUT_USER_KEY = "put_user";
  
  //问题照片
  private static final String QUESTION_PIC_KEY = "question_pic";
  
  public String getExperiencePic() {
	  return this.getString(QUESTION_PIC_KEY);
  }
  public void setExperiencePic(String experiencePic) {
	  this.put(QUESTION_PIC_KEY, experiencePic);
  }
  public String getTitle() {
	    return this.getString(Title_KEY);
	  }

  public void setTitle(String title) {
	    this.put(Title_KEY, title);
	  }
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
  
  
}
