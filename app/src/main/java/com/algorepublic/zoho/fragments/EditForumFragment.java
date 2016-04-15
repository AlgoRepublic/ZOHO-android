package com.algorepublic.zoho.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.algorepublic.zoho.Models.CreateForumModel;
import com.algorepublic.zoho.Models.ForumCategoryModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.DialogList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;


import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by waqas on 2/25/16.
 */
public class EditForumFragment extends BaseFragment  {


    AQuery aq;
    BaseClass baseClass;
    EditText category_list;
    ForumService service;
    CharSequence[] categoryList;
    int selected=-1;
    ArrayList<DialogList> category_List;
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

        if (!baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service.updateForum(Integer.toString(ForumsModel.getInstance().responseObject.get(Pos).ID),
                    aq.id(R.id.comment_title).getText().toString(),
                    aq.id(R.id.comment_description).getText().toString(),
                    baseClass.getSelectedProject(),
                    true,
                    true,
                    category_List.get(selected).getID(),baseClass.getUserId(),
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
        category_list = (EditText) view.findViewById(R.id.forum_list);

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
        category_list.setOnClickListener(new View.OnClickListener() {
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
        builder.setSingleChoiceItems(categoryList, selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selected = which;
                category_list.setText(categoryList[selected]);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    public void GetAllCategory(Object caller, Object model) {
        ForumCategoryModel.getInstance().setList((ForumCategoryModel) model);
        if (ForumCategoryModel.getInstance().responseCode == 100) {
            category_List= new ArrayList<>();
            DialogList dialogList = new DialogList();
            dialogList.setName("None");
            dialogList.setID(0);
            category_List.add(dialogList);
            for(int loop=0;loop< ForumCategoryModel.getInstance().responseObject.size();loop++) {
                dialogList = new DialogList();
                dialogList.setName(ForumCategoryModel.getInstance().responseObject.get(loop).categoryName);
                dialogList.setID(ForumCategoryModel.getInstance().responseObject.get(loop).CategoryId);
            }
            category_List.add(dialogList);
            categoryList = new CharSequence[category_List.size()];
            for(int loop=0;loop< category_List.size();loop++) {
                categoryList[loop] = category_List.get(loop).getName();
            }
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
            selected = 0;
        }else {
            String categoryName = ForumsModel.getInstance().responseObject.get(Pos).categoryName;
            if (!categoryName.equals(null)) {
                for (int loop = 0; loop < category_List.size(); loop++) {
                    if (category_List.get(loop).getName().equalsIgnoreCase(categoryName)) {
                        selected = loop;
                    }
                }
            }
        }
        category_list.setText(category_List.get(selected).getName());
    }
}
