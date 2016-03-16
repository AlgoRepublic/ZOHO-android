package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DepartmentService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.services.UserService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 3/11/16.
 */
public class RestPasswordFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    UserService service;
    View view;
    static RestPasswordFragment fragment;

    public RestPasswordFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RestPasswordFragment newInstance() {
        if (fragment == null) {
            fragment = new RestPasswordFragment();
        }
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:
                baseClass.hideKeyPad(getView());
                if(aq.id(R.id.old_password).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.alert_old_password),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.new_password).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.alert_new_password),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.confirm_password).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.alert_confirm_password),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(!aq.id(R.id.new_password).getText().toString()
                        .equalsIgnoreCase(aq.id(R.id.confirm_password).getText().toString())){
                    Snackbar.make(getView(),getString(R.string.alert_match_password),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                RestPasswod();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void RestPasswod(){
        service.restSetPassword(baseClass.getUserId(),
                aq.id(R.id.confirm_password).getText().toString()
                , true, new CallBack(RestPasswordFragment.this, "UpdatePassword"));
    }

    public void UpdatePassword(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true ) {
            Snackbar.make(getView(),getString(R.string.update_password),Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_rest_password, container, false);
        aq = new AQuery(view);
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        service =  new UserService(getActivity());
        return view;
    }

}
