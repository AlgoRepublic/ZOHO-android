package com.algorepublic.zoho;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.adapters.AdapterTaskAttachment;
import com.algorepublic.zoho.adapters.AdapterUploadAttachment;
import com.algorepublic.zoho.adapters.AttachmentList;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.androidquery.AQuery;
import com.dropbox.chooser.android.DbxChooser;
import com.flyco.animation.SlideEnter.SlideLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.poliveira.apps.parallaxlistview.ParallaxListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 1/7/16.
 */
public class UploadDocsFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, ResultCallback
        , GoogleApiClient.OnConnectionFailedListener {

    AQuery aq;
    static UploadDocsFragment fragment;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    static final int RESULT_GOOGLEDRIVE = 4;
    static final int DBX_CHOOSER_REQUEST = 5;
    static final int REQUEST_ACCOUNT_PICKER = 6;
    AdapterUploadAttachment adapter;
    public static ParallaxListView listView;
    private DbxChooser mChooser;
    GoogleApiClient mGoogleApiClient;
    com.google.api.services.drive.Drive mService = null;
    DriveId driveId;
    DriveFile selectedFile;
    BaseClass baseClass;
    InputStream inputStream;
    DocumentsService service;
    static int ID;View view;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { DriveScopes.DRIVE_METADATA_READONLY };
    com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential mCredential;
    public static ACProgressFlower dialog;
    public static ArrayList<AttachmentList> filesList = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public static UploadDocsFragment newInstance(int Id) {
        ID = Id;
        if (fragment==null) {
            fragment = new UploadDocsFragment();
        }
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:
                if(baseClass.getSelectedProject().equalsIgnoreCase("0")){
                    new UploadDocsBYTask().execute();
                }else {
                    new UploadDocsBYProject().execute();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        getToolbar().setTitle(getString(R.string.documents));
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.activity_upload_docs, container, false);
        listView = (ParallaxListView) view.findViewById(R.id.images_layout);
        listView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.view_header, listView, false));
        adapter = new AdapterUploadAttachment(getActivity());
        listView.setAdapter(adapter);
        aq = new AQuery(view);
        baseClass =  ((BaseClass) getActivity().getApplicationContext());
        service = new DocumentsService(getActivity());
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        mCredential = GoogleAccountCredential.usingOAuth2(
                getActivity().getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(PREF_ACCOUNT_NAME);

        aq.id(R.id.add_file).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });

        return view;
    }


    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void CallForAttachments() {
        String[] menuItems = {"Camera", "Gallery", "Others", "Google Drive", "Drop Box"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(), menuItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos", "/" + position);
                dialog.dismiss();
                if (position == 0) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    startActivityForResult(intent, TAKE_PICTURE);
                }
                if (position == 1) {
                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_GALLERY);
                }
                if (position == 2) {
                    if (Build.VERSION.SDK_INT > 19) {
                        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        mediaIntent.setType("*/file"); //set mime type as per requirement
                        startActivityForResult(mediaIntent, PICK_File);
                    } else {
                        Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_GALLERY);
                    }
                }
                if (position == 3) {
                    chooseAccount();
                }
                if (position == 4) {
                    mChooser = new DbxChooser("kl26qefbf8cmwm9");
                    mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT)
                            .launch(UploadDocsFragment.this, DBX_CHOOSER_REQUEST);
                }
            }
        });
    }
    private void chooseAccount() {
        startActivityForResult(
                mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }
    public void DocumentsList(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {

        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("code", requestCode + "/" + resultCode + "/");
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    File destination = new File(getRealPathFromURI(selectedImageUri));
                    checkFileLenght(destination);
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                    File newFile = new File(URI.create("file://" + getDataColumn(getActivity(), data.getData(), null, null)));
                    checkFileLenght(newFile);
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    File newFile = null;
                    try {
                        newFile = new File(new URI("file://" + getDataColumn(getActivity(), contactData, null, null)));
                        checkFileLenght(newFile);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case DBX_CHOOSER_REQUEST:
                if (resultCode == getActivity().RESULT_OK) {
                    DbxChooser.Result result = new DbxChooser.Result(data);
                    String path = result.getLink().toString();
                    path.replace("file://", "");
                    Log.e("Path", path.toString());
                    Uri u = result.getLink();
                    String name = u.getLastPathSegment();
                    Log.e("Call", result.getLink().toString());
                    File file = null;
                    try {
                        file = new File(new URI(path));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    checkFileLenght(file);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == getActivity().RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        mCredential.setSelectedAccountName(accountName);
                    }
                    buildGoogleApiClient();
                }
                break;
            case RESULT_GOOGLEDRIVE:
                if (resultCode == getActivity().RESULT_OK) {
                try {
                    driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);//this extra contains the drive id of the selected file
                }catch (NullPointerException e){
                    buildGoogleApiClient();
                }
                HttpTransport transport = AndroidHttp.newCompatibleTransport();
                JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                mService = new com.google.api.services.drive.Drive.Builder(
                        transport, jsonFactory, mCredential)
                        .setApplicationName("ZOHO")
                        .build();
                selectedFile  = Drive.DriveApi.getFile(mGoogleApiClient, driveId);
                selectedFile.getMetadata(mGoogleApiClient).setResultCallback(metadataRetrievedCallback);
                }
                break;
            default:
                break;
        }
    }

    ResultCallback<DriveResource.MetadataResult> metadataRetrievedCallback = new
            ResultCallback<DriveResource.MetadataResult>() {
                @Override
                public void onResult(DriveResource.MetadataResult result) {
                    if (!result.getStatus().isSuccess()) {
                        return;
                    }
                    Metadata metadata = result.getMetadata();
                    ArrayList<String> passed = new ArrayList<>();
                    passed.add(metadata.getOriginalFilename());
                    new DownloadFile().execute(passed);
                }
            };

    public class DownloadFile extends AsyncTask<ArrayList<String>, Void, String> {
        File file = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<String>... params) {
            ArrayList<String> passed = params[0];
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    +File.separator, "ZOHO");
            if (!root.exists()) {
                root.mkdirs();
            }
           file = new File(root ,passed.get(0));

            try {
                inputStream = mService.files().get(driveId.getResourceId())
                        .executeMediaAsInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(inputStream !=null) {
                try {
                    FileUtils.copyInputStreamToFile(inputStream, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            checkFileLenght(file);
        }
    }


    private void checkFileLenght(File file) {
        if (file.length() > 1048576 * 5) {
            MaterialAlertDialog();
        } else {
            showFileInList(file, "", -1, "", false);
        }
    }

    private void showFileInList(File file,String ApiUrl,Integer ID,String name,boolean IsDeleted) {
        if(IsDeleted== false) {
            AttachmentList attachmentList = new AttachmentList();
            attachmentList.setFileID(ID);
            attachmentList.setFileName(name);
            attachmentList.setFileUrl(ApiUrl);
            attachmentList.setFile(file);
            attachmentList.setIsDeleted(IsDeleted);
            filesList.add(attachmentList);
            adapter.notifyDataSetChanged();
        }
    }

    public void MaterialAlertDialog() {
        final MaterialDialog dialog = new MaterialDialog(getActivity());
        dialog//
                .btnNum(1)
                .title("File size alert!")
                .titleTextColor(getResources().getColor(R.color.colorBaseHeader))
                .content("File size should be less than (5) five MB")//
                .contentTextColor(getResources().getColor(R.color.colorContentWrapper))
                .btnText("OK")//
                .btnTextColor(getResources().getColor(R.color.colorContentWrapper))
                .showAnim(new SlideLeftEnter())//
                .dismissAnim(new SlideRightExit())//
                .show();

        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        IntentSender intentSender = Drive.DriveApi.newOpenFileActivityBuilder()
                .build(mGoogleApiClient);
        try {
            getActivity().startIntentSenderForResult(
                    intentSender, RESULT_GOOGLEDRIVE, null, 0, 0, 0);
        } catch (IntentSender.SendIntentException e) {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), RESULT_GOOGLEDRIVE);
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), getActivity(), 0).show();
        }
    }

    @Override
    public void onResult(Result result) {

    }


    public class UploadDocsBYProject extends AsyncTask<Void, Void, String> {
        GenericHttpClient httpClient;
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                httpClient = new GenericHttpClient();
                response = httpClient.uploadDocumentsByProject(Constants.UploadDocumentsByProject_API,ID, filesList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            onFinish();
            PopulateModel(result);
        }
    }
    public class UploadDocsBYTask extends AsyncTask<Void, Void, String> {
        GenericHttpClient httpClient;
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                httpClient = new GenericHttpClient();
                response = httpClient.uploadDocumentsByTask(Constants.UploadDocumentsByTasks_API,ID, filesList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            PopulateModel(result);
            onFinish();
        }
    }
    private void PopulateModel(String json) {
        Log.e("Json", "/" + json);
        filesList.clear();
        Snackbar.make(getView(),"File(s) uploaded successfully.", Snackbar.LENGTH_SHORT).show();
    }

    private void onFinish(){

        View mainView  = aq.id(R.id.upload_container).getView();
        Snackbar.make(mainView, "File (s) have been uploaded successfully.", Snackbar.LENGTH_LONG).show();
        ((ViewGroup) aq.id(R.id.images_layout).getView()).removeAllViews();
        filesList.clear();
    }
}
