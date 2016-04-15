package com.algorepublic.zoho.fragments;

import org.json.JSONObject;


import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;

public class WebViewFragment extends BaseFragment {

    AQuery aq;
    static WebViewFragment fragment;
    static String weblink,downloadlink,fileName;
    public WebViewFragment() {
    }

    @SuppressWarnings("unused")
    public static WebViewFragment newInstance(String link,String name) {
        downloadlink = link;fileName= name;
        fragment = new WebViewFragment();
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        getToolbar().setTitle(fileName);
        super.onViewCreated(view, savedInstanceState);
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
                downloadFile(downloadlink,fileName);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void downloadFile(String url, String title) {
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
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE
                | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        aq = new AQuery(view);
        if(!(BaseClass.getExtension(downloadlink)==1 || BaseClass.getExtension(downloadlink)==2
                || BaseClass.getExtension(downloadlink)==3 || BaseClass.getExtension(downloadlink)==4)){
            weblink = Constants.DocViewer_URL+downloadlink;
        }else{
            weblink = downloadlink;
        }
        aq.id(R.id.progress_bar).visibility(View.VISIBLE);
        WebView wv_linker = (WebView) view.findViewById(R.id.weblinker);
        wv_linker.loadUrl(weblink);
        wv_linker.getSettings().setJavaScriptEnabled(true);
        wv_linker.getSettings().setDisplayZoomControls(true);
        wv_linker.getSettings().setBuiltInZoomControls(true);

        wv_linker.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Oh no! webpage not found.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                return super.shouldOverrideUrlLoading(view, url);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                aq.id(R.id.progress_bar).visibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub

        super.onDestroy();
    }

}
