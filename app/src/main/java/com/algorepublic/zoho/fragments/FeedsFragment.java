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
import android.widget.Toast;

import com.algorepublic.zoho.Models.FeedsModel;
import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterFeeds;
import com.algorepublic.zoho.adapters.AdapterForumsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DashBoardService;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 3/29/16.
 */
public class FeedsFragment extends BaseFragment{

    private AQuery aq;
    private BaseClass baseClass;


    public static FeedsFragment newInstance() {
        FeedsFragment fragment = new FeedsFragment();
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
                    Toast.makeText(getActivity(), "Please Select Project", Toast.LENGTH_SHORT).show();
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
        DashBoardService service = new DashBoardService(getActivity());
        if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service.getFeedsByUser(baseClass.getUserId(), true,
                    new CallBack(FeedsFragment.this, "Feeds"));
        }else {
            service.getFeedsByProject(baseClass.getSelectedProject(), true,
                    new CallBack(FeedsFragment.this, "Feeds"));
        }
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.feeds));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getToolbar().setTitle(getString(R.string.forums));
        super.onViewCreated(view, savedInstanceState);
    }

    public void Feeds(Object caller, Object model){
        FeedsModel.getInstance().setList((FeedsModel) model);
        aq.id(R.id.alertMessage).text("No Forums");
        if (FeedsModel.getInstance().responseObject.size()!=0) {
            aq.id(R.id.response_alert).visibility(View.GONE);
            aq.id(R.id.forums_list).adapter(new AdapterFeeds(getActivity()));
        }else {
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            Snackbar.make(getView(), getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
}
