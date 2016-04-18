package com.algorepublic.zoho.fragments;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public void downloadFile(String url, String title, String extention) {
        Log.e("Url", url);
        Uri Download_Uri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) getActivity()
                .getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle(title);
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File");
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + "." + extention);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE
                | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
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
        aq.id(R.id.doc_image).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = Constants.Image_URL + docObject.getID()
                        + "." + BaseClass.getExtensionType(
                        docObject.getFileDescription());
                Log.e("Doc", link);
                callFragmentWithBackStack(R.id.container,
                        WebViewFragment.newInstance(link, docObject.getFileName()), "WebViewFragment");
            }
        });
        if(docObject.getFileTypeID()>=0 &&
                docObject.getFileTypeID()<=4 ){
            Glide.with(getActivity()).load(Constants.Image_URL +
                    docObject.getID()+"."+ BaseClass.getExtensionType(docObject.getFileDescription())).into(aq.id(R.id.doc_image).getImageView());
        }else {
            Glide.with(getActivity()).load(BaseClass.
                    getIcon(docObject.getFileTypeID())).into(aq.id(R.id.doc_image).getImageView());
        }
        setHasOptionsMenu(true);
        return view;
    }

}
