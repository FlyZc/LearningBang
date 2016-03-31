package com.zhouchi.learningbang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.zhouchi.learningbang.R;

public class PersonalInfoActivity extends Activity{
	
	private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	private String imgPath  = "";
	private String imgName = "";
	/*用于记录性别选择*/
	private int sexRecord = 0;
	private String[] sexItems = new String[]{"男","女"};
	private String user_pic;
	
	private TextView personalChangeSexInfo;
	private ImageView iv_head_photo;
	private TextView personalName;
	private TextView personalSex;
	private TextView personalPhoneNum;
	private TextView personalEmail;
	private Button exitLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.personalinfo);
		 personalName = (TextView)findViewById(R.id.personalNameInfo);
		 personalSex = (TextView)findViewById(R.id.personalSexInfo);
		 personalPhoneNum = (TextView)findViewById(R.id.personalPhoneNumInfo);
		 personalEmail  = (TextView)findViewById(R.id.personalEmailfo);
		 exitLogin = (Button)findViewById(R.id.exitLogin);
		 
		 AVUser currentUser = AVUser.getCurrentUser();
		 if(currentUser != null && currentUser.getUsername().toString() != null && currentUser.get("sex").toString() != null && currentUser.getMobilePhoneNumber().toString() != null && currentUser.getEmail().toString() != null) {
			 personalName.setText(currentUser.getUsername().toString());
			 personalSex.setText(currentUser.get("sex").toString());
			 personalPhoneNum.setText(currentUser.getMobilePhoneNumber().toString());
			 personalEmail.setText(currentUser.getEmail().toString());
			 user_pic = currentUser.getString("user_pic");
			 if(user_pic!=null) {
				 AVFile.withObjectIdInBackground(user_pic, new GetFileCallback<AVFile>() {
						@Override
						public void done(AVFile avFile, AVException e) {
							// TODO Auto-generated method stub
						        avFile.getDataInBackground(new GetDataCallback() {
						          @Override
						          public void done(byte[] bytes, AVException e) {
						        	  if(e == null) {
						        		  Log.d("leancloud", "done,size is " + bytes.length);
						        		  Bitmap bitMap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);							 
						        		  iv_head_photo.setImageBitmap(bitMap);
						        	  }
						          }
						        });
						}
				});
			 }
		 }
		 
		 
		 
		 
		/*//修改昵称
		 RelativeLayout personalChangeName = (RelativeLayout)findViewById(R.id.personalName);
		 personalChangeName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this, ChangeNameActivity.class);
				startActivity(intent);
				
			}
		});*/
		 
		//修改密码
		RelativeLayout personalChangePwd = (RelativeLayout)findViewById(R.id.personalChangePwd);
		personalChangePwd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this, ChangePwdActivity.class);
				startActivity(intent);
				PersonalInfoActivity.this.finish();
			}
		});
		
		//修改头像
		iv_head_photo = (ImageView)findViewById(R.id.personalHeadPicture);
		RelativeLayout personalChangeHead = (RelativeLayout)findViewById(R.id.personalHead);
		personalChangeHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openPictureSelectDialog();
			}
		});
		
		//修改性别
		personalChangeSexInfo = (TextView)findViewById(R.id.personalSexInfo);
		RelativeLayout personalChangeSex = (RelativeLayout)findViewById(R.id.personalSex);
		personalChangeSex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSexDialog();
			}
		});
		
		//退出登录
		exitLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AVUser.logOut();             //清除缓存用户对象
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		
	}
	
	/*显示修改性别对话框*/
	public void showSexDialog() {
		new AlertDialog.Builder(this)
		//设置对话框标题
		.setTitle("选择性别")
		//默认选择第一项，索引为0
		.setSingleChoiceItems(sexItems, sexRecord, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				switch(arg1) {
				case 0:
					sexRecord=0;
					
					break;
				case 1:
					sexRecord=1;
					
					break;
				}
				
			}
		}).setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				AVUser currentUser = AVUser.getCurrentUser();
				if(sexRecord==0) {
					personalChangeSexInfo.setText("男");
					
					currentUser.put("sex", "男");
				}
				else if(sexRecord==1) {
					personalChangeSexInfo.setText("女");
					currentUser.put("sex", "女");
				}
				currentUser.saveInBackground();
				dialog.dismiss();
			}
		}).show();
		
	}
	/**打开对话框**/
	private void openPictureSelectDialog() {
		//自定义Context,添加主题
		Context dialogContext = new ContextThemeWrapper(PersonalInfoActivity.this, android.R.style.Theme_Light);
		String[] choiceItems= new String[2];
		choiceItems[0] = "相机拍摄";  //拍照
		choiceItems[1] = "本地相册";  //从相册中选择
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1,choiceItems);
		//对话框建立在刚才定义好的上下文上
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("添加图片");
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:  //相机
					doTakePhoto();
					break;
				case 1:  //从图库相册中选取
					doPickPhotoFromGallery();
					break;
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
	}	
	
	/**拍照获取相片**/
	private void doTakePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //调用系统相机
	   
	    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       
	    //直接使用，没有缩小  
        startActivityForResult(intent, PHOTO_WITH_CAMERA);  //用户点击了从相机获取
	}
	
	/**从相册获取图片**/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");  // 开启Pictures画面Type设定为image
		intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action 
		startActivityForResult(intent, PHOTO_WITH_DATA); //取得相片后返回到本画面
	}
	/**You will receive this call immediately before onResume() when your activity is re-starting.**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
		if(resultCode == RESULT_OK) {  //返回成功
			switch (requestCode) {
			case PHOTO_WITH_CAMERA:  {//拍照获取图片
				String status = Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)) { //是否有SD卡
					
					Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
					imgPath = Environment.getExternalStorageDirectory()+"/image.jpg";
					imgName = createPhotoFileName();
					//写一个方法将此文件保存到本应用下面啦
	            	savePicture(imgName,bitmap);

	            	if (bitmap != null) {
						//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
						Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);
						
						iv_head_photo.setImageBitmap(smallBitmap);
					}
	            	Toast.makeText(PersonalInfoActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_SHORT).show();
	            	
	            	AVFile file=null;
					try {
						file = AVFile.withAbsoluteLocalPath(imgPath, imgPath);
						
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						file.save();
						String head_pic = file.getObjectId();
						AVUser currentUser = AVUser.getCurrentUser();
						currentUser.put("user_pic",head_pic);				
						} catch (AVException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}        		        
			      
			    }
			 
				else {
					Toast.makeText(PersonalInfoActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
				}
				break;
			}
				case PHOTO_WITH_DATA:  {//从图库中选择图片
					ContentResolver resolver = getContentResolver();
					//照片的原始资源地址
					Uri originalUri = data.getData();  
					//System.out.println(originalUri.toString());  //" content://media/external/images/media/15838 "

	                //将原始路径转换成图片的路径 
	                String selectedImagePath = uri2filePath(originalUri);  
	                //imgPath = Environment.getExternalStorageDirectory()+"/image.jpg";
	                imgPath = selectedImagePath;
	            	System.out.println(selectedImagePath);  //" /mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
					try {
						 //使用ContentProvider通过URI获取原始图片
						Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
						
						imgName = createPhotoFileName();
		       			//写一个方法将此文件保存到本应用下面啦
		            	savePicture(imgName,photo);
		            	
		            	if (photo != null) {
							//为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
							Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / 5, photo.getHeight() / 5);
							
							iv_head_photo.setImageBitmap(smallBitmap);
						}
//		                ivHelpPicture.setImageURI(originalUri);   //在界面上显示图片
		            	Toast.makeText(PersonalInfoActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
		            	AVFile file=null;
						try {
							file = AVFile.withAbsoluteLocalPath(imgPath, imgPath);
							
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						try {
							file.save();
							String head_pic = file.getObjectId();
							System.out.println(head_pic);
							AVUser currentUser = AVUser.getCurrentUser();
							currentUser.put("user_pic",head_pic);
							currentUser.saveInBackground();
							} catch (AVException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}        		        
				      
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**创建图片不同的文件名**/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());  //系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}
	
	/**获取文件路径**/
	 public String uri2filePath(Uri uri)  
	    {  
	        String[] projection = {MediaStore.Images.Media.DATA };  
	        Cursor cursor = managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 
	 /**保存图片到本应用下**/
	 private void savePicture(String fileName,Bitmap bitmap) {
		   
			FileOutputStream fos =null;
			try {//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
				fos = PersonalInfoActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if(null != fos) {
						fos.close();
						fos = null;
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}

}
