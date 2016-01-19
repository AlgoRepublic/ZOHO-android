package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.algorepublic.zoho.Models.TaskAttachmentsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by android on 1/19/16.
 */
public class AdapterTaskDetailAttachments extends RecyclerView.Adapter<AdapterTaskDetailAttachments.SimpleViewHolder> {

    Context ctx;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;

    public AdapterTaskDetailAttachments(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.file_image);
        }
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = l_Inflater.inflate(R.layout.layout_taskdetail_attachments, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        Glide.with(ctx).load(BaseClass.getIcon(TaskAttachmentsModel.getInstance().responseObject
                .get(position).fileTypeID))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return TaskAttachmentsModel.getInstance().responseObject.size();
    }

}
