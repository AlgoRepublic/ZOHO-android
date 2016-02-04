package com.algorepublic.zoho.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
    public static ArrayList<DocumentsList> docsList = new ArrayList<>();
    BaseClass baseClass;
    StickyListHeadersListView listView;

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
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        getToolbar().setTitle(getString(R.string.documents));
        service = new DocumentsService(getActivity());
        service.getDocuments(1, true, new CallBack(DocumentsListFragment.this, "DocumentsList"));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callFragmentWithBackStack(R.id.container,DocsPreviewFragment.newInstance(position),"DocsPreview");
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

    public void callForDocsSorting(){
        String[] menuItems = {"All Files","Pictures","Videos","Favorites"};
        final ActionSheetDialog dialog = new ActionSheetDialog(getActivity(),menuItems, getView());
        dialog.isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
              //  if (isLoaded())
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
                //SortList();
            }
        });
    }
    public void DocumentsList(Object caller, Object model) {
        DocumentsListModel.getInstance().setList((DocumentsListModel) model);
        if (DocumentsListModel.getInstance().responseCode == 100) {
            GetDocumentsList();
            adapterDocsList = new AdapterDocumentsList(getActivity());
            listView.setAreHeadersSticky(true);
            listView.setAdapter(adapterDocsList);
        } else {
            Toast.makeText(getActivity(), getString(R.string.invalid_credential), Toast.LENGTH_SHORT).show();
        }
    }

    public void GetDocumentsList() {
       docsList.clear();
        for (int loop = 0; loop < DocumentsListModel.getInstance().responseObject.size(); loop++) {
            for (int loop1 = 0; loop1 < DocumentsListModel.getInstance().responseObject.get(loop).files.size(); loop1++) {
                DocumentsList documentsList = new DocumentsList();
                documentsList.setFileName(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileDescription);
                documentsList.setFileDescription(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileName);
                documentsList.setFileSizeInByte(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileSizeInByte);
                documentsList.setCreatedAt(DateFormatter(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).createdAt));
                documentsList.setCreatedMilli(DateMilli(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).createdAt));
                documentsList.setFileTypeID(DocumentsListModel.getInstance().responseObject.get(loop).files.get(loop1).fileTypeID);
                docsList.add(documentsList);
            }
        }
        Collections.sort(docsList);
    }
}