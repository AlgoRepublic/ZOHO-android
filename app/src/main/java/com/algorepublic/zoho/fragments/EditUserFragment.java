package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.algorepublic.zoho.Models.AllProjectsByUserModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.Models.UserRoleModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterTaskPriority;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.services.UserService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.guna.libmultispinner.MultiSelectionSpinner;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 3/3/16.
 */
public class EditUserFragment extends BaseFragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    File newFile;
    AQuery aq;int Color,selectedPosition=-1;
    ACProgressFlower dialogAC;
    ArrayList<Integer> selectedIds = new ArrayList<>();
    CharSequence[] roleList;
    ArrayList<String> projectList;
    EditText role_list;
    AdapterTaskPriority adapter;
    ProjectsListService service ;
    UserService service1;
    MultiSelectionSpinner projectsList;
    BaseClass baseClass;
    static int position;

    public EditUserFragment() {
        // Required empty public constructor
    }


    public static EditUserFragment newInstance(int pos) {
        position = pos;
        EditUserFragment fragment = new EditUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_user, container, false);
        dialogAC =  InitializeDialog(getActivity());
        role_list = (EditText) view.findViewById(R.id.role_list);
        projectsList = (MultiSelectionSpinner) view.findViewById(R.id.projects_list);
        projectsList.setListener(this);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
            Color = android.graphics.Color.parseColor("#4B7BAA");
        }else{
            Color = android.graphics.Color.parseColor("#414042");
        }
        aq = new AQuery(getActivity(), view);
        aq.id(R.id.lblListHeader).text(getString(R.string.edit_user));
        setHasOptionsMenu(true);
        service = new ProjectsListService(getActivity());
        service1 = new UserService(getActivity());

        getToolbar().setTitle(getString(R.string.edit_user));
        aq.id(R.id.profile).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });

        if(baseClass.getSelectedProject().equalsIgnoreCase("0")){
            aq.id(R.id.layout).visibility(View.VISIBLE);
            service.getAllProjectsByUser_API(baseClass.getUserId(), true,
                    new CallBack(EditUserFragment.this, "AllProjects"));
        }else {
            aq.id(R.id.layout).visibility(View.GONE);
        }
        service1.getUserRole(true, new CallBack(EditUserFragment.this, "UserRole"));
        UpdateValues();
        role_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListView();
            }
        });
        return view;
    }
    private void showListView() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_user));
        builder.setSingleChoiceItems(roleList, selectedPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPosition = which;
                role_list.setText(roleList[selectedPosition]);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void UpdateValues(){
        aq.id(R.id.first_name).text(UserListModel.getInstance().responseObject.get(position).firstName);
        aq.id(R.id.last_name).text(UserListModel.getInstance().responseObject.get(position).lastName);
        aq.id(R.id.nick_name).text(UserListModel.getInstance().responseObject.get(position).nickName);
        aq.id(R.id.user_email).text(UserListModel.getInstance().responseObject.get(position).email);
        String imagePath =UserListModel
                .getInstance().responseObject.get(position).profileImagePath;

        if(imagePath != null) {
            Glide.with(getActivity()).load(Constants.Image_URL + UserListModel.getInstance()
                    .responseObject.get(position).profilePictureID
                    + "." + BaseClass.getExtensionType(
                    UserListModel.getInstance().responseObject.get(position).profileImagePath))
                    .into(aq.id(R.id.profile).getImageView());
        }
        aq.id(R.id.user_phoneno).text(UserListModel.getInstance().responseObject.get(position).mobile);
    }
    public void AllProjects(Object caller, Object model){
        AllProjectsByUserModel.getInstance().setList((AllProjectsByUserModel) model);
        if (AllProjectsByUserModel.getInstance().responseCode == 100
                || AllProjectsByUserModel.getInstance().responseData.size() != 0) {
            projectList = new ArrayList<>();
            for(int loop=0;loop< AllProjectsByUserModel.getInstance().responseData.size();loop++) {
                try {
                    if(AllProjectsByUserModel.getInstance().responseData.get(loop).projectName ==null){
                        projectList.add("No Data");
                    }else
                        projectList.add(AllProjectsByUserModel.getInstance().responseData.get(loop).projectName);
                }catch (NullPointerException e){}
            }
            projectsList.setItems(projectList);
            if(position > -1) {
                if (UserListModel.getInstance().responseObject.get(position).projectIDs != null) {

                    int[] Ids = new int[UserListModel.getInstance().responseObject.get(position).projectIDs.size()];
                    for (int loop = 0; loop < UserListModel.getInstance()
                            .responseObject.get(position).projectIDs.size(); loop++) {
                        for (int loop1 = 0; loop1 < AllProjectsByUserModel.getInstance()
                                .responseData.size(); loop1++) {
                            if (UserListModel.getInstance()
                                    .responseObject.get(position).projectIDs.get(loop) == AllProjectsByUserModel
                                    .getInstance().responseData.get(loop1).projectID)
                                Ids[loop] = loop1;
                            selectedIds.add(AllProjectsByUserModel
                                    .getInstance().responseData.get(loop1).projectID);
                        }
                        projectsList.setSelection(Ids);
                    }
                }
            }
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

        }
        dialogAC.dismiss();
    }
    public void UserRole(Object caller, Object model){
        UserRoleModel.getInstance().setList((UserRoleModel) model);
        if (UserRoleModel.getInstance().responseObject.size() !=0 ) {
            ArrayList<String> list= new ArrayList<>();
            for(int loop=0;loop< UserRoleModel.getInstance().responseObject.size();loop++) {
                list.add(UserRoleModel.getInstance().responseObject.get(loop).role);
            }
            roleList = list.toArray(new CharSequence[list.size()]);
            for(int loop=0;loop<list.size();loop++){
                if(list.get(loop).equalsIgnoreCase(UserListModel.getInstance().responseObject
                        .get(position).userRole.role)){
                    selectedPosition = loop;
                }
            }
            role_list.setText(roleList[selectedPosition]);
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:
                baseClass.hideKeyPad(getView());
                if(aq.id(R.id.first_name).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.user_first_name), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.last_name).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.user_last_name), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.user_email).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.add_email), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.user_phoneno).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.add_phoneno), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.role_list).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.add_userrole), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                    service1.updateUserWithoutProjectSelected(Integer.toString(UserListModel.getInstance().responseObject.get(position).ID),
                            aq.id(R.id.first_name).getText().toString(),
                            aq.id(R.id.last_name).getText().toString(),
                            aq.id(R.id.nick_name).getText().toString(),
                            aq.id(R.id.user_email).getText().toString(),
                            aq.id(R.id.user_phoneno).getText().toString(),
                            UserRoleModel.getInstance().responseObject.get
                                    (selectedPosition).ID, selectedIds
                            , true, new CallBack(EditUserFragment.this, "UpdateUser"));
                }else {
                    service1.updateUserWithProjectSelected(Integer.toString(UserListModel.getInstance().responseObject.get(position).ID),
                            aq.id(R.id.first_name).getText().toString(),
                            aq.id(R.id.last_name).getText().toString(),
                            aq.id(R.id.nick_name).getText().toString(),
                            aq.id(R.id.user_email).getText().toString(),
                            aq.id(R.id.user_phoneno).getText().toString(),
                            UserRoleModel.getInstance().responseObject.get
                                    (selectedPosition).ID
                            , true, new CallBack(EditUserFragment.this, "UpdateUser"));
                }
        }
        return super.onOptionsItemSelected(item);
    }
    public void UpdateUser(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.user_updated), Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    private void CallForAttachments() {
        String[] menuItems = {getString(R.string.camera),getString(R.string.gallery)
                ,getString(R.string.others)};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems,
                getString(R.string.cancel),getView());
        dialog.titleTextColor(Color);
        dialog.itemTextColor(Color);
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
        Log.e("code", requestCode + "/" + resultCode );
        if (resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getActivity(), "Cancelled",
                    Toast.LENGTH_SHORT).show();
        }
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == getActivity().RESULT_OK) {
                    // NO need tp add here
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

        if(newFile== null){
            aq.id(R.id.profile).image(R.drawable.nav_user_black);
        }else {
            Bitmap bitmap =baseClass .adjustImageOrientation(newFile);
            aq.id(R.id.profile).image(bitmap);
            new UploadPicture().execute();
        }
    }
    public class UploadPicture extends AsyncTask<Void, Void, String> {
        GenericHttpClient httpClient;
        String response = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialogAC.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                httpClient = new GenericHttpClient();
                response = httpClient.uploadImage(Constants.UploadImage_URL,
                        Integer.toString(UserListModel.getInstance().responseObject
                                .get(position).ID),baseClass.getUserId(), newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            if ((dialogAC != null) && dialogAC.isShowing()) {
                dialogAC.dismiss();
            }
            if(result != null) {
                if (result.contains("100")) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.user_updated), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
    @Override
    public void selectedIndices(List<Integer> indices) {
        selectedIds.clear();
        for(int loop=0;loop<indices.size();loop++) {
            selectedIds.add(AllProjectsByUserModel
                    .getInstance().responseData.get(loop).projectID);
        }
    }

    @Override
    public void selectedStrings(List<String> strings) {
        projectsList.setSelection(strings);
    }

    @Override
    public void onDestroyView() {
        UserFragment.assigneeList.clear();
        super.onDestroyView();
    }
}