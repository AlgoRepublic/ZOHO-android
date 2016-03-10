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
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.DocumentsListModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDocsComments;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
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
    DocumentsService service;
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
        service = new DocumentsService(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        adapter = new AdapterDocsComments(getActivity());
        listView.setAdapter(adapter);
        aq.id(R.id.user_image).image(Constants.Image_URL + baseClass.getProfileImageID()
               + "." + BaseClass.getExtensionType(
                baseClass.getProfileImage()));
        aq.id(R.id.user_name).text(docObject.getFileName());
        aq.id(R.id.doc_title).text(docObject.getFileName());
        aq.id(R.id.doc_size).text(docObject.getFileSizeInByte());
        if(docObject.getFileTypeID()>=0 &&
                docObject.getFileTypeID()<=4 ){
            if(docObject.getFileDescription() != null) {
                Glide.with(getActivity()).load(Constants.Image_URL + docObject.getID()
                        + "." + BaseClass.getExtensionType(
                        docObject.getFileDescription()))
                        .into(aq.id(R.id.doc_image).getImageView());
            }
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
        service.getDocumentComments(docObject.getID(),true,new CallBack(DocsPreviewFragment.this,"AllDocComments"));
        return view;
    }
    public void AllDocComments(Object caller, Object model) {
        TaskCommentsModel.getInstance().setList((TaskCommentsModel) model);
        if (TaskCommentsModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            GetGeneralList();
        } else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetGeneralList() {
        arrayList.clear();
        for (int loop = 0; loop < TaskCommentsModel.getInstance().responseObject.size(); loop++) {
            TaskComments taskComments = new TaskComments();
            taskComments.setComment(TaskCommentsModel.getInstance().responseObject.get(loop).message);
            taskComments.setDateTime(GetDateTimeComment(TaskCommentsModel.getInstance().responseObject.get(loop).createdAt));
            taskComments.setUserName(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.firstName);
            taskComments.setUserImagePath(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.profileImagePath);
            taskComments.setUserImageID(TaskCommentsModel.getInstance().responseObject.get(loop).userObject.profilePictureID);
            arrayList.add(taskComments);
        }
        adapter.notifyDataSetChanged();
    }
    public void CreateComment(Object caller, Object model) {
        CreateCommentModel.getInstance().setList((CreateCommentModel) model);
        if (CreateCommentModel.getInstance().responseCode ==100){
            Snackbar.make(getView(),"Comment Added",Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
        if(aq.id(R.id.comment_user).getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(getView(), getString(R.string.enter_comment), Snackbar.LENGTH_SHORT).show();
            return;
        }
        service.createDocComments(Integer.toString(docObject.getID()),comment,baseClass.getUserId(),false,
                new CallBack(DocsPreviewFragment.this,"CreateComment"));
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
