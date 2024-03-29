package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.AllProjectsByUserModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.GetUserModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.LoginService;
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
 * Created by android on 3/10/16.
 */
public class EditProfileFragment extends BaseFragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener{

    private static final int TAKE_PICTURE = 1;
    public static final int RESULT_GALLERY = 2;
    public static final int PICK_File = 3;
    File newFile;int Color;
    AQuery aq;ACProgressFlower dialogAC;
    ArrayList<Integer> selectedIds = new ArrayList<>();
    ArrayList<String> projectList;
    ProjectsListService service ;
    UserService service1;
    LoginService loginService;
    MultiSelectionSpinner projectsList;
    BaseClass baseClass;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);
        dialogAC = InitializeDialog(getActivity());

        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
            Color = android.graphics.Color.parseColor("#4B7BAA");
        }else{
            Color = android.graphics.Color.parseColor("#414042");
        }
        aq = new AQuery(getActivity(), view);
        service = new ProjectsListService(getActivity());
        service1 = new UserService(getActivity());
        loginService = new LoginService(getActivity());

        getToolbar().setTitle(getString(R.string.profile));
        aq.id(R.id.profile).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallForAttachments();
            }
        });
        loginService.GetById(baseClass.getUserId(), true,
                new CallBack(EditProfileFragment.this, "GetById"));

        return view;
    }
    public void GetById(Object caller,Object model) {
        GetUserModel.getInstance().setList((GetUserModel) model);
        if (GetUserModel.getInstance().responseCode.equalsIgnoreCase("100")
                && GetUserModel.getInstance().user.toString() !="null") {
            UpdateValues();
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void GetByIdFreshData(Object caller,Object model) {
        GetUserModel.getInstance().setList((GetUserModel) model);
        if (GetUserModel.getInstance().responseCode.equalsIgnoreCase("100")
                && GetUserModel.getInstance().user.toString() !="null") {
            baseClass.setFirstName(GetUserModel.getInstance().user.firstName);
            baseClass.setLastName(GetUserModel.getInstance().user.lastName);
            baseClass.setProfileImage(GetUserModel.getInstance().user.profileImagePath);
            baseClass.setProfileImageID(GetUserModel.getInstance().user.profilePictureID);
            baseClass.setEmail(GetUserModel.getInstance().user.eMail);
            Toast.makeText(getActivity(), getActivity().getString(R.string.user_updated), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    public void UpdateValues(){
        aq.id(R.id.first_name).text(GetUserModel.getInstance().user.firstName);
        aq.id(R.id.last_name).text(GetUserModel.getInstance().user.lastName);
        aq.id(R.id.nick_name).text(GetUserModel.getInstance().user.nickName);
        aq.id(R.id.user_email).text(GetUserModel.getInstance().user.eMail);
        String imagePath =GetUserModel.getInstance().user.profileImagePath;
        if(imagePath != null) {
            Glide.with(getActivity()).load(Constants.Image_URL +
                    GetUserModel.getInstance().user.profilePictureID
                    + "." + BaseClass.getExtensionType(
                    GetUserModel.getInstance().user.profileImagePath))
                    .into(aq.id(R.id.profile).getImageView());
        }
        aq.id(R.id.user_phoneno).text(GetUserModel.getInstance().user.mobile);
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
                Log.e("firstName", aq.id(R.id.first_name).getText().toString());
                Log.e("last_name", aq.id(R.id.last_name).getText().toString());
                Log.e("nick_name", aq.id(R.id.nick_name).getText().toString());
                Log.e("user_email", aq.id(R.id.user_email).getText().toString());
                Log.e("user_phoneno", aq.id(R.id.user_phoneno).getText().toString());
                Log.e("userRole", String.valueOf(GetUserModel.getInstance().user.userRole.ID));
                Log.e("selectedIds",selectedIds.toString());
                service1.updateUserWithoutProjectSelected(baseClass.getUserId(),
                        aq.id(R.id.first_name).getText().toString(),
                        aq.id(R.id.last_name).getText().toString(),
                        aq.id(R.id.nick_name).getText().toString(),
                        aq.id(R.id.user_email).getText().toString(),
                        aq.id(R.id.user_phoneno).getText().toString(),
                        GetUserModel.getInstance().user.userRole.ID, selectedIds
                        , true, new CallBack(EditProfileFragment.this, "UpdateUser"));
        }
        return super.onOptionsItemSelected(item);
    }
    public void UpdateUser(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            loginService.GetById(baseClass.getUserId(), true,
                    new CallBack(EditProfileFragment.this, "GetByIdFreshData"));
        }else {
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    private void CallForAttachments() {
        String[] menuItems = {getString(R.string.camera),getString(R.string.gallery)
                ,getString(R.string.others)};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems,
                getString(R.string.cancel) ,getView());
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
                        GetUserModel.getInstance().user.Id,baseClass.getUserId(), newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            dialogAC.dismiss();
            if(result != null) {
                if (result.contains("100")) {
                    loginService.GetById(baseClass.getUserId(),
                            true, new CallBack(EditProfileFragment.this, "GetByIdFreshData"));
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
        // projectsList.setSelection(strings);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}