package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.algorepublic.zoho.Models.UserListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.AddUserFragment;
import com.algorepublic.zoho.fragments.EditUserFragment;
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
    private LayoutInflater l_Inflater;

    public AdapterUser (Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
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
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_user_row, null);
            holder = new ViewHolder();

            holder.userImage = (CircularImageView) convertView.findViewById(R.id.user_image);
            holder.btEdit = (TextView) convertView.findViewById(R.id.btEdit);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        aq.id(R.id.user_title).text(getItem(position).firstName);
        aq.id(R.id.user_email).text(getItem(position).email);
        if(getItem(position).profileImagePath !=null) {
            Glide.with(ctx).load(Constants.Image_URL +
                    getItem(position).profileImagePath).into(holder.userImage);
        }
            holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, EditUserFragment.newInstance(position), "EditUserFragment");
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView taskComment;
        TextView userName;
        CircularImageView userImage;
        TextView btEdit;
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}
