package com.zhouchi.learningbang;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.avos.avoscloud.SaveCallback;

public class SingleAnswerActivity extends Activity{
	
	private Button singleAnswerBack;
	private TextView answerText;
	private ImageView answerPic;
	private String questionContent;
	private String questionObjectId;
	private TextView attentionSubmit;
	private String answer_pic;
	private int score;
	private Button singleAnswerAccept;
	private String scoreRecordObjectId;
	private String questionAnswerUserName;
	private String questionAnswerObjectId;
	public static final String TAG = SingleAnswerActivity.class.getName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.singleanswer);
		 
		 Bundle extras = getIntent().getExtras();
		 questionObjectId = extras.getString("question_id");
		 questionContent = extras.getString("question_content");
		 questionAnswerObjectId = extras.getString("question_answer_id");
		 questionAnswerUserName = extras.getString("question_answer_user_name");
		 score = extras.getInt("question_score");
		 //关注该答题者
		 attentionSubmit = (TextView)findViewById(R.id.singleanswer_add);
		 attentionSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(SingleAnswerActivity.this)
				.setTitle("我要关注")
				.setMessage("是否关注该答案提出者");
				
				setPositiveButton(builder);
				setNegativeButton(builder).create().show();
				
			}
		});
		 
		//返回
		singleAnswerBack = (Button)findViewById(R.id.singleanswer_return_back);
		singleAnswerBack.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SingleAnswerActivity.this, MyQuestionsDetailedActivity.class);
				i.putExtra("questionObjectId", questionObjectId);
				i.putExtra("content",questionContent);
				i.putExtra("score", score);
				startActivityForResult(i, 1);
				SingleAnswerActivity.this.finish();
				
			}
		});
		 
		//显示答案
		answerText = (TextView)findViewById(R.id.singleanswer_describe);
		answerPic = (ImageView)findViewById(R.id.singleanswer_pic);
		if (extras != null) {
			String content = extras.getString("question_answer");
			
			System.out.println(score);
			//String question_answer_user_name = extras.getString("question_answer_user_name");
			if (content != null) {
				answerText.setText(content);
			}
		}
		AVQuery<Answer> query = new AVQuery<Answer>("Answer");
		Answer answer=null;
		 try {
			 answer = query.get(questionAnswerObjectId);
			 answer_pic = answer.getString("answer_pic");
			 if(answer_pic!=null) {
				 AVFile.withObjectIdInBackground(answer_pic, new GetFileCallback<AVFile>() {
						@Override
						public void done(AVFile avFile, AVException e) {
							// TODO Auto-generated method stub
						    avFile.getDataInBackground(new GetDataCallback() {
						    	@Override
						        public void done(byte[] bytes, AVException e) {
						    		if(e == null) {
						    			Log.d("leancloud", "done,size is " + bytes.length);
						        		Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);							 
						        		answerPic.setImageBitmap(bitMap);
						        	}
						        }
						    });
						}
					});
			 }
			 
			 
		 } catch (AVException e) {
		     // e.getMessage()
		 }
		
		//接受该答案
		singleAnswerAccept = (Button)findViewById(R.id.singleanswer_accept);
		singleAnswerAccept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AVQuery<AVObject> query = new AVQuery<AVObject>("Todo");
				AVObject search = null;
				try {
				    search = query.get(questionObjectId);
				} catch (AVException e) {
				    // e.getMessage()
				}
				if(search != null && search.getBoolean("state")!=true) {
					SaveCallback saveCallback=new SaveCallback() {
						@Override
				        public void done(AVException e) {
							// done方法一定在UI线程执行
				            if (e != null) {
				              Log.e("CreateTodo", "Update todo failed.", e);
				            }
				            Bundle bundle = new Bundle();
				            bundle.putBoolean("success", e == null);
				            Intent intent = new Intent();
				            intent.putExtras(bundle);
				            setResult(RESULT_OK, intent);
				            finish();
				          }
				        };
					AVService.createOrUpdateScoreRecord(scoreRecordObjectId,questionAnswerUserName,score,saveCallback);
					Toast toast = Toast.makeText(SingleAnswerActivity.this, "采纳成功", Toast.LENGTH_SHORT);
					toast.show();
					search.put("state", true);
					search.saveInBackground();
				} else {
					Toast toast = Toast.makeText(SingleAnswerActivity.this, "你已经采纳某答案，不能再次采纳", Toast.LENGTH_SHORT);
					toast.show();
				}			
			}
		});
				
	}
	
	private AlertDialog.Builder setPositiveButton(AlertDialog.Builder builder) {
		return builder.setPositiveButton("关注",new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
				query.whereEqualTo("username", questionAnswerUserName);
				query.findInBackground(new FindCallback<AVUser>() {
				    public void done(List<AVUser> avObjects, AVException e) {
				        if (e == null) {
				            Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
				            //关注
							AVUser.getCurrentUser().followInBackground(avObjects.get(0).getObjectId(), 
									new FollowCallback() {
							        @Override
							        public void done(AVObject object, AVException e) {
							            if (e == null) {
							                Log.i(TAG, "follow succeed.");
							                Toast toast = Toast.makeText(SingleAnswerActivity.this, "关注成功", 
							                		Toast.LENGTH_SHORT);
							                toast.show();
							            } else if (e.getCode() == AVException.DUPLICATE_VALUE) {
							                Log.w(TAG, "Already followed.");
							            }
							        }
							    });
				        } else {
				            Log.d("失败", "查询错误: " + e.getMessage());
				        }
				    }
				});
			}
		});
	}
	private AlertDialog.Builder setNegativeButton(AlertDialog.Builder builder) {
		return builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
