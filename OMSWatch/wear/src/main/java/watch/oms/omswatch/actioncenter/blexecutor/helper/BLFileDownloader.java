package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import watch.oms.omswatch.WatchDB.TransDatabaseUtil;
import watch.oms.omswatch.actioncenter.blexecutor.BLExecutorXMLParser;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLColumnDTO;
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFileDownloadDTO;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;


/**
 * Created by mohan on 11/22/14.
 */
public class BLFileDownloader extends AsyncTask<String,String,String>{
	private final String TAG = this.getClass().getSimpleName();
	private ProgressDialog progressDialog;
    private  int totalSize = 0;
    private Activity context;
	private OMSReceiveListener rListener;
	private String uniqueId = null;
	private List<String> transUsidList;
    
    public BLFileDownloader(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList =  new ArrayList<String>(transUsidList);

	}
    @Override
    protected String doInBackground(String... params) {
    	int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
        String serviceUrl = null;
        String loadingText = null;
        List<BLFileDownloadDTO> fileDownloaderList = null;
        String tableName = null;
        String URLColumn = null;
        String fileNameColumn = null;
        String mediaType = "video";
        String updateColName = null;
        String xml_file = params[0];
        List<String> queryArray = new ArrayList<String>();
        BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
   
        java.util.Date date = new java.util.Date();
		 String timeStamp = new SimpleDateFormat("yyyyMMMdd_HHmmss").format(date
				.getTime());
	     String downLoadedFile = null;
	     String queryString= "";
	     
		try {
			fileDownloaderList = xmlParser.parseFileDownLoadURL(xml_file,"");
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
        if(fileDownloaderList != null) {
        	tableName = fileDownloaderList.get(0).tableName;
        	fileNameColumn = fileDownloaderList.get(0).fileColName;
        	URLColumn = fileDownloaderList.get(0).urlColName;;
         	mediaType =  fileDownloaderList.get(0).fileType;
        	loadingText = fileDownloaderList.get(0).loadingText;
        	updateColName =  fileDownloaderList.get(0).updateColumn;
            publishProgress(loadingText); 
       try{
         	Cursor sourceCursor = TransDatabaseUtil.query(
					tableName, null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "='" + transUsidList.get(0) + "'",
					null, null, null, null);
		if (sourceCursor.moveToFirst()) {
			   serviceUrl = sourceCursor.getString(sourceCursor.getColumnIndex(URLColumn));
			   downLoadedFile = sourceCursor.getString(sourceCursor.getColumnIndex(fileNameColumn));   //File Name to be Download
			   // serviceUrl ="https://nslijedcdev.cognizant.com/nslij/edcweb/ns/downloadContent?edpId=1_123456_1656&chapterId=3&contentId=548ae3856eead78ac7a91c5b";
			   // serviceUrl = "http://shoponmobile.cognizant.com/AiMfiles/10017/sample_OMS_2seconds.mp4";   //Video
			   // serviceUrl = " http://shoponmobile.cognizant.com/AiMfiles/338143/logo.png";      //Image
			   // mediaType = "Video";//sourceCursor.getString(sourceCursor.getColumnIndex("mimeType"));  //MimeType i.e Audio/Video/Image/Document
			 
    		   Log.d(TAG, "Service URL::" + serviceUrl);
    		   Log.d(TAG, "downLoadedFile::" + downLoadedFile);
    		   Log.d(TAG, "mediaType::" + mediaType);
    		   
				if (TextUtils.isEmpty(downLoadedFile)) {
					if (mediaType.equalsIgnoreCase("video")) {
						downLoadedFile = "VID_" + timeStamp + ".mp4";
					} else if (mediaType.equalsIgnoreCase("audio")) {
						downLoadedFile = "AUDIO_" + timeStamp + ".3gpp";
					} else if (mediaType.equalsIgnoreCase("image")) {
						downLoadedFile = "IMG" + timeStamp + ".png";
					} else{
						downLoadedFile = "DOC" + timeStamp + ".txt";
					}
				}
    		   
		   if(!TextUtils.isEmpty(serviceUrl)){
			   
				@SuppressWarnings("unchecked")
				Hashtable<String,Object> tempGlobalHash= OMSApplication
						.getInstance().getGlobalBLHash();
				
				if(tempGlobalHash != null) {
				  List<BLColumnDTO> getColumnList = fileDownloaderList.get(0).getColList;
				if(getColumnList != null && getColumnList.size() > 0){
					for(int i=0;i<getColumnList.size();i++){
						if(!TextUtils.isEmpty((String)tempGlobalHash.get(getColumnList.get(i).columnName))){
							String colVal = (String)tempGlobalHash.get(getColumnList.get(i).columnName);
							String query = getColumnList.get(i).columnName+"="+colVal.replace(" ", "%20");
							queryArray.add(query);
						}
					}
					
					if(queryArray != null && queryArray.size() > 0) {
						for(int i = 0; i < queryArray.size(); i++){
							if (i == 0) {
								queryString+=queryArray.get(i);
							} else {
								queryString+="&"+queryArray.get(i);
							}
						}
					}
					
					if(queryString!=null && queryString.length() > 0) {
						queryString = queryString.replaceAll(" ", "%20");
						if (serviceUrl.contains("?")) {
							serviceUrl = serviceUrl + "&" + queryString;
						} else {
							serviceUrl = serviceUrl + "?" + queryString;
						}
				     }
				  }
				   Log.d(TAG, "Service URL After Query Params::" + serviceUrl);
				}
				
		   URL url = new URL(serviceUrl);
           HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
           urlConnection.setRequestMethod("GET");
          // urlConnection.setDoOutput(true);
	           
           urlConnection.connect();
           
          // File file = new File(localPath);
			File mediaRootStorageDir = new File(
					Environment.getExternalStorageDirectory(),
					OMSApplication.getInstance().getAppId());
			File file = null;
			if (!mediaRootStorageDir.exists()) {
				if (!mediaRootStorageDir.mkdirs()) {
					file = new File(mediaRootStorageDir, mediaType);
					if (!file.exists()) {
						if (!file.mkdirs()) {

						}
					}
				}
			} else {
				file = new File(
						Environment.getExternalStorageDirectory(),
						OMSApplication.getInstance().getAppId()
								+ "/"+mediaType);
			}
		
	      
           InputStream inputStream = urlConnection.getInputStream();
           totalSize = urlConnection.getContentLength();

	       if(totalSize > 0 ){
	    	   File file1 = new File(file,downLoadedFile);
	           FileOutputStream fileOutput = new FileOutputStream(file1);
	           byte[] buffer = new byte[1024];
	           int bufferLength = 0;
	           while((bufferLength=inputStream.read(buffer))!=-1){
	               fileOutput.write(buffer,0,bufferLength);
	           }
	           
	           fileOutput.close();
	       
	           ContentValues cvalss = new ContentValues();
	           cvalss.put(URLColumn, serviceUrl);
	        }else{
				Log.e(TAG,"No Data found for Trans USID:"+ transUsidList.get(0) );
			 }
	       
           result =  -1;/* new BLHelper(context).insertOrUpdateData(
        		   tableName, null, cvalss);*/
    		   }
		} else{
			Log.e(TAG,"No Data found for Trans USID:"+ transUsidList.get(0) );
		}
       }
       catch(Exception e){
           e.printStackTrace();
       }
      }
        return Integer.toString(result);

    }

    @Override
    protected void onPreExecute() {
      //  super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("DownLoading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    
    @Override
    protected void onProgressUpdate(String... values) {
      progressDialog.setMessage(values[0]); 
	  } 
  
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (Integer.parseInt(result) != -1) {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_SUCCESS.getValue());
			}
		} else {
			if (rListener != null) {
				rListener.receiveResult(OMSMessages.BL_FAILURE.getValue());
			}
		}
        progressDialog.dismiss();
    }

}
