package com.zhouchi.learningbang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImgDisplayActivity extends Activity {
	
	public static final String TAG = "ImgDisplayActivity";

	private Button btnZoomin, btnZoomout;
	private ImageView imgDisPlay;
	private LinearLayout lLayoutDisplay;
	private FrameLayout fLayoutDisplay;
	
	private Bitmap bitmap;
	
	private int imgId = 0;
	private double scale_in = 0.8;//��С����
	private double scale_out = 1.25;//�Ŵ����
	
	private float scaleWidth = 1;
	private float scaleHeight = 1;
	
	public static final int NONE = 0;
	public static final int DRAG = 1;
	public static final int ZOOM = 2;
	
	private int mode = NONE;
	
	private Matrix matrix;
	private Matrix currMatrix;
	
	private PointF starPoint;
	private PointF midPoint;
	
	private float startDistance;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_img);
		
		//��ʼ��
		fLayoutDisplay = (FrameLayout) findViewById(R.id.flayout_img_display);
		lLayoutDisplay = (LinearLayout) findViewById(R.id.linearLayout_img_display);
		imgDisPlay = (ImageView) findViewById(R.id.img_display);
		btnZoomin = (Button) findViewById(R.id.btn_min);
		btnZoomout = (Button) findViewById(R.id.btn_out);
		
		
		matrix = new Matrix(); //������ק�仯
		currMatrix = new Matrix();// ��ǰ��
		starPoint = new PointF();//��ʼ���λ��
		
		
		btnZoomin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zoomIn();
			}
		});
		btnZoomout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zoomOut();//�Ŵ�
			}
		});
		Intent intent = getIntent();
		String imgUrl = intent.getStringExtra("imgUrl");
		bitmap = BitmapFactory.decodeFile(imgUrl);
		imgDisPlay.setImageBitmap(bitmap);
		
		//��ͼƬ�󶨼�����Ŷ
		imgDisPlay.setOnTouchListener(new ImageViewOnTouchListener());
		
	}
	
	private void zoomOut() {
		reSizeBmp(scale_out);
		btnZoomin.setEnabled(true);
	}
	private void zoomIn() {
		reSizeBmp(scale_in);
	}
	
	private void reSizeBmp(double scale) {
		//���ű���
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); //������ű���
		imgDisPlay.setImageMatrix(matrix);
	}
	
	
	
	final class ImageViewOnTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:  //һֻ��ָ����
				Log.i(TAG,"һֻ��ָ����");
				currMatrix.set(matrix);
				starPoint.set(event.getX(), event.getY());
				
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN: //�����һֻ��ָ������Ļ����������һ����ָ����     // ��ֻ��ָ����
				Log.i(TAG,"����һֻ��ָ����");
				
				startDistance = distance(event);//��������ľ���
				Log.i(TAG, startDistance+"");
				
				if(startDistance > 5f) {  //������ָ֮�����С�������ش���5����Ϊ�Ƕ�㴥��
					mode = ZOOM;
					currMatrix.set(matrix);
					
					midPoint = getMidPoint(event); //����������֮������ĵ�
					
				}
				
				break;
				
			case MotionEvent.ACTION_MOVE:
				
				if(mode == DRAG) {  //��קģʽ
					Log.i(TAG,"һֻ��ָ����ק");
					
					//��ʼ--��������ľ���
					float dx = event.getX() - starPoint.x;
					float dy = event.getY() - starPoint.y;
					
					matrix.set(currMatrix);
					matrix.postTranslate(dx, dy);//�ƶ���ָ���㣺�����ƶ�������eg:���������ű���
				} else if(mode == ZOOM) {  //����ģʽ
					Log.i(TAG,"��������");
					
					float distance = distance(event);  //����֮��ľ���
					if(distance > 5f) {
						matrix.set(currMatrix);
						float cale = distance / startDistance;
						matrix.preScale(cale, cale, midPoint.x, midPoint.y);  //���б�������
					}
				}
				break;
				
			case MotionEvent.ACTION_UP: //���һֻ��ָ�뿪��Ļ�󴥷����¼�
			case MotionEvent.ACTION_POINTER_UP: //һֻ��ָ�뿪��Ļ��������һֻ��ָ������ᴥ���¼�
				//ʲô��û��
				mode = NONE;
				break;

			default:
				break;
			}
			
			imgDisPlay.setImageMatrix(matrix);
			
			//��ֻ��ָ������
			return true;
		}
	}
	
	/**��������֮��ľ�������**/
	private float distance(MotionEvent e) {
		
		float eX = e.getX(1) - e.getX(0);  //����ĵ����� - ǰ�������� 
		float eY = e.getY(1) - e.getY(0);
		return FloatMath.sqrt(eX * eX + eY * eY);
	}
	
	/**��������֮������ĵ�**/
	private PointF getMidPoint(MotionEvent event) {
		
		float x = (event.getX(1) - event.getX(0)) / 2;
		float y = (event.getY(1) - event.getY(0)) / 2;
		return new PointF(x,y);
	}
	
	
	
	
	
	
	
	
	
	
	
	/**�Ŵ�**//*
	private void zoomOut() {
		//ͼƬ��ԭʼ���
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		//���ű���
		scaleWidth = (float) (scaleWidth * scale_out);
		scaleHeight = (float) (scaleHeight * scale_out);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); //������ű���
		//��������һ���Ŵ���С���ͼƬ
		Bitmap reSizeBmp = Bitmap.createBitmap(bitmap, 0, 0,bmpWidth , bmpHeight, matrix, true);
		
		//�Ѿɵ�ImageView���Ƴ�
		if(imgId == 0) {
			lLayoutDisplay.removeView(imgDisPlay);
		} else {
			lLayoutDisplay.removeView(findViewById(imgId));
		}
		
		imgId ++;
		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(reSizeBmp);
		imageView.setId(imgId);
		
		lLayoutDisplay.addView(imageView);
		setContentView(fLayoutDisplay);
	}
	
	*//**�Ŵ�**//*
	private void zoomIn() {
		//ͼƬ��ԭʼ���
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		//���ű���
		scaleWidth = (float) (scaleWidth * scale_in);
		scaleHeight = (float) (scaleHeight * scale_in);
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); //������ű���
		//��������һ���Ŵ���С���ͼƬ
		Bitmap reSizeBmp = Bitmap.createBitmap(bitmap, 0, 0,bmpWidth , bmpHeight, matrix, true);
		
		//�Ѿɵ�ImageView���Ƴ�
		if(imgId == 0) {
			lLayoutDisplay.removeView(imgDisPlay);
		} else {
			lLayoutDisplay.removeView(findViewById(imgId));
		}
		
		imgId ++;
		ImageView imageView = new ImageView(this);
		imageView.setImageBitmap(reSizeBmp);
		imageView.setId(imgId);
		
		lLayoutDisplay.addView(imageView);
		setContentView(fLayoutDisplay);
	}
*/
}
