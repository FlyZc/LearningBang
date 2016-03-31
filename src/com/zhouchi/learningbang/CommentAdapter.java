package com.zhouchi.learningbang;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhouchi.learningbang.CommentAdapter.ViewHolder;

public class CommentAdapter extends BaseAdapter {

	  Context mContext;
	  List<Comment> Comments;

	  public CommentAdapter(Context context, List<Comment> Comments) {
	    mContext = context;
	    this.Comments = Comments;
	  }

	  @Override
	  public int getCount() {
	    return Comments != null ? Comments.size() : 0;
	  }

	  @Override
	  public Object getItem(int position) {
	    if (Comments != null)
	      return Comments.get(position);
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
	      convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, null);
	      holder = new ViewHolder();
	      holder.CommentUser = (TextView)convertView.findViewById(R.id.commentUser);
	      holder.CommentContent = (TextView)convertView.findViewById(R.id.commentContent);
	      convertView.setTag(holder);
	    }
	    else {
	    	holder = (ViewHolder)convertView.getTag();
	    }
	    
	    Comment Comment = Comments.get(position);
	    if (Comment != null) {
	    	holder.CommentUser.setText(Comment.getCommentUser());
	    	holder.CommentContent.setText(Comment.getContent());
	    }
	    	    
	    return convertView;
	  }
	  
	  static class ViewHolder {
		  
		  TextView CommentUser;
		  TextView CommentContent;
		  
	  }
	  
}
