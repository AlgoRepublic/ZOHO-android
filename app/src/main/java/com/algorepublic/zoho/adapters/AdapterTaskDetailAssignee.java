package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocsPreviewFragment;
import com.algorepublic.zoho.fragments.ForumsDetailFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import org.lucasr.twowayview.widget.TwoWayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 1/19/16.
 */
public class AdapterTaskDetailAssignee extends RecyclerView.Adapter<AdapterTaskDetailAssignee.SimpleViewHolder> {

    Context ctx;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    ArrayList<TaskListAssignee> arraylist = new ArrayList<TaskListAssignee>();

    public AdapterTaskDetailAssignee(Context context, ArrayList<TaskListAssignee> results) {
        arraylist.addAll(results);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.assignee_image);
        }
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = l_Inflater.inflate(R.layout.layout_task_assignee, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if(baseClass.getProfileImage() != null) {
            Glide.with(ctx).load(Constants.Image_URL + baseClass.getProfileImageID()
                    + "." + BaseClass.getExtensionType(baseClass.getProfileImage())).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

}
