package com.wing.ydt.view.component;

import com.wing.ydt.R;
import com.wing.ydt.util.StringUtil;
import com.wing.ydt.vo.Message;
import com.wing.ydt.vo.WMessageList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class ListWMessageListButton extends Button {
	private WMessageList cat;
	private Bitmap image;
	private static Bitmap whiteImage;
	private static Bitmap blueImage;
	private static Bitmap onFocusImage;
	private Paint mPaint;
	private boolean isOnTouch;
	public ListWMessageListButton(Context context, WMessageList cat,int index) {
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
					ListWMessageListButton.this.invalidate();
				} else {
					isOnTouch=false;
					ListWMessageListButton.this.invalidate();
				}
				return false;
			}

		});
	}
	public WMessageList getMessage() {
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
			canvas.drawText(cat.getWmessagelist_name(), 20, 20, mPaint);
		} else {
			mPaint.setAntiAlias(true);
			canvas.drawBitmap(image, 0, 0, mPaint);
			mPaint.setTextSize(16);
			Typeface typeface = Typeface.defaultFromStyle(Typeface.BOLD);
			mPaint.setTypeface(typeface);
			canvas.drawText(cat.getWmessagelist_name(), 20, 20, mPaint);
	
		}
		
	}

}
