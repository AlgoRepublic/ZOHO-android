package com.algorepublic.zoho.FragmentsTasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 12/17/15.
 */
public class TaskEditTitleFragment extends BaseFragment {

    static TaskEditTitleFragment fragment;
    AQuery aq;
    BaseClass baseClass;
    public TaskEditTitleFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskEditTitleFragment newInstance() {
        fragment = new TaskEditTitleFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_title_tasks, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq= new AQuery(view);
        aq.id(R.id.title_name).text(BaseClass.db.getString("TaskDesc"));
        aq.id(R.id.title_name).getEditText().requestFocus();
        aq.id(R.id.title_name).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                BaseClass.db.putString("TaskDesc", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        aq.id(R.id.title_name).getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    baseClass.showKeyPad(v);
                }else
                    baseClass.hideKeyPad(v);
            }
        });
        return view;
    }
}
