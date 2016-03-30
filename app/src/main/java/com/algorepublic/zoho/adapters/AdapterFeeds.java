package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ForumsModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.EditForumFragment;
import com.algorepublic.zoho.fragments.ForumsDetailFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

/**
 * Created by android on 3/29/16.
 */
public class AdapterFeeds extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    int Clicked=0;
    ForumService service;
    private LayoutInflater l_Inflater;

    public AdapterFeeds(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new ForumService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ForumsModel.getInstance().responseObject.size();
    }

    @Override
    public ForumsModel.ResponseObject getItem(int position) {
        return ForumsModel.getInstance().responseObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_feeds_row, null);
        aq = new AQuery(convertView);

        return convertView;
    }

    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}
