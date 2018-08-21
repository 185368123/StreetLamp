package com.shuorigf.streetlampapp.fragment;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.LampControlDetailsActivity;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.adapter.LampControlContentAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LampControlData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.LampControlData.Data.LampControl;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LampControlListFragment extends BaseFragment implements XListView.IXListViewListener {

    private static final String TAG = LampControlListFragment.class.getSimpleName();

    public static LampControlListFragment newInstance(String network_id) {
        LampControlListFragment lampControllistFragment = new LampControlListFragment();

        lampControllistFragment.network_id = network_id;
        return lampControllistFragment;
    }

    private String network_id;
    private int page=1;
    private int count=10;
    private boolean isRefrsh=true;
    private LampControlData data;
    private XListView mLampControlContentLv;
    private LampControlContentAdapter mLampControlContentAdapter;
    private List<LampControl> mLampControls;

    private LoginData mLoginData;


    @Override
    public View initView(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.fragment_lamp_control_list, null);
        mLampControlContentLv = (XListView) view.findViewById(R.id.lv_lamp_control_list_content);
        mLampControlContentLv.setPullRefreshEnable(true);
        mLampControlContentLv.setPullLoadEnable(true);
        mLampControlContentLv.setXListViewListener(this);
        mLampControlContentLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < 1) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("lamp_id", mLampControlContentAdapter.getItem(position - 1).getId());
                bundle.putString("lamp_numble", mLampControlContentAdapter.getItem(position - 1).getNumber());
                bundle.putString("network_name", mLampControlContentAdapter.getItem(position - 1).getNetwork_name());
                goLampControlDetails(bundle);
            }
        });

        return view;
    }


    public void initData() {
        mLoginData = ((StreetlampApp) mActivity.getApplication()).mLoginData;
        mLampControlContentAdapter = new LampControlContentAdapter(mActivity);
        mLampControlContentLv.setAdapter(mLampControlContentAdapter);
        getLampControlData();
    }


    private void getLampControlData() {
        if (network_id == null) {
            return;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("network_id", network_id);
        params.put("page", page+"");
        params.put("count", count+"");
        OkHttpUtils
                .post()
                .url(NetManager.LAMP_CONTROL_LIST_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<LampControlData>(new JsonGenericsSerializator()) {

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        if (isRefrsh){
                            mLampControlContentLv.stopRefresh();
                        }else
                            mLampControlContentLv.stopLoadMore();

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Log.e(TAG, "LampControlData onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(LampControlData lampControlData, int id) {
                        Log.i(TAG, "LampControlData onResponse:" + lampControlData.toString());
                        if (lampControlData.getStatus().equals(NetManager.SUCCESS)) {
//						    mLampControlData = lampControlData;
                            if (isRefrsh){
                                data=lampControlData;
                            }else {
                                //if (lampControlData.getData().getLamps().size()>0){}
                                data.getData().getLamps().addAll(lampControlData.getData().getLamps());
                            }
                            setLampControlData(data);
                        } else {
                            Toast.makeText(mActivity, lampControlData.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setLampControlData(LampControlData lampControlData) {
        mLampControls = lampControlData.getData().getLamps();
        mLampControlContentAdapter.changeData(mLampControls);

    }

    private void goLampControlDetails(Bundle bundle) {
        Intent intent = new Intent(mActivity, LampControlDetailsActivity.class);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void onRefresh() {
        page=1;
        isRefrsh=true;
        getLampControlData();
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        page++;
        isRefrsh=false;
        getLampControlData();
    }

}
