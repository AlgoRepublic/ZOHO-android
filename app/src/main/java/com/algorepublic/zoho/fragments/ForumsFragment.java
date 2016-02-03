package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterForumsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by waqas on 2/1/16.
 */
public class ForumsFragment extends BaseFragment implements AdapterView.OnItemClickListener{

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_forums, container, false);
        aq = new AQuery(getActivity(), view);
        forums_list=(ListView)view.findViewById(R.id.forums_list);
        forumAdapter=new AdapterForumsList(getActivity());
        forums_list.setOnItemClickListener(this);
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.forums));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        ForumListService serviceForum = new ForumListService(getActivity());
        serviceForum.getForumsList(4, true, new CallBack(ForumsFragment.this, "ForumListCallback"));

    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forums, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_forum:
                // TODO add new forum activity here
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void ForumListCallback(Object caller, Object model){
        ForumsModel.getInstance().setList((ForumsModel) model);
        if (ForumsModel.getInstance().responseObject.size()!=0) {
            forums_list.setAdapter(forumAdapter);
        }else {
            Toast.makeText(getActivity(), getString(R.string.forums_list_empty), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        callFragmentWithReplace(R.id.container, ForumsDetailFragment.newInstance(position),"FroumsDetailFragment");
    }
}
