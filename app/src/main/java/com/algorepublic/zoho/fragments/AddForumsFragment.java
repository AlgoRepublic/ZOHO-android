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
    NiceSpinner category_list;
    ForumService service;
    LinkedList<String> categoryList;

    public static AddForumsFragment newInstance() {
        AddForumsFragment fragment = new AddForumsFragment();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_forums, container, false);

        aq = new AQuery(getActivity(), view);
        aq.id(R.id.lblListHeader).text(getString(R.string.new_forum_post));
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        category_list = (NiceSpinner) view.findViewById(R.id.forum_list);
        service = new ForumService(getActivity());
        service.getCategoryList(baseClass.getUserId(), true, new CallBack(AddForumsFragment.this, "GetAllCategory"));

        return view;
    }

    public void GetAllCategory(Object caller, Object model) {
        AddforumModel.getInstance().setList((AddforumModel) model);
        if (AddforumModel.getInstance().responseCode == 100) {
            categoryList = new LinkedList<>();
            categoryList.add("None");
            for(int loop=0;loop<AddforumModel.getInstance().responseObject.size();loop++) {
                categoryList.add(AddforumModel.getInstance().responseObject.get(loop).Title);
            }
           category_list.attachDataSource(categoryList);
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();

        }
    }

    public void CreateForum(){
        int categoryID;
        Log.e("ProjectId",baseClass.getSelectedProject());
        if(categoryList.get(0).equalsIgnoreCase("None")){
            categoryID = 0;
        }else
        {
            categoryID = AddforumModel.getInstance().responseObject.
                    get(category_list.getSelectedIndex()).ID;
        }
        if (baseClass.getSelectedProject()!="0") {
            service.createForum(aq.id(R.id.forum_title).getText().toString(),
                    aq.id(R.id.forum_description).getText().toString(),
                    baseClass.getSelectedProject(),
                    true,
                    true
                    ,categoryID,baseClass.getUserId(),
                    true, new CallBack(AddForumsFragment.this, "CreateForum"));
        }else{
            Snackbar.make(getView(),getString(R.string.select_project),Snackbar.LENGTH_SHORT).show();
        }
    }

    public void CreateForum(Object caller, Object model){
        CreateForumModel.getInstance().setList((CreateForumModel) model);
        if (CreateForumModel.getInstance().responseObject != null ) {
            Snackbar.make(getView(),getString(R.string.forum_created),Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
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
                if(aq.id(R.id.forum_title).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.forum_addname),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.forum_description).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.forum_add_description),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                CreateForum();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
