package com.wing.ydt.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class CommonImageButton extends View {
	protected Bitmap image;
	protected Bitmap onFocusImg;
	protected Paint mPaint;
	private boolean isOnTouch;
	public CommonImageButton(Context context, Bitmap image, Bitmap onFocusImg) {
		super(context);
		mPaint = new Paint();
		this.image = image;
		if (image.equals(onFocusImg)) {
			this.setFocusable(false);
			this.onFocusImg=image;
		}else{
			this.setFocusable(true);
			this.onFocusImg = onFocusImg;
		}
		setOnTouch();
	}


	public CommonImageButton(Context context, int resid, int onFocesResid) {
		super(context);
		mPaint = new Paint();
		image = BitmapFactory.decodeResource(context.getResources(), resid);
		if(resid==onFocesResid){
			onFocusImg=image;
			this.setFocusable(false);
		}else{
			onFocusImg= BitmapFactory.decodeResource(context.getResources(),
					onFocesResid);
			this.setFocusable(true);
		}
		setOnTouch();
	}
	
	private void setOnTouch() {
		this.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					isOnTouch=true;
					CommonImageButton.this.invalidate();
				} else {
					isOnTouch=false;
					CommonImageButton.this.invalidate();
				}
				return false;
			}

		});
	}
	
	public void setImage(Context context,int resid){
		image = BitmapFactory.decodeResource(context.getResources(), resid);
	}
	public void setImage(Bitmap image){
		this.image=image;
	}
	public void changImage(Context context,int resid){
		image = BitmapFactory.decodeResource(context.getResources(), resid);
		this.invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		mPaint.setAntiAlias(true);
		if (this.hasFocus() || isOnTouch) {
			canvas.drawBitmap(onFocusImg, 0, 0, mPaint);
		} else {
			canvas.drawBitmap(image, 0, 0, mPaint);
		}
	}
	
	public void repaint(){
		this.invalidate();
	}
	public void release(){
		image=null;
		onFocusImg=null;
		mPaint=null;
	}
	public Bitmap getImage() {
		return image;
	}

	public Bitmap getOnFocusImg() {
		return onFocusImg;
	}
}
