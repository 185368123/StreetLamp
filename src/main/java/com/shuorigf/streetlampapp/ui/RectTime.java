package com.shuorigf.streetlampapp.ui;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.util.DensityUtils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RectTime extends View {

	private int layout_height;
	private int layout_width;
	
	private int defaultSize ;
	private int defaultRectWidth;
	private int defaultLineWidth;


	private Paint paint;
	private Paint linePaint;
	private Paint textPaint;

	private int defautlFirstRectColor = 0xff12b7f5;
	private int defaultSecondRectColor = 0x7a12b7f5;
	private int defaultLeftTextColor = 0xff8c8c8c;
	private int defaultRightTextColor = 0xff333333;
	
	private float defaultLeftTextSize;
	private float defaultRightTextSize;
	
	private int firstRectHeightPercentage;
	private int secondRectHeightPercentage;
	
	private float defaultHeightPercentage = 24f;
	
	private Path linePath;
	
	
	private Bitmap firstBitmap;
	private Bitmap secondBitmap;
	
	private String defaultFirstText = "";
	private String defaultSecondText = "";
	
	
	public RectTime(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}


	public RectTime(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	public RectTime(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		defaultSize = DensityUtils.dip2px(context, 100);
		defaultLineWidth = DensityUtils.dip2px(context, 1);
		defaultLeftTextSize = getResources().getDimension(R.dimen.rect_time_left_text_size);
		defaultRightTextSize = getResources().getDimension(R.dimen.rect_time_right_text_size);
	
	}

	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		layout_width = w;
		layout_height = h;
		defaultRectWidth = layout_width/7;
		initPaint();
	}
	

	private void initPaint() {
		paint = new Paint();
		paint.setAntiAlias(true); 
		linePaint =  new Paint();
		PathEffect effects = new DashPathEffect(new float[] {10, 5}, 1); 
		linePaint.setPathEffect(effects);
		linePaint.setAntiAlias(true); 
		linePaint.setStrokeWidth(defaultLineWidth);
		linePaint.setStyle(Paint.Style.STROKE);
		linePath = new Path();
		
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(widthMeasureSpec, measure(heightMeasureSpec));
	}
	
	private int measure(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = defaultSize;
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(defautlFirstRectColor);
		float firstRectTop =  layout_height*(1-firstRectHeightPercentage/defaultHeightPercentage)+defaultLineWidth;
		canvas.drawRect(0, firstRectTop, defaultRectWidth, layout_height,  paint);
		
		linePaint.setColor(defautlFirstRectColor);
		linePath.reset();
		linePath.moveTo(defaultRectWidth,firstRectTop);
		linePath.lineTo(defaultRectWidth*3, firstRectTop);     
		canvas.drawPath(linePath, linePaint);
		
		linePath.reset();
		linePath.moveTo(defaultRectWidth*3,firstRectTop);
		linePath.lineTo(defaultRectWidth*7/2, layout_height/2-defaultLineWidth);     
		canvas.drawPath(linePath, linePaint);
		
		linePath.reset();
		linePath.moveTo(defaultRectWidth*7/2, layout_height/2-defaultLineWidth);  
		linePath.lineTo(layout_width, layout_height/2-defaultLineWidth);     
		canvas.drawPath(linePath, linePaint);
		
		
		paint.setColor(defaultSecondRectColor);
		float secondRectTop =  layout_height*(1-secondRectHeightPercentage/defaultHeightPercentage)+defaultLineWidth;
		canvas.drawRect(defaultRectWidth,secondRectTop, defaultRectWidth*2, layout_height, paint);
		
		
		linePaint.setColor(defaultSecondRectColor);
		linePath.reset();
		linePath.moveTo(defaultRectWidth*2,secondRectTop);
		linePath.lineTo(defaultRectWidth*3, secondRectTop);     
		canvas.drawPath(linePath, linePaint);
		
		linePath.reset();
		linePath.moveTo(defaultRectWidth*3,secondRectTop);
		linePath.lineTo(defaultRectWidth*7/2, layout_height-defaultLineWidth);     
		canvas.drawPath(linePath, linePaint);
		
		linePath.reset();
		linePath.moveTo(defaultRectWidth*7/2, layout_height-defaultLineWidth);  
		linePath.lineTo(layout_width, layout_height-defaultLineWidth);     
		canvas.drawPath(linePath, linePaint);
		
		textPaint.setColor(defaultLeftTextColor);
		textPaint.setTextSize(defaultLeftTextSize);
		if(secondBitmap!=null){
			canvas.drawBitmap(secondBitmap, defaultRectWidth*7/2,  layout_height-secondBitmap.getHeight()*3/2, textPaint);
			canvas.drawText(defaultSecondText, defaultRectWidth*7/2+secondBitmap.getWidth()*2, layout_height-defaultLineWidth*10, textPaint);
		}else {
			canvas.drawText(defaultSecondText, defaultRectWidth*7/2, layout_height-defaultLineWidth*10, textPaint);
		}
		
		if(firstBitmap!=null){
			canvas.drawBitmap(firstBitmap, defaultRectWidth*7/2,  layout_height/2-firstBitmap.getHeight()*3/2, textPaint);
			canvas.drawText(defaultFirstText, defaultRectWidth*7/2+firstBitmap.getWidth()*2, layout_height/2-defaultLineWidth*10, textPaint);
		}else {
			canvas.drawText(defaultFirstText, defaultRectWidth*7/2, layout_height/2-defaultLineWidth*10, textPaint);
		}
		textPaint.setTextSize(defaultRightTextSize);
		textPaint.setColor(defaultRightTextColor);
		canvas.drawText(firstRectHeightPercentage+"h", layout_width-textPaint.measureText(firstRectHeightPercentage+"h"), layout_height/2-defaultLineWidth*10, textPaint);
		canvas.drawText(secondRectHeightPercentage+"h", layout_width-textPaint.measureText(secondRectHeightPercentage+"h"), layout_height-defaultLineWidth*10, textPaint);
		
	}
	
	public void setFirstTextAndImage(int textId, int imageId){
		defaultFirstText = getResources().getString(textId);
		if (imageId>0) {
			firstBitmap = BitmapFactory.decodeResource(getResources(), imageId);
		}
		
	}
	
	public void setSecondTextAndImage(int textId, int imageId){
		defaultSecondText = getResources().getString(textId);
		if (imageId>0) {
			secondBitmap = BitmapFactory.decodeResource(getResources(), imageId);
		}
		
	}
	
	
	public void setRectHeight(int firstPercentage, int secondPercentag){
		firstRectHeightPercentage =  firstPercentage;
		secondRectHeightPercentage=  secondPercentag;
		invalidate();
	}


}
