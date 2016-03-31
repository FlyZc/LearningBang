package com.zhouchi.learningbang;

import java.util.List;

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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class ReadExperienceActivity extends ListActivity{
	private TextView detailExperienceTitle;
	private TextView detailExperienceContent;	
	private TextView detailExperienceAuthor;
	private ImageView detailExperiencePic;
	private ImageView detailExperienceLove;
	private EditText detailExperienceCommentEdit;
	private Button detailExperienceCommentSubmit;

	private String scoreRecordObjectId;
	private String experienceObjectId;
	private String commentObjectId;
	private String experiencePic;
	private String sTitle;
	private String sContent;
	private String sAuthor;
	private String sComment;
	private volatile List<Comment> comments;
	private Dialog progressDialog;
	public static final String TAG = ReadExperienceActivity.class.getName();
	
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
	    // Override this method to do custom remote calls
	    @Override
	    protected Void doInBackground(Void... params) {
	    	//显示信息
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				sTitle = extras.getString("experience_title");
				sContent = extras.getString("experience_content");
				sAuthor = extras.getString("experience_author");
				experienceObjectId = extras.getString("experienceObjectId");
				if (sTitle != null && sContent != null && sAuthor != null) {
					detailExperienceTitle.setText(sTitle);
					detailExperienceAuthor.setText(sAuthor);
					detailExperienceContent.setText(sContent);
				}
			}
			comments = AVService.findComments(experienceObjectId);
			AVQuery<Experience> query = new AVQuery<Experience>("Experience");
			Experience experience=null;
			try {
				experience = query.get(experienceObjectId);
				experiencePic = experience.getExperiencePic();
				if(experiencePic!=null) {
					AVFile.withObjectIdInBackground(experiencePic, new GetFileCallback<AVFile>() {
						@Override
						public void done(AVFile avFile, AVException e) {
							avFile.getDataInBackground(new GetDataCallback() {
								@Override
							    public void done(byte[] bytes, AVException e) {
									if(e == null) {
										Log.d("leancloud", "done,size is " + bytes.length);
							        	Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);							 
							        	detailExperiencePic.setImageBitmap(bitMap);
							        }
							    }
							});
						}
					});		
				
				} else {
					
				}
				 
			 } catch (AVException e) {
			     // e.getMessage()
			 }
	      return null;
	    }

	    @Override
	    protected void onPreExecute() {
	    	ReadExperienceActivity.this.progressDialog =
	          ProgressDialog.show( ReadExperienceActivity.this, "", "Loading...", true);
	    	super.onPreExecute();
	    }

	    @Override
	    protected void onProgressUpdate(Void... values) {

	      super.onProgressUpdate(values);
	    }
	    
	    @Override
	    protected void onPostExecute(Void result) {
	      // 展现ListView
	      CommentAdapter adapter = new CommentAdapter(ReadExperienceActivity.this, comments);
	      setListAdapter(adapter);
	      registerForContextMenu(getListView());
	      ReadExperienceActivity.this.progressDialog.dismiss();
	      TextView empty = (TextView) findViewById(android.R.id.empty);
	      if (comments != null && !comments.isEmpty()) {
	        empty.setVisibility(View.INVISIBLE);
	      } 
	    }
	  
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details_for_experience);
		
		TextView empty = (TextView) findViewById(android.R.id.empty);
		empty.setVisibility(View.VISIBLE);
		
		detailExperienceTitle = (TextView)findViewById(R.id.detailsExperienceTitle);
		detailExperiencePic = (ImageView)findViewById(R.id.detailsExperiencePic);
		detailExperienceContent = (TextView)findViewById(R.id.detailsExperienceContent);
		detailExperienceAuthor = (TextView)findViewById(R.id.detailsExperienceAuthor);
		detailExperienceLove = (ImageView)findViewById(R.id.detailsExperienceLove);
		detailExperienceCommentEdit = (EditText)findViewById(R.id.detailsExperienceComment);
		detailExperienceCommentSubmit = (Button)findViewById(R.id.datailExperienceCommentSubmit);
																  
		new RemoteDataTask().execute();
		
		//点赞
		detailExperienceLove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(AVUser.getCurrentUser()!=null) {
					if(!sAuthor.equals(AVUser.getCurrentUser().getUsername())) {
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
						AVService.createOrUpdateScoreRecord(scoreRecordObjectId,sAuthor,1,saveCallback);
						Toast fToast = Toast.makeText(ReadExperienceActivity.this, "已赞", Toast.LENGTH_SHORT);
						fToast.show();
						
						AVQuery<AVUser> query = new AVQuery<AVUser>("_User");
						query.whereEqualTo("username", sAuthor);
						
						query.findInBackground(new FindCallback<AVUser>() {
						    public void done(List<AVUser> avObjects, AVException e) {
						        if (e == null) {
						            Log.d("成功", "查询到" + avObjects.size() + " 条符合条件的数据");
						          
						          //关注
									AVUser.getCurrentUser().followInBackground(avObjects.get(0).getObjectId(), new FollowCallback() {
									        @Override
									        public void done(AVObject object, AVException e) {
									            if (e == null) {
									                Log.i(TAG, "follow succeed.");
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
						

	
					} else {
						Toast fToast = Toast.makeText(ReadExperienceActivity.this, "你不能对自己的帖子点赞", Toast.LENGTH_SHORT);
						fToast.show();
					}
				
				} else {
					Toast toast = Toast.makeText(ReadExperienceActivity.this, "请先登录", Toast.LENGTH_SHORT);
					toast.show();
				}
				
			}
		});
		
		//发表评论
		detailExperienceCommentSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(AVUser.getCurrentUser()!=null) {
					SaveCallback saveCallback=new SaveCallback() {
				          @Override
				          public void done(AVException e) {
				            // done方法一定在UI线程执行
				            if (e != null) {
				              Log.e("CreateComment", "Update comment failed.", e);
				            }
				            Bundle bundle = new Bundle();
				            bundle.putBoolean("success", e == null);
				            Intent intent = new Intent();
				            intent.putExtras(bundle);
				            setResult(RESULT_OK, intent);
				            finish();
				        }
				     };
				     sComment = detailExperienceCommentEdit.getText().toString();
				     String commentUser = AVUser.getCurrentUser().getUsername();
				     AVService.createOrUpdateComment(commentObjectId, sComment,commentUser,experienceObjectId, saveCallback);
				     Toast toast = Toast.makeText(ReadExperienceActivity.this, "评论成功", Toast.LENGTH_SHORT);
				     toast.show();
				} else {
					Toast toast = Toast.makeText(ReadExperienceActivity.this, "请先登录", Toast.LENGTH_SHORT);
					toast.show();
				}
				
			 }
		});
		
	}
	
}