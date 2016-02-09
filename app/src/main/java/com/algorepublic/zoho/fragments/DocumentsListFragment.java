package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.ActivityUploadDocs;
import com.algorepublic.zoho.Models.DocumentsListModel;
import com.algorepublic.zoho.Models.GeneralModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDocumentsList;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.animation.BounceEnter.BounceLeftEnter;
import com.flyco.animation.SlideExit.SlideRightExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;

import java.util.ArrayList;
import java.util.Collections;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsListFragment extends BaseFragment {

    static DocumentsListFragment fragment;
    StickyListHeadersAdapter adapterDocsList;
    AQuery aq;
    View view;
    DocumentsService service;
    public static ArrayList<DocumentsList> generalDocsList = new ArrayList<>();
    public static ArrayList<DocumentsList> allDocsList = new ArrayList<>();
    public static ArrayList<Integer> deleteDocsList = new ArrayList<>();
    BaseClass baseClass;
    public static StickyListHeadersListView listView;

    public DocumentsListFragment() {
    }

    @SuppressWarnings("unused")
    public static DocumentsListFragment newInstance() {
        if (fragment == null) {
            fragment = new DocumentsListFragment();
        }
        return fragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_documents, container, false);
        listView = (StickyListHeadersListView) view.findViewById(R.id.list_documents);
        aq = new AQuery(view);
        aq.id(R.id.sort).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForDocsSorting();
            }
        });
        aq.id(R.id.delete).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForDocsDelete(getActivity().getResources().getString(R.string.delete_doc));
            }
        });
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.documents));
        service = new DocumentsService(getActivity());
        if(isLoaded()) {
            FilterList();
        }else {
            service.getDocuments(4, true, new CallBack(DocumentsListFragment.this, "DocumentsList"));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callFragmentWithBackStack(R.id.container, DocsPreviewFragment.newInstance(position), "DocsPreview");
            }
        });
        return view;
    }

    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.  For this method
     * to be called, you must have first called {@link #setHasOptionsMenu}.  See
     * {@link Activity#onCreateOptionsMenu(Menu) Activity.onCreateOptionsMenu}
     * for more information.
     *
     * @param menu     The options menu in which you place your items.
     * @param inflater
     * @see #setHasOptionsMenu
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_document_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_document:
                startActivity(new Intent(getActivity(), ActivityUploadDocs.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void callForDocsDelete(String content) {
        final NormalDialog dialog = new NormalDialog(getActivity());
        dialog.isTitleShow(false)//
                .bgColor(getResources().getColor(R.color.colorBaseWrapper))//
                .cornerRadius(5)//
                .content(content)//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(getResources().getColor(R.color.colorBaseHeader))//
                .dividerColor(getResources().getColor(R.color.colorContentWrapper))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(getResources().getColor(R.color.colorBaseHeader)
                        , getResources().getColor(R.color.colorBaseHeader))//
                .btnPressColor(getResources().getColor(R.color.colorBaseMenu))//
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
                    service.deleteDocument(deleteDocsList
                            , true, new CallBack(DocumentsListFragment.this, "DeleteDoc"));
                        deleteDocsList.clear();
                    }
                });
    }
    public void DeleteDoc(Object caller, Object model) {
        GeneralModel.getInstance().setList((GeneralModel) model);
        if (GeneralModel.getInstance().responseCode.equalsIgnoreCase("100")) {
            Snackbar.make(getView(), getString(R.string.doc_deleted), Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Snackbar.make(getView(), getString(R.string.invalid_credential), Snackbar.LENGTH_SHORT).show();
        }
    }
    public void callForDocsSorting(){
        String[] menuItems = {"All Files","Pictures","Videos","Favorites"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (isLoaded())
                if (position == 0) {
                    baseClass.setDocsSortType("AllFiles");
                }
                if (position == 1) {
                    baseClass.setDocsSortType("Pictures");
                }
                if (position == 2) {
                    baseClass.setDocsSortType("Videos");
                }
                if (position == 3) {
                    baseClass.setDocsSortType("Favorites");
                }
                FilterList();
            }
        });
    }
    public void FilterList(){
        if(baseClass.getDocsSortType().equalsIgnoreCase("AllFiles"))
            FilterByAllDocs();
        if(baseClass.getDocsSortType().equalsIgnoreCase("Pictures"))
            FilterByPicture();
        if(baseClass.getDocsSortType().equalsIgnoreCase("Videos"))
            FilterByVideo();
        if(baseClass.getDocsSortType().equalsIgnoreCase("Favorites"))
            FilterByFav();

        SetAdapterList();
    }
    public void SetAdapterList(){
        if (DocumentsListModel.getInstance().responseCode == 100) {
            adapterDocsList = new AdapterDocumentsList(getActivity());
            listView.setAreHeadersSticky(true);
            listView.setAdapter(adapterDocsList);
        }
    }
    public void DocumentsList(Object caller, Object model) {
        DocumentsListModel.getInstance().setList((DocumentsListModel) model);
        if (DocumentsListModel.getInstance().responseCode == 100) {
            GetAllDocumentsList();
            FilterList();
        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

    public void GetAllDocumentsList() {
       allDocsList.clear();
        for (int loop = 0; loop < DocumentsListModel.getInstance().responseObject.size(); loop++) {
            for (int loop1 = 0; loop1 < DocumentsListModel.getInstance().responseObject.get(loop).files.size(); loop1++) {
                DocumentsList documentsList = new DocumentsList();
                documentsList.setID(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).ID);
                documentsList.setFileName(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileDescription);
                documentsList.setFileDescription(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileName);
                documentsList.setFileSizeInByte(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileSizeInByte);
                documentsList.setCreatedAt(DateFormatter(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).createdAt));
                documentsList.setCreatedMilli(DateMilli(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).createdAt));
                documentsList.setFileTypeID(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileTypeID);
                documentsList.setIsFav(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).isFav);
                allDocsList.add(documentsList);
            }
        }
        Collections.sort(allDocsList);
    }

    public void FilterByAllDocs(){
        generalDocsList.clear();
       generalDocsList.addAll(allDocsList);
    }
    public void FilterByPicture(){
        generalDocsList.clear();
        for(int loop=0;loop<allDocsList.size();loop++){
            if(BaseClass.getExtension(allDocsList.get(loop).getFileName())>=1&&
                    BaseClass.getExtension(allDocsList.get(loop).getFileName()) <=4){
                generalDocsList.add(allDocsList.get(loop));
            }
        }
    }
    public void FilterByVideo(){
        generalDocsList.clear();
        for(int loop=0;loop<allDocsList.size();loop++){
            if(BaseClass.getExtension(allDocsList.get(loop).getFileName())>=9&&
                    BaseClass.getExtension(allDocsList.get(loop).getFileName()) <=11){
                generalDocsList.add(allDocsList.get(loop));
            }
        }
    }
    public void FilterByFav(){
        generalDocsList.clear();
        for(int loop=0;loop<allDocsList.size();loop++){
            if(allDocsList.get(loop).getIsFav()== true){
                generalDocsList.add(allDocsList.get(loop));
            }
        }
    }
    public boolean isLoaded(){
        Boolean isloading=false;
        if(allDocsList.size()==0)
            isloading = false;
        else
            isloading= true;

        return isloading;
    }
}