package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterForumsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/1/16.
 */
public class ForumsFragment extends BaseFragment{

    private AQuery aq;
    private BaseClass baseClass;


    public static ForumsFragment newInstance() {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_forum, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                baseClass.hideKeyPad(getView());
                if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                    Toast.makeText(getActivity(),"Please Select Project", Toast.LENGTH_SHORT).show();
                    return false;
                }
                callFragmentWithBackStack(R.id.container, AddForumsFragment.newInstance(), getString(R.string.forums));
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        aq = new AQuery(getActivity(), view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ForumService service = new ForumService(getActivity());

        if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            Toast.makeText(getActivity(), getString(R.string.select_project), Toast.LENGTH_SHORT).show();
        }else {
            service.getForumsList(baseClass.getSelectedProject(), true,
                    new CallBack(ForumsFragment.this, "ForumListCallback"));
        }
        // check if has permissions to add New Forum
        if(baseClass.hasPermission(getResources().getString(R.string.forums_add)))
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.forums));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getToolbar().setTitle(getString(R.string.forums));
        super.onViewCreated(view, savedInstanceState);
    }



    public void ForumListCallback(Object caller, Object model){
        ForumsModel.getInstance().setList((ForumsModel) model);
        aq.id(R.id.alertMessage).text(getString(R.string.no_forums));
        if (ForumsModel.getInstance().responseObject.size()!=0) {
            aq.id(R.id.response_alert).visibility(View.GONE);
            aq.id(R.id.forums_list).adapter(new AdapterForumsList(getActivity()));
        }else {
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

        }
    }
}
