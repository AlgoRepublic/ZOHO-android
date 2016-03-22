package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.AllProjectsByUserModel;
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
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.guna.libmultispinner.MultiSelectionSpinner;

import org.angmarch.views.NiceSpinner;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by waqas on 2/15/16.
 */
public class AddUserFragment extends BaseFragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    File newFile;
    AQuery aq;
    int[] Ids;
    ACProgressFlower dialogAC;
    ArrayList<Integer> selectedIds = new ArrayList<>();
    ArrayList<String> roleList;
    ArrayList<String> projectList;
    ListView role_list;
    AdapterTaskPriority adapter;
    ProjectsListService service ;
    UserService service1;
    MultiSelectionSpinner projectsList;
    BaseClass baseClass;
    static int position;

    public AddUserFragment() {
        // Required empty public constructor
    }


    public static AddUserFragment newInstance(int pos) {
        position = pos;
        AddUserFragment fragment = new AddUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_user, container, false);
        dialogAC = InitializeDialog(getActivity());
        role_list = (ListView) view.findViewById(R.id.role_list);
        projectsList = (MultiSelectionSpinner) view.findViewById(R.id.projects_list);
        projectsList.setListener(this);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        service = new ProjectsListService(getActivity());
        service1 = new UserService(getActivity());

        aq.id(R.id.profile).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });

        if(baseClass.getSelectedProject().equalsIgnoreCase("0")){
            aq.id(R.id.layout).visibility(View.VISIBLE);
            service.getAllProjectsByUser_API(baseClass.getUserId(),
                    true, new CallBack(AddUserFragment.this, "AllProjects"));
        }else {
            aq.id(R.id.layout).visibility(View.GONE);
        }
        service1.getUserRole(true,new CallBack(AddUserFragment.this,"UserRole"));
        return view;
    }

    public void AllProjects(Object caller, Object model){
        AllProjectsByUserModel.getInstance().setList((AllProjectsByUserModel) model);
        if (AllProjectsByUserModel.getInstance().responseCode == 100
                || AllProjectsByUserModel.getInstance().responseData.size() != 0) {
            projectList = new ArrayList<>();
            for(int loop=0;loop< AllProjectsByUserModel.getInstance().responseData.size();loop++) {
                try {
                    if(AllProjectsByUserModel.getInstance().responseData.get(loop).projectName ==null){
                        projectList.add(getString(R.string.no_data));
                    }else
                    projectList.add(AllProjectsByUserModel.getInstance().responseData.get(loop).projectName);
                }catch (NullPointerException e){}
            }
            projectsList.setItems(projectList);
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UserRole(Object caller, Object model){
        UserRoleModel.getInstance().setList((UserRoleModel) model);
        if (UserRoleModel.getInstance().responseObject.size() !=0 ) {
            roleList= new ArrayList<>();
            for(int loop=0;loop< UserRoleModel.getInstance().responseObject.size();loop++) {
                roleList.add(UserRoleModel.getInstance().responseObject.get(loop).role);
            }
            adapter = new AdapterTaskPriority(getActivity(),role_list, roleList);
            role_list.setAdapter(adapter);
        }else {
            Snackbar.make(getView() , getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
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
                baseClass.hideKeyPad(getView());
                if(aq.id(R.id.first_name).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.user_first_name),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.last_name).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.user_last_name),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.user_email).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.add_email),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.user_phoneno).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.add_phoneno),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                new AddUser().execute();
        }
        return super.onOptionsItemSelected(item);
    }
    private void CallForAttachments() {
        String[] menuItems = {getString(R.string.camera),getString(R.string.gallery)
                ,getString(R.string.others)};
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
                    newFile = new File(getRealPathFromURI(selectedImageUri));
                }
                break;
            case RESULT_GALLERY:
                if (null != data) {
                     newFile = new File(URI.create("file://" + getDataColumn(getActivity(), data.getData(), null, null)));
                }
                break;
            case PICK_File:
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    try {
                        newFile = new File(new URI("file://"+getDataColumn(getActivity(), contactData,null,null)));
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        aq.id(R.id.profile).image(newFile,200);
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

    public class AddUser extends AsyncTask<Void, Void, String> {
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
                response = httpClient.createUser(Constants.CreateUser_API,
                        aq.id(R.id.first_name).getText().toString(),
                        aq.id(R.id.last_name).getText().toString(),
                        aq.id(R.id.nick_name).getText().toString(),
                        aq.id(R.id.user_email).getText().toString(),
                        aq.id(R.id.user_phoneno).getText().toString(),
                        UserRoleModel.getInstance().responseObject.get
                                (adapter.getSelectedIndex()).ID,selectedIds,newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialogAC.dismiss();
            Log.e("Res", result);
            if(result.contains("100")){
                Snackbar.make(getView(),getString(R.string.user_created),Snackbar.LENGTH_SHORT).show();
            }else if(result.contains("101")) {
                Snackbar.make(getView(),getString(R.string.email_already),Snackbar.LENGTH_SHORT).show();
            }else
                Snackbar.make(getView(),getString(R.string.response_error),Snackbar.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        UserFragment.assigneeList.clear();
        super.onDestroyView();
    }
}
