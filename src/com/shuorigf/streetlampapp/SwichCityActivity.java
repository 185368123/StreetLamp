package com.shuorigf.streetlampapp;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.shuorigf.streetlampapp.adapter.AllCityListAdapter;
import com.shuorigf.streetlampapp.adapter.AllCityListAdapter.OnCityClickListener;
import com.shuorigf.streetlampapp.adapter.CitySearchResultAdapter;
import com.shuorigf.streetlampapp.db.dao.DBCityManager;
import com.shuorigf.streetlampapp.domain.City;
import com.shuorigf.streetlampapp.ui.SwitchCitySideBar;
import com.shuorigf.streetlampapp.ui.SwitchCitySideBar.OnTouchingLetterChangedListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class SwichCityActivity extends NavigationBarActivity implements OnClickListener{
	private static final String TAG = SwichCityActivity.class.getSimpleName();
	
	private ListView mAllCityLv;
    private ListView mSearchResultLv;
    private SwitchCitySideBar mLetterSideBar;
    private EditText mCitySearchEdt;
    private ViewGroup mSearchEmptyView;
//    private ImageView clearBtn;
    
    private AllCityListAdapter mAllCityListAdapter;
    private CitySearchResultAdapter mCitySearchResultListAdapter;
    private List<City> mAllCities;
    private DBCityManager mDBCityManager;
    
    private LocationClient mLocClient;
	private LocationClientOption mOption;
	private MyLocationListenner myListener = new MyLocationListenner();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	private void initView() {
		View view = View.inflate(this, R.layout.activity_switch_city, null);
		mAllCityLv = (ListView) view.findViewById(R.id.lv_switch_city_all_city);

		mLetterSideBar =  (SwitchCitySideBar) view.findViewById(R.id.sidebar_switch_city);
		mLetterSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String letter) {
				// TODO Auto-generated method stub
				int position = mAllCityListAdapter.getLetterPosition(letter);
				mAllCityLv.setSelection(position);
			}
		});
		
		    mSearchEmptyView = (ViewGroup) view.findViewById(R.id.llyt_switch_city_search_empty);
		    mSearchResultLv = (ListView) view.findViewById(R.id.lv_switch_city_search_result);
		    mSearchResultLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                back(mCitySearchResultListAdapter.getItem(position).getName());
	            }
	        });

//	        clearBtn = (ImageView) findViewById(R.id.iv_search_clear);
//	        clearBtn.setOnClickListener(this);

		    mCitySearchEdt = (EditText) view.findViewById(R.id.edit_switch_city_search);
		    mCitySearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
//                  clearBtn.setVisibility(View.GONE);
                	mSearchEmptyView.setVisibility(View.GONE);
                	mSearchResultLv.setVisibility(View.GONE);
                } else {
//                    clearBtn.setVisibility(View.VISIBLE);
                	mSearchResultLv.setVisibility(View.VISIBLE);
                    List<City> result = mDBCityManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                    	mSearchEmptyView.setVisibility(View.VISIBLE);
                    } else {
                    	mSearchEmptyView.setVisibility(View.GONE);
                    	mCitySearchResultListAdapter.changeData(result);
                    }
                }
            }
        });

		setNavigationBarContentView(view);
	}

	private void initData() {
		setTitleText(R.string.switch_city);
		mDBCityManager = new DBCityManager(this);
        mAllCities = mDBCityManager.getAllCities();
        mAllCityListAdapter = new AllCityListAdapter(this, mAllCities);
        mAllCityLv.setAdapter(mAllCityListAdapter);
        mAllCityListAdapter.setOnCityClickListener(new OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
            	back(name);
            }

            @Override
            public void onLocateClick() {
//                Log.e("onLocateClick", "重新定位...");
//                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
//                mLocationClient.startLocation();
            }
        });

        mCitySearchResultListAdapter = new CitySearchResultAdapter(this);
		mSearchResultLv.setAdapter(mCitySearchResultListAdapter);
		
		mLocClient = new LocationClient(this);
        mLocClient.setLocOption(getClientOption());
        mLocClient.registerLocationListener(myListener);
        mLocClient.start();
	}
	
	private LocationClientOption getClientOption(){
		if(mOption == null){
			mOption = new LocationClientOption();
			mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
			mOption.setOpenGps(true); // 打开gps
			mOption.setIsNeedAddress(true);
		}
		return mOption;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	private void back(String city) {
        Intent data = new Intent();
        data.putExtra("city", city);
        setResult(RESULT_OK, data);
        finish();
	}
	
	/**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }
            String city = location.getCity();
            if(!TextUtils.isEmpty(city)) {
            	if (city.substring(city.length()-1).equals("市")) {
            		city = city.substring(0, city.length()-1);
            		mAllCityListAdapter.updateLocateState(city);
				}
            }
            mLocClient.stop();
            
        }

//        public void onReceivePoi(BDLocation poiLocation) {
//        }
    }

	@Override
	protected void onDestroy() {
		if (mLocClient!=null&&mLocClient.isStarted()) {
			mLocClient.stop();
		}
		super.onDestroy();
	}
    
    

}
