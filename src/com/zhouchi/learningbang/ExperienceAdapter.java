package com.zhouchi.learningbang;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExperienceAdapter extends BaseAdapter {

  Context mContext;
  List<Experience> experiences;

  public ExperienceAdapter(Context context, List<Experience> experiences) {
    mContext = context;
    this.experiences = experiences;
  } 

  @Override
  public int getCount() {
    return experiences != null ? experiences.size() : 0;
  }

  @Override
  public Object getItem(int position) {
    if (experiences != null)
      return experiences.get(position);
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
      convertView = LayoutInflater.from(mContext).inflate(R.layout.social_experience_item, null);
      holder = new ViewHolder();
      holder.experienceTitle = (TextView)convertView.findViewById(R.id.experienceItemTitle);
      holder.experienceUser = (TextView)convertView.findViewById(R.id.experienceItemUser);
      convertView.setTag(holder);
    }
    else {
    	holder = (ViewHolder)convertView.getTag();
    }
    
    Experience Experience = experiences.get(position);
    if (Experience != null) {
    	holder.experienceTitle.setText(Experience.getTitle());
    	holder.experienceUser.setText(Experience.getPutUser());
    }
    return convertView;
  }
  
  static class ViewHolder
  {
	  
	  TextView experienceTitle;
	  TextView experienceUser;
	 
  }
  

}
