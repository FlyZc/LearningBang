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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SocialActivity extends ListActivity{
	private volatile List<Experience> experiences;
	private Dialog progressDialog;

public static final String TAG = AnswerQuestionsActivity.class.getName();

private class RemoteDataTask extends AsyncTask<Void, Void, Void> {
	// Override this method to do custom remote calls
	@Override
	protected Void doInBackground(Void... params) {
		experiences = AVService.findExperiences();
		return null;
	}

	@Override
	protected void onPreExecute() {
		SocialActivity.this.progressDialog =
				ProgressDialog.show( SocialActivity.this, "", "Loading...", true);
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {
		// չ��ListView
		ExperienceAdapter adapter = new ExperienceAdapter(SocialActivity.this, experiences);
		setListAdapter(adapter);
		registerForContextMenu(getListView());
		SocialActivity.this.progressDialog.dismiss();
		TextView empty = (TextView) findViewById(android.R.id.empty);
		if (experiences != null && !experiences.isEmpty()) {
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
		// �򿪱༭ҳ�棬����content��objectId��ȥ
		Intent i = new Intent(this, ReadExperienceActivity.class);
		//���ݵ���experience��id
		i.putExtra("experienceObjectId", experiences.get(position).getObjectId());
		//����experience��title
		i.putExtra("experience_title", experiences.get(position).getTitle());
		//����experience��content
		i.putExtra("experience_content", experiences.get(position).getContent());
		//����experience�������
		i.putExtra("experience_author", experiences.get(position).getPutUser());
		startActivityForResult(i, 1);
	}
}
