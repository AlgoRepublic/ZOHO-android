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

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.AddforumModel;
import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;

import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * Created by waqas on 2/25/16.
 */
public class EditForumFragment extends BaseFragment  {


    AQuery aq;
    BaseClass baseClass;
    NiceSpinner category_list;
    ForumService service;
    LinkedList<String> categoryList;
    static int Pos;

    public static EditForumFragment newInstance(int pos) {
        Pos = pos;
        EditForumFragment fragment = new EditForumFragment();

        return fragment;
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
                if(aq.id(R.id.comment_title).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.forum_addname), Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.comment_description).getText().toString().isEmpty()){
                    Snackbar.make(getView(),getString(R.string.forum_add_description),Snackbar.LENGTH_SHORT).show();
                    return false;
                }
                UpdateForum();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void UpdateForum(){
        int categoryID;
        Log.e("ProjectId",baseClass.getSelectedProject());
        if(categoryList.get(0).equalsIgnoreCase("None")){
            categoryID = 0;
        }else
        {
            categoryID = AddforumModel.getInstance().responseObject.
                    get(category_list.getSelectedIndex()).ID;
        }
        if (!baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service.updateForum(Integer.toString(ForumsModel.getInstance().responseObject.get(Pos).ID),
                    aq.id(R.id.comment_title).getText().toString(),
                    aq.id(R.id.comment_description).getText().toString(),
                    baseClass.getSelectedProject(),
                    true,
                    true,
                    categoryID,baseClass.getUserId(),
                    true, new CallBack(EditForumFragment.this, "UpdateForum"));
        }else{
            Snackbar.make(getView(),getString(R.string.select_project),Snackbar.LENGTH_SHORT).show();
        }
    }

    public void UpdateForum(Object caller, Object model){
        CreateForumModel.getInstance().setList((CreateForumModel) model);
        if (CreateForumModel.getInstance().responseObject != null ) {
            Snackbar.make(getView(),getString(R.string.forum_updated),Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_forums, container, false);
        aq = new AQuery(getActivity(), view);
        aq.id(R.id.lblListHeader).text(getString(R.string.edit_forum_post));
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        category_list = (NiceSpinner) view.findViewById(R.id.forum_list);
        service = new ForumService(getActivity());
        service.getCategoryList(baseClass.getSelectedProject(), true,
                new CallBack(EditForumFragment.this, "GetAllCategory"));
        return view;
    }
    public void GetAllCategory(Object caller, Object model) {
        AddforumModel.getInstance().setList((AddforumModel) model);
        if (AddforumModel.getInstance().responseCode == 100) {
            categoryList= new LinkedList<>();
            categoryList.add("None");
            for(int loop=0;loop<AddforumModel.getInstance().responseObject.size();loop++) {
                categoryList.add(AddforumModel.getInstance().responseObject.get(loop).Title);
            }
            category_list.attachDataSource(categoryList);
            UpdateValues();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdateValues(){
        aq.id(R.id.lblListHeader).text(getString(R.string.edit_forum_post));
        aq.id(R.id.comment_title).text(ForumsModel.getInstance().responseObject.get(Pos).title);
        aq.id(R.id.comment_description).text(ForumsModel.getInstance().responseObject.get(Pos).forumContent);
        if(ForumsModel.getInstance().responseObject.get(Pos).categoryID==0){
            category_list.setSelectedIndex(0);
        }else {
            String categoryName = ForumsModel.getInstance().responseObject.get(Pos).categoryName;
            if (!categoryName.equals(null)) {
                for (int loop = 0; loop < categoryList.size(); loop++) {
                    if (categoryList.get(loop).equalsIgnoreCase(categoryName)) {
                        category_list.setSelectedIndex(loop);
                    }
                }
            }
        }
    }
}
