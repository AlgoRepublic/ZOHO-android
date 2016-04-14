package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by android on 1/19/16.
 */
public class AdapterTaskDetailAssignee extends RecyclerView.Adapter<AdapterTaskDetailAssignee.SimpleViewHolder> {

    Context ctx;
    BaseClass baseClass;
    AQuery aq;
    private LayoutInflater l_Inflater;
    ArrayList<TaskListAssignee> arraylist = new ArrayList<>();

    public AdapterTaskDetailAssignee(Context context, ArrayList<TaskListAssignee> results) {
        arraylist.addAll(results);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task_assignee, parent, false);
        aq=new AQuery(ctx);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        if(arraylist.get(position).getProfileImage() != null) {
            Glide.with(ctx).load(Constants.Image_URL + arraylist.get(position).getUserID()
                    + "." + BaseClass.getExtensionType(arraylist.get(position).getProfileImage())).into(holder.imageView);
        }
        for (int i=0;i<arraylist.size();i++){
            Log.e("name", String.valueOf(arraylist.get(position).getFirstName().charAt(0)));
            String fName = arraylist.get(position).getFirstName();
            String lName = arraylist.get(position).getLastName();
            holder.textView.setText(validateAndGetFullNameInitials(fName,lName).toUpperCase());
        }



        /*for (int i=0;i<arraylist.size();i++) {
            Log.e("name", String.valueOf(arraylist.get(position).getFirstName().charAt(0)));
            holder.textView.setText(String.valueOf(arraylist.get(position).getFirstName().charAt(1)));
        }*/
    }

    private String validateAndGetFullNameInitials(String fName,String lName){
        if(fName!=null && lName!=null)
            return String.valueOf(fName.charAt(0)).concat(""+lName.charAt(0));
        else if(fName!=null)
            return String.valueOf(fName.charAt(0));
        else if(lName !=null)
            return String.valueOf(lName.charAt(0));
        return "";
    }
     @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        SimpleViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.assignee_image);
            textView=(TextView)view.findViewById(R.id.user_name);
        }
    }
}
