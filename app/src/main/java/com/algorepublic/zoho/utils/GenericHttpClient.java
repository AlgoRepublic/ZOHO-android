package com.algorepublic.zoho.utils;


import android.util.Log;
import android.webkit.MimeTypeMap;

import com.algorepublic.zoho.adapters.AttachmentList;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
        result.add(new BasicHeader("Content-type", "application/x-www-form-urlencoded/octet-stream"));
        return result.toArray(new Header[]{});
    }

    public String postAddTask(String url,ArrayList<Integer> assignee,ArrayList<AttachmentList> files ,BaseClass baseClass) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            for(int i=0;i<files.size();i++) {
                Log.e("File","/"+files.get(i).getFile().getName());
                mpEntity.addPart("files["+i+"]",new FileBody(files.get(i).getFile()));
            }
            for(int i=0;i<assignee.size();i++) {
                Log.e("Assignee", "/" + assignee.get(i));
                mpEntity.addPart("taskResponsible["+i+"]", new StringBody(Integer.toString(assignee.get(i))));
            }
            mpEntity.addPart("CreateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("UpdateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("OwnerID", new StringBody(baseClass.getUserId()));
            mpEntity.addPart("TaskListID",new StringBody(Integer.toString(BaseClass.db.getInt("TaskListNameID"))));
            mpEntity.addPart("Priority", new StringBody(Integer.toString(BaseClass.db.getInt("Priority"))));
            mpEntity.addPart("ProjectID", new StringBody(Integer.toString(BaseClass.db.getInt("ProjectID"))));
            mpEntity.addPart("Title", new StringBody(BaseClass.db.getString("TaskName")));
            mpEntity.addPart("Description", new StringBody(BaseClass.db.getString("TaskDesc")));
            if(BaseClass.db.getString("StartDate").equalsIgnoreCase("")){
                BaseClass.db.putString("StartDate","0001-01-01");
            }
            if(BaseClass.db.getString("EndDate").equalsIgnoreCase("")){
                BaseClass.db.putString("EndDate","0001-01-01");
            }
            mpEntity.addPart("StartDate", new StringBody(BaseClass.db.getString("StartDate")));
            mpEntity.addPart("EndDate", new StringBody(BaseClass.db.getString("EndDate")));
            p.setEntity(mpEntity);

            Log.e("CreateBy", "/" + baseClass.getUserId());
            Log.e("Title", "/"+BaseClass.db.getString("TaskName"));
            Log.e("Description", "/"+BaseClass.db.getString("TaskDesc"));
            Log.e("StartDate","/"+ BaseClass.db.getString("StartDate"));
            Log.e("EndDate", "/"+BaseClass.db.getString("EndDate"));
            Log.e("TaskListID","/"+ BaseClass.db.getInt("TaskListNameID"));
            Log.e("Priority", "/"+BaseClass.db.getInt("Priority"));
            Log.e("ProjectID", "/"+BaseClass.db.getInt("ProjectID"));

            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                message = convertStreamToString(resp.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public String postAddTaskByParent(String url,ArrayList<Integer> assignee,String parentID,
                                      ArrayList<AttachmentList> files ,BaseClass baseClass) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            for(int i=0;i<files.size();i++) {
                Log.e("File","/"+files.get(i).getFile().getName());
                mpEntity.addPart("files["+i+"]",new FileBody(files.get(i).getFile()));
            }
            for(int i=0;i<assignee.size();i++) {
                Log.e("Assignee", "/" + assignee.get(i));
                mpEntity.addPart("taskResponsible["+i+"]", new StringBody(Integer.toString(assignee.get(i))));
            }
            mpEntity.addPart("CreateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("UpdateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("OwnerID", new StringBody(baseClass.getUserId()));
            mpEntity.addPart("TaskListID",new StringBody(Integer.toString(BaseClass.db.getInt("TaskListNameID"))));
            mpEntity.addPart("ParentTaskID",new StringBody(parentID));
            mpEntity.addPart("Priority", new StringBody(Integer.toString(BaseClass.db.getInt("Priority"))));
            mpEntity.addPart("ProjectID", new StringBody(Integer.toString(BaseClass.db.getInt("ProjectID"))));
            mpEntity.addPart("Title", new StringBody(BaseClass.db.getString("TaskName")));
            mpEntity.addPart("Description", new StringBody(BaseClass.db.getString("TaskDesc")));
            if(BaseClass.db.getString("StartDate").equalsIgnoreCase("")){
                BaseClass.db.putString("StartDate","0001-01-01");
            }
            if(BaseClass.db.getString("EndDate").equalsIgnoreCase("")){
                BaseClass.db.putString("EndDate","0001-01-01");
            }
            mpEntity.addPart("StartDate", new StringBody(BaseClass.db.getString("StartDate")));
            mpEntity.addPart("EndDate", new StringBody(BaseClass.db.getString("EndDate")));
            p.setEntity(mpEntity);

            Log.e("CreateBy", "/" + baseClass.getUserId());
            Log.e("Title", "/"+BaseClass.db.getString("TaskName"));
            Log.e("Description", "/"+BaseClass.db.getString("TaskDesc"));
            Log.e("StartDate","/"+ BaseClass.db.getString("StartDate"));
            Log.e("EndDate", "/"+BaseClass.db.getString("EndDate"));
            Log.e("TaskListID","/"+ BaseClass.db.getInt("TaskListNameID"));
            Log.e("ParentTaskID","/"+ parentID);
            Log.e("Priority", "/"+BaseClass.db.getInt("Priority"));
            Log.e("ProjectID", "/"+BaseClass.db.getInt("ProjectID"));

            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                message = convertStreamToString(resp.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public String postUpdateTask(String url,ArrayList<Integer> assignee,ArrayList<AttachmentList> files, ArrayList<Integer> filesToDelete,BaseClass baseClass) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            for(int i=0;i<files.size();i++) {
                if(files.get(i).getFile() != null) {
                    Log.e("files", "/" + files.get(i).getFile());
                    mpEntity.addPart("files["+i+"]", new FileBody(files.get(i).getFile()));
                }
            }
            for(int i=0;i<filesToDelete.size();i++) {
                Log.e("DFile","/"+filesToDelete.get(i));
                mpEntity.addPart("filesToDelete["+i+"]",new StringBody(Integer.toString(filesToDelete.get(i))));
            }
            for(int i=0;i<assignee.size();i++) {
                Log.e("Assignee", "/" + assignee.get(i));
                mpEntity.addPart("taskResponsible["+i+"]", new StringBody(Integer.toString(assignee.get(i))));
            }
            mpEntity.addPart("CreateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("UpdateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("OwnerID", new StringBody(baseClass.getUserId()));
            mpEntity.addPart("TaskListID",new StringBody(Integer.toString(BaseClass.db.getInt("TaskListNameID"))));
            mpEntity.addPart("Priority", new StringBody(Integer.toString(BaseClass.db.getInt("Priority"))));
            mpEntity.addPart("ProjectID", new StringBody(Integer.toString(BaseClass.db.getInt("ProjectID"))));
            mpEntity.addPart("ID", new StringBody(Integer.toString(BaseClass.db.getInt("TaskID"))));
            mpEntity.addPart("Title", new StringBody(BaseClass.db.getString("TaskName")));
            mpEntity.addPart("Description", new StringBody(BaseClass.db.getString("TaskDesc")));
            mpEntity.addPart("StartDate", new StringBody(BaseClass.db.getString("StartDate")));
            mpEntity.addPart("EndDate", new StringBody(BaseClass.db.getString("EndDate")));
            p.setEntity(mpEntity);
            Log.e("CreateBy", "/" + baseClass.getUserId());
            Log.e("Title", "/" + BaseClass.db.getString("TaskName"));
            Log.e("ID", "/" + BaseClass.db.getInt("TaskID"));
            Log.e("Description", "/" + BaseClass.db.getString("TaskDesc"));
            Log.e("StartDate", "/" + BaseClass.db.getString("StartDate"));
            Log.e("EndDate", "/" + BaseClass.db.getString("EndDate"));
            Log.e("TaskListID", "/" + BaseClass.db.getInt("TaskListNameID"));
            Log.e("Priority", "/" + BaseClass.db.getInt("Priority"));
            Log.e("ProjectID", "/" + BaseClass.db.getInt("ProjectID"));
            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                message = convertStreamToString(resp.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public String postUpdateTaskByParent(String url,ArrayList<Integer> assignee,String parentID,ArrayList<AttachmentList> files, ArrayList<Integer> filesToDelete,BaseClass baseClass) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            for(int i=0;i<files.size();i++) {
                if(files.get(i).getFile() != null) {
                    Log.e("File", "/" + files.get(i).getFile().getName());
                    mpEntity.addPart("files["+i+"]", new FileBody(files.get(i).getFile()));
                }
            }
            for(int i=0;i<filesToDelete.size();i++) {
                Log.e("DFile","/"+filesToDelete.get(i));
                mpEntity.addPart("filesToDelete["+i+"]",new StringBody(Integer.toString(filesToDelete.get(i))));
            }
            for(int i=0;i<assignee.size();i++) {
                Log.e("Assignee", "/" + assignee.get(i));
                mpEntity.addPart("taskResponsible["+i+"]", new StringBody(Integer.toString(assignee.get(i))));
            }
            mpEntity.addPart("CreateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("UpdateBy",new StringBody(baseClass.getUserId()));
            mpEntity.addPart("OwnerID", new StringBody(baseClass.getUserId()));
            mpEntity.addPart("ParentTaskID",new StringBody(parentID));
            mpEntity.addPart("TaskListID",new StringBody(Integer.toString(BaseClass.db.getInt("TaskListNameID"))));
            mpEntity.addPart("Priority", new StringBody(Integer.toString(BaseClass.db.getInt("Priority"))));
            mpEntity.addPart("ProjectID", new StringBody(Integer.toString(BaseClass.db.getInt("ProjectID"))));
            mpEntity.addPart("ID", new StringBody(Integer.toString(BaseClass.db.getInt("TaskID"))));
            mpEntity.addPart("Title", new StringBody(BaseClass.db.getString("TaskName")));
            mpEntity.addPart("Description", new StringBody(BaseClass.db.getString("TaskDesc")));
            mpEntity.addPart("StartDate", new StringBody(BaseClass.db.getString("StartDate")));
            mpEntity.addPart("EndDate", new StringBody(BaseClass.db.getString("EndDate")));
            p.setEntity(mpEntity);
            Log.e("CreateBy", "/" + baseClass.getUserId());
            Log.e("Title", "/" + BaseClass.db.getString("TaskName"));
            Log.e("ID", "/" + BaseClass.db.getInt("TaskID"));
            Log.e("Description", "/" + BaseClass.db.getString("TaskDesc"));
            Log.e("StartDate", "/" + BaseClass.db.getString("StartDate"));
            Log.e("EndDate", "/" + BaseClass.db.getString("EndDate"));
            Log.e("ParentTaskID","/"+ parentID);
            Log.e("TaskListID", "/" + BaseClass.db.getInt("TaskListNameID"));
            Log.e("Priority", "/" + BaseClass.db.getInt("Priority"));
            Log.e("ProjectID", "/" + BaseClass.db.getInt("ProjectID"));
            HttpResponse resp = hc.execute(p);
            if (resp != null) {
                message = convertStreamToString(resp.getEntity().getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }
    public String uploadDocumentsByProject(String url,int ProjectID, ArrayList<AttachmentList> files) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        for(int i=0;i<files.size();i++) {
            Log.e("File", "/" + files.get(i).getFile().getName());
            mpEntity.addPart("files["+i+"]", new FileBody(files.get(i).getFile()));
        }

        mpEntity.addPart("ID", new StringBody(Integer.toString(BaseClass.db.getInt("RootID"))));
        mpEntity.addPart("ProjectId", new StringBody(Integer.toString(ProjectID)));
        mpEntity.addPart("CreateBy", new StringBody(Integer.toString(1)));
        mpEntity.addPart("UpdateBy", new StringBody(Integer.toString(1)));
        p.setEntity(mpEntity);
        Log.e("ID/PID",BaseClass.db.getInt("RootID")+"/"+ProjectID);
        HttpResponse resp = hc.execute(p);
        if (resp != null) {
            message = convertStreamToString(resp.getEntity().getContent());
        }
        return message;
    }
    public String uploadDocumentsByTask(String url, int taskID, ArrayList<AttachmentList> files) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        for(int i=0;i<files.size();i++) {
            Log.e("File", "/" + files.get(i).getFile().getName());
            mpEntity.addPart("files["+i+"]", new FileBody(files.get(i).getFile()));
        }

        mpEntity.addPart("taskID", new StringBody(Integer.toString(taskID)));
        p.setEntity(mpEntity);
        HttpResponse resp = hc.execute(p);
        if (resp != null) {
            message = convertStreamToString(resp.getEntity().getContent());
        }
        return message;
    }
    public String createUser(String url,String firstname,String lastname,
                             String email,String mobileNo, File files) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        Log.e("File", "/" + files.getName());
        mpEntity.addPart("files", new FileBody(files));

        mpEntity.addPart("FirstName", new StringBody(firstname));
        mpEntity.addPart("LastName", new StringBody(lastname));
        mpEntity.addPart("Email", new StringBody(email));
        mpEntity.addPart("Mobile", new StringBody(mobileNo));
        p.setEntity(mpEntity);
        HttpResponse resp = hc.execute(p);
        if (resp != null) {
            message = convertStreamToString(resp.getEntity().getContent());
        }
        return message;
    }
    public String updateUser(String url,String ID,String firstname,String lastname,
                             String email,int mobileNo, File files) throws IOException {

        HttpClient hc = new DefaultHttpClient();
        String message =null;
        HttpPost p = new HttpPost(url);
        MultipartEntity mpEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

        Log.e("File", "/" + files.getName());
        if(files !=null)
        mpEntity.addPart("files", new FileBody(files));


        mpEntity.addPart("ID", new StringBody(ID));
        mpEntity.addPart("FirstName", new StringBody(firstname));
        mpEntity.addPart("LastName", new StringBody(lastname));
        mpEntity.addPart("Email", new StringBody(email));
        mpEntity.addPart("Mobile", new StringBody(Integer.toString(mobileNo)));
        p.setEntity(mpEntity);
        HttpResponse resp = hc.execute(p);
        if (resp != null) {
            message = convertStreamToString(resp.getEntity().getContent());
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
    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
}
