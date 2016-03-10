package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
    private AdapterForumsList forumAdapter;
    ListView forums_list;


    public static ForumsFragment newInstance() {
        ForumsFragment fragment = new ForumsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                callFragmentWithBackStack(R.id.container, AddForumsFragment.newInstance(), "AddForumsFragment");
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forums, container, false);
        setHasOptionsMenu(true);
        aq = new AQuery(getActivity(), view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());

        ForumService service = new ForumService(getActivity());
        if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            Toast.makeText(getActivity(), "Please Select Project", Toast.LENGTH_SHORT).show();
        }else {
            service.getForumsList(baseClass.getSelectedProject(), true, new CallBack(ForumsFragment.this, "ForumListCallback"));
        }
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.forums));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        getToolbar().setTitle(getString(R.string.forums));
        super.onViewCreated(view, savedInstanceState);
    }



    public void ForumListCallback(Object caller, Object model){
        ForumsModel.getInstance().setList((ForumsModel) model);
        if (ForumsModel.getInstance().responseObject.size()!=0) {
            aq.id(R.id.forums_list).adapter(new AdapterForumsList(getActivity()));
        }else {
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }

    }
}
