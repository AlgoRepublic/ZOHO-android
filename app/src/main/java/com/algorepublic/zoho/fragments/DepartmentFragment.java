package com.algorepublic.zoho.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.Models.ProjectsByDepartmentModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDepartment;
import com.algorepublic.zoho.adapters.DeptList;
import com.algorepublic.zoho.adapters.ProjectsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.ProjectsListService;
import com.algorepublic.zoho.utils.BaseClass;
import com.nineoldandroids.animation.ObjectAnimator;
import com.woxthebox.draglistview.BoardView;
import com.woxthebox.draglistview.DragItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2/24/16.
 */
public class DepartmentFragment extends BaseFragment{
    private BoardView mBoardView;
    ProjectsListService service;
    BaseClass baseClass;
    private static int sCreatedItems = 0;
    static  ArrayList<DeptList> allDeptList = new ArrayList<>();
    static DepartmentFragment fragment;

    public static DepartmentFragment newInstance() {
        if(fragment == null) {
            fragment = new DepartmentFragment();
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board_layout, container, false);
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        service = new ProjectsListService(getActivity());
        service.getProjectsByDepartment(baseClass.getUserId(),
                true, new CallBack(this, "ProjectsByDepartment"));
        mBoardView = (BoardView) view.findViewById(R.id.board_view);
        mBoardView.setSnapToColumnsWhenScrolling(true);
        mBoardView.setSnapToColumnWhenDragging(true);
        mBoardView.setSnapDragItemToTouch(true);
        mBoardView.setDragEnabled(true);
        mBoardView.setCustomDragItem(new MyDragItem(getActivity(), R.layout.column_item));
        mBoardView.setBoardListener(new BoardView.BoardListener() {
            @Override
            public void onItemDragStarted(int column, int row) {
                Toast.makeText(mBoardView.getContext(), "Start - column: " + column + " row: " + row, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemChangedColumn(int oldColumn, int newColumn) {
                TextView itemCount1 = (TextView) mBoardView.getHeaderView(oldColumn).findViewById(R.id.header_count);
                itemCount1.setText(Integer.toString(mBoardView.getAdapter(oldColumn).getItemCount()));
                TextView itemCount2 = (TextView) mBoardView.getHeaderView(newColumn).findViewById(R.id.header_count);
                itemCount2.setText(Integer.toString(mBoardView.getAdapter(newColumn).getItemCount()));
            }

            @Override
            public void onItemDragEnded(int fromColumn, int fromRow, int toColumn, int toRow) {
                if (fromColumn != toColumn || fromRow != toRow) {
                    Toast.makeText(mBoardView.getContext(), "End - column: " + toColumn + " row: " + toRow, Toast.LENGTH_SHORT).show();
                }
            }
        });
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
            for(int loop1=0;loop1<ProjectsByDepartmentModel.getInstance().responseData.get(loop).projects.size();loop1++){
                ProjectsList projectsList = new ProjectsList();
                if(ProjectsByDepartmentModel.getInstance().responseData.get(loop).ID.equals("0")){
                    projectsList.setCompOrDeptName("Unassigned Projects");
                }else {
                    projectsList.setCompOrDeptName(ProjectsByDepartmentModel.getInstance().responseData.get(loop).departmentName);
                }
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
        addColumnList(allDeptList);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Department");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tasklist, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_project:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addColumnList(List<DeptList> mItemArrayParent) {
        ArrayList<Pair<Long, ProjectsList>> mItemArray;
        for (int i = 0; i < mItemArrayParent.size(); i++) {
            mItemArray = new ArrayList<>();
            for (int loop=0;loop<mItemArrayParent.get(i).getProjectList().size();loop++) {
                long id = sCreatedItems++;
                ProjectsList  projectsList = mItemArrayParent.get(i).getProjectList().get(loop);
                mItemArray.add(new Pair<>(id,  projectsList));
            }

            AdapterDepartment listAdapter = new AdapterDepartment(mItemArray, R.layout.column_item, R.id.item_layout, true);
            View header = View.inflate(getActivity(), R.layout.column_header, null);
            ((TextView) header.findViewById(R.id.header)).setText(mItemArrayParent.get(i).getDeptName());
            ((TextView) header.findViewById(R.id.header_count)).setText(Integer.toString(mItemArray.size()));
            mBoardView.addColumnList(listAdapter, header, false);
        }
    }

    public static class MyDragItem extends DragItem {

        public MyDragItem(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void onBindDragView(View clickedView, View dragView) {
            CharSequence text = ((TextView) clickedView.findViewById(R.id.project_title)).getText();
            ((TextView) dragView.findViewById(R.id.project_title)).setText(text);
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));

            dragCard.setMaxCardElevation(40);
            dragCard.setCardElevation(clickedCard.getCardElevation());
            // I know the dragView is a FrameLayout and that is why I can use setForeground below api level 23
            dragCard.setForeground(clickedView.getResources().getDrawable(R.drawable.card_view_drag_foreground));
        }

        @Override
        public void onMeasureDragView(View clickedView, View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            CardView clickedCard = ((CardView) clickedView.findViewById(R.id.card));
            int widthDiff = dragCard.getPaddingLeft() - clickedCard.getPaddingLeft() + dragCard.getPaddingRight() -
                    clickedCard.getPaddingRight();
            int heightDiff = dragCard.getPaddingTop() - clickedCard.getPaddingTop() + dragCard.getPaddingBottom() -
                    clickedCard.getPaddingBottom();
            int width = clickedView.getMeasuredWidth() + widthDiff;
            int height = clickedView.getMeasuredHeight() + heightDiff;
            dragView.setLayoutParams(new FrameLayout.LayoutParams(width, height));

            int widthSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            dragView.measure(widthSpec, heightSpec);
        }

        @Override
        public void onStartDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 40);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }

        @Override
        public void onEndDragAnimation(View dragView) {
            CardView dragCard = ((CardView) dragView.findViewById(R.id.card));
            ObjectAnimator anim = ObjectAnimator.ofFloat(dragCard, "CardElevation", dragCard.getCardElevation(), 6);
            anim.setInterpolator(new DecelerateInterpolator());
            anim.setDuration(ANIMATION_DURATION);
            anim.start();
        }
    }
}
