package com.algorepublic.zoho.FragmentsTasks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.Models.SubTaskAttachmentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.flyco.animation.SlideEnter.SlideLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.MaterialDialog;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

public class TaskAttachmentFragment extends BaseFragment {

    static TaskAttachmentFragment fragment;
    AQuery aq;
    protected static final int RESULT_CODE = 123;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    int Tag = 0;
    static TasksList taskObj;
    DocumentsService service;
    public static int position;
    public TaskAttachmentFragment() {
    }
    @SuppressWarnings("unused")
    public static TaskAttachmentFragment newInstance(TasksList tasksList,int pos) {
        position = pos;
        taskObj = tasksList;
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
            service = new DocumentsService(getActivity());
        if(TaskAddUpdateFragment.filesList.size()==0){
            service.getDocsBySubTasks(taskObj.getTaskID(), true, new CallBack(TaskAttachmentFragment.this, "DocumentsList"));
        }
        return view;
    }
    public void DocumentsList(Object caller, Object model) {
        SubTaskAttachmentsModel.getInstance().setList((SubTaskAttachmentsModel) model);
        if (SubTaskAttachmentsModel.getInstance().responseCode == 100) {
            GetAllDocumentsList();
        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetAllDocumentsList() {
        TaskAddUpdateFragment.allDocsList.clear();
        for (int loop = 0; loop < SubTaskAttachmentsModel.getInstance().responseObject.size(); loop++) {
            DocumentsList documentsList = new DocumentsList();
            documentsList.setID(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).Id);
            documentsList.setFileName(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileDescription);
            documentsList.setFileDescription(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileName);
            documentsList.setFileSizeInByte(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileSizeInByte);
            documentsList.setUpdatedAt(DateFormatter(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).updatedAt));
            documentsList.setUpdatedMilli(DateMilli(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).updatedAt));
            documentsList.setFileTypeID(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileTypeID);
            documentsList.setIsFav(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).isFav);
            TaskAddUpdateFragment.allDocsList.add(documentsList);

            File file = null;
            showFileInList(file, Constants.Image_URL +
                    SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileName);
        }
        for (int loop=0;loop< TaskAddUpdateFragment.filesList.size();loop++)
        {
            showFileInList(TaskAddUpdateFragment.filesList.get(loop),"");
        }
    }
    private void CallForAttachments() {
        String[] menuItems = {"Camera","Gallery","Others"};
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
                    if (Build.VERSION.SDK_INT > 19) {
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
                    File newFile = new File(getRealPathFromURI(selectedImageUri));
                    checkFileLenght(newFile);
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                    File  newFile = new File(URI.create("file://"+getDataColumn(getActivity(), data.getData(),null,null)));
                    checkFileLenght(newFile);
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    File  newFile = null;
                    try {
                        newFile = new File(new URI("file://"+getDataColumn(getActivity(), contactData,null,null)));
                        checkFileLenght(newFile);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }
    private void checkFileLenght(File file){
        if(file.length() > 1048576 * 5) {
            MaterialAlertDialog();
        }else {
            TaskAddUpdateFragment.filesList.add(file);
            showFileInList(file,"");
        }
    }
    private void showFileInList(File file,String ApiUrl) {
        try {
                final LinearLayout linearLayout = (LinearLayout) aq
                        .id(R.id.images_layout).visible().getView();
                final View child = getActivity().getLayoutInflater().inflate(
                        R.layout.layout_task_attachment, null);
                ImageView addFile = (ImageView) child.findViewById(R.id.file_added);
                ImageView deleteFile = (ImageView) child.findViewById(R.id.file_delete);
                TextView text = (TextView) child.findViewById(R.id.file_title);
            if(file !=null) {
                Glide.with(this).load(file).into(addFile);
                text.setText(file.getName());
                child.setTag("image_" + Tag);
            }else{
                Glide.with(this).load(ApiUrl).into(addFile);
                String fullname = new File(
                        new URI(ApiUrl).getPath()).getName();
                text.setText(fullname);
                child.setTag("imageUrl_" + Tag);
            }
            deleteFile.setTag(Tag);
            child.setId(Tag);
            Tag++;
            linearLayout.addView(child);
            deleteFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((View) v.getParent()).setVisibility(View.GONE);
                }
            });
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Failed to load",
                        Toast.LENGTH_SHORT).show();
            }
        }
    public void MaterialAlertDialog(){
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
}
