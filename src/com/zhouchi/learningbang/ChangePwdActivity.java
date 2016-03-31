package com.zhouchi.learningbang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

public class ChangePwdActivity extends Activity{
	private EditText changePwdOld;
	private EditText changePwdNew;
	private Button changePwdSubmit;
	private String pwdOld;
	private String pwdNew;
	private int state;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.changepwd);
		 
		 changePwdOld = (EditText)findViewById(R.id.etChangePwdOld);
		 changePwdNew = (EditText)findViewById(R.id.etChangePwdNew);
		 changePwdSubmit = (Button)findViewById(R.id.btnChangePwd);
		 
		 changePwdSubmit.setOnClickListener(new ChangePwdOnClickListener());
	} 
	
	class ChangePwdOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			pwdOld = changePwdOld.getText().toString();
			pwdNew = changePwdNew.getText().toString();
			
			//获取当前用户信息
			AVUser currentUser = AVUser.getCurrentUser();
			AVUser.logInInBackground(currentUser.getUsername(), pwdOld, new LogInCallback() {
			    public void done(AVUser user, AVException e) {
			        if (user != null) {
			            // 登录成功
			        	state=1;
			        	
			        } else {
			        	state=0;
			        	Toast toast = Toast.makeText(ChangePwdActivity.this, "请检查是否当前密码错误", Toast.LENGTH_SHORT);
				    	 toast.show();
				    	 
			        }
			    }
			});
			if(state==1) {
			currentUser.updatePasswordInBackground(pwdOld, pwdNew,new UpdatePasswordCallback() {

	  		      @Override
	  		      public void done(AVException e) {
	  		    	 Log.d("TAG","something wrong");
	  		    	 if(e==null) {
	  		    		Toast toast = Toast.makeText(ChangePwdActivity.this, "密码修改成功", Toast.LENGTH_SHORT);
		  		    	toast.show();
		  		    	AVService.logout();
		  		    	Intent intent = new Intent();
		  		    	intent.setClass(ChangePwdActivity.this,LoginActivity.class);
		  		    	startActivity(intent);
		  		    	ChangePwdActivity.this.finish();
	  		    	 }
	  		    	 /*Toast toast = Toast.makeText(ChangePwdActivity.this, "修改密码失败，请检查是否当前密码错误", Toast.LENGTH_SHORT);
	  		    	 toast.show();*/
	  		      }
	  		    });
			
		    
			}
		
		}
	}
}


