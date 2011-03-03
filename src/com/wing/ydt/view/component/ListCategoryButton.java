package com.wing.ydt.view.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.wing.ydt.R;
import com.wing.ydt.util.StringUtil;
import com.wing.ydt.vo.Category;

public class ListCategoryButton extends Button {

	private Category cat;
	private Bitmap image;
	private static Bitmap whiteImage;
	private static Bitmap blueImage;
	private static Bitmap onFocusImage;
	private Paint mPaint;
	private boolean isOnTouch;
	public ListCategoryButton(Context context, Category cat,int index) {
		super(context);
		this.setFocusable(true);  
		mPaint = new Paint();   
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(15);
		Typeface typeface = Typeface.defaultFromStyle(Typeface.BOLD);
		mPaint.setTypeface(typeface);
		this.setBackgroundColor(0);
		this.cat = cat;
		this.setWidth(320);
		this.setHeight(50);
		initButtontnImage();
//		if(index%2==0){
			image = whiteImage;
//		}else{
//			image = blueImage;
//		}
		setOnTouch();
	}
	
	private void initButtontnImage(){
		if(whiteImage==null){
			whiteImage= BitmapFactory.decodeResource(getResources(),
					R.drawable.item);
		}
		if(onFocusImage==null){
			onFocusImage=BitmapFactory.decodeResource(getResources(),
					R.drawable.item_over);
		}
	}
	
	private void setOnTouch() {
		this.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
					isOnTouch=true;
					ListCategoryButton.this.invalidate();
				} else {
					isOnTouch=false;
					ListCategoryButton.this.invalidate();
				}
				return false;
			}

		});
	}
	public Category getCategory() {
		return cat;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (this.hasFocus() || isOnTouch) {
			mPaint.setAntiAlias(true);
			canvas.drawBitmap(onFocusImage, 0, 0, mPaint);
			mPaint.setTextSize(16);
			Typeface typeface = Typeface.defaultFromStyle(Typeface.BOLD);
			mPaint.setTypeface(typeface);
			canvas.drawText(cat.getCategory_name(), 20, 20, mPaint);
			mPaint.setTextSize(12);
			 typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
			mPaint.setTypeface(typeface);
			canvas.drawText(StringUtil.limitedDisplayStr2(cat.getCategory_desc(), 300, mPaint), 20, 37, mPaint);
		} else {
			mPaint.setAntiAlias(true);
			canvas.drawBitmap(image, 0, 0, mPaint);
			mPaint.setTextSize(16);
			Typeface typeface = Typeface.defaultFromStyle(Typeface.BOLD);
			mPaint.setTypeface(typeface);
			canvas.drawText(cat.getCategory_name(), 20, 20, mPaint);
			mPaint.setTextSize(12);
			 typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
			mPaint.setTypeface(typeface);
			canvas.drawText(StringUtil.limitedDisplayStr2(cat.getCategory_desc(), 300, mPaint), 20, 37, mPaint);
		}
		
	}


}
