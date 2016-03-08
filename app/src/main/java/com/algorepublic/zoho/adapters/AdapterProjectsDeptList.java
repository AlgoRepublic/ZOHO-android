package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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


    public AdapterProjectsDeptList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new ProjectsListService((AppCompatActivity)ctx);
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return ProjectsFragment.ByDepartmentList.size();
    }

    @Override
    public Object getItem(int pos) {
        return ProjectsFragment.ByDepartmentList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_project_list_row, null);
        aq = new AQuery(convertView);
        aq.id(R.id.project_title).text(ProjectsFragment.ByDepartmentList.get(position).getProjectName());
        if(!ProjectsFragment.ByDepartmentList.get(position).getTotalTasks().isEmpty())
            aq.id(R.id.task_alert).text(ProjectsFragment.ByDepartmentList.get(position).getTotalTasks());
        if(!ProjectsFragment.ByDepartmentList.get(position).getTotalMilestones().isEmpty())
            aq.id(R.id.milestone_alert).text(ProjectsFragment.ByDepartmentList.get(position).getTotalMilestones());

        aq.id(R.id.project_id).text(ProjectsFragment.ByDepartmentList.get(position).getProjectID());
        if(ProjectsFragment.ByDepartmentList.get(position).getProjectDesc() != null)
            aq.id(R.id.project_desc).text(Html.fromHtml(ProjectsFragment.ByDepartmentList.get(position).getProjectDesc()));

        if(baseClass.getSelectedProject().equals(ProjectsFragment.ByDepartmentList.get(position).getProjectID())){
            aq.id(R.id.selected_project).getView().setBackgroundColor(Color.parseColor("#99cc00"));
        }else{
            aq.id(R.id.selected_project).getView().setBackgroundColor(Color.parseColor("#00000000"));
        }
        aq.id(R.id.parent1).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = v;
                if (baseClass.getSelectedProject().equalsIgnoreCase(((TextView) view.findViewById(R.id.project_id)).getText().toString())) {
                    baseClass.setSelectedProject("0");
                    baseClass.db.putInt("ProjectID", 0);
                } else {
                    baseClass.setSelectedProject(((TextView)view.findViewById(R.id.project_id)).getText().toString());
                    baseClass.db.putInt("ProjectID", Integer.parseInt(baseClass.getSelectedProject()));
                    baseClass.db.putString("ProjectName", (((TextView)view.findViewById(R.id.project_id)).getText().toString()));
                }
                notifyDataSetInvalidated();
                ProjectsFragment.listViewDept.setSelection(position);
            }
        });
        aq.id(R.id.btEdit).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!baseClass.getSelectedProject().equalsIgnoreCase("0")) {
                    callFragmentWithBackStack(R.id.container, EditProjectFragment.
                            newInstance(ProjectsFragment.ByDepartmentList,position), "EditProjectFragment");
                }
            }
        });

        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogCustomAttr("Delete Project?", position);
            }
        });
//        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        convertView.startAnimation(animation);
        return convertView;
    }
    public void DeleteProject(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            ProjectsFragment.ByDepartmentList.remove(lastPosition);
            notifyDataSetChanged();
            Snackbar.make(aq.id(R.id.shadow_item_container).getView(), ctx.getString(R.string.project_deleted), Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(aq.id(R.id.shadow_item_container).getView(), ctx.getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
           }
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();

    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_generic_header, parent , false);
        aq_header = new AQuery(convertView);
        aq_header.id(R.id.header).text(ProjectsFragment.ByDepartmentList.get(position).getCompOrDeptName());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return Long.valueOf(ProjectsFragment.ByDepartmentList.get(position).getCompOrDeptID());
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
                        service.DeleteProject(ProjectsFragment.ByDepartmentList.get(position).getProjectID(), true
                                , new CallBack(AdapterProjectsDeptList.this, "DeleteProject"));
                    }
                });
    }

}
