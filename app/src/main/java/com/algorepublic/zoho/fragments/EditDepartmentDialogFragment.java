package com.algorepublic.zoho.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateProjectModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DepartmentService;
import com.algorepublic.zoho.services.TaskListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by sidhu on 4/12/2016.
 */
public class EditDepartmentDialogFragment extends DialogFragment implements View.OnClickListener {
    BaseClass baseClass;
    private String deptName;
    private int deptID;
    private EditText editText;
    private Button btnSave,btnCancel;
    private TextView lblListHeader;

    public EditDepartmentDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        View rootView = inflater.inflate(R.layout.fragment_add_department, container, false);
        // Inflate the layout to use as dialog or embedded fragment
        editText = (EditText) rootView.findViewById(R.id.dept_title);
        btnSave = (Button) rootView.findViewById(R.id.btnSave);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        lblListHeader = (TextView) rootView.findViewById(R.id.lblListHeader);
        deptName = getArguments().getString("deptName");
        editText.setText(deptName);

        lblListHeader.setText(getString(R.string.edit_dept));
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return rootView;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        return dialog;
    }
    public void CreateProject(){
        deptID = getArguments().getInt("deptID");
        baseClass.hideKeyPad(getView());
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getActivity().getString(R.string.department_name), Toast.LENGTH_SHORT).show();
            return ;
        }
        DepartmentService service = new DepartmentService(getActivity());
        service.updateDepartment(deptID+"", editText.getText().toString(), baseClass.getUserId()
                , true, new CallBack(EditDepartmentDialogFragment.this, "UpdateDept"));
    }

    public void UpdateDept(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true ) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.department_updated), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(),getActivity().getString(R.string.response_error),Toast.LENGTH_SHORT).show();
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                CreateProject();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
                break;
        }
    }
}
