package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.Models.ForumCategoryModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import org.angmarch.views.NiceSpinner;

import java.util.LinkedList;

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
        menu.clear();
        inflater.inflate(R.menu.menu_save_project, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_project:
                baseClass.hideKeyPad(getView());
                if(aq.id(R.id.comment_title).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.forum_addname), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if(aq.id(R.id.comment_description).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), getActivity().getString(R.string.forum_add_description), Toast.LENGTH_SHORT).show();
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
            categoryID = ForumCategoryModel.getInstance().responseObject.
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
            Toast.makeText(getActivity(), getActivity().getString(R.string.select_project), Toast.LENGTH_SHORT).show();

        }
    }

    public void UpdateForum(Object caller, Object model){
        CreateForumModel.getInstance().setList((CreateForumModel) model);
        if (CreateForumModel.getInstance().responseObject != null ) {
            Toast.makeText(getActivity(), getActivity().getString(R.string.forum_updated), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();
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

        category_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                baseClass.hideKeyPad(v);
                return false;
            }
        });
        service = new ForumService(getActivity());
        service.getCategoryList(baseClass.getSelectedProject(), true,
                new CallBack(EditForumFragment.this, "GetAllCategory"));
        return view;
    }
    public void GetAllCategory(Object caller, Object model) {
        ForumCategoryModel.getInstance().setList((ForumCategoryModel) model);
        if (ForumCategoryModel.getInstance().responseCode == 100) {
            categoryList= new LinkedList<>();
            categoryList.add("None");
            for(int loop=0;loop< ForumCategoryModel.getInstance().responseObject.size();loop++) {
                categoryList.add(ForumCategoryModel.getInstance().responseObject.get(loop).categoryName);
            }
            category_list.attachDataSource(categoryList);

            UpdateValues();
        }
        else
        {
            Toast.makeText(getActivity(),getActivity().getString(R.string.response_error),Toast.LENGTH_SHORT).show();

        }
    }
    public void UpdateValues(){

        aq.id(R.id.lblListHeader).text(getString(R.string.edit_forum_post));
        aq.id(R.id.comment_title).text(Html.fromHtml(ForumsModel.getInstance().responseObject.get(Pos).title));
        aq.id(R.id.comment_description).text(Html.fromHtml(ForumsModel.getInstance().responseObject.get(Pos).forumContent));
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
