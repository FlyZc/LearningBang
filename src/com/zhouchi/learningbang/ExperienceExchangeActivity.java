package com.zhouchi.learningbang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.zhouchi.learningbang.R;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

public class ExperienceExchangeActivity extends Activity{
	private static final int PHOTO_WITH_DATA = 18;  //从SD卡中得到图片
	private static final int PHOTO_WITH_CAMERA = 37;// 拍摄照片
	private Dialog dialog_pic;
	private String imgPath  = "";
	private String imgName = "";
	private boolean flag=false;
	private String objectId;
	private EditText experienceExchangeTitle;
	private EditText experienceExchangeContent;
	private Button experienceExchangeSubmit;
	private ImageView experienceExchangeAddPic;
	
	
	private String sExperienceTitle;
	private String sExperienceContent;
	private String sExperienceUser;
	
	
	 @Override
	  protected void onPause() {
	    super.onPause();
	    // 页面统计，结束
	    AVAnalytics.onPause(this);
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	    // 页面统计，开始
	    AVAnalytics.onResume(this);
	  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);		 
		 setContentView(R.layout.activity_experience_exchange);
		 flag=false;
		 experienceExchangeTitle = (EditText)findViewById(R.id.experienceExchangeTitle);
		 experienceExchangeContent = (EditText)findViewById(R.id.experienceExchangeContent);
		 experienceExchangeSubmit = (Button)findViewById(R.id.experienceExchangeSubmit);
		 
		 experienceExchangeAddPic = (ImageView)findViewById(R.id.experienceExchangeAddPicture);
		 experienceExchangeAddPic.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openPictureSelectDialog();
			}
		});
		 
		//经验分享
		experienceExchangeSubmit.setOnClickListener(new View.OnClickListener() {
		      public void onClick(View view) {
		        SaveCallback saveCallback=new SaveCallback() {
		          @Override
		          public void done(AVException e) {
		            // done方法一定在UI线程执行
		            if (e != null) {
		              Log.e("CreateExperience", "Update experience failed.", e);
		            }
		            Bundle bundle = new Bundle();
		            bundle.putBoolean("success", e == null);
		            Intent intent = new Intent();
		            intent.putExtras(bundle);
		            setResult(RESULT_OK, intent);
		            finish();
		          }
		        };
		        
		        
		        AVUser currentUser = AVUser.getCurrentUser();
		        if(currentUser==null) {
		        	Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "请先登录", Toast.LENGTH_SHORT);
		        	toast.show();
		        }
		        else {
		        	sExperienceTitle = experienceExchangeTitle.getText().toString();
			        sExperienceContent = experienceExchangeContent.getText().toString();
		        	sExperienceUser = AVUser.getCurrentUser().getUsername().toString();
		        	 if(sExperienceTitle.isEmpty()|sExperienceContent.isEmpty()) {
				        	Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "请检查标题或者内容是否填写", Toast.LENGTH_SHORT);
				        	toast.show();
				        }
				        else if(flag==true){
				        	AVFile file=null;
							try {
								file = AVFile.withAbsoluteLocalPath(imgPath, imgPath);
							} catch (FileNotFoundException e1) {								
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}	
							try {
								file.save();
								String experience_pic = file.getObjectId();
				            	AVService.createOrUpdateExperience(objectId, sExperienceTitle,sExperienceContent,
				            			sExperienceUser, experience_pic, saveCallback);
				            	Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "分享成功", Toast.LENGTH_SHORT);
								   toast.show();
							} catch (AVException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}    
					   } else {
						   AVService.createOrUpdateExperienceNoFile(objectId, sExperienceTitle,sExperienceContent,
			            			sExperienceUser, saveCallback);
						   Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "分享成功", Toast.LENGTH_SHORT);
						   toast.show();
					   }
		        	 Intent intent = new Intent();
					 intent.setClass(ExperienceExchangeActivity.this, MainActivity.class);
					 startActivity(intent);
		        }
		         
		      }
		    });
	
	}
	
	/**打开对话框**/
	private void openPictureSelectDialog() {
		//自定义Context,添加主题
		Context dialogContext = new ContextThemeWrapper(ExperienceExchangeActivity.this, android.R.style.Theme_Light);
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
			flag=true;
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
						
						experienceExchangeAddPic.setImageBitmap(smallBitmap);
					}
	            	Toast.makeText(ExperienceExchangeActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(ExperienceExchangeActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
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
							
							experienceExchangeAddPic.setImageBitmap(smallBitmap);
						}
//		                ivHelpPicture.setImageURI(originalUri);   //在界面上显示图片
		            	Toast.makeText(ExperienceExchangeActivity.this, "已保存本应用的files文件夹下", Toast.LENGTH_LONG).show();
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
				fos = ExperienceExchangeActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
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
