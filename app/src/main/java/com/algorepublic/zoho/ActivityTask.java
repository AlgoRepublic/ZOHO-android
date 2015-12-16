package com.algorepublic.zoho;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import com.androidquery.AQuery;

public class ActivityTask extends AppCompatActivity {
    AQuery aq;
    RadioGroup radioGroup1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        aq =new AQuery(this);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.e("Tag", "/" + radioGroup1.indexOfChild(findViewById(checkedId)));
                switch (radioGroup1.indexOfChild(findViewById(checkedId))) {
                    case 0:
                        aq.id(R.id.radioButton2).textColor(getResources().getColor(android.R.color.white));
                        aq.id(R.id.radioButton1).textColor(getResources().getColor(R.color.colortextMenu));
                        break;
                    case 1:
                        aq.id(R.id.radioButton2).textColor(getResources().getColor(R.color.colortextMenu));
                        aq.id(R.id.radioButton1).textColor(getResources().getColor(android.R.color.white));
                        break;
                }
            }
        });
    }
}
