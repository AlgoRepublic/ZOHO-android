package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.AddforumModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.services.AddForumService;
import com.algorepublic.zoho.services.CallBack;
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
    NiceSpinner owner_list;
    AddForumService service;
    LinkedList userList;

    public static EditForumFragment newInstance() {
        EditForumFragment fragment = new EditForumFragment();

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.layout_editforums, container, false);

        aq = new AQuery(getActivity(), view);
        setHasOptionsMenu(true);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        owner_list = (NiceSpinner) view.findViewById(R.id.forum_list);
        service = new AddForumService(getActivity());
        service.getCategoryList("4", true, new CallBack(EditForumFragment.this, "GetAllUsers"));

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




}
