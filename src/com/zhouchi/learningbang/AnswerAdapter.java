package com.zhouchi.learningbang;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class AnswerAdapter extends BaseAdapter {

	  Context mContext;
	  List<Answer> Answers;

	  public AnswerAdapter(Context context, List<Answer> Answers) {
	    mContext = context;
	    this.Answers = Answers;
	  }

	  @Override
	  public int getCount() {
	    return Answers != null ? Answers.size() : 0;
	  }

	  @Override
	  public Object getItem(int position) {
	    if (Answers != null)
	      return Answers.get(position);
	    else
	      return null;
	  }

	  @Override
	  public long getItemId(int position) {
	    return position;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
	    if (convertView == null) {
	      convertView = LayoutInflater.from(mContext).inflate(R.layout.detail_answer_item, null);
	      holder = new ViewHolder();
	      holder.answerItemUser = (TextView)convertView.findViewById(R.id.answerItemUser);
	      holder.answerItemDesc = (TextView)convertView.findViewById(R.id.answerItemDescribe);
	      convertView.setTag(holder);
	    }
	    else {
	    	holder = (ViewHolder)convertView.getTag();
	    }
	    
	    Answer Answer = Answers.get(position);
	    if (Answer != null) {
	    	holder.answerItemUser.setText(Answer.getAnswerUser());
	    	holder.answerItemDesc.setText(Answer.getContent());
	    	
	    }
	    	    
	    return convertView;
	  }
	  
	  static class ViewHolder {
		  
		  TextView answerItemUser;
		  TextView answerItemDesc;
		  ImageView questionsPic;
	  }
	  
}
