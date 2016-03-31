package com.zhouchi.learningbang;



import java.util.Date;
import java.util.List;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.zhouchi.learningbang.Todo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

	//我要求助
		private ImageView iv_ask_for_help;
		private TextView mainPersonalName;
		public static TextView mainPersonalScore;
		private LinearLayout mainSign;
		private ImageView refresh;
		private LinearLayout flag1;
		private LinearLayout flag2;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	    	AVOSCloud.initialize(this, "mjod6auojhv0y2ir8i33xsdsbj9d65o92m3860k8d6bosfdx", "kiohn4oqtvptujmqozpzasnc7ajxqvnm62ndsr0t5sju0um3");
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        if (android.os.Build.VERSION.SDK_INT > 9) {
	            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	            StrictMode.setThreadPolicy(policy);
	        }
	        flag1 = (LinearLayout)findViewById(R.id.flag1);
	        flag2 = (LinearLayout)findViewById(R.id.flag2);
	        // 启用崩溃错误报告
	        AVOSCloud.setDebugLogEnabled(true);
	        AVAnalytics.enableCrashReport(MainActivity.this, true);
	        // 注册子类
	        AVObject.registerSubclass(Todo.class);
	        AVObject.registerSubclass(Experience.class);
	        AVObject.registerSubclass(Answer.class);
	        AVObject.registerSubclass(ScoreRecord.class);
	        AVObject.registerSubclass(Comment.class);
	        
	        AVUser currentUser = AVUser.getCurrentUser();
        	
	        //初始化
	        mainPersonalName = (TextView)findViewById(R.id.mainPersonalName);
	        mainPersonalScore = (TextView)findViewById(R.id.mainPersonalScore);
	        TextView empty = (TextView) findViewById(android.R.id.empty);
	        if(currentUser!=null) {
	        	mainPersonalName.setText(currentUser.getUsername().toString());
	        	mainPersonalScore.setText(currentUser.get("score").toString());
	        	empty.setVisibility(View.INVISIBLE);
	        } else {
	        	flag1.setVisibility(View.INVISIBLE);
	        	flag2.setVisibility(View.INVISIBLE);
	   	        empty.setVisibility(View.VISIBLE);     
	        }
				 
	        //个人中心
	        ImageView userHead = (ImageView)findViewById(R.id.ivPersonalHead);
	        userHead.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser currentUser = AVUser.getCurrentUser();
					if(currentUser!=null) {
						System.out.println("有用户在线");		
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, PersonalInfoActivity.class);
						startActivity(intent);

					}
					else {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, LoginActivity.class);
						startActivity(intent);
					}
				}
			});
	        //意见反馈
	        ImageView userFeedback = (ImageView)findViewById(R.id.userFeedback);
	        userFeedback.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser currentUser = AVUser.getCurrentUser();
					if(currentUser!=null) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this,SuggestionsActivity.class);
					startActivity(intent);
					} else {
						Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
			});
	        //我来解答
	        LinearLayout mainAnswer = (LinearLayout)findViewById(R.id.mainAnswer);
	        mainAnswer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser currentUser = AVUser.getCurrentUser();
					if(currentUser!=null) {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this,AnswerQuestionsActivity.class);
						startActivity(intent);
					} else {
						Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
						toast.show();
					}
					
				}
			});
	        
	        //我要求助
	      	iv_ask_for_help = (ImageView)findViewById(R.id.ivMainHelp);
	      	iv_ask_for_help.setOnClickListener(new OnClickListener() {
	      			
	      		@Override
	      		public void onClick(View arg0) {
	      			// TODO Auto-generated method stub
	      			AVUser currentUser = AVUser.getCurrentUser();
	      			if(currentUser!=null) {
	      			Intent intent = new Intent();
	      			intent.setClass(MainActivity.this, AskForHelpActivity.class);
	      			startActivity(intent);
	      			} else {
	      				Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
						toast.show();
	      			}
	      				
	      		}
	      	});
	      	
	      	//我求助的题目
	      	LinearLayout mainMyQuestions = (LinearLayout)findViewById(R.id.mainMyQuestions);
	      	mainMyQuestions.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser currentUser = AVUser.getCurrentUser();
					if(currentUser!=null) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, MyQuestionsActivity.class);
					startActivity(intent);
					} else {
						Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
						toast.show();
					}
					
				}
			});
	      	
	      	//经验交流
	      	LinearLayout mainShare = (LinearLayout)findViewById(R.id.mainShare);
	      	mainShare.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser currentUser = AVUser.getCurrentUser();
					if(currentUser!=null) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ExperienceExchangeActivity.class);
					startActivity(intent);
					} else {
						Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
						toast.show();
					}
					
				}
			});
	      	
	      	//逛逛
	      	LinearLayout mainSocial = (LinearLayout)findViewById(R.id.mainSocial);
	      	mainSocial.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, SocialActivity.class);
					startActivity(intent);
					
				}
			});
	      	
	      	//签到
	      	mainSign = (LinearLayout)findViewById(R.id.mainSign);
	      	mainSign.setOnClickListener(new OnClickListener() {
	      		
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser currentUser = AVUser.getCurrentUser();
		      		if(currentUser==null) {
		      			Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
		      			toast.show();
		      		}
		      		else {
		      			Date date = new Date();
		      			long dateOld = currentUser.getLong("sign_date");
		      			System.out.println(dateOld);
		      			System.out.println(date.getTime());
		      			long choose = (long)((date.getTime() - dateOld)/86400000); 
		      			System.out.println(choose);
		      			if(choose >= 1) {
		      				
		      				int score = (Integer)currentUser.get("score") + 1 ;
							currentUser.put("score",score);
							currentUser.put("sign_date",date.getTime());
							currentUser.saveInBackground(new SaveCallback() {
							   @Override
							   public void done(AVException e) {
							        if (e == null) {
							            Log.i("LeanCloud", "Save successfully.");
							            AVUser currentUser = AVUser.getCurrentUser();
							            mainPersonalScore.setText(currentUser.get("score").toString());
							            Toast toast = Toast.makeText(MainActivity.this, "签到成功", Toast.LENGTH_SHORT);
							            toast.show();
							        } else {
							            Log.e("LeanCloud", "Save failed.");
							        }
							    }
							});
		      			}
		      			else {
		      				Toast toast = Toast.makeText(MainActivity.this, "你今天已签到或者签到时间未开始", Toast.LENGTH_SHORT);
		      				toast.show();
		      			}	      				
		      		}					
				}
			});
	      	
	      	//刷新个人积分
	      	refresh = (ImageView)findViewById(R.id.refreshInfo);
	      	refresh.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					AVUser cUser = AVUser.getCurrentUser();
					if(cUser == null) {
						Toast toast = Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT);
						toast.show();
					} else {
						AVService.ownScore(cUser);
					}
					
				}
			});
	      	
	      	//设置
	      	LinearLayout mainSetting = (LinearLayout)findViewById(R.id.mainSetting);
	      	mainSetting.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, Settings.class);
					startActivity(intent);
				}
			});
	      	//我的好友
	      	LinearLayout mainFriends = (LinearLayout)findViewById(R.id.mainFriends);
	      	mainFriends.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(MainActivity.this,FriendsActivity.class);
					startActivity(intent);
					
				}
			});
	      	
	    }
	    

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }

	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        // Handle action bar item clicks here. The action bar will
	        // automatically handle clicks on the Home/Up button, so long
	        // as you specify a parent activity in AndroidManifest.xml.
	        int id = item.getItemId();
	        if (id == R.id.action_settings) {
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }
	}