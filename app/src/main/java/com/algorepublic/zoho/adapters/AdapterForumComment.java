package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.ForumsCommentModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.ForumsDetailFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ForumService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.Constants;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

/**
 * Created by waqas on 2/3/16.
 */
public class AdapterForumComment extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    ForumService service;

    private LayoutInflater l_Inflater;

    public AdapterForumComment(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service =  new ForumService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ForumsDetailFragment.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return ForumsDetailFragment.arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = l_Inflater.inflate(R.layout.layout_comments_maker, null);
        aq = new AQuery(convertView);

        aq.id(R.id.comment_description).text(ForumsDetailFragment.arrayList.get(position).getComment());
        aq.id(R.id.comment_title).text(ForumsDetailFragment.arrayList.get(position).getUserName()
                + " , " + ForumsDetailFragment.arrayList.get(position).getDateTime());
        if(ForumsDetailFragment.arrayList
                .get(position).getUserImagePath() != null) {
            Glide.with(ctx).load(Constants.Image_URL + ForumsDetailFragment
                    .arrayList.get(position).getUserImageID()
                    + "." + BaseClass.getExtensionType(ForumsDetailFragment.arrayList
                    .get(position).getUserImagePath())).into(aq.id(R.id.comment_image).getImageView());
        }
//        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ForumsDetailFragment.flag = true;ForumsDetailFragment.ClickedPosition = position;
//                ForumsDetailFragment.comment_user.setText(ForumsDetailFragment
//                        .arrayList.get(position).getComment());
//            }
//        });
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForumsDetailFragment.ClickedPosition = position;
                service.deleteForumComment(ForumsDetailFragment
                        .arrayList.get(position).getCommentID(),false,
                        new CallBack(AdapterForumComment.this,"DeleteComment"));
                ForumsDetailFragment.dialogAC.show();
            }
        });

        return convertView;
    }
    public void DeleteComment(Object caller, Object model){
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject ==true) {
            ForumsDetailFragment.arrayList.remove(ForumsDetailFragment.ClickedPosition);
            notifyDataSetChanged();
        }else {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content),
                    ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
        ForumsDetailFragment.dialogAC.dismiss();
    }

}
