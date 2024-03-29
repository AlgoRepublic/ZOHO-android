package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

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

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by android on 1/7/16.
 */
public class DocumentsListFragment extends BaseFragment{

    static DocumentsListFragment fragment;
    AQuery aq;
    View view;int Color;
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
        getToolbar().setTitle(getString(R.string.documents));
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

        deleteDocsList.clear();
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        if(baseClass.getThemePreference() == R.style.AppThemeBlue) {
            Color = android.graphics.Color.parseColor("#4B7BAA");
        }else{
            Color = android.graphics.Color.parseColor("#414042");
        }
        if(baseClass.hasPermission(getResources().getString(R.string.documents_add)))
            setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.documents));
        service = new DocumentsService(getActivity());
        if(baseClass.hasPermission(getResources().getString(R.string.documents_view))){
            service.getDocuments(baseClass.db.getInt("ProjectID"),
                    true, new CallBack(DocumentsListFragment.this, "DocumentsList"));
        }else {
            aq.id(R.id.layout_bottom).visibility(View.GONE);
            aq.id(R.id.response_alert).visibility(View.VISIBLE);
            aq.id(R.id.alertMessage).text("You don't have permissions to view Documents.");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callFragmentWithBackStack(R.id.container,  DocsPreviewFragment.newInstance(generalDocsList.get(position)), getString(R.string.document_preview));
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
                    Toast.makeText(getActivity(), getString(R.string.select_project), Toast.LENGTH_SHORT).show();
                }else {
                    callFragmentWithBackStack(R.id.container,UploadDocsFragment.
                            newInstance(baseClass.db.getInt("ProjectID"),0), getString(R.string.documents));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void callForDocsSorting(){
        String[] menuItems = {getString(R.string.all_files),getString(R.string.picture),
                getString(R.string.videos),getString(R.string.favorites)};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems,
                getString(R.string.cancel), getView());
        dialog.titleTextColor(Color);
        dialog.itemTextColor(Color);
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                if (isLoaded()) {
                    if (position == 0) {
                        baseClass.setDocsSortType("AllFiles");
                        aq.id(R.id.list_title).text(getString(R.string.all_files));
                    }
                    if (position == 1) {
                        baseClass.setDocsSortType("Pictures");
                        aq.id(R.id.list_title).text(getString(R.string.picture));
                    }
                    if (position == 2) {
                        baseClass.setDocsSortType("Videos");
                        aq.id(R.id.list_title).text(getString(R.string.videos));
                    }
                    if (position == 3) {
                        baseClass.setDocsSortType("Favorites");
                        aq.id(R.id.list_title).text(getString(R.string.favorites));
                    }
                    FilterList();
                }
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
            aq.id(R.id.alertMessage).text(getString(R.string.no_documents));
            if(generalDocsList.size() ==0){
                aq.id(R.id.response_alert).visibility(View.VISIBLE);
            }else{
                aq.id(R.id.response_alert).visibility(View.GONE);
            }
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
            Toast.makeText(getActivity(), getString(R.string.response_error), Toast.LENGTH_SHORT).show();
        }
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
                documentsList.setFileSizeInByte(Integer.toString(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileSizeInByte / 1000));
                documentsList.setUpdatedAt(DateFormatter(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).updatedAt));
                documentsList.setUpdatedMilli(DateMilli(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).updatedAt));
                documentsList.setFileTypeID(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileTypeID);
                documentsList.setIsFav(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).isFav);
                documentsList.setUserName(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).user.firstName);

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