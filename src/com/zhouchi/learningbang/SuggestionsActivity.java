package com.zhouchi.learningbang;

import com.avos.avoscloud.feedback.FeedbackAgent;
import com.zhouchi.learningbang.R;

import android.app.Activity;
import android.os.Bundle;

public class SuggestionsActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 
		 setContentView(R.layout.avoscloud_feedback_activity_conversation);
		 FeedbackAgent agent = new FeedbackAgent(this);
		 agent.startDefaultThreadActivity();
	}

}
