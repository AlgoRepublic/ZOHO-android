package com.algorepublic.zoho.utils;


import android.util.Log;
import android.webkit.MimeTypeMap;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 8/17/15.
 */
public class GenericHttpClient {

    private Header[] generateHttpRequestHeaders() {

        List<Header> result = new ArrayList<Header>();
        result.add(new BasicHeader("Content-type", "application/json/octet-stream"));
        return result.toArray(new Header[]{});
    }

    public String postAddTask(String url,ArrayList<Integer> assignee,ArrayList<File> files ) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            for(int i=0;i<files.size();i++) {
                Log.e("File","/"+files.get(i).getName());
                mpEntity.addPart("file[]",new FileBody(files.get(i), "application"));
            }
            for(int i=0;i<assignee.size();i++) {
                Log.e("Assignee","/"+assignee.get(i));
                mpEntity.addPart("taskResponsible[]["+i+"]", new StringBody(Integer.toString(assignee.get(i))));
            }
            mpEntity.addPart("CreateBy",new StringBody(Integer.toString(BaseClass.db.getInt("CreateBy"))));
            mpEntity.addPart("UpdateBy",new StringBody(Integer.toString(BaseClass.db.getInt("UpdateBy"))));
            mpEntity.addPart("OwnerID", new StringBody(Integer.toString(BaseClass.db.getInt("OwnerID"))));
            mpEntity.addPart("Priority", new StringBody(Integer.toString(BaseClass.db.getInt("Priority"))));
            mpEntity.addPart("ProjectID", new StringBody(Integer.toString(4)));
            mpEntity.addPart("Title", new StringBody(BaseClass.db.getString("TaskName")));
            p.setEntity(mpEntity);
            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                message = convertStreamToString(resp.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    public String get(String url) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpGet p = new HttpGet(url);

        try {
            p.setHeaders(generateHttpRequestHeaders());
            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                message = convertStreamToString(resp.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
