package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.algorepublic.zoho.BaseActivity;
import com.algorepublic.zoho.Models.DocumentsListModel;
import com.algorepublic.zoho.R;
import com.algorepublic.zoho.adapters.AdapterDocumentsList;
import com.algorepublic.zoho.adapters.DocumentsList;
import com.algorepublic.zoho.services.CallBack;
import com.algorepublic.zoho.services.DocumentsService;
import com.algorepublic.zoho.utils.BaseClass;
import com.androidquery.AQuery;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;

import java.util.ArrayList;
import java.util.Collections;

import cc.cloudist.acplibrary.ACProgressFlower;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsListFragment extends BaseFragment{

    static DocumentsListFragment fragment;
    AQuery aq;
    View view;
    DocumentsService service;
    public static ACProgressFlower dialogAC;
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
        getToolbar().setTitle(getString(R.string.documents));
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_documents, container, false);
        dialogAC = InitializeDialog(getActivity());
        listView = (StickyListHeadersListView) view.findViewById(R.id.list_documents);
        aq = new AQuery(view);
        aq.id(R.id.sort).clicked(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callForDocsSorting();
            }
        });

        deleteDocsList.clear();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.documents));
        service = new DocumentsService(getActivity());
        service.getDocuments(baseClass.db.getInt("ProjectID"),
                false, new CallBack(DocumentsListFragment.this, "DocumentsList"));
        dialogAC.show();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callFragmentWithBackStack(R.id.container,  DocsPreviewFragment.newInstance(generalDocsList.get(position)), "DocsPreview");
            }
        });
        applyLightBackground(aq.id(R.id.layout_bottom).getView(), baseClass);
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
                baseClass.hideKeyPad(getView());
                if(baseClass.getSelectedProject().equalsIgnoreCase("0")){
                    Toast.makeText(getActivity(), "Please Select Project", Toast.LENGTH_SHORT).show();
                }else {
                    callFragmentWithBackStack(R.id.container,UploadDocsFragment.
                            newInstance(baseClass.db.getInt("ProjectID"),0), "UploadDocsFragment");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
                        baseClass.setDocsSortType(getString(R.string.all_files));
                        aq.id(R.id.list_title).text(getString(R.string.all_files));
                    }
                if (position == 1) {
                    baseClass.setDocsSortType(getString(R.string.picture));
                    aq.id(R.id.list_title).text(getString(R.string.picture));
                }
                if (position == 2) {
                    baseClass.setDocsSortType(getString(R.string.videos));
                    aq.id(R.id.list_title).text(getString(R.string.videos));
                }
                if (position == 3) {
                    baseClass.setDocsSortType(getString(R.string.favorites));
                    aq.id(R.id.list_title).text(getString(R.string.favorites));
                }
                FilterList();
            }
        });
    }
    public void FilterList(){
        if(baseClass.getDocsSortType().equalsIgnoreCase(getString(R.string.all_files)))
            FilterByAllDocs();
        if(baseClass.getDocsSortType().equalsIgnoreCase(getString(R.string.picture)))
            FilterByPicture();
        if(baseClass.getDocsSortType().equalsIgnoreCase(getString(R.string.videos)))
            FilterByVideo();
        if(baseClass.getDocsSortType().equalsIgnoreCase(getString(R.string.favorites)))
            FilterByFav();

        SetAdapterList();
    }
    public void SetAdapterList(){
        if (DocumentsListModel.getInstance().responseCode == 100) {
            listView.setAreHeadersSticky(true);
            listView.setAdapter(new AdapterDocumentsList(getActivity(),-1, generalDocsList));
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
        dialogAC.dismiss();
    }

    public void GetAllDocumentsList() {
       allDocsList.clear();
        for (int loop = 0; loop < DocumentsListModel.getInstance().responseObject.size(); loop++) {
            if(DocumentsListModel.getInstance().responseObject.get(loop).folderName.equalsIgnoreCase("Root")){
                BaseClass.db.putInt("RootID", DocumentsListModel.getInstance().responseObject.get(loop).ID);
            }
            for (int loop1 = 0; loop1 < DocumentsListModel.getInstance().responseObject.get(loop).files.size(); loop1++) {
                DocumentsList documentsList = new DocumentsList();
                documentsList.setID(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).ID);
                documentsList.setFileName(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileDescription);
                documentsList.setFileDescription(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileName);
                documentsList.setFileSizeInByte(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileSizeInByte);
                documentsList.setUpdatedAt(DateFormatter(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).updatedAt));
                documentsList.setUpdatedMilli(DateMilli(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).updatedAt));
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