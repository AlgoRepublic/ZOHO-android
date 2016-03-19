package com.algorepublic.zoho.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.fragments.DocsPreviewFragment;
import com.algorepublic.zoho.fragments.DocumentsListFragment;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;
import java.util.Calendar;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by android on 1/7/16.
 */
public class AdapterDocumentsList extends BaseAdapter implements StickyListHeadersAdapter {

    Context ctx;
    AQuery aq,aq_header;
    BaseClass baseClass;
    DocumentsService service;
    private LayoutInflater l_Inflater;
    private int lastPosition = -1;
    static int ID;
    ArrayList<DocumentsList> documentsLists = new ArrayList<>();

    public AdapterDocumentsList(Context context,int Id,ArrayList<DocumentsList> arrayList ) {
        ID = Id;
        documentsLists.addAll(arrayList);
        l_Inflater = LayoutInflater.from(context);
        this.ctx = context;
        service = new DocumentsService((AppCompatActivity)ctx);
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
        aq.id(R.id.file_size).text(documentsLists.get(position).getFileSizeInByte()+"K");
        aq.id(R.id.file_time).text(GetTime(documentsLists.get(position).getUpdatedMilli()));
        aq.id(R.id.doc_checkbox).image(BaseClass.getIcon(documentsLists.
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
        aq.id(R.id.btDelete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DocumentsListFragment.deleteDocsList.
                                    add(documentsLists.get(position).getID());
        callForDocsDelete(ctx.getResources().getString(R.string.delete_doc));
            }
        });
        aq.id(R.id.parent1).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFragmentWithBackStack(R.id.container,  DocsPreviewFragment.newInstance(documentsLists.get(position)), "DocsPreview");
            }
        });
//        aq.id(R.id.doc_checkbox).clicked(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Log.e("pos",DocumentsListFragment.listView.getListChildCount()+"/"+position);
//                View view = getViewByPosition(position, DocumentsListFragment.listView);
//                AQuery aQuery = new AQuery(view);
//                if (DocumentsListFragment.deleteDocsList.size() > 0) {
//                    for (int loop = 0; loop < DocumentsListFragment.deleteDocsList.size(); loop++) {
//                        view = getViewByPosition(position, DocumentsListFragment.listView);
//                        aQuery = new AQuery(view);
//                        if (aQuery.id(R.id.doc_checkbox).isChecked()) {
//                            DocumentsListFragment.deleteDocsList.
//                                    add(documentsLists.get(position).getID());
//                            aQuery.id(R.id.selected_doc).visibility(View.VISIBLE);
//                            aQuery.id(R.id.selected_doc).background(R.mipmap.doc_cancel);
//                            break;
//                        } else if (DocumentsListFragment.deleteDocsList.get(loop) ==
//                                Integer.parseInt(aQuery.id(R.id.file_id).getText().toString())) {
//                            DocumentsListFragment.deleteDocsList.remove(loop);
//                            aQuery.id(R.id.selected_doc).visibility(View.GONE);
//                            aQuery.id(R.id.doc_checkbox).image(BaseClass.getIcon(documentsLists.
//                                    get(position).getFileTypeID()));
//                        }
//                    }
//                } else {
//                    DocumentsListFragment.deleteDocsList.
//                            add(documentsLists.get(position).getID());
//                    aQuery.id(R.id.selected_doc).visibility(View.VISIBLE);
//                    aQuery.id(R.id.selected_doc).background(R.mipmap.doc_cancel);
//                }
//            }
//        });
        return convertView;
    }
    public void callFragmentWithBackStack(int containerId, Fragment fragment, String tag){
        ((AppCompatActivity)ctx).getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
    void callForDocsDelete(String content) {
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
                        if(ID==-1) {
                            service.deleteDocument(DocumentsListFragment.deleteDocsList
                                    , true, new CallBack(AdapterDocumentsList.this, "DeleteDoc"));
                        }
                        else{
                            service.deleteDocumentByTask(ID, DocumentsListFragment.deleteDocsList
                                    , true, new CallBack(AdapterDocumentsList.this, "DeleteDoc"));
                        }
                    }
                });
    }
    public void DeleteDoc(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseObject==true) {
            UpdatedAfterDelete();
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content), ctx.getString(R.string.doc_deleted), Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(((AppCompatActivity)ctx).findViewById(android.R.id.content), ctx.getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void UpdatedAfterDelete(){
        ArrayList<DocumentsList> arrayList = new ArrayList<>();
        arrayList.addAll(documentsLists);
        documentsLists.clear();
        for(int loop=0;loop<arrayList.size();loop++) {
            if (DocumentsListFragment.deleteDocsList.get(0) != arrayList.get(loop).getID()) {
                documentsLists.add(arrayList.get(loop));
            }
        }
        DocumentsListFragment.deleteDocsList.clear();
        notifyDataSetChanged();
    }
    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        convertView = l_Inflater.inflate(R.layout.layout_taskslist_header, parent , false);
        aq_header = new AQuery(convertView);

        if (documentsLists.get(position).getUpdatedAt().equalsIgnoreCase("3/0/1"))
            aq_header.id(R.id.header).text(ctx.getString(R.string.no_date));
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
