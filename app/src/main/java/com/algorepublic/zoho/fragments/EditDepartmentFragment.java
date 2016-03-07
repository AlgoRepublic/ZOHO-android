package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DepartmentService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 2/29/16.
 */
public class EditDepartmentFragment extends BaseFragment {

    AQuery aq;
    BaseClass baseClass;
    TaskListService service;
    View view;
    static int Pos;
    static EditDepartmentFragment fragment;

    public EditDepartmentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EditDepartmentFragment newInstance(int pos) {
        Pos = pos;
        if (fragment == null) {
            fragment = new EditDepartmentFragment();
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
        service.updateDepartment(baseClass.getUserId(), aq.id(R.id.dept_title).getText().toString(), baseClass.getUserId()
                , true, new CallBack(EditDepartmentFragment.this, "UpdateDept"));
    }

    public void UpdateDept(Object caller, Object model){
        CreateProjectModel.getInstance().setList((CreateProjectModel) model);
        if (CreateProjectModel.getInstance().responseObject ==null ) {
            Snackbar.make(getView(),getString(R.string.department_updated),Snackbar.LENGTH_SHORT).show();
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
        view  = inflater.inflate(R.layout.fragment_add_department, container, false);

        aq = new AQuery(view);
        aq.id(R.id.dept_title).text(DepartmentFragment.allProjects.get(Pos).getCompOrDeptName());
        aq.id(R.id.lblListHeader).text(getString(R.string.edit_dept));
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        return view;
    }

}
