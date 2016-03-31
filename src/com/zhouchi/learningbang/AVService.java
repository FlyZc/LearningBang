package com.zhouchi.learningbang;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.*;
import com.avos.avoscloud.search.AVSearchQuery;
import com.zhouchi.learningbang.Todo;

import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzw on 14-9-11.
 */
public class AVService {
	
	static int ownScore =0;
  public static void countDoing(String doingObjectId, CountCallback countCallback) {
    AVQuery<AVObject> query = new AVQuery<AVObject>("DoingList");
    query.whereEqualTo("doingListChildObjectId", doingObjectId);
    Calendar c = Calendar.getInstance();
    c.add(Calendar.MINUTE, -10);
    // query.whereNotEqualTo("userObjectId", userId);
    query.whereGreaterThan("createdAt", c.getTime());
    query.countInBackground(countCallback);
  }
  


  //Use callFunctionMethod
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static void getAchievement(String userObjectId) {
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("userObjectId", userObjectId);
    AVCloud.callFunctionInBackground("hello", parameters,
        new FunctionCallback() {
          @Override
          public void done(Object object, AVException e) {
            if (e == null) {
              Log.e("at", object.toString());// processResponse(object);
            } else {
              // handleError();
            }
          }
        });
  }

  public static void createDoing(String userId, String doingObjectId) {
    AVObject doing = new AVObject("DoingList");
    doing.put("userObjectId", userId);
    doing.put("doingListChildObjectId", doingObjectId);
    doing.saveInBackground();
  }

  public static void requestPasswordReset(String email, RequestPasswordResetCallback callback) {
    AVUser.requestPasswordResetInBackground(email, callback);
  }

  public static void findDoingListGroup(FindCallback<AVObject> findCallback) {
    AVQuery<AVObject> query = new AVQuery<AVObject>("DoingListGroup");
    query.orderByAscending("Index");
    query.findInBackground(findCallback);
  }

  public static void findChildrenList(String groupObjectId, FindCallback<AVObject> findCallback) {
    AVQuery<AVObject> query = new AVQuery<AVObject>("DoingListChild");
    query.orderByAscending("Index");
    query.whereEqualTo("parentObjectId", groupObjectId);
    query.findInBackground(findCallback);
  }

  public static void initPushService(Context ctx) {
    PushService.setDefaultPushCallback(ctx, LoginActivity.class);
    PushService.subscribe(ctx, "public", LoginActivity.class);
    AVInstallation.getCurrentInstallation().saveInBackground();
  }

  public static void signUp(String username, String password, String email,String sex,String phoneNum, SignUpCallback signUpCallback) {
    AVUser user = new AVUser();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    user.put("sex", sex);
    user.setMobilePhoneNumber(phoneNum);
    user.put("score", 0);
    user.put("sign_date", 0);
    user.signUpInBackground(signUpCallback);
  }

  public static void logout() {
    AVUser.logOut();
  }

  public static void createAdvice(String userId, String advice, SaveCallback saveCallback) {
    AVObject doing = new AVObject("SuggestionByUser");
    doing.put("UserObjectId", userId);
    doing.put("UserSuggestion", advice);
    doing.saveInBackground(saveCallback);
  }
  
  //2015-5-19
  public static void fetchTodoById(String objectId,GetCallback<AVObject> getCallback) {
	    Todo todo = new Todo();
	    todo.setObjectId(objectId);
	    // ͨ��Fetch��ȡcontent����
	    todo.fetchInBackground(getCallback);
	  }
  

	  public static String createOrUpdateTodo(String objectId, String content,String put_user,int question_score, String question_pic,SaveCallback saveCallback) {
		  final Todo todo = new Todo();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		      todo.setObjectId(objectId);
		    }
		    todo.setContent(content);
		    todo.setPutUser(put_user);
		    todo.setQuestionScore(question_score);
		    todo.setQuestionPic(question_pic);
		    // �첽����
		    todo.saveInBackground(saveCallback);
		    return todo.getObjectId();
	  }
	  
	  public static String createOrUpdateTodoNoFile(String objectId, String content,String put_user,int question_score,SaveCallback saveCallback) {
		  final Todo todo = new Todo();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		      todo.setObjectId(objectId);
		    }
		    todo.setContent(content);
		    todo.setPutUser(put_user);
		    todo.setQuestionScore(question_score);
		    // �첽����
		    todo.saveInBackground(saveCallback);
		    return todo.getObjectId();
	  }
	  
	  public static void createOrUpdateExperience(String objectId, String title,String content,String put_user,String experience_pic,SaveCallback saveCallback) {
		  final Experience experience = new Experience();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		    	experience.setObjectId(objectId);
		    }
		    experience.setContent(content);
		    experience.setPutUser(put_user);
		    experience.setTitle(title);
		    experience.setExperiencePic(experience_pic);
		    // �첽����
		    experience.saveInBackground(saveCallback);
	  }
	  
	  public static void createOrUpdateExperienceNoFile(String objectId, String title,String content,String put_user,SaveCallback saveCallback) {
		  final Experience experience = new Experience();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		    	experience.setObjectId(objectId);
		    }
		    experience.setContent(content);
		    experience.setPutUser(put_user);
		    experience.setTitle(title);
		    // �첽����
		    experience.saveInBackground(saveCallback);
	  }
	  
	  public static void createOrUpdateScoreRecord(String objectId, String user_name,int score,SaveCallback saveCallback) {
		  final ScoreRecord record = new ScoreRecord();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		    	record.setObjectId(objectId);
		    }
		    record.setUserName(user_name);
		    record.setScore(score);
		    // �첽����
		    record.saveInBackground(saveCallback);
	  }
	  public static void createOrUpdateAnswer(String objectId, String content,String answer_user,String question_id, String answer_pic, SaveCallback saveCallback) {
		  final Answer todo = new Answer();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		      todo.setObjectId(objectId);
		    }
		    todo.setContent(content);
		    todo.setAnswerUser(answer_user);
		    todo.setPutQuestionKey(question_id);
		    todo.setAnswerPic(answer_pic);
		    // �첽����
		    todo.saveInBackground(saveCallback);
	  }
	  
	  public static void createOrUpdateAnswerNoFile(String objectId, String content,String answer_user,String question_id, SaveCallback saveCallback) {
		  final Answer todo = new Answer();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		      todo.setObjectId(objectId);
		    }
		    todo.setContent(content);
		    todo.setAnswerUser(answer_user);
		    todo.setPutQuestionKey(question_id);
		    // �첽����
		    todo.saveInBackground(saveCallback);
	  }
	  
	  public static void createOrUpdateComment(String objectId, String content,String comment_user, String experience_id,SaveCallback saveCallback) {
		  final Comment comment = new Comment();
		    if (!TextUtils.isEmpty(objectId)) {
		      // �������objectId��������ɸ��²�����
		      comment.setObjectId(objectId);
		    }
		    comment.setContent(content);
		    comment.setCommentUser(comment_user);
		    comment.setExperienceId(experience_id);
		    
		    // �첽����
		    comment.saveInBackground(saveCallback);
	  }

	  public static List<Todo> findTodos() {
	    // ��ѯ��ǰTodo�б�
	    AVQuery<Todo> query = AVQuery.getQuery(Todo.class);
	    query.whereNotEqualTo("put_user", AVUser.getCurrentUser().getUsername());
	    query.whereNotEqualTo("state", true);
	    // ���ո���ʱ�併������
	    query.orderByDescending("updatedAt");
	    // ��󷵻�1000��
	    query.limit(1000);
	    try {
	      return query.find();
	    } catch (AVException exception) {
	      Log.e("tag", "Query todos failed.", exception);
	      return Collections.emptyList();
	    }
	  }
	  
	  public static List<AVUser> findFriends() {
		  //��ѯ��ע��
			AVQuery<AVUser> followeeQuery = AVUser.followeeQuery(AVUser.getCurrentUser().getObjectId(), AVUser.class);
			followeeQuery.include("followee");
			//AVQuery<AVUser> followeeQuery = userB.followeeQuery(AVUser.class);
			try {
				return followeeQuery.find();
			} catch (AVException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Collections.emptyList();
			}
	  }
	  
	  public static List<AVStatus> findMessage() {
		  AVStatusQuery inboxQuery = AVStatus.inboxQuery(AVUser.getCurrentUser(),AVStatus.INBOX_TYPE.PRIVATE.toString());
		  inboxQuery.setLimit(50);  //������෵��50��״̬
		  inboxQuery.setSinceId(0);  //��ѯ���ص�status��messageId�������sinceId��Ĭ��Ϊ0
		  
		  try {
			return inboxQuery.find();
		} catch (AVException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			return Collections.emptyList();
		}

		    
			
	  }
	  public static List<Todo> findMyTodos() {
		    // ��ѯ��ǰTodo�б�
		    AVQuery<Todo> query = AVQuery.getQuery(Todo.class);
		    query.whereEqualTo("put_user", AVUser.getCurrentUser().getUsername());
		    // ���ո���ʱ�併������
		    query.orderByDescending("updatedAt");
		    // ��󷵻�1000��
		    query.limit(1000);
		    try {
		      return query.find();
		    } catch (AVException exception) {
		      Log.e("tag", "Query todos failed.", exception);
		      return Collections.emptyList();
		    }
	  }
	  
	  public static List<Comment> findComments(String experienceId) {
		    // ��ѯ��ǰTodo�б�
		    AVQuery<Comment> query = AVQuery.getQuery(Comment.class);
		    query.whereEqualTo("experience_id", experienceId);
		    // ���ո���ʱ�併������
		    query.orderByDescending("updatedAt");
		    // ��󷵻�1000��
		    query.limit(1000);
		    try {
		      return query.find();
		    } catch (AVException exception) {
		      Log.e("tag", "Query todos failed.", exception);
		      return Collections.emptyList();
		    }
	  }
	  //����ÿ�ε�½���´��ⱻ���ɺ���»���
	  public static void ownScore(final AVUser user) {
		  ownScore =0;
		  AVQuery<ScoreRecord> query = new AVQuery<ScoreRecord>("ScoreRecord");
		  query.whereEqualTo("user_name", user.getUsername());
		  query.findInBackground(new FindCallback<ScoreRecord>() {
		      public void done(List<ScoreRecord> avObjects, AVException e) {
		          if (e == null) {
		              Log.d("�ɹ�", "��ѯ��" + avObjects.size() + " ����������������e");
		              for(int i=0;i<avObjects.size();i++) {
		            	  ScoreRecord tRecord = avObjects.get(i);
		            	  
		            	  ownScore += tRecord.getScore();
		            	  System.out.println(ownScore);
		            	  tRecord.deleteInBackground();
		              }
		              
		           System.out.println((Integer)user.get("score"));
		              final int ttscore = (Integer)user.get("score")+ownScore;
		    		  user.put("score",ttscore);
		    		  System.out.println(ttscore);
		    		  user.saveInBackground(new SaveCallback() {
		    			   @Override
		    			   public void done(AVException e) {
		    			        if (e == null) {
		    			        	MainActivity.mainPersonalScore.setText(ttscore+"");
		    			            Log.i("LeanCloud", "Save successfully " + ttscore);
		    			            
		    			        } else {
		    			            Log.e("LeanCloud", "Save failed.");
		    			        }
		    			    }
		    			});
		          } else {
		              Log.d("ʧ��", "��ѯ����: " + e.getMessage());
		          }
		      }
		  });
		  
	  }
	  public static List<Answer> findAnswers(String question_id) {
		    // ��ѯ��ǰTodo�б�
		    AVQuery<Answer> query = AVQuery.getQuery(Answer.class);
		    query.whereEqualTo("question_id", question_id);
		    // ���ո���ʱ�併������
		    query.orderByDescending("updatedAt");
		    // ��󷵻�1000��
		    query.limit(1000);
		    try {
		      return query.find();
		    } catch (AVException exception) {
		      Log.e("tag", "Query answers failed.", exception);
		      return Collections.emptyList();
		    }
		  }
	  
	  public static List<Experience> findExperiences() {
		    // ��ѯ��ǰTodo�б�
		    AVQuery<Experience> query = AVQuery.getQuery(Experience.class);
		    
		    // ���ո���ʱ�併������
		    query.orderByDescending("updatedAt");
		    // ��󷵻�1000��
		    query.limit(1000);
		    try {
		      return query.find();
		    } catch (AVException exception) {
		      Log.e("tag", "Query todos failed.", exception);
		      return Collections.emptyList();
		    }
		  }

	  public static void searchQuery(String inputSearch) {
	    AVSearchQuery searchQuery = new AVSearchQuery(inputSearch);
	    searchQuery.search();
	  }
	  
	  public static void updateScore(int dScore) {
		  AVUser currentUser = AVUser.getCurrentUser();
		  int originScore = currentUser.getInt("score");
		  currentUser.put("score",originScore-dScore);
		  currentUser.saveInBackground(new SaveCallback() {
		     @Override
		     public void done(AVException e) {
		          if (e == null) {
		              Log.i("LeanCloud", "Save successfully.");
		          } else {
		              Log.e("LeanCloud", "Save failed.");
		          }
		      }
		  });
		  
	  }
	  
	  
  
  
  
}
