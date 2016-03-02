/**
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DepartmentFragment;
import com.algorepublic.zoho.fragments.EditDepartmentFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DepartmentService;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.algorepublic.zoho.utils.ViewUtils;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class AdapterDepartment
        extends RecyclerView.Adapter<AdapterDepartment.MyViewHolder>
        implements DraggableItemAdapter<AdapterDepartment.MyViewHolder> {
    private static final String TAG = "MyDragSectionAdapter";
    public static final int ITEM_VIEW_TYPE_SECTION_HEADER = 0;
    public static final int ITEM_VIEW_TYPE_SECTION_ITEM = 1;
    DepartmentService service;ProjectsListService service1;
    Context ctx;
    BaseClass baseClass;
    ACProgressFlower dialogAC;
    int Clicked, DeptDeletePosition,ProjectDeletePosition;

    // NOTE: Make accessible with short name
    private interface Draggable extends DraggableItemConstants {
    }


    public static class MyViewHolder extends AbstractDraggableItemViewHolder {
        public RelativeLayout mContainer;
        public TextView mTextView;
        public TextView title;
        public TextView btEdit;
        public TextView btDelete;

        public MyViewHolder(View v) {
            super(v);
            mContainer = (RelativeLayout) v.findViewById(R.id.container);
            mTextView = (TextView) v.findViewById(R.id.project_title);
            title = (TextView) v.findViewById(R.id.header);
            btEdit = (TextView) v.findViewById(R.id.btEdit);
            btDelete = (TextView) v.findViewById(R.id.btDelete);
        }
    }

    public AdapterDepartment(Context context) {
        this.ctx = context;
        baseClass =((BaseClass) ctx.getApplicationContext());
        service = new DepartmentService((AppCompatActivity)context);
        service1 = new ProjectsListService((AppCompatActivity)context);
        dialogAC = new ACProgressFlower.Builder(ctx)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return DepartmentFragment.allProjects.get(position).getType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        final View v;
        switch (viewType) {
            case ITEM_VIEW_TYPE_SECTION_HEADER:
                v = inflater.inflate(R.layout.layout_generic_header_dept, parent, false);
                break;
            case ITEM_VIEW_TYPE_SECTION_ITEM:
                v = inflater.inflate(R.layout.layout_department_list_row, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected viewType (= " + viewType + ")");
        }

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ITEM_VIEW_TYPE_SECTION_HEADER:
                onBindSectionHeaderViewHolder(holder, position);
                break;
            case ITEM_VIEW_TYPE_SECTION_ITEM:
                onBindSectionItemViewHolder(holder, position);
                break;
        }
    }

    private void onBindSectionHeaderViewHolder(MyViewHolder holder, final int position) {

        // set text
        holder.title.setText(DepartmentFragment.allProjects.get(position).getCompOrDeptName());
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clicked =1;
                DeptDeletePosition = position;
                NormalDialogCustomAttr(ctx.getString(R.string.deleted_dept), position);
            }
        });
        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container, EditDepartmentFragment.newInstance(position), "EditDepartmentFragment");
            }
        });
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){

        ((AppCompatActivity) ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();

    }
    private void onBindSectionItemViewHolder(MyViewHolder holder,final int position) {

        // set text
        holder.mTextView.setText(DepartmentFragment.allProjects.get(position).getProjectName());
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clicked=2;
                ProjectDeletePosition = position;
                NormalDialogCustomAttr(ctx.getString(R.string.deleted_project),position);
            }
        });
        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;
            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;
                holder.mContainer.setBackgroundResource(bgResId);
            } else if (
                    ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) &&
                            ((dragState & Draggable.STATE_FLAG_IS_IN_RANGE) != 0)) {
                bgResId = R.drawable.bg_item_dragging_state;
                holder.mContainer.setBackgroundResource(bgResId);
            }
        }
    }

    @Override
    public int getItemCount() {
        return DepartmentFragment.allProjects.size();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

        if (fromPosition == toPosition) {
            return;
        }

        ProjectsList projectsList = DepartmentFragment.allProjects.get(fromPosition);
        DepartmentFragment.allProjects.remove(fromPosition);
        DepartmentFragment.allProjects.add(toPosition, projectsList);
        notifyItemMoved(fromPosition, toPosition);
        service.addProjectIntoDepartment(DepartmentFragment.allProjects.get(toPosition).getCompOrDeptID()
                , DepartmentFragment.allProjects.get(fromPosition).getProjectID(), baseClass.getUserId()
                , true, new CallBack(AdapterDepartment.this, "MoveProject"));
    }
    public void MoveProject(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content)
                    , ctx.getString(R.string.project_moved), Snackbar.LENGTH_SHORT).show();
        } else {
            Toast.makeText(((AppCompatActivity) ctx), ctx.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        // x, y --- relative from the itemView's top-left


        final View containerView = holder.mContainer;
        if(containerView == null)
            return false;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(containerView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        final int start = findFirstSectionItem(position);
        final int end = findLastSectionItem(position);

        return new ItemDraggableRange(start, end);
    }

    private int findFirstSectionItem(int position) {

        while (position > 1) {
            position -= 1;
        }
        return position;
    }

    private int findLastSectionItem(int position) {

        final int lastIndex = getItemCount() - 1;
        while (position < lastIndex) {
            position += 1;
        }

        return position;
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
                        if(Clicked==1) {
                            dialogAC.show();
                            service.DeleteDepartment(DepartmentFragment.allProjects.get(position).getCompOrDeptID(), true
                                    , new CallBack(AdapterDepartment.this, "DeleteDept"));
                        }
                        if(Clicked==2){
                            service1.DeleteProject(DepartmentFragment.allProjects.get(position).getProjectID(), true
                                    , new CallBack(AdapterDepartment.this, "DeleteProject"));
                        }
                    }
                });
    }
    public void DeleteDept(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            UpdateList();
        } else {
            Snackbar.make(((AppCompatActivity) ctx).findViewById(android.R.id.content),
                    ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void DeleteProject(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            DepartmentFragment.allProjects.remove(ProjectDeletePosition);
            notifyDataSetChanged();
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content),
                   ctx.getString(R.string.project_deleted),Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(((AppCompatActivity) ctx).findViewById(android.R.id.content),
                    ctx.getString(R.string.response_error), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdateList() {
        service1.getProjectsByDepartment(baseClass.getUserId(),
                true, new CallBack(this, "ProjectsByDepartment"));
    }
    public void ProjectsByDepartment(Object caller, Object model){
        ProjectsByDepartmentModel.getInstance().setList((ProjectsByDepartmentModel) model);
        if (ProjectsByDepartmentModel.getInstance().responseCode == 100
                || ProjectsByDepartmentModel.getInstance().responseData.size() != 0) {
            AddDepartmentProjects();

        } else {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content),
                    ctx.getString(R.string.projects_list_empty), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void AddDepartmentProjects(){
        DepartmentFragment.allProjects.clear();
        for (int loop = 0; loop < ProjectsByDepartmentModel.getInstance().responseData.size(); loop++) {
            ProjectsList projectsList = new ProjectsList();
            projectsList.setType(0);
            projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
            projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
            DepartmentFragment.allProjects.add(projectsList);
            for(int loop1 = 0; loop1 < ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.size(); loop1++){
                projectsList = new ProjectsList();
                projectsList.setType(1);
                projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
                projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
                projectsList.setProjectID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectID);
                projectsList.setProjectName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).projectName);
                projectsList.setOwnerID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).ownerID);
                projectsList.setOwnerName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).ownerName);
                projectsList.setProjectDesc(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).description);
                projectsList.setTotalTasks(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).totalTasks);
                projectsList.setTotalUsers(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).usersCount);
                projectsList.setTotalMilestones(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).toalMilestones);
                projectsList.setDeleted(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).IsDeleted);
                projectsList.setPrivate(ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.get(loop1).Isprivate);
                DepartmentFragment.allProjects.add(projectsList);
            }
        }
        dialogAC.dismiss();
        Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content),
                ctx.getString(R.string.department_deleted), Snackbar.LENGTH_SHORT).show();
       notifyDataSetChanged();
    }
}
