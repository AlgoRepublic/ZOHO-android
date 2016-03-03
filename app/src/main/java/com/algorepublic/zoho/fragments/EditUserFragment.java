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

import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.algorepublic.zoho.utils.GenericHttpClient;
import com.androidquery.AQuery;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by android on 3/3/16.
 */
public class EditUserFragment extends BaseFragment{

    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    File newFile;
    AQuery aq;
    ACProgressFlower dialog;
    BaseClass baseClass;
    static int Pos;

    public EditUserFragment() {
        // Required empty public constructor
    }


    public static EditUserFragment newInstance(int pos) {
        Pos = pos;
        EditUserFragment fragment = new EditUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_user, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        dialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        getToolbar().setTitle(getString(R.string.save));
        aq.id(R.id.profile).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });
        UpdateValues();
        return view;
    }
    public void UpdateValues(){
        aq.id(R.id.first_name).text(UserListModel.getInstance().responseObject.get(Pos).firstName);
        aq.id(R.id.last_name).text(UserListModel.getInstance().responseObject.get(Pos).lastName);
        aq.id(R.id.user_email).text(UserListModel.getInstance().responseObject.get(Pos).email);
        String imagePath =UserListModel
                .getInstance().responseObject.get(Pos).profileImagePath;
        if(imagePath != null) {
            aq.id(R.id.profile).image(Constants.UserImage_URL + UserListModel
                    .getInstance().responseObject.get(Pos).profileImagePath);
        }
        aq.id(R.id.user_phoneno).text(UserListModel.getInstance().responseObject.get(Pos).mobile);
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
                    Snackbar.make(getView(), getString(R.string.user_first_name), Snackbar.LENGTH_SHORT).show();
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
                new EditUser().execute();
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
    public class EditUser extends AsyncTask<Void, Void, String> {
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
                response = httpClient.updateUser(Constants.UpdateUser_API,
                        Integer.toString(UserListModel.getInstance().responseObject.get(Pos).ID),
                        aq.id(R.id.first_name).getText().toString(),
                        aq.id(R.id.last_name).getText().toString(),
                        aq.id(R.id.user_email).getText().toString(),
                        aq.id(R.id.user_phoneno).getText().toString(), newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            if(result.contains("100")){
                Snackbar.make(getView(),getString(R.string.user_updated),Snackbar.LENGTH_SHORT).show();
            }else{
                Snackbar.make(getView(),getString(R.string.response_error),Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
