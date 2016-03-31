package com.zhouchi.learningbang;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;


public class MyQuestionsDetailedActivity extends ListActivity{
	//传进来的问题的id
	private String questionObjectId;
	//传进来的回答者的id
	private String answerObjectId;
	private String question_pic;
	private String content;
	private TextView contentText;
	private TextView scoreText;
	private volatile List<Answer> answers;
	private ImageView detailQuestionPic;
	private Dialog progressDialog;
	private int score;

	public static final String TAG = MyQuestionsDetailedActivity.class.getName();
	
	
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
	    // Override this method to do custom remote calls
	    @Override
	    protected Void doInBackground(Void... params) {
	    	Bundle extras = getIntent().getExtras();
			 if (extras != null) {
				 questionObjectId = extras.getString("questionObjectId");
				 answers = AVService.findAnswers(questionObjectId);	 
			 }
			 return null;
	    }

	    @Override
	    protected void onPreExecute() {
	      MyQuestionsDetailedActivity.this.progressDialog =
	          ProgressDialog.show( MyQuestionsDetailedActivity.this, "", "Loading...", true);
	      super.onPreExecute();
	    }

	    @Override
	    protected void onProgressUpdate(Void... values) {

	      super.onProgressUpdate(values);
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	      // 展现ListView
	      AnswerAdapter adapter = new AnswerAdapter(MyQuestionsDetailedActivity.this, answers);
	      setListAdapter(adapter);
	      registerForContextMenu(getListView());
	      MyQuestionsDetailedActivity.this.progressDialog.dismiss();   
	  
	    }
	}
	@Override
	protected void onPause() {
	  super.onPause();
	  // 页面统计，结束
	  AVAnalytics.onPause(this);
	}

	@Override
	protected void onResume() {
	  super.onResume();
	  // 页面统计，开始
	  AVAnalytics.onResume(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_details_for_answer);		 
		 contentText = (TextView)findViewById(R.id.detailAnswerDescribe);
		 scoreText = (TextView)findViewById(R.id.detailAnswerScore);	
		 detailQuestionPic = (ImageView)findViewById(R.id.detailAnswerPic);
		 Bundle extras = getIntent().getExtras();
		 if (extras != null) {
			 content = extras.getString("content");
			 //System.out.println(content);	 
			 score = extras.getInt("score");
			 //System.out.println(score);
			 questionObjectId = extras.getString("questionObjectId");

			 if (content != null) {
				 contentText.setText(content);
				 scoreText.setText(score + "");
			 }
		 }
		 AVQuery<Todo> query = new AVQuery<Todo>("Todo");
		 Todo question=null;
		 try {
			 question = query.get(questionObjectId);
			 question_pic = question.getString("question_pic");
			 if(question_pic!=null) {
				 AVFile.withObjectIdInBackground(question_pic, new GetFileCallback<AVFile>() {

						@Override
						public void done(AVFile avFile, AVException e) {
							// TODO Auto-generated method stub
						        avFile.getDataInBackground(new GetDataCallback() {
						          @Override
						          public void done(byte[] bytes, AVException e) {
						        	  if(e == null) {
						        		  Log.d("leancloud", "done,size is " + bytes.length);
								          Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);							 
								          detailQuestionPic.setImageBitmap(bitMap);
						        	  }
						            
						          }
						        });
						}
					});	
			 } 	 		 
		 } catch (AVException e) {
		     // e.getMessage()
		 }
		 new RemoteDataTask().execute();
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	   super.onListItemClick(l, v, position, id);
	    // 打开编辑页面，传递content和objectId过去
	    Intent i = new Intent(this, SingleAnswerActivity.class);
	    //传递相应问题的分数	    
	    i.putExtra("question_score",score);
	    //传递该问题特定答案的答案内容
	    i.putExtra("question_answer", answers.get(position).getContent());
	    //传递该问题的id
	    i.putExtra("question_id", questionObjectId);
	    //传递该问题的内容
	    i.putExtra("question_content",content);
	    //传递该问题特定答案的答案id
	    i.putExtra("question_answer_id", answers.get(position).getObjectId());
	    //传递回答该问题的作者信息
	    i.putExtra("question_answer_user_name", answers.get(position).getAnswerUser());
	    startActivityForResult(i, 1);
	    MyQuestionsDetailedActivity.this.finish();
	  }

}
