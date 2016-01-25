package com.algorepublic.zoho;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
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
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
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

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 1/7/16.
 */
public class ActivityUploadDocs extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, ResultCallback
        , GoogleApiClient.OnConnectionFailedListener {

    AQuery aq;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    static final int RESULT_GOOGLEDRIVE = 4;
    static final int DBX_CHOOSER_REQUEST = 5;
    static final int REQUEST_ACCOUNT_PICKER = 6;
    int Tag = 0;
    private DbxChooser mChooser;
    GoogleApiClient mGoogleApiClient;
    com.google.api.services.drive.Drive mService = null;
    DriveId driveId;
    DriveFile selectedFile;
    BaseClass baseClass;
    DocumentsService service;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { DriveScopes.DRIVE_METADATA_READONLY };
    com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential mCredential;
    public static ACProgressFlower dialog;
    public static ArrayList<File> filesList = new ArrayList<>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_docs);
        aq = new AQuery(this);
        service = new DocumentsService(this);
        dialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(PREF_ACCOUNT_NAME);

        aq.id(R.id.add_file).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });
        aq.id(R.id.back_arrow).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        aq.id(R.id.done).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTry().execute();
                //service.uploadDocuments(filesList.get(0),true,new CallBack(this,"Upload"));
//
            }
        });
        for (int loop = 0; loop < filesList.size(); loop++) {
            showFileInList(filesList.get(loop));
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void CallForAttachments() {
        String[] menuItems = {"Camera", "Gallery", "Others", "Google Drive", "Drop Box"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, menuItems, findViewById(android.R.id.content).getRootView());
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
                            .launch(ActivityUploadDocs.this, DBX_CHOOSER_REQUEST);
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
            Toast.makeText(this, getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    OutputStream outputStream;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("code", requestCode + "/" + resultCode + "/");
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    File destination = new File(getRealPathFromURI(selectedImageUri));
                    checkFileLenght(destination);
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                    File newFile = new File(URI.create("file://" + getDataColumn(this, data.getData(), null, null)));
                    checkFileLenght(newFile);
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    File newFile = null;
                    try {
                        newFile = new File(new URI("file://" + getDataColumn(this, contactData, null, null)));
                        checkFileLenght(newFile);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case DBX_CHOOSER_REQUEST:
                if (resultCode == RESULT_OK) {
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
                if (resultCode == RESULT_OK && data != null &&
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
                if (resultCode == RESULT_OK) {

                driveId = data.getParcelableExtra(
                        OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);//this extra contains the drive id of the selected file

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
           file = new File(Environment.getExternalStorageDirectory(),passed.get(0));
            Log.e("File",file.getPath()+"/"+file.getAbsolutePath());
//            if(file.exists()){
//                file.delete();
//            }

            outputStream = new ByteArrayOutputStream();
            try {
                mService.files().get(driveId.getResourceId())
                        .executeMediaAndDownloadTo(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if(outputStream !=null) {
                try {
                    FileUtils.copyFile(file, outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                checkFileLenght(file);
            }else{
                Log.e("no","no");
            }
        }
    }


    private void checkFileLenght(File file) {
        if (file.length() > 1048576 * 5) {
            MaterialAlertDialog();
        } else {
            filesList.add(file);
            showFileInList(file);
        }
    }

    private void showFileInList(File file) {
        try {
            final LinearLayout linearLayout = (LinearLayout) aq
                    .id(R.id.images_layout).visible().getView();
            final View child = getLayoutInflater().inflate(
                    R.layout.layout_task_attachment, null);
            ImageView addFile = (ImageView) child.findViewById(R.id.file_added);
            ImageView deleteFile = (ImageView) child.findViewById(R.id.file_delete);
            TextView text = (TextView) child.findViewById(R.id.file_title);
            if (BaseClass.getExtension(file.getName()) >= 0 &&
                    BaseClass.getExtension(file.getName()) <= 4) {
                Glide.with(this).load(file).into(addFile);
            } else {
                Glide.with(this).load(BaseClass.getIcon(BaseClass.getExtension(file.getName()))).into(addFile);
            }
            text.setText(file.getName());
            deleteFile.setTag(Tag);
            child.setId(Tag);
            Tag++;
            linearLayout.addView(child);
            deleteFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RelativeLayout layout = (RelativeLayout) linearLayout.findViewById(Integer
                            .parseInt(v.getTag().toString()));
                    linearLayout.removeView(layout);
                    filesList.remove(Integer
                            .parseInt(v.getTag().toString()));
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void MaterialAlertDialog() {
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog//
                .btnNum(1)
                .title("File size alert!")
                .titleTextColor(getResources().getColor(R.color.colorPrimaryDark))
                .content("File size should be less than (5) five MB")//
                .contentTextColor(getResources().getColor(R.color.colorPrimary))
                .btnText("OK")//
                .btnTextColor(getResources().getColor(R.color.colorPrimary))
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
            startIntentSenderForResult(
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
                connectionResult.startResolutionForResult(this, RESULT_GOOGLEDRIVE);
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    @Override
    public void onResult(Result result) {

    }


    public class AsyncTry extends AsyncTask<Void, Void, String> {
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
                response = httpClient.uploadDocuments(Constants.UploadDocuments_API, filesList, 4);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            PopulateModel(result);
        }
    }

    private void PopulateModel(String json) {
        Log.e("Json", "/" + json);
        JSONObject jsonObj;
//        try {
//            jsonObj = new JSONObject(json.toString());
//            Gson gson = new Gson();
//            RegisterUserModel obj ;
//            obj = gson.fromJson(jsonObj.toString(),
//                    RegisterUserModel.class);
//            RegisterUserModel.getInstance().setList(obj);
//            Log.e("status","/"+RegisterUserModel.getInstance().status+RegisterUserModel.getInstance().message);

//        }catch (Exception e){}
    }
}
