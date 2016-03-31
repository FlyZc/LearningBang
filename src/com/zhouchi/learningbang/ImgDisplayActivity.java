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
	private double scale_in = 0.8;//缩小比例
	private double scale_out = 1.25;//放大比例
	
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
		
		//初始化
		fLayoutDisplay = (FrameLayout) findViewById(R.id.flayout_img_display);
		lLayoutDisplay = (LinearLayout) findViewById(R.id.linearLayout_img_display);
		imgDisPlay = (ImageView) findViewById(R.id.img_display);
		btnZoomin = (Button) findViewById(R.id.btn_min);
		btnZoomout = (Button) findViewById(R.id.btn_out);
		
		
		matrix = new Matrix(); //保存拖拽变化
		currMatrix = new Matrix();// 当前的
		starPoint = new PointF();//开始点的位置
		
		
		btnZoomin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zoomIn();
			}
		});
		btnZoomout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				zoomOut();//放大
			}
		});
		Intent intent = getIntent();
		String imgUrl = intent.getStringExtra("imgUrl");
		bitmap = BitmapFactory.decodeFile(imgUrl);
		imgDisPlay.setImageBitmap(bitmap);
		
		//给图片绑定监听器哦
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
		//缩放比例
		scaleWidth = (float) (scaleWidth * scale);
		scaleHeight = (float) (scaleHeight * scale);
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); //设计缩放比例
		imgDisPlay.setImageMatrix(matrix);
	}
	
	
	
	final class ImageViewOnTouchListener implements OnTouchListener{
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:  //一只手指按下
				Log.i(TAG,"一只手指按下");
				currMatrix.set(matrix);
				starPoint.set(event.getX(), event.getY());
				
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN: //如果有一只手指按下屏幕，后续又有一个手指按下     // 两只手指按下
				Log.i(TAG,"又有一只手指按下");
				
				startDistance = distance(event);//记下两点的距离
				Log.i(TAG, startDistance+"");
				
				if(startDistance > 5f) {  //两个手指之间的最小距离像素大于5，认为是多点触摸
					mode = ZOOM;
					currMatrix.set(matrix);
					
					midPoint = getMidPoint(event); //记下两个点之间的中心点
					
				}
				
				break;
				
			case MotionEvent.ACTION_MOVE:
				
				if(mode == DRAG) {  //拖拽模式
					Log.i(TAG,"一只手指在拖拽");
					
					//开始--》结束点的距离
					float dx = event.getX() - starPoint.x;
					float dy = event.getY() - starPoint.y;
					
					matrix.set(currMatrix);
					matrix.postTranslate(dx, dy);//移动到指定点：矩阵移动比例；eg:缩放有缩放比例
				} else if(mode == ZOOM) {  //缩放模式
					Log.i(TAG,"正在缩放");
					
					float distance = distance(event);  //两点之间的距离
					if(distance > 5f) {
						matrix.set(currMatrix);
						float cale = distance / startDistance;
						matrix.preScale(cale, cale, midPoint.x, midPoint.y);  //进行比例缩放
					}
				}
				break;
				
			case MotionEvent.ACTION_UP: //最后一只手指离开屏幕后触发此事件
			case MotionEvent.ACTION_POINTER_UP: //一只手指离开屏幕，但还有一只手指在上面会触此事件
				//什么都没做
				mode = NONE;
				break;

			default:
				break;
			}
			
			imgDisPlay.setImageMatrix(matrix);
			
			//两只手指的缩放
			return true;
		}
	}
	
	/**计算两点之间的距离像素**/
	private float distance(MotionEvent e) {
		
		float eX = e.getX(1) - e.getX(0);  //后面的点坐标 - 前面点的坐标 
		float eY = e.getY(1) - e.getY(0);
		return FloatMath.sqrt(eX * eX + eY * eY);
	}
	
	/**计算两点之间的中心点**/
	private PointF getMidPoint(MotionEvent event) {
		
		float x = (event.getX(1) - event.getX(0)) / 2;
		float y = (event.getY(1) - event.getY(0)) / 2;
		return new PointF(x,y);
	}
	
	
	
	
	
	
	
	
	
	
	
	/**放大**//*
	private void zoomOut() {
		//图片的原始宽高
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		//缩放比例
		scaleWidth = (float) (scaleWidth * scale_out);
		scaleHeight = (float) (scaleHeight * scale_out);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); //设计缩放比例
		//重新生成一个放大、缩小后的图片
		Bitmap reSizeBmp = Bitmap.createBitmap(bitmap, 0, 0,bmpWidth , bmpHeight, matrix, true);
		
		//把旧的ImageView的移除
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
	
	*//**放大**//*
	private void zoomIn() {
		//图片的原始宽高
		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();
		//缩放比例
		scaleWidth = (float) (scaleWidth * scale_in);
		scaleHeight = (float) (scaleHeight * scale_in);
		
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight); //设计缩放比例
		//重新生成一个放大、缩小后的图片
		Bitmap reSizeBmp = Bitmap.createBitmap(bitmap, 0, 0,bmpWidth , bmpHeight, matrix, true);
		
		//把旧的ImageView的移除
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
