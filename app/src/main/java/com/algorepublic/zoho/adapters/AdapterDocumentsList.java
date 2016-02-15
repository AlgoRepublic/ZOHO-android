package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;

import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;

import java.util.ArrayList;
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
    ArrayList<DocumentsList> documentsLists = new ArrayList<>();

    public AdapterDocumentsList(Context context,ArrayList<DocumentsList> arrayList ) {
        documentsLists.addAll(arrayList);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        baseClass = ((BaseClass) ctx.getApplicationContext());
    }

    @Override
    public int getCount() {
        return documentsLists.size();
    }

    @Override
    public Object getItem(int pos) {
        return documentsLists.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

       convertView = l_Inflater.inflate(R.layout.layout_docs_row, null);

        aq = new AQuery(convertView);

        aq.id(R.id.file_id).text(Integer.toString(documentsLists.get(position).getID()));
        aq.id(R.id.file_name).text(documentsLists.get(position).getFileName());
        aq.id(R.id.file_time).text(GetTime(documentsLists.get(position).getUpdatedMilli()));
        aq.id(R.id.file_image).image(BaseClass.getIcon(documentsLists.
                get(position).getFileTypeID()));
        try{
            for(int loop=0;loop<DocumentsListFragment.deleteDocsList.size();loop++) {
                if (DocumentsListFragment.deleteDocsList.get(loop) ==
                        Integer.parseInt(aq.id(R.id.file_id).getText().toString()) ) {
                    Log.e("ID",DocumentsListFragment.deleteDocsList.get(loop)+"/"+
                            Integer.parseInt(aq.id(R.id.file_id).getText().toString()));
                    aq.id(R.id.doc_checkbox).checked(true);
                }
            }
        }catch (IndexOutOfBoundsException e){
            aq.id(R.id.doc_checkbox).checked(false);
        }
        aq.id(R.id.doc_checkbox).getCheckBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    DocumentsListFragment.deleteDocsList.
                            add(documentsLists.get(position).getID());
                }else
                {
                    View view = getViewByPosition(position,DocumentsListFragment.listView);
                    AQuery aQuery= new AQuery(view);
                    for(int loop=0;loop<DocumentsListFragment.deleteDocsList.size();loop++) {
                        if (DocumentsListFragment.deleteDocsList.get(loop) ==
                                Integer.parseInt(aQuery.id(R.id.file_id).getText().toString())) {
                            DocumentsListFragment.deleteDocsList.remove(loop);
                        }
                    }
                }
            }
        });
        return convertView;
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
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_header, parent , false);
        aq_header = new AQuery(convertView);

        if (documentsLists.get(position).getUpdatedAt().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text("No Date");
        else
            aq_header.id(R.id.header).text(documentsLists.get(position).getUpdatedAt());

        Animation animation = AnimationUtils.loadAnimation(ctx, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = position;
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return Long.parseLong(documentsLists.get(position).getUpdatedMilli().substring(0, 5));
    }
    public String GetTime(String milli){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(milli));

        String delegate = "hh:mm aaa";
        String time  = (String) DateFormat.format(delegate, calendar.getTime());
        return (time);
    }
}
