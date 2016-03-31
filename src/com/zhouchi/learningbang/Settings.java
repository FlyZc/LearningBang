package com.zhouchi.learningbang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class Settings extends Activity{
	private RelativeLayout func;
	private RelativeLayout about;
	private RelativeLayout share;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.setting);	 
		 
		 func = (RelativeLayout)findViewById(R.id.setting_function_introduction);
		 about = (RelativeLayout)findViewById(R.id.setting_about_us);
		 share = (RelativeLayout)findViewById(R.id.setting_share);
		 
		 share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEMPLATE, "ª∂”≠ π”√—ßœ∞∞Ô∞Ô∞Ù»Ìº˛");
				sendIntent.setType("text/plain");
				startActivity(sendIntent);
			}
		});
		 
		 about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(Settings.this,AboutUsActivity.class);
				startActivity(intent);
			}
		});
		 
		 
	}
}
