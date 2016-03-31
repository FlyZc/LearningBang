package com.zhouchi.learningbang;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVStatus;
import com.avos.avoscloud.AVStatus;

public class MessageAdapter extends BaseAdapter{
	Context mContext;
	List<AVStatus> Message;

	public MessageAdapter(Context context, List<AVStatus> Message) {
	    mContext = context;
	    this.Message = Message;
	  }

	  @Override
	  public int getCount() {
	    return Message != null ? Message.size() : 0;
	  }

	  @Override
	  public Object getItem(int position) {
	    if (Message != null)
	      return Message.get(position);
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
	      convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
	      holder = new ViewHolder();
	      holder.messageInfo = (TextView)convertView.findViewById(R.id.message_info);
	      
	      convertView.setTag(holder);
	    }
	    else
	    {
	    	holder = (ViewHolder)convertView.getTag();
	    }
	    
	    AVStatus AVStatus = Message.get(position);
	    if (AVStatus != null) {
	    	holder.messageInfo.setText(AVStatus.getData().get("content").toString());
	    	
	    }
	    	
	    return convertView;
	  }
	  
	  static class ViewHolder
	  {
		  
		  TextView messageInfo;
		  
	  }
	  

	

}
