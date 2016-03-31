package com.zhouchi.learningbang;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.zhouchi.learningbang.AVService;

import com.zhouchi.learningbang.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPwdActivity extends Activity{
	
	EditText emailText;
	Button findPasswordButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_find_pwd);
		 this.getActionBar().setDisplayHomeAsUpEnabled(true);
		 emailText = (EditText) findViewById(R.id.find_pwd_email);
		 findPasswordButton = (Button) findViewById(R.id.find_pwd_submit);
		 findPasswordButton.setOnClickListener(findPasswordListener);
	}
	
	OnClickListener findPasswordListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
      String email = emailText.getText()
          .toString();
      if (email != null) {
        RequestPasswordResetCallback callback=new RequestPasswordResetCallback() {
          public void done(AVException e) {
            if (e == null) {
              Toast.makeText(FindPwdActivity.this,
                  R.string.forget_password_send_email,
                  Toast.LENGTH_LONG).show();
              Intent LoginIntent = new Intent(FindPwdActivity.this,
                  LoginActivity.class);
              startActivity(LoginIntent);
              finish();
            } else {
              showError(FindPwdActivity.this
                  .getString(R.string.forget_password_email_error));
            }
          }
        };
        AVService.requestPasswordReset(email, callback);
      } else {
				showError(FindPwdActivity.this.getResources().getString(
						R.string.error_register_email_address_null));
			}
		}
	};



protected void showError(String errorMessage) {
    showError(errorMessage, FindPwdActivity.this);
  }

  public void showError(String errorMessage, Activity activity) {
    new AlertDialog.Builder(activity)
        .setTitle(
            activity.getResources().getString(
                R.string.dialog_message_title))
        .setMessage(errorMessage)
        .setNegativeButton(android.R.string.ok,
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog,
                                  int which) {
                dialog.dismiss();
              }
            }).show();
  }
}
