package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.EditProjectFragment;
import com.algorepublic.zoho.fragments.ProjectsFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressFlower;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 2/2/16.
 */
public class AdapterProjectsDeptList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;

    private LayoutInflater l_Inflater;
    ProjectsListService service;
    private int lastPosition;
    ArrayList<ProjectsList> arrayList = new ArrayList<>();


    public AdapterProjectsDeptList(Context context,ArrayList<ProjectsList> lists) {
        l_Inflater = LayoutInflater.from(context);
        arrayList.addAll(lists);
        this.ctx = context;
        service = new ProjectsListService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        return arrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = l_Inflater.inflate(R.layout.layout_project_list_row, null);
            holder = new ViewHolder();
            holder.projectTitle = (TextView) convertView.findViewById(R.id.project_title);
            holder.taskAlert = (TextView) convertView.findViewById(R.id.task_alert);
            holder.userAlert = (TextView) convertView.findViewById(R.id.users_alert);
            holder.milestoneAlert = (TextView) convertView.findViewById(R.id.milestone_alert);
            holder.projectId = (TextView) convertView.findViewById(R.id.project_id);
            holder.parent = (RelativeLayout) convertView.findViewById(R.id.parent1);
            holder.editText = (TextView) convertView.findViewById(R.id.btEdit);
            holder.delText = (TextView) convertView.findViewById(R.id.btDelete);
            holder.projectDesc = (TextView) convertView.findViewById(R.id.project_desc);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        aq = new AQuery(convertView);
        holder.projectTitle.setText(arrayList.get(position).getProjectName());
        if(!arrayList.get(position).getTotalTasks().isEmpty())
            holder.taskAlert.setText(arrayList.get(position).getTotalTasks());
        if(!arrayList.get(position).getTotalMilestones().isEmpty())
            holder.milestoneAlert.setText(arrayList.get(position).getTotalMilestones());
        if(!arrayList.get(position).getTotalUsers().isEmpty())
            holder.userAlert.setText(arrayList.get(position).getTotalUsers());

        holder.projectId.setText(arrayList.get(position).getProjectID());
        if(arrayList.get(position).getProjectDesc() != null)
            holder.projectDesc.setText(Html.fromHtml(arrayList.get(position).getProjectDesc()));

        if(baseClass.getSelectedProject().equals(arrayList.get(position).getProjectID())){
            aq.id(R.id.selected_project).getView().setBackgroundColor(Color.parseColor("#99cc00"));
        }else{
            aq.id(R.id.selected_project).getView().setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = v;
                if (baseClass.getSelectedProject().equalsIgnoreCase(((TextView) view.findViewById(R.id.project_id)).getText().toString())) {
                    baseClass.setSelectedProject("0");
                    baseClass.db.putInt("ProjectID", 0);
                } else {
                    baseClass.setSelectedProject(((TextView)view.findViewById(R.id.project_id)).getText().toString());
                    baseClass.db.putInt("ProjectID", Integer.parseInt(baseClass.getSelectedProject()));
                    baseClass.db.putString("ProjectName", (((TextView)view.findViewById(R.id.project_title)).getText().toString()));
                }
                notifyDataSetChanged();
            }
        });
        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                    callFragmentWithBackStack(R.id.container, EditProjectFragment.
                            newInstance(arrayList, position), "EditProjectFragment");
                }
            }
        });

        holder.delText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogCustomAttr("Delete project?", position);
            }
        });
        return convertView;
    }
    public void DeleteProject(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            arrayList.remove(lastPosition);
            notifyDataSetChanged();
            Snackbar.make(aq.id(R.id.shadow_item_container).getView(), ctx.getString(R.string.project_deleted), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(aq.id(R.id.shadow_item_container).getView(), ctx.getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
           }
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_generic_header, parent , false);
        aq_header = new AQuery(convertView);
        aq_header.id(R.id.header).text(arrayList.get(position).getCompOrDeptName());
        Log.e("SS","S"+arrayList.get(position).getCompOrDeptName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Long.valueOf(arrayList.get(position).getCompOrDeptID());
    }
    private void NormalDialogCustomAttr(String content,final int position) {
        final NormalDialog dialog = new NormalDialog(ctx);
        dialog.isTitleShow(false)//
                .bgColor(ctx.getResources().getColor(R.color.colorBaseWrapper))//
                .cornerRadius(5)//
                .content(content)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(ctx.getResources().getColor(R.color.colorBaseHeader))//
                .dividerColor(ctx.getResources().getColor(R.color.colorContentWrapper))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(ctx.getResources().getColor(R.color.colorBaseHeader)
                        , ctx.getResources().getColor(R.color.colorBaseHeader))//
                .btnPressColor(ctx.getResources().getColor(R.color.colorBaseMenu))//
                .widthScale(0.85f)//
                .showAnim(new BounceLeftEnter())//
                .dismissAnim(new SlideRightExit())//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        lastPosition = position;
                        service.DeleteProject(arrayList.get(position).getProjectID(), true
                                , new CallBack(AdapterProjectsDeptList.this, "DeleteProject"));
                    }
                });
    }

    static class ViewHolder {
        private TextView projectTitle;
        private TextView taskAlert;
        private TextView userAlert;
        private TextView milestoneAlert;
        private TextView projectId;
        private TextView projectDesc;
        private TextView editText;
        private TextView delText;
        private RelativeLayout parent;
    }

}
