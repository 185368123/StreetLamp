package com.shuorigf.streetlampapp.ui;



import com.shuorigf.streetlampapp.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SideBar extends View {
	private OnTouchingLetterChangedListener listener;
	// private TextView dialogTv;
	public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };

	private int choose = -1;

	private Paint paint = new Paint();
	
	private float defaultTextSize;
	private int defaultTextColorNor;
	private int defaultTextColorSel;

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}


	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SideBar(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		defaultTextSize = getResources().getDimension(R.dimen.side_bar_letter_size);
		defaultTextColorNor = getResources().getColor(R.color.text_gray);
		defaultTextColorSel = getResources().getColor(R.color.black);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / b.length;

		for (int i = 0; i < b.length; i++) {
			paint.setColor(defaultTextColorNor);
			// paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAntiAlias(true);
			paint.setTextSize(defaultTextSize);

			if (choose == i) {
//				paint.setTextSize(25);
				paint.setColor(defaultTextColorSel);
//				paint.setFakeBoldText(true);
			}

			float xPos = width / 2 - paint.measureText(b[i]) / 2;
			float yPos = singleHeight * i + singleHeight;

			canvas.drawText(b[i], xPos, yPos, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float y = event.getY();
		int c = (int) (y / getHeight() * b.length);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					choose = c;
					invalidate();
				}
			break;
		case MotionEvent.ACTION_UP:
			choose = -1;
			invalidate();
			// if (dialogTv != null) {
			// dialogTv.setVisibility(View.INVISIBLE);
			// }
			break;
		case MotionEvent.ACTION_MOVE:
			if (choose != c) {
				if (c >= 0 && c < b.length) {
					if (listener != null) {
						listener.onTouchingLetterChanged(b[c]);
					}
					// if (dialogTv != null) {
					// if(action == MotionEvent.ACTION_DOWN) {
					// dialogTv.setFirstProgress(c);
					// if (listener != null) {
					// listener.onDownTouch(c);
					// }
					// }else {
					// //dialogTv.setText(b[c]);
					// dialogTv.setProgress(c);
					// }
					//
					// dialogTv.setVisibility(View.VISIBLE);
					// }
					choose = c;
					invalidate();
				}
			}
			break;
		default:
		

			break;
		}
		return true;
	}

	// public void setTextView(TextView dialogTv) {
	// this.dialogTv = dialogTv;
	// }

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.listener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String letter);
	}

}
