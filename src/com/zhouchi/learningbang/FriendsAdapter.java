package com.zhouchi.learningbang;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.avos.avoscloud.AVUser;

public class FriendsAdapter extends BaseAdapter{
	Context mContext;
	List<AVUser> friends;

	public FriendsAdapter(Context context, List<AVUser> friends) {
	    mContext = context;
	    this.friends = friends;
	  }

	  @Override
	  public int getCount() {
	    return friends != null ? friends.size() : 0;
	  }

	  @Override
	  public Object getItem(int position) {
	    if (friends != null)
	      return friends.get(position);
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
	      convertView = LayoutInflater.from(mContext).inflate(R.layout.friends_item, null);
	      holder = new ViewHolder();
	      holder.friendsUsername = (TextView)convertView.findViewById(R.id.friendsUserName);
	      
	      convertView.setTag(holder);
	    }
	    else
	    {
	    	holder = (ViewHolder)convertView.getTag();
	    }
	    
	    AVUser AVUser = friends.get(position);
	    if (AVUser != null) {
	    	holder.friendsUsername.setText(AVUser.getUsername());
	    	
	    }
	    	
	    
	    return convertView;
	  }
	  
	  static class ViewHolder
	  {
		  
		  TextView friendsUsername;
		  
	  }
	  

	

}
