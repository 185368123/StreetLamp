package com.shuorigf.streetlampapp.dialog;


import com.shuorigf.streetlampapp.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DialogFactory {
	
	public static Dialog creatRequestDialog(final Context context, int tipId) {

		final Dialog dialog = new Dialog(context,R.style.dialog);
		dialog.setContentView(R.layout.dialog_waiting);
		dialog.setCanceledOnTouchOutside(false);
		if (tipId!=0) {
			TextView titleTv = (TextView) dialog.findViewById(R.id.tv_show_content);
			titleTv.setVisibility(View.VISIBLE);
			titleTv.setText(tipId);
		}
		return dialog;
	}
	
	public static Dialog creatResultDialog(final Context context,int resId, int tipId) {

		final Dialog dialog = new Dialog(context,R.style.dialog);
		dialog.setContentView(R.layout.dialog_result);
		ImageView titleIv = (ImageView) dialog.findViewById(R.id.iv_show_content);
		TextView titleTv = (TextView) dialog.findViewById(R.id.tv_show_content);
		titleIv.setImageResource(resId);
		titleTv.setText(tipId);
		return dialog;
	}
}
