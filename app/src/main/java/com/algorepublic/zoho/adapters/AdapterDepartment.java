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
import android.database.DataSetObserver;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DepartmentFragment;
import com.algorepublic.zoho.utils.DrawableUtils;
import com.algorepublic.zoho.utils.ViewUtils;
import com.androidquery.AQuery;
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableDraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemConstants;
import com.h6ah4i.android.widget.advrecyclerview.expandable.ExpandableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

public class AdapterDepartment
        extends AbstractExpandableItemAdapter<AdapterDepartment.MyGroupViewHolder, AdapterDepartment.MyChildViewHolder>
        implements ExpandableDraggableItemAdapter<AdapterDepartment.MyGroupViewHolder, AdapterDepartment.MyChildViewHolder> {

    private static final String TAG = "MyEDSItemAdapter";

    static Context mContext;
    AQuery aq_header;
    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private LayoutInflater l_Inflater;


    public interface EventListener {
        void onGroupItemRemoved(int groupPosition);

        void onChildItemRemoved(int groupPosition, int childPosition);

        void onGroupItemPinned(int groupPosition);

        void onChildItemPinned(int groupPosition, int childPosition);

        void onItemViewClicked(View v, boolean pinned);
    }
    private interface Expandable extends ExpandableItemConstants {
    }

    private interface Draggable extends DraggableItemConstants {
    }


    public static abstract class MyBaseViewHolder extends AbstractDraggableSwipeableItemViewHolder implements ExpandableItemViewHolder {
        public View mContainer;
        public View mDragHandle;
        public TextView mTextView;
        private int mExpandStateFlags;

        public MyBaseViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.parent1);
            mTextView = (TextView) v.findViewById(R.id.project_title);
        }

        @Override
        public int getExpandStateFlags() {
            return ExpandableItemConstants.STATE_FLAG_IS_EXPANDED;
        }

        @Override
        public void setExpandStateFlags(int flag) {
            mExpandStateFlags = flag;
        }
    }

    public static class MyGroupViewHolder extends MyBaseViewHolder {
        public TextView textView;
        public MyGroupViewHolder(View v) {
            super(v);
            textView = (TextView) v.findViewById(R.id.header);
        }
    }

    public static class MyChildViewHolder extends MyBaseViewHolder {
        public MyChildViewHolder(View v) {
            super(v);
        }
    }

    public AdapterDepartment(Context context) {
        this.mContext = context;
        aq_header = new AQuery(context);
        l_Inflater = LayoutInflater.from(context);
        setHasStableIds(true);
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        };
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v, true);  // true --- pinned
        }
    }
    @Override
    public int getGroupCount() {
        return DepartmentFragment.allDeptList.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        Log.e("Size","/"+DepartmentFragment.allDeptList.get(groupPosition).getProjectList().size());
        return DepartmentFragment.allDeptList.get(groupPosition).getProjectList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return 0;
    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public MyGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.layout_generic_header, parent, false);
        return new MyGroupViewHolder(v);
    }

    @Override
    public MyChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.layout_project_list_row, parent, false);
        return new MyChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(MyGroupViewHolder holder, int groupPosition, int viewType) {

        // set text
        holder.textView.setText(DepartmentFragment.allDeptList.get(groupPosition).getDeptName());
        holder.textView.setOnClickListener(mItemViewOnClickListener);
    }

    @Override
    public void onBindChildViewHolder(MyChildViewHolder holder, int groupPosition, int childPosition, int viewType) {

        // set text
        holder.mTextView.setText(DepartmentFragment.allDeptList.get(groupPosition).getProjectList().get(childPosition).getProjectName());

        final int dragState = holder.getDragStateFlags();

        if (((dragState & Draggable.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & Draggable.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
            } else if ((dragState & Draggable.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(MyGroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        // check the item is *not* pinned

        // check is enabled
        if (!(holder.itemView.isEnabled() && holder.itemView.isClickable())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean onCheckGroupCanStartDrag(MyGroupViewHolder holder, int groupPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
//        final View containerView = holder.mContainer;
//        final View dragHandleView = holder.mDragHandle;
//
//        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
//        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return true;
    }

    @Override
    public boolean onCheckChildCanStartDrag(MyChildViewHolder holder, int groupPosition, int childPosition, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetGroupItemDraggableRange(MyGroupViewHolder holder, int groupPosition) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public ItemDraggableRange onGetChildItemDraggableRange(MyChildViewHolder holder, int groupPosition, int childPosition) {
        // no drag-sortable range specified
        return null;
    }

    @Override
    public void onMoveGroupItem(int fromGroupPosition, int toGroupPosition) {
    }

    @Override
    public void onMoveChildItem(int fromGroupPosition, int fromChildPosition, int toGroupPosition, int toChildPosition) {
        ProjectsList projectsList = DepartmentFragment.allDeptList.get
                (fromGroupPosition).getProjectList().get(fromChildPosition);
        DepartmentFragment.allDeptList.get(fromGroupPosition).getProjectList().remove(fromChildPosition);
        DepartmentFragment.allDeptList.get(toGroupPosition).getProjectList().add(toChildPosition, projectsList);
    }

}
