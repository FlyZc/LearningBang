package com.zhouchi.learningbang;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter.LengthFilter;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.zhouchi.learningbang.R;

public class RegisteActivity extends Activity {
	EditText userName;
	EditText userPassword;
	EditText userRePassword;
	EditText userPhoneNum;
	EditText userEmail;
	RadioButton boy;
	RadioButton girl;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_register);
		 this.getActionBar().setDisplayHomeAsUpEnabled(true);
		 
		 userName = (EditText)findViewById(R.id.registe_userName);
		 boy = (RadioButton)findViewById(R.id.registe_radioboy);
		 girl = (RadioButton)findViewById(R.id.registe_radiogirl);
		 userPassword = (EditText)findViewById(R.id.registe_password);
		 userRePassword = (EditText)findViewById(R.id.registe_rePassword);
		 userPhoneNum = (EditText)findViewById(R.id.registe_phoneNum);
		 userEmail = (EditText)findViewById(R.id.registeEmail);
		 Button registeSubmit = (Button)findViewById(R.id.registeSubmit);
		 registeSubmit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (userPassword.getText().toString().equals(userRePassword.getText().toString())) {
						if (!userPassword.getText().toString().isEmpty()) {
							if (!userName.getText().toString().isEmpty()) {
								if (!userEmail.getText().toString().isEmpty()) {
									progressDialogShow();
									register();
								} else {
									showError(RegisteActivity.this
											.getString(R.string.error_register_email_address_null));
								}
							} else {
								showError(RegisteActivity.this
										.getString(R.string.error_register_user_name_null));
							}
						} else {
							showError(RegisteActivity.this
									.getString(R.string.error_register_password_null));
						}
					} else {
						showError(RegisteActivity.this
								.getString(R.string.error_register_password_not_equals));
					}
				}
			});
		}

		

		public void register() {
	    SignUpCallback signUpCallback = new SignUpCallback() {
	      public void done(AVException e) {
	        progressDialogDismiss();
	        if (e == null) {
	          showRegisterSuccess();
	          Intent mainIntent = new Intent(RegisteActivity.this, MainActivity.class);
	          startActivity(mainIntent);
	          RegisteActivity.this.finish();
	        } else {
	          switch (e.getCode()) {
	            case 202:
	              showError(RegisteActivity.this
	                  .getString(R.string.error_register_user_name_repeat));
	              break;
	            case 203:
	              showError(RegisteActivity.this
	                  .getString(R.string.error_register_email_repeat));
	              break;
	            default:
	              showError(RegisteActivity.this
	                  .getString(R.string.network_error));
	              break;
	          }
	        }
	      }
	    };
	    String username = userName.getText().toString();
	    String password = userPassword.getText().toString();
	    String phoneNum = userPhoneNum.getText().toString();
	    String email = userEmail.getText().toString();
	    String sex = "ÄÐ";
	    if(boy.isChecked()) {
			 sex="ÄÐ";
		}
		else if(girl.isChecked()) {
			 sex="Å®";
		}

	    AVService.signUp(username, password, email, sex, phoneNum, signUpCallback);
		}

	  private void progressDialogDismiss() {
			if (progressDialog != null)
				progressDialog.dismiss();
		}

		private void progressDialogShow() {
			progressDialog = ProgressDialog
					.show(RegisteActivity.this,
							RegisteActivity.this.getResources().getText(
									R.string.dialog_message_title),
							RegisteActivity.this.getResources().getText(
									R.string.dialog_text_wait), true, false);
		}

		private void showRegisterSuccess() {
			new AlertDialog.Builder(RegisteActivity.this)
					.setTitle(
							RegisteActivity.this.getResources().getString(
									R.string.dialog_message_title))
					.setMessage(
							RegisteActivity.this.getResources().getString(
									R.string.success_register_success))
					.setNegativeButton(android.R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
	
protected void showError(String errorMessage) {
    showError(errorMessage, RegisteActivity.this);
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
