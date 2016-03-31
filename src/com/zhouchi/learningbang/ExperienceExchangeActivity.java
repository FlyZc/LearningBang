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
	private static final int PHOTO_WITH_DATA = 18;  //��SD���еõ�ͼƬ
	private static final int PHOTO_WITH_CAMERA = 37;// ������Ƭ
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
	    // ҳ��ͳ�ƣ�����
	    AVAnalytics.onPause(this);
	  }

	  @Override
	  protected void onResume() {
	    super.onResume();
	    // ҳ��ͳ�ƣ���ʼ
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
		 
		//�������
		experienceExchangeSubmit.setOnClickListener(new View.OnClickListener() {
		      public void onClick(View view) {
		        SaveCallback saveCallback=new SaveCallback() {
		          @Override
		          public void done(AVException e) {
		            // done����һ����UI�߳�ִ��
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
		        	Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "���ȵ�¼", Toast.LENGTH_SHORT);
		        	toast.show();
		        }
		        else {
		        	sExperienceTitle = experienceExchangeTitle.getText().toString();
			        sExperienceContent = experienceExchangeContent.getText().toString();
		        	sExperienceUser = AVUser.getCurrentUser().getUsername().toString();
		        	 if(sExperienceTitle.isEmpty()|sExperienceContent.isEmpty()) {
				        	Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "���������������Ƿ���д", Toast.LENGTH_SHORT);
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
				            	Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "����ɹ�", Toast.LENGTH_SHORT);
								   toast.show();
							} catch (AVException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}    
					   } else {
						   AVService.createOrUpdateExperienceNoFile(objectId, sExperienceTitle,sExperienceContent,
			            			sExperienceUser, saveCallback);
						   Toast toast = Toast.makeText(ExperienceExchangeActivity.this, "����ɹ�", Toast.LENGTH_SHORT);
						   toast.show();
					   }
		        	 Intent intent = new Intent();
					 intent.setClass(ExperienceExchangeActivity.this, MainActivity.class);
					 startActivity(intent);
		        }
		         
		      }
		    });
	
	}
	
	/**�򿪶Ի���**/
	private void openPictureSelectDialog() {
		//�Զ���Context,�������
		Context dialogContext = new ContextThemeWrapper(ExperienceExchangeActivity.this, android.R.style.Theme_Light);
		String[] choiceItems= new String[2];
		choiceItems[0] = "�������";  //����
		choiceItems[1] = "�������";  //�������ѡ��
		ListAdapter adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1,choiceItems);
		//�Ի������ڸղŶ���õ���������
		AlertDialog.Builder builder = new AlertDialog.Builder(dialogContext);
		builder.setTitle("���ͼƬ");
		builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:  //���
					doTakePhoto();
					break;
				case 1:  //��ͼ�������ѡȡ
					doPickPhotoFromGallery();
					break;
				}
				dialog.dismiss();
			}
		});
		builder.create().show();
	}	
	
	/**���ջ�ȡ��Ƭ**/
	private void doTakePhoto() {
	    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //����ϵͳ���
	   
	    Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));
		//ָ����Ƭ����·����SD������image.jpgΪһ����ʱ�ļ���ÿ�����պ����ͼƬ���ᱻ�滻
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
       
	    //ֱ��ʹ�ã�û����С  
        startActivityForResult(intent, PHOTO_WITH_CAMERA);  //�û�����˴������ȡ
	}
	
	/**������ȡͼƬ**/
	private void doPickPhotoFromGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");  // ����Pictures����Type�趨Ϊimage
		intent.setAction(Intent.ACTION_GET_CONTENT); //ʹ��Intent.ACTION_GET_CONTENT���Action 
		startActivityForResult(intent, PHOTO_WITH_DATA); //ȡ����Ƭ�󷵻ص�������
	}
	/**You will receive this call immediately before onResume() when your activity is re-starting.**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   
		if(resultCode == RESULT_OK) {  //���سɹ�
			flag=true;
			switch (requestCode) {
			case PHOTO_WITH_CAMERA:  {//���ջ�ȡͼƬ
				String status = Environment.getExternalStorageState();
				if(status.equals(Environment.MEDIA_MOUNTED)) { //�Ƿ���SD��
					
					Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/image.jpg");
					imgPath = Environment.getExternalStorageDirectory()+"/image.jpg";
					imgName = createPhotoFileName();
					//дһ�����������ļ����浽��Ӧ��������
	            	savePicture(imgName,bitmap);

	            	if (bitmap != null) {
						//Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
						Bitmap smallBitmap = ImageTools.zoomBitmap(bitmap, bitmap.getWidth() / 5, bitmap.getHeight() / 5);
						
						experienceExchangeAddPic.setImageBitmap(smallBitmap);
					}
	            	Toast.makeText(ExperienceExchangeActivity.this, "�ѱ��汾Ӧ�õ�files�ļ�����", Toast.LENGTH_SHORT).show();
				}else {
					Toast.makeText(ExperienceExchangeActivity.this, "û��SD��", Toast.LENGTH_SHORT).show();
				}
				break;
			}
				case PHOTO_WITH_DATA:  {//��ͼ����ѡ��ͼƬ
					ContentResolver resolver = getContentResolver();
					//��Ƭ��ԭʼ��Դ��ַ
					Uri originalUri = data.getData();  
					//System.out.println(originalUri.toString());  //" content://media/external/images/media/15838 "

	                //��ԭʼ·��ת����ͼƬ��·�� 
	                String selectedImagePath = uri2filePath(originalUri);  
	                imgPath = selectedImagePath;
	            	System.out.println(selectedImagePath);  //" /mnt/sdcard/DCIM/Camera/IMG_20130603_185143.jpg "
					try {
						 //ʹ��ContentProviderͨ��URI��ȡԭʼͼƬ
						Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
						
						imgName = createPhotoFileName();
		       			//дһ�����������ļ����浽��Ӧ��������
		            	savePicture(imgName,photo);
		            	
		            	if (photo != null) {
							//Ϊ��ֹԭʼͼƬ�������ڴ��������������Сԭͼ��ʾ��Ȼ���ͷ�ԭʼBitmapռ�õ��ڴ�
							Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / 5, photo.getHeight() / 5);
							
							experienceExchangeAddPic.setImageBitmap(smallBitmap);
						}
//		                ivHelpPicture.setImageURI(originalUri);   //�ڽ�������ʾͼƬ
		            	Toast.makeText(ExperienceExchangeActivity.this, "�ѱ��汾Ӧ�õ�files�ļ�����", Toast.LENGTH_LONG).show();
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
	
	/**����ͼƬ��ͬ���ļ���**/
	private String createPhotoFileName() {
		String fileName = "";
		Date date = new Date(System.currentTimeMillis());  //ϵͳ��ǰʱ��
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		fileName = dateFormat.format(date) + ".jpg";
		return fileName;
	}
	
	/**��ȡ�ļ�·��**/
	 public String uri2filePath(Uri uri)  
	    {  
	        String[] projection = {MediaStore.Images.Media.DATA };  
	        Cursor cursor = managedQuery(uri, projection, null, null, null);  
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);  
	        cursor.moveToFirst();  
	        String path =  cursor.getString(column_index);
	        return path;  
	    }  
	 
	 /**����ͼƬ����Ӧ����**/
	 private void savePicture(String fileName,Bitmap bitmap) {
		   
			FileOutputStream fos =null;
			try {//ֱ��д�����Ƽ��ɣ�û�лᱻ�Զ�������˽�У�ֻ�б�Ӧ�ò��ܷ��ʣ���������д��ᱻ����
				fos = ExperienceExchangeActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// ��ͼƬд��ָ���ļ�����
				
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
