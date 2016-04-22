package com.algorepublic.zoho.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.FeedsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterFeeds;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DashBoardService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 3/29/16.
 */
public class FeedsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    private AQuery aq;
    private BaseClass baseClass;
    private SwipeRefreshLayout swipeRefreshLayout;
    DashBoardService service;

    public static FeedsFragment newInstance() {
        FeedsFragment fragment = new FeedsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);
        aq = new AQuery(getActivity(), view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(true, 20,200);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        service   = new DashBoardService(getActivity());

        if(baseClass.hasPermission(getResources().getString(R.string.dashboard_feeds_view))){
            if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                service.getFeedsByUser(baseClass.getUserId(), true,
                        new CallBack(FeedsFragment.this, "Feeds"));
            }else {
                service.getFeedsByProject(baseClass.getSelectedProject(), true,
                        new CallBack(FeedsFragment.this, "Feeds"));
            }
        }else {
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            aq.id(R.id.alertMessage).text("You don't have permissions to view Feeds.");
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setEnabled(false);
        }

        setHasOptionsMenu(true);
        // getToolbar().setTitle(getString(R.string.feeds));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getToolbar().setTitle(getString(R.string.feeds));
        super.onViewCreated(view, savedInstanceState);
    }

    public void Feeds(Object caller, Object model){
        Log.e("Feedslist",String.valueOf(FeedsModel.getInstance().responseObject.size()));
        FeedsModel.getInstance().setList((FeedsModel) model);
        aq.id(R.id.alertMessage).text(getString(R.string.no_feeds));
        if (FeedsModel.getInstance().responseObject.size()!=0) {
            Log.e("Feedslist",String.valueOf(FeedsModel.getInstance().responseObject.size()));
            aq.id(R.id.response_alert).visibility(View.GONE);
            aq.id(R.id.feed_list).adapter(new AdapterFeeds(getActivity()));
            swipeRefreshLayout.setRefreshing(false);
        }else {
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            Toast.makeText(getActivity(), getActivity().getString(R.string.response_error), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onRefresh() {
        FeedsModel.getInstance().responseObject.clear();
        Log.e("Feedslist", String.valueOf(FeedsModel.getInstance().responseObject.size()));
        FeedsModel.getInstance().responseObject.clear();
        if (baseClass.getSelectedProject().equalsIgnoreCase("0")) {
            service.getFeedsByUser(baseClass.getUserId(), false,
                    new CallBack(FeedsFragment.this, "Feeds"));
        }else {
            service.getFeedsByProject(baseClass.getSelectedProject(), false,
                    new CallBack(FeedsFragment.this, "Feeds"));
        }
    }
}
