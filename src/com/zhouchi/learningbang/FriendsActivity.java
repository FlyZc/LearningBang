package com.zhouchi.learningbang;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.SaveCallback;


public class FriendsActivity extends ListActivity{
	private volatile List<AVUser> friends;
	private Dialog progressDialog;
	public static final String TAG = FriendsActivity.class.getName();
	

	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		@Override
		protected Void doInBackground(Void... params) {
		      friends = AVService.findFriends();
		      return null;
		}

		@Override
		protected void onPreExecute() {
			FriendsActivity.this.progressDialog =
					ProgressDialog.show( FriendsActivity.this, "", "Loading...", true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {

			super.onProgressUpdate(values);
		}
		
		@Override
	    protected void onPostExecute(Void result) {
			// ’πœ÷ListView
			FriendsAdapter adapter = new FriendsAdapter(FriendsActivity.this, friends);
			setListAdapter(adapter);
			registerForContextMenu(getListView());
			FriendsActivity.this.progressDialog.dismiss();
			TextView empty = (TextView) findViewById(android.R.id.empty);
			if (friends != null && !friends.isEmpty()) {
				empty.setVisibility(View.INVISIBLE);
			} else {
				empty.setVisibility(View.VISIBLE);
			}
		}

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.friends);
		 TextView empty = (TextView) findViewById(android.R.id.empty);
		 empty.setVisibility(View.VISIBLE);
		 new RemoteDataTask().execute();
		 TextView test = (TextView)findViewById(R.id.friendsEmail);
		 test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(FriendsActivity.this,MessageActivity.class);
				startActivity(intent);
				
			}
		});
	   	 
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	   super.onListItemClick(l, v, position, id);
	   Intent i = new Intent(this, ReplyActivity.class);
	   i.putExtra("reply_userId", friends.get(position).getObjectId());
	   startActivityForResult(i, 1);
	  }
}