package com.algorepublic.zoho.FragmentsTasks;

import android.app.Activity;
import android.content.ContentResolver;
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
import android.provider.ContactsContract;
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
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.internal.BaseAlertDialog;

import java.io.File;

public class TaskAttachmentFragment extends BaseFragment {

    static TaskAttachmentFragment fragment;
    AQuery aq;
    protected static final int RESULT_CODE = 123;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    Uri imageUri;
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
        final String[] stringItems = {"Camera", "Gallery", "Others"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),stringItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos", "/" + position);
                dialog.dismiss();
                if (position == 0) {
                    Intent intent = new Intent(
                            "android.media.action.IMAGE_CAPTURE");
                    String path = Environment.getExternalStorageDirectory()
                            .toString();
                    File makeDirectory = new File(path + File.separator
                            + "ZOHO");
                    makeDirectory.mkdir();
                    File photo = new File(Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "ZOHO" + File.separator, "ZOHO_"
                            + System.currentTimeMillis() + ".JPG");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photo));
                     imageUri = Uri.fromFile(photo);
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
        Log.e("code",requestCode+"/"+resultCode+"/");
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == getActivity().RESULT_OK)
                    showFileInList(imageUri);
            case RESULT_GALLERY:
                if (null != data) {
                    showFileInList(data.getData());
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = getActivity().getContentResolver().query(
                            contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c
                                .getString(c
                                        .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    }
                    showFileInList(contactData);
                }
                break;
            default:
                break;
        }
    }

    private void showFileInList(Uri file) {

        getActivity().getContentResolver().notifyChange(file, null);
        ContentResolver cr = getActivity().getContentResolver();
        String type = MimeTypeMap.getFileExtensionFromUrl(file.toString());

            try {
                final LinearLayout item = (LinearLayout) aq
                        .id(R.id.images_layout).visible().getView();
                final View child = getActivity().getLayoutInflater().inflate(
                        R.layout.layout_task_attachment, null);
                ImageView image = (ImageView) child.findViewById(R.id.file_added);
                TextView text = (TextView) child.findViewById(R.id.file_title);
                image.setImageURI(file);
                text.setText(file.getLastPathSegment().toString());
//                ImageView imagemenu = (ImageView) child
//                        .findViewById(R.id.image_menu);
//                Tag = Tag + 1;
//                imagemenu.setTag(Tag);
//                child.setId(Tag);

//                imagemenu.setOnClickListener(new OnClickListener() {
//
//                    @Override
//                    public void onClick(View arg0) {
//                        // TODO Auto-generated method stub
//                        Toast.makeText(getActivity(),
//                                arg0.getId() + "     " + arg0.getTag(),
//                                Toast.LENGTH_LONG).show();
//                        ll_iner = (LinearLayout) item.findViewById(Integer
//                                .parseInt(arg0.getTag().toString()));
//
//                        if (popupWindowAttach.isShowing()) {
//                            popupWindowAttach.dismiss();
//
//                        } else {
//                            popupWindowAttach.showAsDropDown(arg0, 5, 0);
//                        }
//                    }
//                });
//
//                aq_menu.id(R.id.menu_item1).text("Save file")
//                        .clicked(new OnClickListener() {
//
//                            @Override
//                            public void onClick(View arg0) {
//                                // TODO Auto-generated method stub
//                                popupWindowAttach.dismiss();
//                            }
//                        });
//                aq_menu.id(R.id.menu_item2).text("Delete")
//                        .clicked(new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                // TODO Auto-generated method stub
//                                if (ll_iner != null)
//                                    item.removeView(ll_iner);
//                                popupWindowAttach.dismiss();
//                            }
//                        });
//
//                aq.id(image).image(Utils.getRoundedCornerBitmap(bitmap, 7));
//                TextView by = (TextView) child
//                        .findViewById(R.id.image_added_by);
//                TextView size = (TextView) child
//                        .findViewById(R.id.image_added_size);
//                Calendar cal = Calendar.getInstance();
//                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
//                by.setText("By Usman Ameer on " + sdf.format(cal.getTime()));
//                filename = selectedImage;
//                File myFile = new File(file.toString());
//
//                myFile.getAbsolutePath();
//                imageupload();
//                if (selectedImage.getLastPathSegment().contains(".")) {
//                    text.setText(selectedImage.getLastPathSegment());
//
//                } else {
//                    text.setText(selectedImage.getLastPathSegment() + "."
//                            + type);
//
//                }
//
//                size.setText("(" + (new File(selectedImage.getPath()).length())
//                        / 1024 + " KB)");
//                child.findViewById(R.id.image_cancel).setOnClickListener(
//                        new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                item.removeView(child);
//                            }
//                        });

                item.addView(child);
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Failed to load",
                        Toast.LENGTH_SHORT).show();
            }
        }
}
