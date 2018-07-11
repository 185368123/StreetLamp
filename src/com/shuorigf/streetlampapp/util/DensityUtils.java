package com.shuorigf.streetlampapp.util;

import android.content.Context;
import android.util.TypedValue;

public class DensityUtils {
	
	public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
	}

}
