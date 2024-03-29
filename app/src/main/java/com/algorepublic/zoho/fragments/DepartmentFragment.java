package com.algorepublic.zoho.fragments;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDepartment;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.AddDepartmentDialog;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;

import org.lucasr.twowayview.widget.DividerItemDecoration;
import org.lucasr.twowayview.widget.SpacingItemDecoration;

import java.util.ArrayList;

/**
 * Created by android on 2/24/16.
 */
public class DepartmentFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";
    static DepartmentFragment fragment;
    public static ArrayList<ProjectsList> allProjects = new ArrayList<>();
    private ProjectsListService service;
    private BaseClass baseClass;
    Parcelable eimSavedState;
    AQuery aq;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private SwipeRefreshLayout swipeRefreshLayout;


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
        View view = inflater.inflate(R.layout.fragment_department, container, false);
        aq=  new AQuery(view);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list_view);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);

        mRecyclerViewDragDropManager.setInitiateOnMove(false);
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(view.getContext(), R.drawable.material_shadow_z3));

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
        allProjects.clear();
        for (int loop = 0; loop < ProjectsByDepartmentModel.getInstance().responseData.size(); loop++) {
            ProjectsList projectsList = new ProjectsList();
            projectsList.setType(0);
            projectsList.setCompOrDeptID(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID);
            projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
            allProjects.add(projectsList);
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
                allProjects.add(projectsList);
            }
        }
        swipeRefreshLayout.setRefreshing(false);
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
                // callFragmentWithBackStack(R.id.container, AddDepartmentFragment.newInstance(), "AddDepartmentFragment");
                addDepartment();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addDepartment(){
        mRecyclerView.getAdapter().notifyDataSetChanged();
        FragmentManager fragmentManager = getFragmentManager();
        AddDepartmentDialog addDepartmentDialog = new AddDepartmentDialog();
        addDepartmentDialog.show(fragmentManager,"AddDepartment");
    }


    private void addColumnList() {
        aq.id(R.id.alertMessage).text(getString(R.string.no_departments));
        if (allProjects.size() == 0) {
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
        }else{
            aq.id(R.id.response_alert).visibility(View.GONE);
        }
        AdapterDepartment listAdapter = new AdapterDepartment(getActivity());
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(aq.getContext(), R.drawable.material_shadow_z3));
        mRecyclerViewDragDropManager.setInitiateOnLongPress(true);
        mRecyclerViewDragDropManager.setInitiateOnMove(false);

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(listAdapter);      // wrap for dragging

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);
        mRecyclerView.setHasFixedSize(false);

        if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(aq.getContext(), R.drawable.material_shadow_z1)));
        }
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(null, true));

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);

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
    public void onRefresh() {
        service.getProjectsByDepartment(baseClass.getUserId(),
                false, new CallBack(this, "ProjectsByDepartment"));
    }
}
