package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.Calendar;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by android on 1/7/16.
 */
public class AdapterDocumentsList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    private LayoutInflater l_Inflater;
    private int lastPosition = -1;

    public AdapterDocumentsList(Context context) {
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return DocumentsListFragment.generalDocsList.size();
    }

    @Override
    public Object getItem(int pos) {
        return DocumentsListFragment.generalDocsList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.layout_docs_row, null);
        }
        aq = new AQuery(convertView);
        aq.id(R.id.file_id).text(Integer.toString(DocumentsListFragment.generalDocsList.get(position).getID()));
        aq.id(R.id.file_name).text(DocumentsListFragment.generalDocsList.get(position).getFileName());
        aq.id(R.id.file_time).text(GetTime(DocumentsListFragment.generalDocsList.get(position).getCreatedMilli()));
        aq.id(R.id.file_image).image(BaseClass.getIcon(DocumentsListFragment.generalDocsList.
                get(position).getFileTypeID()));
        aq.id(R.id.doc_checkbox).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentsListFragment.selectedID = Integer.parseInt(aq.id(R.id.file_id).getText().toString());
                for (int loop = 0; loop < DocumentsListFragment.generalDocsList.size(); loop++) {
                    int id = DocumentsListFragment.generalDocsList.get(loop).getID();
                    if(DocumentsListFragment.selectedID==id) {
                        aq.id(R.id.doc_checkbox).checked(true);
                    }else {
                        View view = getViewByPosition(loop, DocumentsListFragment.listView);
                        AQuery aQuery = new AQuery(view);
                        aQuery.id(R.id.doc_checkbox).checked(false);
                    }
                }
            }
        });
        return convertView;
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);

        if (DocumentsListFragment.generalDocsList.get(position).getCreatedAt().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text("No Date");
        else
            aq_header.id(R.id.header).text(DocumentsListFragment.generalDocsList.get(position).getCreatedAt());

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return Long.parseLong(DocumentsListFragment.generalDocsList.get(position).getCreatedMilli().substring(0,5));
    }
    public String GetTime(String milli){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milli));

        String delegate = "hh:mm aaa";
        String time  = (String) DateFormat.format(delegate, calendar.getTime());
        return (time);
    }
    public View getViewByPosition(int pos, StickyListHeadersListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
