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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.DocumentsListModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.file.FileResource;
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
import com.google.android.gms.common.api.PendingResult;
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
import com.google.android.gms.drive.events.CompletionEvent;
import com.google.android.gms.drive.events.DriveEventService;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import retrofit.http.Path;

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
    int Tag = 0;
    private DbxChooser mChooser;
    GoogleApiClient mGoogleApiClient;
    DriveFile selectedFile;
    BaseClass baseClass;
    DocumentsService service;
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
                    buildGoogleApiClient();
                }
                if (position == 4) {
                    mChooser = new DbxChooser("kl26qefbf8cmwm9");
                    mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT)
                            .launch(ActivityUploadDocs.this, DBX_CHOOSER_REQUEST);
                }
            }
        });
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
            case RESULT_GOOGLEDRIVE:
                if (resultCode == RESULT_OK) {

                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);//this extra contains the drive id of the selected file
                    Log.e("ID", driveId.getResourceId());

                    com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential _crd  =
                            GoogleAccountCredential.usingOAuth2(this, Arrays.asList(DriveScopes.DRIVE_FILE));
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    _crd.setSelectedAccountName(accountName);
                    HttpTransport transport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
                    com.google.api.services.drive.Drive mService = new com.google.api.services.drive.Drive.Builder(
                            transport, jsonFactory, _crd)
                            .setApplicationName("Drive API Android Quickstart")
                            .build();

                    outputStream = new ByteArrayOutputStream();
                    try {
                        mService.files().get(driveId.getResourceId())
                                .executeMediaAndDownloadTo(outputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mGoogleApiClient.connect();
                    selectedFile  = Drive.DriveApi.getFile(mGoogleApiClient, driveId);
                    ResultCallback<DriveApi.DriveContentsResult> contentsOpenedCallback =
                            new ResultCallback<DriveApi.DriveContentsResult>() {
                                @Override
                                public void onResult(DriveApi.DriveContentsResult result) {
                                    if (!result.getStatus().isSuccess()) {
                                        Log.e("Error:","No se puede abrir el archivo o no se encuentra");
                                        return;
                                    }
                                    DriveContents contents = result.getDriveContents();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(contents.getInputStream()));
                                    StringBuilder builder = new StringBuilder();
                                    String line;
                                    try {
                                        while ((line = reader.readLine()) != null) {
                                            builder.append(line);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String contentsAsString = builder.toString();
                                    Log.e("RESULT:", "/" + contentsAsString.getBytes().length);
                                    byte[] bytesArray = contentsAsString.getBytes();
                                    File newfile = new File(Environment.getExternalStorageDirectory(), "File.png");
                                    if(newfile.exists())
                                    {
                                        newfile.delete();
                                    }
                                    FileOutputStream fos;
                                    try {
                                        fos = new FileOutputStream(newfile);
                                        fos.write(bytesArray);
                                        fos.flush();
                                        fos.close();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    checkFileLenght(newfile);
//                                    File newfile = new File(Environment.getExternalStorageDirectory(), "File.png");
//                                    try {
//                                        Writer writer = new BufferedWriter(new FileWriter(newfile.getName()));
//                                        writer.write(contentsAsString.getBytes());
//
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                }
                            };
//                        selectedFile.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null)
//                                .setResultCallback(contentsOpenedCallback);
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
                    File newfile = new File(Environment.getExternalStorageDirectory(), metadata.getOriginalFilename());
                    try {
                        FileUtils.copyFile(newfile,outputStream);
                        checkFileLenght(newfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    ArrayList<String> passing = new ArrayList<String>();
//                    passing.add(metadata.getOriginalFilename());
//                    passing.add(metadata.getWebContentLink());
//                        new DownloadFile().execute(passing);

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
            HttpURLConnection c;
            ArrayList<String> passed = params[0];
           file = new File(Environment.getExternalStorageDirectory(),passed.get(0));
            if(file.exists()){
                file.delete();
            }
            URL url = null;
            try {
                url = new URL(passed.get(1));
                c = (HttpURLConnection)url.openConnection();
                c.connect();
            BufferedInputStream in = new BufferedInputStream(c.getInputStream());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buf = new byte[256];
            int n = 0;
            while ((n=in.read(buf))>=0) {
                out.write(buf, 0, n);
            }
            out.flush();
            out.close();
            } catch (IOException e) {
                e.printStackTrace();
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
