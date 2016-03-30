package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.UploadDocsFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

/**
 * Created by android on 2/16/16.
 */
public class AdapterUploadAttachment extends BaseAdapter {

    Context ctx;
    private LayoutInflater inflater;

    public AdapterUploadAttachment(Context context) {
        this.ctx = context;
        inflater = LayoutInflater.from(context);
    }

    public Object getItem(int position) {
        return UploadDocsFragment.filesList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return UploadDocsFragment.filesList.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.layout_task_attachment, parent, false);
        AQuery aq = new AQuery(convertView);
        if (UploadDocsFragment.filesList.get(position).getFile() != null) {
            int type = BaseClass.getExtension(UploadDocsFragment.filesList.get(position).getFileName());
            if(type == 1 || type == 2 || type == 3 || type == 4)
            {
                Glide.with(ctx).load(UploadDocsFragment.filesList.get(position).getFile())
                        .into(aq.id(R.id.file_added).getImageView());
            }else{
                Glide.with(ctx).load(BaseClass.getIcon(
                        BaseClass.getExtension(UploadDocsFragment.filesList.get(position).getFileName())))
                        .into(aq.id(R.id.file_added).getImageView());

            }
        }
        aq.id(R.id.file_title).text(UploadDocsFragment.filesList.get(position).getFileName());
        aq.id(R.id.file_delete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDocsFragment.filesList.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

}
