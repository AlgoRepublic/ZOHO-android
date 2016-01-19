package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDocsComments;
import com.algorepublic.zoho.adapters.TaskComments;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by android on 1/13/16.
 */
public class DocsPreviewFragment extends BaseFragment {

    static DocsPreviewFragment fragment;
    AQuery aq;
    static int position;
    BaseClass baseClass;
    AdapterDocsComments adapter;
    public static ListView listView;
    public static ArrayList<TaskComments> arrayList = new ArrayList<>();

    public DocsPreviewFragment() {
    }
    @SuppressWarnings("unused")
    public static DocsPreviewFragment newInstance(int pos) {
        position = pos;
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
        aq.id(R.id.user_name).text(DocumentsListFragment.docsList.get(position).getFileName());
        aq.id(R.id.doc_title).text(DocumentsListFragment.docsList.get(position).getFileName());
        aq.id(R.id.doc_size).text(DocumentsListFragment.docsList.get(position).getFileSizeInByte());
        if(DocumentsListFragment.docsList.get(position).getFileTypeID()>=0 &&
                DocumentsListFragment.docsList.get(position).getFileTypeID()<=4 ){
            Glide.with(getActivity()).load(Constants.Image_URL + DocumentsListFragment.docsList.
                    get(position).getFileDescription()).into(aq.id(R.id.doc_image).getImageView());
        }else {
            Glide.with(getActivity()).load(BaseClass.getIcon(DocumentsListFragment.docsList.
                    get(position).getFileTypeID())).into(aq.id(R.id.doc_image).getImageView());
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
            Toast.makeText(getActivity(), "Enter Your Comment!", Toast.LENGTH_SHORT).show();
            return;
        }
        aq.id(R.id.comment_user).text("");
        TaskComments taskComments = new TaskComments();
        taskComments.setComment(comment);
        taskComments.setUserName(baseClass.getFirstName());
        taskComments.setUserImage("http://www.planwallpaper.com/static/images/magic-of-blue-universe-images.jpg");
        taskComments.setDateTime(GetDateTime());
        arrayList.add(taskComments);
        adapter.notifyDataSetChanged();
    }
}
