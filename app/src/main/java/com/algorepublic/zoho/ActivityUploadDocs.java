package com.algorepublic.zoho;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.dropbox.chooser.android.DbxChooser;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 1/7/16.
 */
public class ActivityUploadDocs extends BaseActivity {
    AQuery aq;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    static final int RESULT_GOOGLEDRIVE = 4;
    static final int DBX_CHOOSER_REQUEST = 5;
    int Tag = 0;
    private DbxChooser mChooser;
    BaseClass baseClass;
    public static ACProgressFlower dialog;
    public static ArrayList<File> filesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_docs);
        aq= new AQuery(this);
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
    }

    private void CallForAttachments() {
        String[] menuItems = {"Camera","Gallery","Others","Google Drive","Drop Box"};
        final ActionSheetDialog dialog = new ActionSheetDialog(this,menuItems,findViewById(android.R.id.content).getRootView());
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
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_GALLERY);
                }
                if (position == 2) {
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent mediaIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        mediaIntent.setType("*/file"); //set mime type as per requirement
                        startActivityForResult(mediaIntent, PICK_File);
                    } else {
                        Intent galleryIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, RESULT_GALLERY);
                    }
                }
                if (position == 3) {
                    mChooser = new DbxChooser("kl26qefbf8cmwm9");
                    mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT)
                            .launch(ActivityUploadDocs.this, DBX_CHOOSER_REQUEST);
                }
                if (position == 4) {
                    Intent dropboxIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    dropboxIntent.setType("*/*");
                    ActivityUploadDocs.this.startActivityForResult(dropboxIntent
                            .createChooser(dropboxIntent
                                    .setPackage("com.google.android.apps.docs"), "Google Drive"), RESULT_GOOGLEDRIVE);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("code", requestCode + "/" + resultCode + "/");
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    File destination = new File(getRealPathFromURI(selectedImageUri));
                    showFileInList(selectedImageUri,destination.getName(),destination);
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                    File  newFile = new File(URI.create("file://" + getDataColumn(this, data.getData(), null, null)));
                    showFileInList(data.getData(), newFile.getName(),newFile);
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    File  newFile = null;
                    try {
                        newFile = new File(new URI("file://"+getDataColumn(this, contactData,null,null)));
                        showFileInList(contactData, newFile.getName(),newFile);
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
                    Log.e("Path",path.toString());
                    Uri u = result.getLink();
                    String name = u.getLastPathSegment();
                    Log.e("Call",result.getLink().toString());
//                    fileData = getFileDataFromUri(path);
//                    if (fileData != null) {
//
//                        new UploadResume().execute();
//                    }
                }
                break;
            case RESULT_GOOGLEDRIVE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String filePath = null;
                        Uri _uri = data.getData();
                        if (_uri != null && "content".equals(_uri.getScheme())) {
                            Cursor cursor = getContentResolver().query(_uri, null, null, null, null);
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
                            cursor.moveToFirst();
                            filePath = cursor.getString(column_index);
                            cursor.close();
                            try {
                                InputStream inputStream = getContentResolver().openInputStream(_uri);
                                ArrayList<String> passing = new ArrayList<String>();
                                passing.add(filePath);
                                passing.add(readBytes(inputStream).toString());
                               // new UploadGoogle().execute(passing);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            filePath = _uri.getPath();
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    private FileData getFileDataFromUri(String path) {

        File file = null;
        try {
            file = new File(new URI(path));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        FileData thisFileData = new FileData();
        thisFileData.sizeInBytes = (int) file.length();
        thisFileData.fileName = file.getName();
        thisFileData.file = file;
        thisFileData.path = file.getPath();
        byte[] bytes = new byte[thisFileData.sizeInBytes];
        String byteString = new String();
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(file));
            buffer.read(bytes, 0, bytes.length);
            byteString.concat(bytes.toString());
            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            thisFileData.errorCode = 1;
            return thisFileData;
        } catch (IOException e) {
            e.printStackTrace();
            thisFileData.errorCode = 1;
            return thisFileData;
        }
        thisFileData.bytes = new String(bytes);
        Log.e("bytes", byteString);
        //return
        return thisFileData;
    }


    public class FileData {
        int errorCode = 0;
        int sizeInBytes = 0;
        File file = null;
        String fileName = "";
        String bytes = "";
        String path = "";
        public String getName() {
            String x = fileName.replaceAll("[-_.+^:,  ]", "");
            return x = x.substring(0, x.length() - 3) + "." + x.substring(x.length() - 3, x.length());
        }
    }
    public byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        Log.e("byte array", byteBuffer.toString());
        return byteBuffer.toByteArray();
    }
    private void showFileInList(Uri date,String fileName,File file) {
        try {
            filesList.add(file);
            final LinearLayout linearLayout = (LinearLayout) aq
                    .id(R.id.images_layout).visible().getView();
            final View child = getLayoutInflater().inflate(
                    R.layout.layout_task_attachment, null);
            ImageView addFile = (ImageView) child.findViewById(R.id.file_added);
            ImageView deleteFile = (ImageView) child.findViewById(R.id.file_delete);
            TextView text = (TextView) child.findViewById(R.id.file_title);
            Glide.with(this).load(date).into(addFile);
            text.setText(fileName);
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
                    ActivityTask.filesList.remove(Integer
                            .parseInt(v.getTag().toString()));
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
