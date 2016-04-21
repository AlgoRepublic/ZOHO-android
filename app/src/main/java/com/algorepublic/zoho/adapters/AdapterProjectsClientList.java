package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.EditProjectFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;

/**
 * Created by android on 1/7/16.
 */
public class AdapterProjectsClientList extends BaseAdapter {

    private Context ctx;
    private BaseClass baseClass;
    private AQuery aq;
    private LayoutInflater l_Inflater;
    ProjectsListService service;
    private int lastPosition;
    private int selectedPosition;
    ArrayList<ProjectsList> arrayList= new ArrayList<>();

    public AdapterProjectsClientList(Context context,ArrayList<ProjectsList> list) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new ProjectsListService((AppCompatActivity)ctx);
        arrayList.addAll(list);
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
        if(!arrayList.get(position).getTotalUsers().isEmpty())
            holder.userAlert.setText(arrayList.get(position).getTotalUsers());
        if(!arrayList.get(position).getTotalMilestones().isEmpty())
            holder.milestoneAlert.setText(arrayList.get(position).getTotalMilestones());

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
                    BaseClass.db.putInt("ProjectID", 0);
                } else {
                    baseClass.setSelectedProject(((TextView)view.findViewById(R.id.project_id)).getText().toString());
                    BaseClass.db.putInt("ProjectID", Integer.parseInt(baseClass.getSelectedProject()));
                    BaseClass.db.putString("ProjectName", (((TextView)view.findViewById(R.id.project_title)).getText().toString()));
                }
                notifyDataSetChanged();
            }
        });
        holder.editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, EditProjectFragment.
                        newInstance(arrayList, position), "EditProjectFragment");
            }
        });
        holder.delText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogCustomAttr(ctx.getString(R.string.deleted_project), position);
            }
        });

        if(baseClass.hasPermission(ctx.getResources().getString(R.string.projects_edit_delete))){
            ((com.daimajia.swipe.SwipeLayout)aq.id(R.id.swipe).getView()).setSwipeEnabled(false);
            ((com.daimajia.swipe.SwipeLayout)aq.id(R.id.swipe).getView()).setEnabled(false);
            aq.id(R.id.rightarrow_layout).visibility(View.GONE);
        }
        return convertView;
    }
    public void DeleteProject(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            arrayList.remove(lastPosition);
            notifyDataSetChanged();
            Toast.makeText(ctx, ctx.getString(R.string.project_deleted), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ctx, ctx.getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction().setCustomAnimations(R.anim.slide_in_enter, R.anim.slide_in_exit, R.anim.slide_pop_enter, R.anim.slide_pop_exit)
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();

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
                        lastPosition =position;
                        service.DeleteProject(arrayList.get(position).getProjectID(), true
                                , new CallBack(AdapterProjectsClientList.this, "DeleteProject"));
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
