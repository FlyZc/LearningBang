package com.zhouchi.learningbang;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodoAdapter extends BaseAdapter {

  Context mContext;
  List<Todo> todos;

  public TodoAdapter(Context context, List<Todo> todos) {
    mContext = context;
    this.todos = todos;
  }

  @Override
  public int getCount() {
    return todos != null ? todos.size() : 0;
  }

  @Override
  public Object getItem(int position) {
    if (todos != null)
      return todos.get(position);
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
      convertView = LayoutInflater.from(mContext).inflate(R.layout.questions_item, null);
      holder = new ViewHolder();
      holder.questionsUsername = (TextView)convertView.findViewById(R.id.questionsUsername);
      holder.questionsDescribe = (TextView)convertView.findViewById(R.id.questionsDescribe);
      holder.questionsScore = (TextView)convertView.findViewById(R.id.questionsScore);
      convertView.setTag(holder);
    }
    else
    {
    	holder = (ViewHolder)convertView.getTag();
    }
    
    Todo todo = todos.get(position);
    if (todo != null) {
    	holder.questionsUsername.setText(todo.getPutUser());
    	holder.questionsDescribe.setText(todo.getContent());
    	holder.questionsScore.setText(todo.getQuestionScore()+"");
    }
    	
    
    return convertView;
  }
  
  static class ViewHolder
  {
	  
	  TextView questionsUsername;
	  TextView questionsDescribe;
	  ImageView questionsPic;
	  TextView questionsScore;
  }
  

}
