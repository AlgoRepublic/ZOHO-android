package com.algorepublic.zoho.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

/**
 * Created by waqas on 2/20/16.
 */
public class DocsPreviewBySubTask extends BaseFragment {
    static DocsPreviewBySubTask fragment;
    AQuery aq;
    static DocumentsList docObject;
    BaseClass baseClass;
    @SuppressLint("ValidFragment")
    public DocsPreviewBySubTask() {

    }
    @SuppressWarnings("unused")
    public static DocsPreviewBySubTask newInstance(DocumentsList obj) {
        docObject = obj;
        if (fragment==null) {
            fragment = new DocsPreviewBySubTask();
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
        View view = inflater.inflate(R.layout.fragment_documents_preview_bytask, container, false);
        InitializeDialog(getActivity());
        aq = new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
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

        return view;
    }

}
