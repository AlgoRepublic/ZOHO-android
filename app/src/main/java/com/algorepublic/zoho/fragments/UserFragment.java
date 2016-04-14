package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterUser;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.UserService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by waqas on 2/8/16.
 */
public class UserFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{


    AQuery aq;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static ArrayList<Integer> assigneeList = new ArrayList<>();
    BaseClass baseClass;
    UserService service;
    AdapterUser adapterUser;


    public UserFragment() {
        // Required empty public constructor
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_user, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        adapterUser=new AdapterUser(getActivity());
        getToolbar().setTitle(getString(R.string.users));
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        service = new UserService(getActivity());
        if(baseClass.getSelectedProject().equalsIgnoreCase("0")){
            service.getAllUsers(true, new CallBack(UserFragment.this, "UserList"));
        }else {
            service.getUserListByProject(Integer.parseInt(baseClass.getSelectedProject()), true,
                    new CallBack(UserFragment.this, "UserList"));
        }
            return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    public void UserList(Object caller, Object model){
        UserListModel.getInstance().setList((UserListModel) model);
        aq.id(R.id.alertMessage).text(getString(R.string.no_users));
        if(UserListModel.getInstance().responseObject.size() ==0){
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
        }else{
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
        if (UserListModel.getInstance().responseObject.size()!=0) {
            aq.id(R.id.user_list).adapter(adapterUser);
            swipeRefreshLayout.setRefreshing(false);
        }else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                callFragmentWithBackStack(R.id.container,AddUserFragment.newInstance(-1), "AddUserFragment");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        UserListModel.getInstance().responseObject.clear();
        service.getUserListByProject(Integer.parseInt(baseClass.getSelectedProject()), false,
                new CallBack(UserFragment.this, "UserList"));
    }
}
