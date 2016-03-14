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
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DepartmentService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 2/25/16.
 */
public class AddDepartmentFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    TaskListService service;
    View view;
    static AddDepartmentFragment fragment;

    public AddDepartmentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddDepartmentFragment newInstance() {
        if (fragment == null) {
            fragment = new AddDepartmentFragment();
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
                if(aq.id(R.id.dept_title).getText().toString().isEmpty()){
                    Snackbar.make(getView(), getString(R.string.department_name), Snackbar.LENGTH_SHORT).show();
                    return false;
                }

                CreateProject();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void CreateProject(){
        DepartmentService service = new DepartmentService(getActivity());
        service.createDepartment(aq.id(R.id.dept_title).getText().toString(), baseClass.getUserId()
                , false, new CallBack(AddDepartmentFragment.this, "CreateDept"));
        BaseActivity.dialogAC.show();
    }

    public void CreateDept(Object caller, Object model){
        CreateForumModel.getInstance().setList((CreateForumModel) model);
        if (CreateForumModel.getInstance().responseObject !=null ) {
            Snackbar.make(getView(),getString(R.string.department_created),Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
        BaseActivity.dialogAC.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view  = inflater.inflate(R.layout.fragment_add_department, container, false);
        InitializeDialog(getActivity());
        aq = new AQuery(view);
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        return view;
    }

}
