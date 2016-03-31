package com.zhouchi.learningbang;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

public class ReplyActivity extends Activity{
	private EditText replyContent;
	private TextView messageInfo;
	private TextView messageAuthor;
	private Button replySubmit;
	private String sReplyContent;
	private String replyUserId;
	public static final String TAG = ReplyActivity.class.getName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_contact);
		 messageInfo = (TextView)findViewById(R.id.getMessage);
		 messageAuthor = (TextView)findViewById(R.id.getmessageAuthorInfo);
		 Bundle extras = getIntent().getExtras();
		 if (extras != null) {
			 replyUserId = extras.getString("reply_userId");
			 sReplyContent = extras.getString("content");
			 
			 if(sReplyContent != null) {
				 messageInfo.setText(sReplyContent);
				 String userName = null;
				try {
					userName = AVUser.getQuery().get(replyUserId).getUsername();
				} catch (AVException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 messageAuthor.setText(userName);
			 } else {
				 messageInfo.setVisibility(View.INVISIBLE);
				 messageAuthor.setVisibility(View.INVISIBLE);
			 }
		 }
		 
		 replyContent = (EditText)findViewById(R.id.contact_content);
		 replySubmit = (Button)findViewById(R.id.contact_submit);
		 replySubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(replyContent.getText().toString()!=null) {
					Map<String, Object> data = new HashMap<String, Object>();
					data.put("content", replyContent.getText().toString());
					data.put("author", AVUser.getCurrentUser().getObjectId());
					AVStatus status = AVStatus.createStatusWithData(data);
					AVStatus.sendPrivateStatusInBackgroud(status, replyUserId, new SaveCallback() {
						@Override
					    public void done(AVException parseException) {
							Log.i(TAG, "Send private status finished.");
							if(parseException == null) {
								Toast toast = Toast.makeText(ReplyActivity.this, "发送成功", Toast.LENGTH_SHORT);
								toast.show();
								ReplyActivity.this.finish();
							}
						}
					});
				} else {
					Toast toast = Toast.makeText(ReplyActivity.this, "请先填写要发送的内容", Toast.LENGTH_SHORT);
					toast.show();
				}
				
			}
		});
	}
}
