package com.algorepublic.zoho;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.algorepublic.zoho.FragmentsTasks.TaskAssignFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskAttachmentFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskEditTitleFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskPriorityFragment;
import com.algorepublic.zoho.FragmentsTasks.TaskScheduleFragment;
import com.androidquery.AQuery;


public class ActivityTask extends BaseActivity{
    AQuery aq;
    RadioGroup radioGroup1,radioGroup2;
    int position;
    RadioGroup.OnCheckedChangeListener changeListener1,changeListener2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = Integer.parseInt(extras.get("pos").toString());
        }

        aq =new AQuery(this);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        aq.id(R.id.back_arrow).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityTask.this.finish();
            }
        });
       changeListener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioGroup1(checkedId);
                UpdateRadioGroup2();
            }
        };
       changeListener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                UpdateRadioGroup1();
                RadioGroup2(checkedId);
            }
        };
        radioGroup1.setOnCheckedChangeListener(changeListener1);
        radioGroup2.setOnCheckedChangeListener(changeListener2);
        aq.id(R.id.edit_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
            }
        });
        aq.id(R.id.schedule_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskAttachmentFragment.newInstance(), "TaskAttachment");
            }
        });
        aq.id(R.id.image_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskAttachmentFragment.newInstance(), "TaskAttachment");
            }
        });
        aq.id(R.id.schedule_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskScheduleFragment.newInstance(), "TaskSchedule");
            }
        });
        aq.id(R.id.employees_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskAssignFragment.newInstance(), "TaskAssign");
            }
        });
        aq.id(R.id.priority_radioButton).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithReplace(R.id.edittask_container, TaskPriorityFragment.newInstance(), "TasksPriority");
            }
        });
        if(savedInstanceState==null){
            callFragmentWithReplace(R.id.edittask_container, TaskEditTitleFragment.newInstance(position), "TaskTitle");
        }
    }
    public void RadioGroup1(int checkedId) {
        switch (radioGroup1.indexOfChild(findViewById(checkedId))) {
            case 0:
                aq.id(R.id.edit_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.category_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.image_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                aq.id(R.id.category_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.edit_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.image_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 2:
                aq.id(R.id.image_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.edit_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.category_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
    }

    public void RadioGroup2(int checkedId) {
        switch (radioGroup2.indexOfChild(findViewById(checkedId))) {
            case 0:
                aq.id(R.id.employees_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.priority_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 1:
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.employees_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.priority_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
            case 2:
                aq.id(R.id.priority_radioButton).textColor(getResources().getColor(R.color.colorAccent));
                aq.id(R.id.employees_radioButton).textColor(getResources().getColor(android.R.color.white));
                aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
                break;
        }
    }
    public void UpdateRadioGroup1()
    {
        radioGroup1.setOnCheckedChangeListener(null);
        radioGroup1.clearCheck();
        radioGroup1.setOnCheckedChangeListener(changeListener1);
        aq.id(R.id.edit_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.category_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.image_radioButton).textColor(getResources().getColor(android.R.color.white));
    }
    public void UpdateRadioGroup2()
    {
        radioGroup2.setOnCheckedChangeListener(null);
        radioGroup2.clearCheck();
        radioGroup2.setOnCheckedChangeListener(changeListener2);
        aq.id(R.id.employees_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.schedule_radioButton).textColor(getResources().getColor(android.R.color.white));
        aq.id(R.id.priority_radioButton).textColor(getResources().getColor(android.R.color.white));
    }
}
