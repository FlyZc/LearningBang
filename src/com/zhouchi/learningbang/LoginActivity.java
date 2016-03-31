package com.zhouchi.learningbang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.zhouchi.learningbang.R;

public class LoginActivity extends Activity{
	private EditText loginUserName;
	private EditText loginPwd;
	private String sUserName;
	private String sPwd;
	private Button loginSubmit;
	private ProgressDialog progressDialog;
	private Button loginCancle;
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_login);
		 
		 loginUserName = (EditText)findViewById(R.id.loginUserName);
		 loginPwd = (EditText)findViewById(R.id.loginPwd);
		 loginSubmit = (Button)findViewById(R.id.loginSubmit);
		 loginSubmit.setOnClickListener(new SubmitButtonListener());
		 loginCancle = (Button)findViewById(R.id.loginCancle);
		 loginCancle.setOnClickListener(new CancleButtonListener());
		 TextView findPwd = (TextView)findViewById(R.id.find_password);
		 //找回密码
		 findPwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, FindPwdActivity.class);
				startActivity(intent);
			}
		});
		
		//注册
		TextView registe = (TextView)findViewById(R.id.tv_register);
		registe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent();
				intent2.setClass(LoginActivity.this, RegisteActivity.class);
				startActivity(intent2);
			}
		});
	}
	 
	 class SubmitButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			sUserName = loginUserName.getText().toString();
			sPwd = loginPwd.getText().toString();
			if(sUserName.isEmpty()) {
				showUserNameEmptyError();
				return;
			}
			if(sPwd.isEmpty()) {
				showUserPasswordEmptyError();
				return;
			}
			progressDialogShow();
			AVUser.logInInBackground(sUserName, sPwd, new LogInCallback() {
			    public void done(AVUser user, AVException e) {
			    	
		        	
			        if (user != null) {
			            Toast toast = Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT);
			            toast.show();
			            AVService.ownScore(user);
			            Intent intent = new Intent();
			            intent.setClass(LoginActivity.this,MainActivity.class);
			            startActivity(intent);			           
			    } else {
			    	Toast toast = Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT);
		            toast.show();
			        }
			    }
			});	
			progressDialog.dismiss();
       }
		 
	 }
	 
	 class CancleButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			loginUserName.setText(null);
			loginPwd.setText(null);
		}
		 
	 }
	 private void showUserNameEmptyError() {
			new AlertDialog.Builder(this)
					.setTitle(
							this.getResources().getString(
									R.string.dialog_error_title))
					.setMessage(
							this.getResources().getString(
									R.string.error_register_user_name_null))
					.setNegativeButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
	 
	 private void showUserPasswordEmptyError() {
			new AlertDialog.Builder(this)
					.setTitle(
							this.getResources().getString(
									R.string.dialog_error_title))
					.setMessage(
							this.getResources().getString(
									R.string.error_register_password_null))
					.setNegativeButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
	 
	 private void progressDialogShow() {
			progressDialog = ProgressDialog
					.show(this,
							this.getResources().getText(
									R.string.dialog_message_title),
							this.getResources().getText(
									R.string.dialog_text_wait), true, false);
		}
}
