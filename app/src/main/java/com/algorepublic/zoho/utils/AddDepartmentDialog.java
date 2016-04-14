package com.algorepublic.zoho.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DepartmentService;

/**
 * Created by sidhu on 4/12/2016.
 */
public class AddDepartmentDialog extends DialogFragment implements View.OnClickListener{
    BaseClass baseClass;
    private EditText editText;
    private Button btnSave,btnCancel;

   public AddDepartmentDialog(){

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

    public void CreateDepartment(){
        baseClass.hideKeyPad(getView());
        if(editText.getText().toString().isEmpty()){
            Toast.makeText(getActivity(), getActivity().getString(R.string.department_name), Toast.LENGTH_SHORT).show();
            return;
        }
        DepartmentService service = new DepartmentService(getActivity());
        service.createDepartment(editText.getText().toString(), baseClass.getUserId()
                , true, new CallBack(AddDepartmentDialog.this, "CreateDept"));
    }

    public void CreateDept(Object caller, Object model){
        CreateForumModel.getInstance().setList((CreateForumModel) model);
        if (CreateForumModel.getInstance().responseObject !=null ) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.department_created), Toast.LENGTH_SHORT).show();
            editText.setText("");
            dismiss();
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                CreateDepartment();
                break;
            case R.id.btnCancel:
                getDialog().dismiss();
                break;
        }
    }
}
