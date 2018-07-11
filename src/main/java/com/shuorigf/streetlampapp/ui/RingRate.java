package com.shuorigf.streetlampapp.ui;

import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.util.DensityUtils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationSet;

public class RingRate extends View {

	private int layout_height;
	private int layout_width;
	private int centre;

	private int defaultSize;
	private int defaultCircleWidth;

	private Paint[] paints;

	private int defaulFirstBgCircleColor;
	private int defaulFirstCircleColor;
	private int defaultSecondBgCircleColor;
	private int defaultSecondCircleColor;
	private int defaultThirdBgCircleColor;
	private int defaultThirdCircleColor;
	private RectF firstCircleRectF;
	private RectF secondCircleRectF;
	private RectF thirdCircleRectF;
	private int radiusFirst;
	private int radiusSecond;
	private int radiusThird;

	private int firstDegree;
	private int secondDegree;
	private int thirdDegree;

	public RingRate(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public RingRate(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RingRate(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		defaultSize = DensityUtils.dip2px(context, 120);
		defaulFirstCircleColor = getResources().getColor(R.color.blue);
		defaulFirstBgCircleColor = getResources().getColor(
				R.color.blue_10_transparency);
		defaultSecondCircleColor = getResources().getColor(R.color.yellow);
		defaultSecondBgCircleColor = getResources().getColor(
				R.color.yellow_10_transparency);
		defaultThirdCircleColor = getResources().getColor(R.color.red);
		defaultThirdBgCircleColor = getResources().getColor(
				R.color.red_10_transparency);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		layout_width = w;
		layout_height = h;
		centre = layout_width / 2;
		defaultCircleWidth = Math.min(layout_width, layout_height) / 9;
		initPaint();
		initRect();
	}

	private void initPaint() {
		paints = new Paint[2];
		paints[0] = new Paint();
		paints[0].setAntiAlias(true);
		paints[0].setStyle(Style.FILL);
		paints[1] = new Paint();
		paints[1].setAntiAlias(true);
		paints[1].setStyle(Style.STROKE);
		paints[1].setStrokeWidth(defaultCircleWidth);
		paints[1].setStrokeCap(Cap.ROUND);
	}

	private void initRect() {
		radiusFirst = centre - defaultCircleWidth / 2;
		radiusSecond = centre - defaultCircleWidth * 3 / 2;
		radiusThird = centre - defaultCircleWidth * 5 / 2;
		firstCircleRectF = new RectF(centre - radiusFirst,
				centre - radiusFirst, centre + radiusFirst, centre
						+ radiusFirst);
		secondCircleRectF = new RectF(centre - radiusSecond, centre
				- radiusSecond, centre + radiusSecond, centre + radiusSecond);
		thirdCircleRectF = new RectF(centre - radiusThird,
				centre - radiusThird, centre + radiusThird, centre
						+ radiusThird);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measure(widthMeasureSpec),
				measure(heightMeasureSpec));
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

		paints[1].setColor(defaulFirstBgCircleColor);
		canvas.drawCircle(centre, centre, radiusFirst, paints[1]);
		paints[0].setColor(defaulFirstCircleColor);
		paints[1].setColor(defaulFirstCircleColor);
		if (firstDegree == 0) {
			canvas.drawCircle(centre, defaultCircleWidth / 2,
					defaultCircleWidth / 2, paints[0]);
		} else {
			canvas.drawArc(firstCircleRectF, -90, firstDegree, false, paints[1]);
		}

		paints[1].setColor(defaultSecondBgCircleColor);
		canvas.drawCircle(centre, centre, radiusSecond, paints[1]);
		paints[0].setColor(defaultSecondCircleColor);
		paints[1].setColor(defaultSecondCircleColor);
		if (secondDegree == 0) {
			canvas.drawCircle(centre, defaultCircleWidth * 3 / 2,
					defaultCircleWidth / 2, paints[0]);
		} else {
			canvas.drawArc(secondCircleRectF, -90, secondDegree, false,
					paints[1]);
		}

		paints[1].setColor(defaultThirdBgCircleColor);
		canvas.drawCircle(centre, centre, radiusThird, paints[1]);
		paints[0].setColor(defaultThirdCircleColor);
		paints[1].setColor(defaultThirdCircleColor);
		if (thirdDegree == 0) {
			canvas.drawCircle(centre, defaultCircleWidth * 5 / 2,
					defaultCircleWidth / 2, paints[0]);
		} else {
			canvas.drawArc(thirdCircleRectF, -90, thirdDegree, false, paints[1]);
		}

	}

	public void setCircleRadian(float firstPercentage, float secondPercentag,
			float thirdPercentage) {
		firstDegree = (int) (360 * firstPercentage);
		secondDegree = (int) (360 * secondPercentag);
		thirdDegree = (int) (360 * thirdPercentage);
		invalidate();
	}

}
