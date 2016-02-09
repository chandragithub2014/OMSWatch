/*
package watch.oms.omswatch.actioncenter.blexecutor.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import org.json.JSONObject;
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
import watch.oms.omswatch.actioncenter.blexecutor.dto.BLFileUploadDTO;
import watch.oms.omswatch.application.OMSApplication;
import watch.oms.omswatch.constants.OMSDatabaseConstants;
import watch.oms.omswatch.constants.OMSDefaultValues;
import watch.oms.omswatch.constants.OMSMessages;
import watch.oms.omswatch.interfaces.OMSReceiveListener;


public class BLFileUploader extends AsyncTask<String,String,String>{
	private final String TAG = this.getClass().getSimpleName();
	private ProgressDialog progressDialog;
    private Activity context;
	private OMSReceiveListener rListener;
	private List<String> transUsidList;
    
    public BLFileUploader(Activity FragmentContext, int containerId,
			OMSReceiveListener receiveListener, List<String> transUsidList) {
		context = FragmentContext;
		rListener = receiveListener;
		this.transUsidList =  new ArrayList<String>(transUsidList);

	}
    @Override
    protected String doInBackground(String... params) {
    	int result = OMSDefaultValues.NONE_DEF_CNT.getValue();
        String fileURL = null;
        String loadingText = null;
        List<BLFileUploadDTO> fileUploaderList = null;
        String tableName = null;
        String tableURLColumn = null;
        String fileNameColumn = null;
        String mediaType = "Video";
        String xml_file = params[0];
        BLExecutorXMLParser xmlParser = new BLExecutorXMLParser(context);
	    String uploadFileName = null;
	    String queryString= "";
	    List<String> queryArray = new ArrayList<String>();
	     
		try {
			fileUploaderList = xmlParser.parseFileUpLoadURL(xml_file,"");
		} catch (XmlPullParserException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
        if(fileUploaderList != null) {
        	fileURL = fileUploaderList.get(0).fileUploadURL;;
        	mediaType =  fileUploaderList.get(0).fileType;
        	loadingText = fileUploaderList.get(0).loadingText;
        	tableName = fileUploaderList.get(0).tableName;
        	fileNameColumn = fileUploaderList.get(0).fileColName;
        	tableURLColumn =  fileUploaderList.get(0).updateColumn;
            publishProgress(loadingText); 
       try{
	       	Cursor sourceCursor = TransDatabaseUtil.query(
					tableName, null,
					OMSDatabaseConstants.UNIQUE_ROW_ID + "='" + transUsidList.get(0) + "'",
					null, null, null, null);
		if (sourceCursor.moveToFirst()) {
		   //  fileURL = "http://shoponmobile.cognizant.com/AiM/file/upload/338143/";    
		   //  mediaType = "Image";//sourceCursor.getString(sourceCursor.getColumnIndex("mimeType"));  //MimeType i.e Audio/Video/Image/Document
		   uploadFileName = sourceCursor.getString(sourceCursor.getColumnIndex(fileNameColumn));   //File Name to be Upload
		  
		   if(uploadFileName.contains("/"))
		    uploadFileName =  uploadFileName.substring(uploadFileName.lastIndexOf("/") + 1, uploadFileName.length());
		   
		   if(mediaType.equalsIgnoreCase("Camera"))
			   mediaType = "Image";
		   
			File file =  new File(
					Environment.getExternalStorageDirectory(),
					OMSApplication.getInstance().getAppId()
							+ "/"+mediaType+ "/"+uploadFileName);
			
			   Log.d(TAG, "fileURL::" + fileURL);
			   Log.d(TAG, "File Name to Upload::" + uploadFileName);
			   Log.d(TAG, "File/Media Type::" + mediaType);
			   Log.d(TAG, "File Local Path::" + file.getPath());
	
			  if(!TextUtils.isEmpty(fileURL)){
					String response = null;
					HttpClient httpclient = null;
					HttpResponse httpResponse = null;
					InputStream is = null;
					JSONObject responseJSON = null;
					httpclient = AppSecurityAndPerformance.getInstance()
							.getAppGuardSecuredHttpClient(fileURL);
					Log.d(TAG, "Service URL :" + fileURL);
					
			@SuppressWarnings("unchecked")
			Hashtable<String,Object> tempGlobalHash= OMSApplication
					.getInstance().getGlobalBLHash();
			
		   if(tempGlobalHash != null) {
				  List<BLColumnDTO> getColumnList = fileUploaderList.get(0).getColList;
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
				}
					
					if(queryString!=null && queryString.length() > 0) {
						queryString = queryString.replaceAll(" ", "%20");
						if (fileURL.contains("?")) {
							fileURL = fileURL + "&" + queryString;
						} else {
							fileURL = fileURL + "?" + queryString;
						}
				     }
				  }
				   Log.d(TAG, "Service URL After Query Params::" + fileURL);
				   
					HttpPost httpPost = new HttpPost(fileURL);
					
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addPart(OMSMessages.FILE.getValue(), new FileBody(file));
					httpPost.setEntity(entity);
					httpResponse = httpclient.execute(httpPost);

					StatusLine statusLine = httpResponse.getStatusLine();
					HttpEntity httpEntity = httpResponse.getEntity();
					Log.d(TAG, "Response code: " + statusLine.getStatusCode());

					if (statusLine.getStatusCode() == 200) {
						if (httpEntity != null) {
							is = httpEntity.getContent();
							response = convertStreamtoString(is);
							responseJSON = new JSONObject(response);
							ContentValues contentValues = new ContentValues();
							
							fileURL = responseJSON.getString(OMSMessages.FILE_PATH
									.getValue());
							Log.d(TAG, "Response File URL:" + fileURL);
							contentValues.put(tableURLColumn, fileURL);
							contentValues.put("status", "fresh");
							new BLHelper(context).insertOrUpdateData(
					        		   tableName, transUsidList.get(0), contentValues);
							Log.d(TAG, "Service Success Response:" + response);
						}
					} else {
						Log.d(TAG, "Service Failed Response:" + response);
					}

					result =  1; 
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
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait..");
        progressDialog.setMessage("Uploading ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
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

    @Override
    protected void onProgressUpdate(String... values) {
      progressDialog.setMessage(values[0]); 
	 } 
    
    */
/**
	 * Converts Input Stream into String.
	 * 
	 * @param inputStream
	 * @return String
	 * @throws IOException
	 *//*

	private String convertStreamtoString(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + OMSMessages.NEWLINE_CHAR.getValue());
		}
		is.close();
		return sb.toString();
	}
}
*/
