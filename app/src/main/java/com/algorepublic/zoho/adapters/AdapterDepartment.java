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

import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.algorepublic.zoho.R;
import com.woxthebox.draglistview.DragItemAdapter;

import java.util.ArrayList;

public class AdapterDepartment extends DragItemAdapter<Pair<Long, String>, AdapterDepartment.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;

    public AdapterDepartment(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        setHasStableIds(true);
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        holder.mText.setText(text);
        holder.itemView.setTag(text);
//        try{
//            holder.mDesc.setText(Html.fromHtml(projects.get(position).description));
//        }catch (NullPointerException npe){}
//        holder.mTasks.setText(projects.get(position).totalTasks);
//        holder.mUsers.setText(projects.get(position).usersCount);
//        holder.mMilestones.setText(projects.get(position).toalMilestones);
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    public class ViewHolder extends DragItemAdapter<Pair<Long, String>, ViewHolder>.ViewHolder {
        public TextView mText;
//        public TextView mDesc;
//        public TextView mTasks;
//        public TextView mUsers;
//        public TextView mMilestones;

        public ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId);
            mText = (TextView) itemView.findViewById(R.id.project_title);
//            mDesc = (TextView) itemView.findViewById(R.id.project_desc);
//            mUsers = (TextView) itemView.findViewById(R.id.task_alert);
//            mTasks = (TextView) itemView.findViewById(R.id.users_alert);
//            mMilestones = (TextView) itemView.findViewById(R.id.milestone_alert);
        }

        @Override
        public void onItemClicked(View view) {
            Toast.makeText(view.getContext(), "Item clicked", Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
