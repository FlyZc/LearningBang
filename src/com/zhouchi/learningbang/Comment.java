package com.zhouchi.learningbang;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(Comment.Comment_CLASS)
public class Comment extends AVObject {

  static final String Comment_CLASS = "Comment";
  
  //��������
  private static final String CONTENT_KEY = "content";
  //������
  private static final String COMMENT_USER_KEY = "comment_user";
  //��Ӧ�ľ�����Id
  private static final String EXPERIENCE_KEY = "experience_id";
  //������ͼƬ
  private static final String EXPERIENCE_PIC_KEY = "experience_pic";
  
  public String getExperiencePic() {
	  return this.getString(EXPERIENCE_PIC_KEY);
  }
  
  public void setExperiencePic(String experience_pic) {
	  this.put(EXPERIENCE_PIC_KEY,experience_pic);
  }
 
  public String getContent() {
    return this.getString(CONTENT_KEY);
  }
  public void setContent(String content) {
    this.put(CONTENT_KEY, content);
  }
  
  public String getCommentUser() {
	  return this.getString(COMMENT_USER_KEY);
  }
  public void setCommentUser(String commentUser) {
	  this.put(COMMENT_USER_KEY, commentUser);
  }
  
  public String getExperienceId() {
	  return this.getString(EXPERIENCE_KEY);
  }
  public void setExperienceId(String experienceId) {
	  this.put(EXPERIENCE_KEY, experienceId);
  }
  
}
