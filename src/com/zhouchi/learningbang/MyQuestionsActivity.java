package com.zhouchi.learningbang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class MyQuestionsActivity extends ListActivity {
	private volatile List<Todo> todos;
	private Dialog progressDialog;

	public static final String TAG = AnswerQuestionsActivity.class.getName();
	
	private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
		// Override this method to do custom remote calls
		@Override
		protected Void doInBackground(Void... params) {
			todos = AVService.findMyTodos();
			return null;
		}

		@Override
		protected void onPreExecute() {
			MyQuestionsActivity.this.progressDialog =
					ProgressDialog.show( MyQuestionsActivity.this, "", "Loading...", true);
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
    
		@Override
		protected void onPostExecute(Void result) {
			// 展现ListView
			TodoAdapter adapter = new TodoAdapter(MyQuestionsActivity.this, todos);
			setListAdapter(adapter);
			registerForContextMenu(getListView());
			MyQuestionsActivity.this.progressDialog.dismiss();
			TextView empty = (TextView) findViewById(android.R.id.empty);
			if (todos != null && !todos.isEmpty()) {
				empty.setVisibility(View.INVISIBLE);
			} else {
				empty.setVisibility(View.VISIBLE);
			}
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		TextView empty = (TextView) findViewById(android.R.id.empty);
		empty.setVisibility(View.VISIBLE);
		new RemoteDataTask().execute();
   	 
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// 打开编辑页面，传递content和objectId过去
		Intent i = new Intent(this, MyQuestionsDetailedActivity.class);
		i.putExtra("questionObjectId", todos.get(position).getObjectId());
		i.putExtra("content", todos.get(position).getContent());
		i.putExtra("score", todos.get(position).getQuestionScore());
		startActivityForResult(i, 1);
	}
}
