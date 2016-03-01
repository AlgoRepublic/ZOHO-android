package com.algorepublic.zoho.fragments;

import android.content.Context;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDepartment;
import com.algorepublic.zoho.adapters.DeptList;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.nineoldandroids.animation.ObjectAnimator;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/24/16.
 */
public class DepartmentFragment extends BaseFragment
        implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    static DepartmentFragment fragment;
    public static ArrayList<DeptList> allDeptList = new ArrayList<>();
    private ProjectsListService service;
    private BaseClass baseClass;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    public static DepartmentFragment newInstance() {
        if(fragment == null) {
            fragment = new DepartmentFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_layout, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);
        mRecyclerViewExpandableItemManager.setOnGroupExpandListener(this);
        mRecyclerViewExpandableItemManager.setOnGroupCollapseListener(this);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));

        service = new ProjectsListService(getActivity());
        service.getProjectsByDepartment(baseClass.getUserId(),
                true, new CallBack(this, "ProjectsByDepartment"));
        return view;
    }
    public void ProjectsByDepartment(Object caller, Object model){
        ProjectsByDepartmentModel.getInstance().setList((ProjectsByDepartmentModel) model);
        if (ProjectsByDepartmentModel.getInstance().responseCode == 100
                || ProjectsByDepartmentModel.getInstance().responseData.size() != 0) {
            AddDepartmentProjects();
        } else {
            Toast.makeText(getActivity(), getString(R.string.projects_list_empty), Toast.LENGTH_SHORT).show();
        }
    }
    public void AddDepartmentProjects(){
        allDeptList.clear();
        for (int loop = 0; loop < ProjectsByDepartmentModel.getInstance().responseData.size(); loop++) {
            DeptList deptList = new DeptList();
            deptList.setDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
            deptList.setDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
            ArrayList<ProjectsList> projectsLists = new ArrayList<>();
            for(int loop1 = 0; loop1 < ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.size(); loop1++){
                ProjectsList projectsList = new ProjectsList();
                projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
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
                projectsLists.add(projectsList);
            }
            deptList.setProjectsLists(projectsLists);
            allDeptList.add(deptList);
        }
        addColumnList();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.departments));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_forum, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_project:
                callFragmentWithBackStack(R.id.container, AddDepartmentFragment.newInstance(), "AddDepartmentFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void addColumnList() {

        AdapterDepartment listAdapter = new AdapterDepartment(getActivity());
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));


        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(listAdapter);
        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mWrappedAdapter);      // wrap for dragging

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);

        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);

    }
    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (fromUser) {
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }

    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
        int childItemHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_padding_material);
        int topMargin = (int) (getActivity().getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp
        int bottomMargin = topMargin; // bottom-spacing: 16dp

        mRecyclerViewExpandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, bottomMargin);
    }
    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mLayoutManager = null;

        super.onDestroyView();
    }
    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

}
