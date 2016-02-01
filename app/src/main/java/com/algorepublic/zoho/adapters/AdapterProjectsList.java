package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.Models.ProjectsModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

/**
 * Created by android on 1/7/16.
 */
public class AdapterProjectsList extends BaseAdapter{

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    private LayoutInflater l_Inflater;
    private int selectedIndex = -1;
    private int lastPosition = -1;

    public AdapterProjectsList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ProjectsModel.getInstance().responseData.size();
    }

    @Override
    public ProjectsModel.Response getItem(int pos) {
        return ProjectsModel.getInstance().responseData.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_projectlist_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(getItem(position).name);
        aq.id(R.id.project_id).text(getItem(position).projectID);

        if(getItem(position).desc != null)
            aq.id(R.id.project_desc).text(Html.fromHtml(getItem(position).desc));

        if(baseClass.getSelectedProject().equals(getItem(position).projectID))
            selectedIndex = position;

//        if(selectedIndex == position)
//            aq.id(R.id.selected_check).getCheckBox().setChecked(true);
//        else
//            aq.id(R.id.selected_check).getCheckBox().setChecked(false);

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        lastPosition = -1;
    }

}
