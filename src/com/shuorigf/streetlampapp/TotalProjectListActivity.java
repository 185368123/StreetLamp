package com.shuorigf.streetlampapp;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import com.shuorigf.streetlampapp.adapter.ProjectContentAdapter;
import com.shuorigf.streetlampapp.adapter.ProjectSearchResultAdapter;
import com.shuorigf.streetlampapp.app.StreetlampApp;
import com.shuorigf.streetlampapp.callback.GenericsCallback;
import com.shuorigf.streetlampapp.callback.JsonGenericsSerializator;
import com.shuorigf.streetlampapp.data.LoginData;
import com.shuorigf.streetlampapp.data.ProjectData;
import com.shuorigf.streetlampapp.data.ProjectData.Data.Project;
import com.shuorigf.streetlampapp.network.NetManager;
import com.shuorigf.streetlampapp.ui.SideBar;
import com.shuorigf.streetlampapp.ui.SideBar.OnTouchingLetterChangedListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TotalProjectListActivity extends NavigationBarActivity implements OnClickListener{

	private static final String TAG = TotalProjectListActivity.class.getSimpleName();
	private static final int ALL_CALLBACK_ID = 101;
	private static final int SEARCH_CALLBACK_ID = 102;
	private static final int ADD_PROJECT_REQUESTCODE = 1;
	private static final int PROJECT_DETAILS_REQUESTCODE = 2;
//	private static final int SELECT_LAMP_CONTROL_REQUESTCODE = 3;
	
	public static final int TOTAL_PROJECT = 0;
	public static final int SELECT_PROJECT = 1;
//	public static final int SELECT_REPORT = 2;
	
	private SideBar mLetterSideBar;
	private EditText mProjectSearchEdt;
	private ViewGroup mSearchEmptyView;
	private ListView mSearchResultLv;
	private ListView mProjectContentLv;
	private ProjectSearchResultAdapter mProjectSearchResultAdapter;
	private ProjectContentAdapter mProjectContentAdapter;
	
	
	private LoginData mLoginData;
	
//	private List <Project> mProjects;
	
	private int type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		View view = View.inflate(this, R.layout.activity_total_project_list, null);
		mSearchEmptyView = (ViewGroup) view.findViewById(R.id.llyt_total_project_list_search_empty);
		mProjectSearchEdt = (EditText) view.findViewById(R.id.edt_total_project_list_search);
		mProjectSearchEdt.addTextChangedListener(new TextWatcher() {
	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {}

	            @Override
	            public void afterTextChanged(Editable s) {
	                String keyword = s.toString();
	                if (TextUtils.isEmpty(keyword)) {
	                	mSearchEmptyView.setVisibility(View.GONE);
	                	mSearchResultLv.setVisibility(View.GONE);
	                } else {
	                	getSearchResult(keyword);

	                }
	            }
	        });
		mSearchResultLv = (ListView) view.findViewById(R.id.lv_total_project_list_search_result);
		mSearchResultLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle project = new Bundle();
				project.putString("project_id", mProjectSearchResultAdapter.getItem(position).getId());
				project.putString("project_name", mProjectSearchResultAdapter.getItem(position).getName());
				if (type==TOTAL_PROJECT) {
					goProjectDetails(project);
				}else if(type==SELECT_PROJECT){
					backDeviceFragment(project);
				}
//				else {
//					goSelectLampControl(project);
//				}
				
			}
		});
		mProjectContentLv = (ListView) view.findViewById(R.id.lv_total_project_list_content);
		mProjectContentLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bundle project = new Bundle();
				project.putString("project_id", mProjectContentAdapter.getItem(position).getId()+"");
				project.putString("project_name", mProjectContentAdapter.getItem(position).getName());
				if (type==TOTAL_PROJECT) {
					goProjectDetails(project);
				}else if(type==SELECT_PROJECT){
					backDeviceFragment(project);
				}
//				else {
//					goSelectLampControl(project);
//				}
				
			}
		});
		
		mLetterSideBar =  (SideBar) view.findViewById(R.id.sidebar_total_project_list);
		mLetterSideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String letter) {
				int position = mProjectContentAdapter.getLetterPosition(letter);
				mProjectContentLv.setSelection(position);
			}
		});
		setNavigationBarContentView(view);
	}
	
	
	private void initData() {
		type = getIntent().getIntExtra("type",TOTAL_PROJECT);
		mLoginData = ((StreetlampApp)getApplication()).mLoginData;
		mRightIBtn.setVisibility(View.VISIBLE);
		mRightIBtn.setImageResource(R.drawable.ic_add);
		mRightIBtn.setOnClickListener(this);
		mProjectContentAdapter = new ProjectContentAdapter(this);
		mProjectContentLv.setAdapter(mProjectContentAdapter);
		mProjectSearchResultAdapter = new ProjectSearchResultAdapter(this);
		mSearchResultLv.setAdapter(mProjectSearchResultAdapter);
		getProjectData();
	}
	
	private void getProjectData() {
		Map<String, String> params = new HashMap<String, String>();
        params.put("username", mLoginData.getUsername());
        params.put("client_key", mLoginData.getClient_key());
        params.put("token", mLoginData.getData().getToken());
        OkHttpUtils
                .post()
                .tag(this)
                .url(NetManager.PROJECT_LIST_URL)
                .params(params)
                .id(ALL_CALLBACK_ID)
                .build()
                .execute(myCallback);
				
	}

	private void setProjectData(ProjectData projectData) {
	     List <Project> projects = projectData.getData().getProjects();
		if (projects!=null&&projects.size()>0) {
			Collections.sort(projects, new ProjectComparator());
			if (type==TOTAL_PROJECT) {
				setTitleText(getString(R.string.total_project)+"("+projects.size()+")");
			}else if(type==SELECT_PROJECT){
				setTitleText(getString(R.string.select_project)+"("+projects.size()+")");
			}
			mProjectContentAdapter.changeData(projects);
		}
		
	}
	
	
	
	 private class ProjectComparator implements Comparator<Project>{
	        @Override
	        public int compare(Project lhs, Project rhs) {
	            String a = lhs.getTag().toUpperCase();
	            String b = rhs.getTag().toUpperCase();
	            return a.compareTo(b);
	        }
	    }
	
	 
	 private void getSearchResult(String keyword) {
		    Map<String, String> params = new HashMap<String, String>();
	        params.put("username", mLoginData.getUsername());
	        params.put("client_key", mLoginData.getClient_key());
	        params.put("token", mLoginData.getData().getToken());
	        params.put("keyword", keyword);
	        OkHttpUtils
	                .post()
	                .tag(this)
	                .url(NetManager.PROJECT_LIST_URL)
	                .params(params)
	                .id(SEARCH_CALLBACK_ID)
	                .build()
	                .execute(myCallback);
	 }
	 
	 
	 private void setSearchProjectData(ProjectData projectData) {
		 mSearchResultLv.setVisibility(View.VISIBLE);
         List <Project> projects = projectData.getData().getProjects();
         if (projects == null || projects.size() == 0) {
         	mSearchEmptyView.setVisibility(View.VISIBLE);
         } else {
         	mSearchEmptyView.setVisibility(View.GONE);
         	mProjectSearchResultAdapter.changeData(projects);
         }
	}	
	 
	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_navigation_bar_right:
			goAddProject();
			break;
		default:
			break;
		}
	}
	
	private void goAddProject() {
		Intent intent = new Intent(this,AddProjectActivity.class);
		startActivityForResult(intent, ADD_PROJECT_REQUESTCODE);
	}
	
	private void goProjectDetails(Bundle project) {
		Intent intent = new Intent(this,ProjectDetailsActivity.class);
		intent.putExtras(project);
		startActivityForResult(intent, PROJECT_DETAILS_REQUESTCODE);
	}
	
	
	private void backDeviceFragment(Bundle project) {
        Intent intent = new Intent();
        intent.putExtras(project);
        setResult(RESULT_OK, intent);
        finish();
	}
	
//	private void goSelectLampControl(Bundle project) {
//		Intent intent = new Intent(this,SelectLampControlActivity.class);
//		intent.putExtras(project);
//		startActivityForResult(intent, SELECT_LAMP_CONTROL_REQUESTCODE);
//	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
        case ADD_PROJECT_REQUESTCODE:
            if (resultCode == RESULT_OK) {
            	getProjectData();
            }
            break;
        case PROJECT_DETAILS_REQUESTCODE:
            if (resultCode == RESULT_OK) {
            	getProjectData();
            }
            break;
//        case SELECT_LAMP_CONTROL_REQUESTCODE:
//            if (resultCode == RESULT_OK) {
//            	setResult(RESULT_OK, data);
//    			finish();
//            }
//            break;
        default:
            break;
        }
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkHttpUtils.getInstance().cancelTag(this);
	}
	
	
   private Callback<ProjectData> myCallback = new GenericsCallback<ProjectData>(new JsonGenericsSerializator())
    {
	
	@Override
    public void onError(Call call, Exception e, int id)
    {
  	  e.printStackTrace();
  	  Log.e(TAG, "ProjectData onError:"+e.getMessage());
  	  Toast.makeText(TotalProjectListActivity.this, R.string.network_is_not_smooth,
					Toast.LENGTH_SHORT).show();
    }
	@Override
	public void onResponse(ProjectData projectData, int id) {
		Log.i(TAG, "ProjectData onResponse:"+projectData.toString());
		 if(projectData.getStatus().equals(NetManager.SUCCESS)){
			 
			 switch (id) {
				case ALL_CALLBACK_ID:
					 setProjectData(projectData);
					break;
				case SEARCH_CALLBACK_ID:
					setSearchProjectData(projectData);
					break;
				}
			   
			    
		 }else {
			 Toast.makeText(TotalProjectListActivity.this, projectData.getMsg(),
						Toast.LENGTH_SHORT).show();
		 }
	}
};

}
