package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.EditUserFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.UserService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;


/**
 * Created by waqas on 2/15/16.
 */
public class AdapterUser extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    UserService service;
    private LayoutInflater l_Inflater;

    public AdapterUser (Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new UserService((AppCompatActivity)ctx.getApplicationContext());
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return UserListModel.getInstance().responseObject.size();
    }

    @Override
    public UserListModel.ResponseObject getItem(int position) {
        return  UserListModel.getInstance().responseObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        convertView = l_Inflater.inflate(R.layout.layout_user_row, null);
        holder = new ViewHolder();

        holder.userImage = (CircularImageView) convertView.findViewById(R.id.user_image);
        holder.btEdit = (TextView) convertView.findViewById(R.id.btEdit);
        holder.btDelete = (TextView) convertView.findViewById(R.id.btDelete);

        aq = new AQuery(convertView);
        aq.id(R.id.user_title).text(getItem(position).firstName);
        aq.id(R.id.user_email).text(getItem(position).email);
        aq.id(R.id.user_role).text(ctx.getString(R.string.role)+getItem(position).userRole.role);
        if(getItem(position).profileImagePath !=null) {
            Glide.with(ctx).load(Constants.Image_URL + getItem(position).profilePictureID
                    +"."+BaseClass.getExtensionType(getItem(position).profileImagePath))
                    .into(holder.userImage);
        }
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callFragmentWithBackStack(R.id.container, EditUserFragment.newInstance(position), "EditUserFragment");
        }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            service.deleteUser(baseClass.getUserId(),baseClass.getSelectedProject()
            ,true,new CallBack(AdapterUser.this,"DeleteUser"));
            }
        });
        return convertView;
    }
    public void DeleteUser(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true ) {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content), ctx.getString(R.string.user_deleted), Snackbar.LENGTH_SHORT).show();
        }else {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content), ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }

    }
    static class ViewHolder {
        CircularImageView userImage;
        TextView btEdit;
        TextView btDelete;
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}
