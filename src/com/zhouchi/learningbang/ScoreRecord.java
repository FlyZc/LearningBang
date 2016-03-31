package com.zhouchi.learningbang;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

@AVClassName(ScoreRecord.ScoreRecord_CLASS)
public class ScoreRecord extends AVObject {

  static final String ScoreRecord_CLASS = "ScoreRecord";
  
  //用户名
  private static final String USER_KEY = "user_name";
  //积分
  private static final String SCORE_KEY = "score";
  
  public String getUserName() {
	    return this.getString(USER_KEY);
	  }

  public void setUserName(String user_name) {
	    this.put(USER_KEY, user_name);
	  }
  public int getScore() {
    return (Integer)this.get(SCORE_KEY);
  	}

  public void setScore(int score) {
    this.put(SCORE_KEY,score);
  	}
 
}
