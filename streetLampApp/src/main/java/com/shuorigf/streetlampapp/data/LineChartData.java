package com.shuorigf.streetlampapp.data;

import java.util.ArrayList;
import java.util.List;



import android.graphics.Color;

public class LineChartData {

	public final static int SOLID_LINE=0;
	public final static int DOTTED_LINE=1;
	public List<LineInfoData>mLinDataList;
	public List<String>mXVal;
	
	public LineChartData(List<String> xVal) {
		this.mXVal = xVal;
		mLinDataList = new ArrayList<LineInfoData>();
	}

	public boolean AddLine(List<Float> yVal,int color,int type)
	{
		if(mXVal == null || yVal == null)
			return false;
		if(mXVal.size() != yVal.size())
			return false;
		LineInfoData data = new LineInfoData(yVal,color,type);
		mLinDataList.add(data);
		return true;
	}
	
	public class LineInfoData{
		public List<Float>yVal;
		public int lineColor;
		public int lineType;
		public LineInfoData(List<Float> yVal, int lineColor, int lineType) {
			this.yVal = yVal;
			this.lineColor = lineColor;
			this.lineType = lineType;
		}
		
	}



}
