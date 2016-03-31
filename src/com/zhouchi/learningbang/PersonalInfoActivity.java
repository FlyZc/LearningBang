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
	
	private static final int PHOTO_WITH_DATA = 18;  //��SD���еõ�ͼƬ
	private static final int PHOTO_WITH_CAMERA = 37;// ������Ƭ
	private String imgPath  = "";
	private String imgName = "";
	/*���ڼ�¼�Ա�ѡ��*/
	private int sexRecord = 0;
	private String[] sexItems = new String[]{"��","Ů"};
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
		 
		 
		 
		 
		/*//�޸��ǳ�
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
		 
		//�޸�����
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
		
		//�޸�ͷ��
		iv_head_photo = (ImageView)findViewById(R.id.personalHeadPicture);
		RelativeLayout personalChangeHead = (RelativeLayout)findViewById(R.id.personalHead);
		personalChangeHead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				openPictureSelectDialog();
			}
		});
		
		//�޸��Ա�
		personalChangeSexInfo = (TextView)findViewById(R.id.personalSexInfo);
		RelativeLayout personalChangeSex = (RelativeLayout)findViewById(R.id.personalSex);
		personalChangeSex.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showSexDialog();
			}
		});
		
		//�˳���¼
		exitLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AVUser.logOut();             //��������û�����
				Intent intent = new Intent();
				intent.setClass(PersonalInfoActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		
	}
	
	/*��ʾ�޸��Ա�Ի���*/
	public void showSexDialog() {
		new AlertDialog.Builder(this)
		//���öԻ������
		.setTitle("ѡ���Ա�")
		//Ĭ��ѡ���һ�����Ϊ0
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
		}).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				AVUser currentUser = AVUser.getCurrentUser();
				if(sexRecord==0) {
					personalChangeSexInfo.setText("��");
					
					currentUser.put("sex", "��");
				}
				else if(sexRecord==1) {
					personalChangeSexInfo.setText("Ů");
					currentUser.put("sex", "Ů");
				}
				currentUser.saveInBackground();
				dialog.dismiss();
			}
		}).show();
		
	}
	/**�򿪶Ի���**/
	private void openPictureSelectDialog() {
		//�Զ���Context,�������
		Context dialogContext = new ContextThemeWrapper(PersonalInfoActivity.this, android.R.style.Theme_Light);
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
						
						iv_head_photo.setImageBitmap(smallBitmap);
					}
	            	Toast.makeText(PersonalInfoActivity.this, "�ѱ��汾Ӧ�õ�files�ļ�����", Toast.LENGTH_SHORT).show();
	            	
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
					Toast.makeText(PersonalInfoActivity.this, "û��SD��", Toast.LENGTH_SHORT).show();
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
	                //imgPath = Environment.getExternalStorageDirectory()+"/image.jpg";
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
							
							iv_head_photo.setImageBitmap(smallBitmap);
						}
//		                ivHelpPicture.setImageURI(originalUri);   //�ڽ�������ʾͼƬ
		            	Toast.makeText(PersonalInfoActivity.this, "�ѱ��汾Ӧ�õ�files�ļ�����", Toast.LENGTH_LONG).show();
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
				fos = PersonalInfoActivity.this.openFileOutput(fileName, Context.MODE_PRIVATE); 
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
