package com.algorepublic.zoho.fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateCommentModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.TaskCommentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDocsComments;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.services.ForumService;
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
    ForumService forumService;
    static DocumentsList docObject;
    BaseClass baseClass;
    public static Button btSend;
    public static EditText comment_user;
    public static boolean flag= false;
    public static int ClickedPosition;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_board, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_file:
                downloadFile(Constants.Image_URL + docObject.getID()
                        + "." + BaseClass.getExtensionType(
                        docObject.getFileDescription()),docObject.getFileName(),BaseClass.getExtensionType(
                        docObject.getFileName()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documens_preview, container, false);
        listView = (ListView) view.findViewById(R.id.listView_comments);
        btSend = (Button) view.findViewById(R.id.send);
        comment_user = (EditText) view.findViewById(R.id.comment_user);
        aq = new AQuery(view);
        service = new DocumentsService(getActivity());
        forumService = new ForumService(getActivity());
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        adapter = new AdapterDocsComments(getActivity());
        listView.setAdapter(adapter);
        aq.id(R.id.user_image).image(Constants.Image_URL + baseClass.getProfileImageID()
                + "." + BaseClass.getExtensionType(
                baseClass.getProfileImage()));
        aq.id(R.id.user_name).text(docObject.getUserName());
        aq.id(R.id.doc_title).text(docObject.getFileName());
       // aq.id(R.id.doc_size).text(docObject.getFileSizeInByte()+"K");
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
                    if (flag == true) {
                        forumService.updateforumComments(DocsPreviewFragment
                                .arrayList.get(ClickedPosition).getCommentID(), DocsPreviewFragment
                                .comment_user.getText().toString(), true, new
                                CallBack(DocsPreviewFragment.this, "UpdateComment"));
                    } else {
                        PerformAction();
                    }
                    return true;
                }
                return false;
            }
        });
        aq.id(R.id.send).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == true) {
                    forumService.updateforumComments(DocsPreviewFragment
                            .arrayList.get(ClickedPosition).getCommentID(), DocsPreviewFragment
                            .comment_user.getText().toString(), true, new
                            CallBack(DocsPreviewFragment.this, "UpdateComment"));
                } else {
                    PerformAction();
                }
            }
        });
        service.getDocumentComments(docObject.getID(), true,
                new CallBack(DocsPreviewFragment.this, "AllDocComments"));
        return view;
    }
    public void downloadFile(String url, String title, String extention) {
        Log.e("Url",url);
        Uri Download_Uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) getActivity()
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File");
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + System.currentTimeMillis() + extention);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE
                | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
    public void UpdateComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            flag = false;
            DocsPreviewFragment
                    .arrayList.get(ClickedPosition).setComment(DocsPreviewFragment
                    .comment_user.getText().toString());
            DocsPreviewFragment
                    .arrayList.get(ClickedPosition).setDateTime(baseClass.GetDateTime());
            adapter.notifyDataSetChanged();
            DocsPreviewFragment
                    .comment_user.setText("");
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

        }
    }
    public void AllDocComments(Object caller, Object model) {
        TaskCommentsModel.getInstance().setList((TaskCommentsModel) model);
        if (TaskCommentsModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            GetGeneralList();
        } else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
        aq.id(R.id.alertMessage).text(getString(R.string.no_comments));
        if(arrayList.size() ==0){
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
        }else{
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
    }
    public void GetGeneralList() {
        arrayList.clear();
        for (int loop = 0; loop < TaskCommentsModel.getInstance().responseObject.size(); loop++) {
            TaskComments taskComments = new TaskComments();
            taskComments.setCommentID(TaskCommentsModel.getInstance().responseObject.get(loop).Id);
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
            Toast.makeText(getActivity(), getActivity().getString(R.string.comments_added), Toast.LENGTH_SHORT).show();
            TaskComments taskComments = new TaskComments();
            taskComments.setCommentID(CreateCommentModel.getInstance().responseObject.Id);
            taskComments.setComment(CreateCommentModel.getInstance().responseObject.message);
            taskComments.setDateTime(GetDateTimeComment(DateMilli(CreateCommentModel.getInstance().responseObject.updatedAt)));
            taskComments.setUserName(baseClass.getFirstName());
            taskComments.setUserImagePath(baseClass.getProfileImage());
            taskComments.setUserImageID(baseClass.getProfileImageID());
            arrayList.add(taskComments);
            adapter.notifyDataSetChanged();
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
        else
        {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void PerformAction()
    {
        String comment = aq.id(R.id.comment_user).getText().toString();
        if(aq.id(R.id.comment_user).getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(getActivity(),getActivity().getString(R.string.enter_comment),Toast.LENGTH_SHORT).show();
            return;
        }
        service.createDocComments(Integer.toString(docObject.getID()), comment, baseClass.getUserId(), false,
                new CallBack(DocsPreviewFragment.this, "CreateComment"));
        aq.id(R.id.comment_user).text("");
        baseClass.hideKeyPad(getView());
    }
}
