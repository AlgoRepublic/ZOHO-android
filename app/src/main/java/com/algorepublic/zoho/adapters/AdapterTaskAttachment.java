package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.FragmentsTasks.TaskAttachmentFragment;
import com.algorepublic.zoho.MainActivity;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.CalendarFragment;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.fragments.ForumsFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.fragments.StarRatingFragment;
import com.algorepublic.zoho.fragments.TaskAddUpdateFragment;
import com.algorepublic.zoho.fragments.TasksListFragment;
import com.algorepublic.zoho.fragments.UserFragment;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by android on 2/16/16.
 */
public class AdapterTaskAttachment extends BaseAdapter {

    Context ctx;
    private LayoutInflater inflater;

    public AdapterTaskAttachment(Context context) {
        this.ctx = context;
        inflater = LayoutInflater.from(context);
    }

    public Object getItem(int position) {
        return TaskAddUpdateFragment.filesList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return TaskAddUpdateFragment.filesList.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.layout_task_attachment, parent, false);
        AQuery aq = new AQuery(convertView);
        if(TaskAddUpdateFragment.filesList.get(position).getFile() !=null) {
            Glide.with(ctx).load(TaskAddUpdateFragment.filesList.get(position).getFile())
            .into(aq.id(R.id.file_added).getImageView());
        }else{
            Glide.with(ctx).load(TaskAddUpdateFragment.filesList.get(position).getFileUrl())
                    .into(aq.id(R.id.file_added).getImageView());
        }
        aq.id(R.id.title_text).text(TaskAddUpdateFragment.filesList.get(position).getFileName());
        aq.id(R.id.file_delete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TaskAddUpdateFragment.filesList.get(position).getFile() == null) {
                    TaskAddUpdateFragment.filesToDelete.add(
                            TaskAddUpdateFragment.filesList.get(position).getFileID());
                }
                TaskAddUpdateFragment.filesList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
