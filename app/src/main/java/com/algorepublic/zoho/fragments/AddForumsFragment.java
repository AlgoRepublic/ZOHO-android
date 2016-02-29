package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.AddforumModel;
import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;

/**
 * Created by waqas on 2/24/16.
 */
public class AddForumsFragment extends BaseFragment{

    AQuery aq;
    BaseClass baseClass;
    NiceSpinner owner_list;
    ForumService service;
    LinkedList<String> userList;

    public static AddForumsFragment newInstance() {
        AddForumsFragment fragment = new AddForumsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.layout_addforums, container, false);

        aq = new AQuery(getActivity(), view);
        aq.id(R.id.lblListHeader).text(getString(R.string.new_forum_post));
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        owner_list = (NiceSpinner) view.findViewById(R.id.forum_list);
        service = new ForumService(getActivity());
        service.getCategoryList("4", true, new CallBack(AddForumsFragment.this, "GetAllUsers"));

        return view;
    }

    public void GetAllUsers(Object caller, Object model) {
        AddforumModel.getInstance().setList((AddforumModel) model);
        if (AddforumModel.getInstance().responseCode == 100) {
            userList= new LinkedList<>();
            for(int loop=0;loop<AddforumModel.getInstance().responseObject.size();loop++) {
                userList.add(AddforumModel.getInstance().responseObject.get(loop).Title);
                Log.e("S", "/" + AddforumModel.getInstance().responseObject.get(loop).Title);
            }
           owner_list.attachDataSource(userList);
        }
        else
        {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

    public void CreateForum(){

        Log.e("ProjectId",baseClass.getSelectedProject());
        if (baseClass.getSelectedProject()!="0") {
            service.createForum(aq.id(R.id.forum_title).getText().toString(),
                    aq.id(R.id.forum_description).getText().toString(),
                    baseClass.getSelectedProject(),
                    true,
                    true
                    , AddforumModel.getInstance().responseObject.
                            get(owner_list.getSelectedIndex()).ID,
                    baseClass.getUserId(),
                    true, new CallBack(AddForumsFragment.this, "CreateForum"));
        }else{
            Snackbar.make(getView(),"Please Select Project ",Snackbar.LENGTH_SHORT).show();
        }
    }

    public void CreateForum(Object caller, Object model){
        CreateForumModel.getInstance().setList((CreateForumModel) model);
        if (CreateForumModel.getInstance().responseObject != null ) {
            Snackbar.make(getView(),"Forum Created Successfully!",Snackbar.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), getString(R.string.forums_list_empty), Toast.LENGTH_SHORT).show();
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
                if(aq.id(R.id.forum_title).getText().toString().isEmpty()){
                    Snackbar.make(getView(),"Please Add Forum Name",Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.forum_description).getText().toString().isEmpty()){
                    Snackbar.make(getView(),"Please Add Forum Description",Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                CreateForum();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
