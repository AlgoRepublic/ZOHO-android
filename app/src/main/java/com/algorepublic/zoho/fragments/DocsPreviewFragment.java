package com.algorepublic.zoho.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDocsComments;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by android on 1/13/16.
 */
@SuppressLint("ValidFragment")
public class DocsPreviewFragment extends BaseFragment {

    static DocsPreviewFragment fragment;
    AQuery aq;
    static DocumentsList docObject;
    BaseClass baseClass;
    AdapterDocsComments adapter;
    public static ListView listView;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public DocsPreviewFragment() {

    }
    @SuppressWarnings("unused")
    public static DocsPreviewFragment newInstance(DocumentsList obj) {
        docObject = obj;
        if (fragment==null) {
            fragment = new DocsPreviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_documens_preview, container, false);
        listView = (ListView) view.findViewById(R.id.listView_comments);
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        adapter = new AdapterDocsComments(getActivity());
        listView.setAdapter(adapter);
        aq.id(R.id.user_image).image(Constants.UserImage_URL+baseClass.getProfileImage());
        aq.id(R.id.user_name).text(docObject.getFileName());
        aq.id(R.id.doc_title).text(docObject.getFileName());
        aq.id(R.id.doc_size).text(docObject.getFileSizeInByte());
        if(docObject.getFileTypeID()>=0 &&
                docObject.getFileTypeID()<=4 ){
            Glide.with(getActivity()).load(Constants.Image_URL +
                    docObject.getID()+"."+ BaseClass.getExtensionType(docObject.getFileDescription())).into(aq.id(R.id.doc_image).getImageView());
        }else {
            Glide.with(getActivity()).load(BaseClass.
                    getIcon(docObject.getFileTypeID())).into(aq.id(R.id.doc_image).getImageView());
        }
        aq.id(R.id.comment_user).getTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == getResources().getInteger(R.integer.add_comment)) {
                    PerformAction();
                    return true;
                }
                return false;
            }
        });
        aq.id(R.id.send).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAction();
            }
        });
        return view;
    }
    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
        if(aq.id(R.id.comment_user).getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(getView(), getString(R.string.enter_comment), Snackbar.LENGTH_SHORT).show();

            return;
        }
        aq.id(R.id.comment_user).text("");
        TaskComments taskComments = new TaskComments();
        taskComments.setComment(comment);
        taskComments.setUserName(baseClass.getFirstName());
        taskComments.setUserImagePath(baseClass.getProfileImage());
        taskComments.setUserImageID(baseClass.getProfileImageID());
        Log.e("Image",Constants.UserImage_URL+baseClass.getProfileImage());
        taskComments.setDateTime(GetDateTime());
        arrayList.add(taskComments);
        adapter.notifyDataSetChanged();
    }
}
