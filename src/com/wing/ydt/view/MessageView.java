package com.wing.ydt.view;

import java.util.Vector;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 处理行尾遇到标点则中文文本混乱的问题，未测试！！
 * @author Administrator
 *
 */
public class MessageView extends TextView {
	private final String namespace = "http://www.nearmobile.net";

	private int resourceId = 0;

	/* 声明Paint对象 */
	private Paint mPaint = null;
	/* 声明TextUtil对象 */
	// private TextUtil mTextUtil = null;

	public static int m_iTextHeight;
	private WindowManager wm = null;
	private String string = "";

	public MessageView(Context context, AttributeSet set) {
		super(context, set);

		resourceId = set.getAttributeResourceValue(namespace, "text", 0);

		if (resourceId == 0)
			string = set.getAttributeValue(null, "text");
		else
			string = this.getResources().getString(resourceId);

		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		/* 构建对象 */
		m_iTextHeight = 2000;
		mPaint = new Paint();
		mPaint.setColor(Color.RED);
		mPaint.setStrokeWidth(40);
		mPaint.setTextSize(20);

		int m_iTextWidth = wm.getDefaultDisplay().getWidth();
		FontMetrics fm = mPaint.getFontMetrics();

		int m_iFontHeight = (int) Math.ceil(fm.descent - fm.top) + 4;
		int line = 0;
		@SuppressWarnings("unused")
		int istart = 0;
		int w = 0;
		for (int i = 0; i < string.length(); i++) {
			char ch = string.charAt(i);
			float[] widths = new float[1];
			String srt = String.valueOf(ch);
			mPaint.getTextWidths(srt, widths);
			if (ch == '\n') {
				line++;
				istart = i + 1;
				w = 0;
			} else {
				w += (int) (Math.ceil(widths[0]));
				if (w > m_iTextWidth) {
					line++;
					istart = i;
					i--;
					w = 0;
				} else {
					if (i == (string.length() - 1)) {
						line++;
					}
				}
			}
		}
		m_iTextHeight = (line + 2) * m_iFontHeight + 2;
		// 用反射机制得到 m_iTextHeight 值

		/*
		 * 实例化TextUtil mTextUtil = new
		 * TextUtil(string,5,25,wm.getDefaultDisplay(
		 * ).getWidth(),this.getHeight(),0x0,0xffffff,255,15);
		 * 
		 * 初始化TextUtil
		 * mTextUtil.InitText(string,5,25,wm.getDefaultDisplay().getWidth
		 * (),wm.getDefaultDisplay().getHeight(),0x0,0xffffff,255,15);
		 */
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
		/* 设置背景颜色 */
		canvas.drawColor(Color.BLACK);

		mPaint.setAntiAlias(true);

		char ch;
		int w = 0;
		int istart = 0;
		int m_iFontHeight;
		int m_iRealLine = 0;
		int x = 2;
		int y = 60;
		Vector<String> m_String = new Vector<String>();
		int m_iTextWidth = wm.getDefaultDisplay().getWidth();
		FontMetrics fm = mPaint.getFontMetrics();

		m_iFontHeight = (int) Math.ceil(fm.descent - fm.top) + 4;
		// m_ipageLineNum = m_iTextHeight / m_iFontHeight;
		for (int i = 0; i < string.length(); i++) {
			ch = string.charAt(i);
			float[] widths = new float[1];
			String srt = String.valueOf(ch);
			mPaint.getTextWidths(srt, widths);
			if (ch == '\n') {
				m_iRealLine++;
				m_String.addElement(string.substring(istart, i));
				istart = i + 1;
				w = 0;
			} else {
				w += (int) (Math.ceil(widths[0]));
				if (w > m_iTextWidth) {
					m_iRealLine++;
					m_String.addElement(string.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				} else {
					if (i == (string.length() - 1)) {
						m_iRealLine++;
						m_String.addElement(string.substring(istart, string
								.length()));
					}
				}
			}
		}
		m_iTextHeight = m_iRealLine * m_iFontHeight + 2;
		System.out.println("m_iTextHeight----->" + m_iTextHeight);
		canvas.setViewport(m_iTextWidth, m_iTextWidth);
		for (int i = 0, j = 0; i < m_iRealLine; i++, j++) {
			canvas.drawText((String) (m_String.elementAt(i)), x, y
					+ m_iFontHeight * j, mPaint);
		}
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int measuredHeight = measureHeight(heightMeasureSpec);

		int measuredWidth = measureWidth(widthMeasureSpec);

		this.setMeasuredDimension(measuredWidth, measuredHeight);
		this.setLayoutParams(new LinearLayout.LayoutParams(measuredWidth,
				measuredHeight));
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private int measureHeight(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);

		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.

		int result = m_iTextHeight;

		if (specMode == MeasureSpec.AT_MOST)

		{

			// Calculate the ideal size of your

			// control within this maximum size.

			// If your control fills the available

			// space return the outer bound.

			result = specSize;

		}

		else if (specMode == MeasureSpec.EXACTLY)

		{

			// If your control can fit within these bounds return that value.

			result = specSize;

		}

		return result;

	}

	private int measureWidth(int measureSpec) {

		int specMode = MeasureSpec.getMode(measureSpec);

		int specSize = MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.

		int result = 500;

		if (specMode == MeasureSpec.AT_MOST)

		{

			// Calculate the ideal size of your control

			// within this maximum size.

			// If your control fills the available space

			// return the outer bound.

			result = specSize;

		}

		else if (specMode == MeasureSpec.EXACTLY)

		{

			// If your control can fit within these bounds return that value.

			result = specSize;

		}

		return result;

	}
}
