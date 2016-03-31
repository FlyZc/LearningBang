package com.zhouchi.learningbang;

import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangeNameActivity extends Activity{
	private EditText changeNameEt;
	private Button changeNameBtn;
	boolean flag=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.changename);
		 
		 changeNameEt = (EditText)findViewById(R.id.etChangeName);
		 changeNameBtn = (Button)findViewById(R.id.btnChangeName);
		 changeNameBtn.setOnClickListener(changeNameOnClickListener);
		 
	}
	OnClickListener changeNameOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			AVUser currentUser = AVUser.getCurrentUser();
			AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
			query.whereEqualTo("username", changeNameEt.getText().toString());
			query.findInBackground(new FindCallback<AVObject>() {
			    public void done(List<AVObject> avObjects, AVException e) {
			        if (e == null) {
			        	//�û����д��ڸ��û���
			            Log.d("�ɹ�", "��ѯ��" + avObjects.size() + " ����������������");
			            
			            if(avObjects.size()==0) {
			            	flag=true;
				            /*Toast toast = Toast.makeText(ChangeNameActivity.this, "�޸ĳɹ�", Toast.LENGTH_SHORT);
				            toast.show();*/
			            }
			            else {
			            	/* Toast toast = Toast.makeText(ChangeNameActivity.this, "���û����Ѵ���", Toast.LENGTH_SHORT);
					            toast.show();*/
			            }
			          
			        } else {
			        	//�û����в����ڸ��û���
			            Log.d("ʧ��", "��ѯ����: " + e.getMessage());
			           
			            
			        }
			    }
			});
			if(flag==true) {
				String tableName = "_User";
				AVObject updateUser = new AVObject(tableName);
				AVQuery<AVObject> queryUpdate = new AVQuery<AVObject>(tableName);


				try {
					updateUser = queryUpdate.get(currentUser.getObjectId());
					
					updateUser.put("username",changeNameEt.getText().toString());
					updateUser.saveInBackground(new SaveCallback() {
					   @Override
					   public void done(AVException e) {
					        if (e == null) {
					            Log.i("LeanCloud", "Save successfully.");
					        } else {
					            Log.e("LeanCloud", "Save failed.");
					        }
					    }
					});
				} catch (AVException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
	};


}
