package com.shuorigf.streetlampapp.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.shuorigf.streetlampapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TimeChooseView extends LinearLayout implements View.OnClickListener {
    private int minYear = DEFAULT_MIN_YEAR;
    private int maxYear = Calendar.getInstance().get(Calendar.YEAR) + 1;
    private String textCancel = "清除";
    private String textConfirm = "确定";
    private String dateChose;
    private int colorCancel = Color.parseColor("#999999");
    private int colorConfirm = Color.parseColor("#303F9F");
    private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
    private int viewTextSize = 25;

    private static final int DEFAULT_MIN_YEAR = 1900;
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView yearLoopView;
    public LoopView monthLoopView;
    public LoopView dayLoopView;
    public View pickerContainerV;
    public View contentView;//root view

    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    private OnDatePickedListener mListener;


    List<String> yearList = new ArrayList();
    List<String> monthList = new ArrayList();
    List<String> dayList = new ArrayList();

    public TimeChooseView(Context context, OnDatePickedListener mListener) {
        super(context);
        init(context);
        this.mListener = mListener;
    }

    public TimeChooseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    public void init(Context context) {
        dateChose = getStrDate();
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_date_picker, this, true);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setTextColor(colorCancel);
        cancelBtn.setTextSize(btnTextSize);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setTextColor(colorConfirm);
        confirmBtn.setTextSize(btnTextSize);
        yearLoopView = (LoopView) contentView.findViewById(R.id.picker_year);
        monthLoopView = (LoopView) contentView.findViewById(R.id.picker_month);
        dayLoopView = (LoopView) contentView.findViewById(R.id.picker_day);
        pickerContainerV = contentView.findViewById(R.id.container_picker);

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 可以方便地修改日期格式
        String hehe = dateFormat.format(now);
        setSelectedDate(hehe);
        yearLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                yearPos = item;
                initDayPickerView();
            }
        });
        monthLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                monthPos = item;
                initDayPickerView();
            }
        });
        dayLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                dayPos = item;
            }
        });

        initPickerViews(); // init year and month loop view
        initDayPickerView(); //init day loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        if (!TextUtils.isEmpty(textConfirm)) {
            confirmBtn.setText(textConfirm);
        }

        if (!TextUtils.isEmpty(textCancel)) {
            cancelBtn.setText(textCancel);
        }

        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());

    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerViews() {

        int yearCount = maxYear - minYear;

        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        for (int j = 0; j < 12; j++) {
            monthList.add(format2LenStr(j + 1));
        }

        yearLoopView.setDataList((ArrayList) yearList);
        yearLoopView.setInitPosition(yearPos);

        monthLoopView.setDataList((ArrayList) monthList);
        monthLoopView.setInitPosition(monthPos);
    }

    /**
     * Init day item
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);

        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

        dayLoopView.setDataList((ArrayList) dayList);
        dayLoopView.setInitPosition(dayPos);
    }


    /**
     * set selected date position value when initView.
     *
     * @param dateStr
     */
    public void setSelectedDate(String dateStr) {

        if (!TextUtils.isEmpty(dateStr)) {
            long milliseconds = getLongFromyyyyMMdd(dateStr);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);

            if (milliseconds != -1) {
                calendar.setTimeInMillis(milliseconds);
                yearPos = calendar.get(Calendar.YEAR) - minYear;
                monthPos = calendar.get(Calendar.MONTH);
                dayPos = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == cancelBtn) {
            mListener.onDatePickCancle();
        } else if (v == confirmBtn) {

            if (null != mListener) {

                int year = minYear + yearPos;
                int month = monthPos + 1;
                int day = dayPos + 1;
                StringBuffer sb = new StringBuffer();

                sb.append(String.valueOf(year));
                sb.append("-");
                sb.append(format2LenStr(month));
                sb.append("-");
                sb.append(format2LenStr(day));
                mListener.onDatePickCompleted(year, month, day, sb.toString());
            }
        }
    }

    public static String getStrDate() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dd.format(new Date());
    }

    /**
     * get long from yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     *
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public interface OnDatePickedListener {

        /**
         * Listener when date has been checked
         *
         * @param year
         * @param month
         * @param day
         * @param dateDesc yyyy-MM-dd
         */
        void onDatePickCompleted(int year, int month, int day,
                                 String dateDesc);

        void onDatePickCancle();
    }
}
