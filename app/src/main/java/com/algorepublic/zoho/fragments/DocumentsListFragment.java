package com.algorepublic.zoho.fragments;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_docs, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_files:
                startActivity(new Intent(getActivity(), ActivityUploadDocs.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        baseClass = ((BaseClass) getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        service = new DocumentsService(getActivity());
        service.getDocuments(4, true, new CallBack(DocumentsListFragment.this, "DocumentsList"));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callFragmentWithBackStack(R.id.container,DocsPreviewFragment.newInstance(position),"DocsPreview");
            }
        });
        return view;
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