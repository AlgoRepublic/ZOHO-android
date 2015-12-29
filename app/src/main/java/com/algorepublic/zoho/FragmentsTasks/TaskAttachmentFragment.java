package com.algorepublic.zoho.FragmentsTasks;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.internal.BaseAlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TaskAttachmentFragment extends BaseFragment {

    static TaskAttachmentFragment fragment;
    AQuery aq;
    protected static final int RESULT_CODE = 123;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    String imageUri;
    public TaskAttachmentFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskAttachmentFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskAttachmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_task_attachment, container, false);
        aq= new AQuery(view);
        aq.id(R.id.add_file).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });
        return view;
    }
    private void CallForAttachments() {
        ArrayList<DialogMenuItem> menuItems = new ArrayList<>();
        DialogMenuItem dialogMenuItem1 = new DialogMenuItem("Camera",R.drawable.camera);
        DialogMenuItem dialogMenuItem2 = new DialogMenuItem("Gallery",R.drawable.ic_menu_gallery);
        DialogMenuItem dialogMenuItem3 = new DialogMenuItem("Others",R.drawable.ic_menu_gallery);
        menuItems.add(dialogMenuItem1);
        menuItems.add(dialogMenuItem2);
        menuItems.add(dialogMenuItem3);

        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems, getView());
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
            }
        });
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
                    showFileInList(selectedImageUri,destination.getName());
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                    File  newFile = new File(URI.create("file://"+getDataColumn(getActivity(), data.getData(),null,null)));
                     showFileInList(data.getData(), newFile.getName());
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    File  newFile = null;
                    try {
                        newFile = new File(new URI("file://"+getDataColumn(getActivity(), contactData,null,null)));
                        showFileInList(contactData, newFile.getName());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showFileInList(Uri file,String fileName) {
        try {
                final LinearLayout item = (LinearLayout) aq
                        .id(R.id.images_layout).visible().getView();
                final View child = getActivity().getLayoutInflater().inflate(
                        R.layout.layout_task_attachment, null);
                ImageView image = (ImageView) child.findViewById(R.id.file_added);
                TextView text = (TextView) child.findViewById(R.id.file_title);
                Glide.with(this).load(file).into(image);
                text.setText(fileName);
                item.addView(child);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Failed to load",
                        Toast.LENGTH_SHORT).show();
            }
        }
}
