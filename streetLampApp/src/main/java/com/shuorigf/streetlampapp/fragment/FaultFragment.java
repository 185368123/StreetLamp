package com.shuorigf.streetlampapp.fragment;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import com.shuorigf.streetlampapp.data.FaultData.DataBean.ListBean.HistoryListBean;
import com.shuorigf.streetlampapp.EditFaultListActivity;
import com.shuorigf.streetlampapp.FaultDetailsActivity;
import com.shuorigf.streetlampapp.MainActivity;
import com.shuorigf.streetlampapp.R;
import com.shuorigf.streetlampapp.adapter.FaultContentAdapter;
import com.shuorigf.streetlampapp.adapter.GirdDropDownAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.FaultData;
import com.shuorigf.streetlampapp.data.FilterFailureTypeData;
import com.shuorigf.streetlampapp.data.FilterProjectData;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.domain.Page;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.widget.DropDownMenu;
import com.shuorigf.streetlampapp.widget.TimeChooseView;
import com.shuorigf.streetlampapp.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class FaultFragment extends BaseFragment implements OnClickListener, XListView.IXListViewListener {

    private static final String TAG = FaultFragment.class.getSimpleName();
    private static final int FAULT_DETAILS_REQUESTCODE = 1;
    private static final int FAULT_EDIT_REQUESTCODE = 2;
    private String headers[] = {"项目", "日期", "故障类型", "故障处理"};
    private String deals[] = {"全部", "已处理", "未处理"};
    private ImageButton mOpenLeftMenuIBtn;
    private ImageButton mEditIBtn;
    private XListView mFaultcontentLv;
    private FaultContentAdapter mFaultContentAdapter;
    private DropDownMenu mDropDownMenu;
    private GirdDropDownAdapter dealsAdapter;
    private GirdDropDownAdapter projectsAdapter;
    private GirdDropDownAdapter failuretypesAdapter;
    private List<View> popupViews = new ArrayList<>();
    private List<String> projects = new ArrayList<>();
    private List<String> failuretypes = new ArrayList<>();

    public List<FilterProjectData.DataBean.ProjectsBean> filterProjects;

    private String filterProject_choose="";

    public List<FilterFailureTypeData.DataBean> filterFailureTypes;

    private String filterFailureType_choose="";

    private String status="";

    private String date="";

    private LoginData mLoginData;

    private Page mPage;

    private boolean isLoading;
    private boolean isLoaded;
    private TimeChooseView chooseView;


    @Override
    public View initView(Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.fragment_fault, null);
        mDropDownMenu = (DropDownMenu) view.findViewById(R.id.dropDownMenu);
        mOpenLeftMenuIBtn = (ImageButton) view.findViewById(R.id.imgbtn_fault_accout);
        mOpenLeftMenuIBtn.setOnClickListener(this);
        mEditIBtn = (ImageButton) view.findViewById(R.id.imgbtn_fault_edit);
        mEditIBtn.setOnClickListener(this);
        mFaultcontentLv = (XListView) view.findViewById(R.id.lv_fault_content);
        mFaultcontentLv.setPullRefreshEnable(true);
        mFaultcontentLv.setPullLoadEnable(true);
        mFaultcontentLv.setAutoLoadEnable(true);
        mFaultcontentLv.setXListViewListener(this);
        mFaultcontentLv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position < 1) {
                    return;
                }
                Bundle fault = new Bundle();
                fault.putString("fault_id", mFaultContentAdapter.getItem(position - 1).getId());
                fault.putInt("fault_status", new Integer(mFaultContentAdapter.getItem(position - 1).getStatusX()));
                goFaultDetails(fault);
            }
        });

        projects.add("全部");
        failuretypes.add("全部");

        final ListView projectsView = new ListView(getActivity());
        projectsAdapter = new GirdDropDownAdapter(getActivity(), projects);
        projectsView.setDividerHeight(0);
        projectsView.setAdapter(projectsAdapter);

        chooseView = new TimeChooseView(getActivity(), new TimeChooseView.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                date=dateDesc;
                mDropDownMenu.setTabText(dateDesc);
                chooseView.setSelectedDate(dateDesc);
                getFaultData("1", "20", true);
                mDropDownMenu.closeMenu();
            }

            @Override
            public void onDatePickCancle() {
                date="";
                mDropDownMenu.setTabText(headers[1]);
                getFaultData("1", "20", true);
                mDropDownMenu.closeMenu();
            }
        });

        final ListView failuretypesView = new ListView(getActivity());
        failuretypesAdapter = new GirdDropDownAdapter(getActivity(), failuretypes);
        failuretypesView.setDividerHeight(0);
        failuretypesView.setAdapter(failuretypesAdapter);

        final ListView dealsView = new ListView(getActivity());
        dealsAdapter = new GirdDropDownAdapter(getActivity(), Arrays.asList(deals));
        dealsView.setDividerHeight(0);
        dealsView.setAdapter(dealsAdapter);

        popupViews.add(projectsView);
        popupViews.add(chooseView);
        popupViews.add(failuretypesView);
        popupViews.add(dealsView);

        projectsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mDropDownMenu.setTabText(position == 0 ? headers[0] : projects.get(position));
                projectsAdapter.setCheckItem(position);
                if (filterProjects!=null){
                    if (position==0){
                        filterProject_choose="";
                    }else
                    filterProject_choose=filterProjects.get(position-1).getId();
                }
                getFaultData("1", "20", true);
                mDropDownMenu.closeMenu();
            }
        });

        failuretypesView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                failuretypesAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : failuretypes.get(position));
                if (filterFailureTypes!=null){
                    if (position==0){
                        filterFailureType_choose="";
                    }else
                        filterFailureType_choose=filterFailureTypes.get(position-1).getName();
                }
                getFaultData("1", "20", true);
                mDropDownMenu.closeMenu();
            }
        });

        dealsView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dealsAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[3] : deals[position]);
                status=(position==0?"":(position-1)+"");
                switch (position){
                    case 0:
                        status="";
                        break;
                    case 1:
                        status="1";
                        break;
                    case 2:
                        status="0";
                        break;
                }
                getFaultData("1", "20", true);
                mDropDownMenu.closeMenu();
            }
        });

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews);
        return view;


    }

    public void initData() {
        mLoginData = ((StreetlampApp) mActivity.getApplication()).mLoginData;
        mFaultContentAdapter = new FaultContentAdapter(mActivity);
        mFaultcontentLv.setAdapter(mFaultContentAdapter);
        mPage = new Page();
        getFaultData("1", "20", true);
        getFilterData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgbtn_fault_accout:
                openLeftMenu();
                break;
            case R.id.imgbtn_fault_edit:
                goEditFaultList();
                break;
            default:
                break;
        }
    }

    private void openLeftMenu() {
        MainActivity mainActivity = (MainActivity) mActivity;
//		mainActivity.getSlidingMenu().toggle();
        mainActivity.toggle();
    }

    private void goFaultDetails(Bundle fault) {
        Intent intent = new Intent(mActivity, FaultDetailsActivity.class);
        intent.putExtras(fault);
        this.startActivityForResult(intent, FAULT_DETAILS_REQUESTCODE);
    }

    private void goEditFaultList() {
        Intent intent = new Intent(mActivity, EditFaultListActivity.class);
        this.startActivityForResult(intent, FAULT_EDIT_REQUESTCODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FAULT_DETAILS_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    OkHttpUtils.getInstance().cancelTag(this);
                    isLoaded = false;
                    getFaultData("1", "20", true);
                }
                break;

            case FAULT_EDIT_REQUESTCODE:
                if (resultCode == Activity.RESULT_OK) {
                    OkHttpUtils.getInstance().cancelTag(this);
                    isLoaded = false;
                    getFaultData("1", "20", true);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onLoadMore() {
        getFaultData(mPage.getCurrentPage(), "10", false);
    }


    private void getFilterData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FILTER_PROJECT_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<FilterProjectData>(new JsonGenericsSerializator()) {
                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Log.e(TAG, "FilterProjectData onError:" + e.getMessage());
                        Toast.makeText(mActivity, R.string.network_is_not_smooth,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(FilterProjectData filterProjectData, int id) {
                        Log.i(TAG, "FilterProjectData onResponse:" + filterProjectData.toString());
                        if (filterProjectData.getStatus().equals(NetManager.SUCCESS)) {
                            filterProjects = filterProjectData.getData().getProjects();
                            if (filterProjects != null) {
                                for (int i = 0; i < filterProjectData.getData().getProjects().size(); i++) {
                                    projects.add(filterProjectData.getData().getProjects().get(i).getName());
                                }
                                projectsAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(mActivity, filterProjectData.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FILTER_FAILURE_TYPE_URL)
                .params(params)
                .build()
                .execute(new GenericsCallback<FilterFailureTypeData>(new JsonGenericsSerializator()) {
                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Log.e(TAG, "FilterFailureTypeData onError:" + e.getMessage());
                        Toast.makeText(mActivity, R.string.network_is_not_smooth,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(FilterFailureTypeData filterFailureTypeData, int id) {
                        Log.i(TAG, "FilterFailureTypeData onResponse:" + filterFailureTypeData.toString());
                        if (filterFailureTypeData.getStatus().equals(NetManager.SUCCESS)) {
                            filterFailureTypes = filterFailureTypeData.getData();
                            if (filterFailureTypes != null) {
                                for (int i = 0; i < filterFailureTypeData.getData().size(); i++) {
                                    failuretypes.add(filterFailureTypeData.getData().get(i).getName());
                                }
                                failuretypesAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(mActivity, filterFailureTypeData.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void getFaultData(String page, String count, final boolean isfresh) {
        if (isLoading) {
            return;
        }
        isLoading = true;

        Log.d("过滤参数：","projectid"+filterProject_choose+"    startDate"+date+"    alarmtype"+filterFailureType_choose+"    status"+status);
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        params.put("page", page);
        params.put("count", count);
        params.put("projectid", filterProject_choose);
        if (!date.equals("")){
            params.put("startDate", date+" 00:00:00");
            params.put("endDate", date+" 23:59:59");
        }
        params.put("alarmtype", filterFailureType_choose);
        params.put("status", status);
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.FAULT_LIST_URL_NEW)
                .params(params)
                .build()
                .execute(new GenericsCallback<FaultData>(new JsonGenericsSerializator()) {
                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mFaultcontentLv.stopRefresh();
                        mFaultcontentLv.stopLoadMore();
                        isLoading = false;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        e.printStackTrace();
                        Log.e(TAG, "FaultData onError:" + e.getMessage());
                        Toast.makeText(mActivity, R.string.network_is_not_smooth,
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(FaultData faultData, int id) {
                        Log.i(TAG, "FaultData onResponse:" + faultData.toString());
                        if (faultData.getStatus().equals(NetManager.SUCCESS)) {
                            if (isfresh) {
                                mPage.setPageStart();
                                mFaultContentAdapter.clear();
                            }
                            isLoaded = true;
                            mPage.addPage();
                            setFaultData(faultData);

                        } else {
                            Toast.makeText(mActivity, faultData.getMsg(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setFaultData(FaultData faultData) {
        MainActivity mainActivity = (MainActivity) mActivity;
        mainActivity.setFaultCount(faultData.getData().getList().getTotal() + "");
        List<HistoryListBean> mFaults = faultData.getData().getList().getHistory_list();
        if (mFaults != null) {
            mFaultContentAdapter.addFaults(mFaults);
        }
    }


    @Override
    public void onRefresh() {
        getFaultData("1", "20", true);
    }

    public void onVisible() {
        if (isPrepared && !isLoaded) {
            getFaultData("1", "20", true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag(this);
    }

}
