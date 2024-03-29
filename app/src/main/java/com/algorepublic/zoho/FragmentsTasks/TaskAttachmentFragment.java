package com.algorepublic.zoho.FragmentsTasks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.adapters.AdapterTaskAttachment;
import com.algorepublic.zoho.adapters.AttachmentList;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.adapters.TasksList;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.Models.SubTaskAttachmentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.BaseFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.poliveira.apps.parallaxlistview.ParallaxListView;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class TaskAttachmentFragment extends BaseFragment {

    static TaskAttachmentFragment fragment;
    AQuery aq;
    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    AdapterTaskAttachment adapter;
    public static ParallaxListView listView;
    static TasksList taskObj;
    DocumentsService service;
    BaseClass baseClass;
    File newFile;
    public TaskAttachmentFragment() {

    }
    @SuppressWarnings("unused")
    public static TaskAttachmentFragment newInstance(TasksList tasksList) {
        taskObj = tasksList;
        if (fragment==null) {
            fragment = new TaskAttachmentFragment();
        }
        return fragment;
    }
    public static TaskAttachmentFragment newInstance() {
        if (fragment==null) {
            fragment = new TaskAttachmentFragment();
        }
        return fragment;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_attachment, container, false);
        listView = (ParallaxListView) view.findViewById(R.id.images_layout);
        listView.setParallaxView(getActivity().getLayoutInflater().inflate(R.layout.view_header, listView, false));
        aq= new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq.id(R.id.add_file).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });
        service = new DocumentsService(getActivity());
        if(TaskAddUpdateFragment.filesList.size()==0) {
            if (TaskAddUpdateFragment.callPosition == 2) {
                service.getDocsBySubTasks(taskObj.getTaskID(), true,
                        new CallBack(TaskAttachmentFragment.this, "DocumentsList"));
            }
        }
        adapter = new AdapterTaskAttachment(getActivity());
        listView.setAdapter(adapter);
        return view;
    }
    public void DocumentsList(Object caller, Object model) {
        SubTaskAttachmentsModel.getInstance().setList((SubTaskAttachmentsModel) model);
        if (SubTaskAttachmentsModel.getInstance().responseCode == 100) {
            GetAllDocumentsList();
        } else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetAllDocumentsList() {
        TaskAddUpdateFragment.apiDocsList.clear();
        for (int loop = 0; loop < SubTaskAttachmentsModel.getInstance().responseObject.size(); loop++) {
            DocumentsList documentsList = new DocumentsList();
            documentsList.setID(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).Id);
            documentsList.setFileName(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileDescription);
            documentsList.setFileDescription(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileName);
            documentsList.setFileSizeInByte(Integer.toString(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileSizeInByte/1000));
            documentsList.setUpdatedAt(DateFormatter(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).updatedAt));
            documentsList.setUpdatedMilli(DateMilli(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).updatedAt));
            documentsList.setFileTypeID(SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileTypeID);
            TaskAddUpdateFragment.apiDocsList.add(documentsList);

            String link = Constants.Image_URL + SubTaskAttachmentsModel.getInstance()
                    .responseObject.get(loop).Id+"."+ BaseClass.getExtensionType(
                    SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileName);
            String name  = SubTaskAttachmentsModel.getInstance().responseObject.get(loop).fileDescription;
            Integer ID = SubTaskAttachmentsModel.getInstance().responseObject.get(loop).Id;
            File file = null;
            showFileInList(file, link,ID,name);
        }
        aq.id(R.id.alertMessage).text("No Attachments");
        if(TaskAddUpdateFragment.apiDocsList.size() ==0){
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
        }else{
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
    }
    private void CallForAttachments() {
        String[] menuItems = {getString(R.string.camera),getString(R.string.gallery)
                ,getString(R.string.others)};
        final ActionSheetDialog dialog = new ActionSheetDialog(
                getActivity(),menuItems,getString(R.string.cancel), getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("pos", "/" + position);
                dialog.dismiss();
                if (position == 0) {
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    newFile = getOutputMediaFile();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
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
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    // No need to add here
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                    String thePath = getUriFromUrl("file://"+
                            getDataColumn(getActivity(), data.getData(),null,null)).toString();
                    newFile = new File(URI.create(thePath));
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    newFile = null;
                    String thePath = getUriFromUrl("file://"+
                            getDataColumn(getActivity(), contactData,null,null)).toString();
                    try {
                        newFile = new File(new URI(thePath));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        showFileInList(newFile,"",0,newFile.getName());
    }

    private void showFileInList(File file,String ApiUrl,Integer ID,String name) {
        AttachmentList attachmentList = new AttachmentList();
        attachmentList.setFileID(ID);
        attachmentList.setFileName(name);
        attachmentList.setFileUrl(ApiUrl);
        attachmentList.setFile(file);
        TaskAddUpdateFragment.filesList.add(attachmentList);
        aq.id(R.id.response_alert).visibility(View.GONE);
        adapter.notifyDataSetChanged();
    }
}
