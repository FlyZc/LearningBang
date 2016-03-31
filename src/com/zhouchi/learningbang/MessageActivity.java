package com.zhouchi.learningbang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatusQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.InboxStatusFindCallback;
import com.avos.avoscloud.SaveCallback;

public class MessageActivity extends ListActivity{
	private List<AVStatus> Messages;
	private Dialog progressDialog;
	public static final String TAG = AnswerQuestionsActivity.class.getName();

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		@Override
		protected Void doInBackground(Void... params) {
		     // Messages = AVService.findMessage();
		      return null;
		}

		@Override
		protected void onPreExecute() {
			MessageActivity.this.progressDialog =
					ProgressDialog.show( MessageActivity.this, "", "Loading...", true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			
			// 展现ListView
			
		}

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_message);
		 AVStatusQuery inboxQuery = AVStatus.inboxQuery(AVUser.getCurrentUser(),
				 AVStatus.INBOX_TYPE.PRIVATE.toString());
			inboxQuery.setLimit(50);  
			inboxQuery.setSinceId(0);  
			inboxQuery.findInBackground(new InboxStatusFindCallback(){
			  @Override
			  public void done(final List<AVStatus> parseObjects, final AVException parseException) {
				  Messages = parseObjects;
				  MessageAdapter adapter = new MessageAdapter(MessageActivity.this, parseObjects);
					setListAdapter(adapter);
					registerForContextMenu(getListView());
					//MessageActivity.this.progressDialog.dismiss();
					TextView empty = (TextView) findViewById(android.R.id.empty);
					if (parseObjects != null && !parseObjects.isEmpty()) {
						System.out.println("Yes");
						empty.setVisibility(View.INVISIBLE);
					} else {
						empty.setVisibility(View.VISIBLE);
					}
			  }
			});
		 TextView empty = (TextView) findViewById(android.R.id.empty);
		 empty.setVisibility(View.VISIBLE);
		 
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	   super.onListItemClick(l, v, position, id);
	    // 打开编辑页面，传递content和objectId过去
	    Intent i = new Intent(this, ReplyActivity.class);
	    i.putExtra("content", Messages.get(position).getData().get("content").toString());
	    i.putExtra("reply_userId", Messages.get(position).getData().get("author").toString());
	    startActivityForResult(i, 1);
	  }
}