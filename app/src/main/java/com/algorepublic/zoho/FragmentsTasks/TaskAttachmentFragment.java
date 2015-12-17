package com.algorepublic.zoho.FragmentsTasks;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;

public class TaskAttachmentFragment extends BaseFragment {

    static TaskAttachmentFragment fragment;
    AQuery aq;


    public TaskAttachmentFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskAttachmentFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskAttachmentFragment();
        }
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_attachment, container, false);
        aq= new AQuery(view);
        aq.id(R.id.add_file).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });
        return view;
    }
    public void CallForAttachments()
    {
        new AlertDialog.Builder(getActivity())
                .setMessage("Select...")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .create().show();
    }
}
