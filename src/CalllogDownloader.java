import java.io.EOFException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.okhttp.Response;

import platform.Platform;


public class CalllogDownloader {

	Platform platform;
	Authorize authorize = new Authorize();
	File file ;
	File file2 ;

	String calllog_URL = "/restapi/v1.0/account/~/call-log?dateFrom=";
	FileWriter fileWriter;
	FileWriter fileWriter2;



	public CalllogDownloader() {
		try {

			platform = authorize.authenticate();
			file = new File("/Users/vyshakh.babji/Desktop/CallLogGenerator/call-log.txt");
			file2 = new File("/Users/vyshakh.babji/Desktop/CallLogGenerator/call-recording-uri.txt");

			fileWriter = new FileWriter(file,true);
			fileWriter2 = new FileWriter(file2,true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JSONObject getCallLog(String url){
		try {
			platform.loggedIn();
			Response response = platform.sendRequest("get", url, null, null);
			System.out.println("Response Code for the url: "+url+ " is "  +response.code());
			return bodyString(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public JSONObject bodyString(Response response){
		String body;
		try {
			body = response.body().string();
			return new JSONObject(body);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}


	public void writeToFile(JSONObject jObject, int page){
		try {

			fileWriter.write("\n-----------------------------PAGE "+page+"-------------------------------\n");
			fileWriter.write(jObject.toString());
			fileWriter.write("\n-----------------------  END of PAGE "+page+"----------------------------\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public String checkPaging(JSONObject jObject) throws JSONException{
		JSONObject jsonObject = new JSONObject();
		String nextPageURI="";
		if(jObject.has("navigation")){
			jsonObject= (JSONObject) jObject.get("navigation");
			if(jsonObject.has("nextPage")) {
				jsonObject=(JSONObject) jsonObject.get("nextPage");
				nextPageURI = (String) jsonObject.get("uri") ;
			}
			else
				return null;
		}
		return nextPageURI;
	}

	public  void downloadCallLog(String dateFrom) throws JSONException, InterruptedException, IOException{

		String uRLString = calllog_URL+dateFrom;
		JSONObject jObject = getCallLog(uRLString);

		int i=1;
		writeToFile(jObject,i);

		processForCallRecordingDownload(jObject);

		while(checkPaging(jObject)!=null){
			i++;
			Thread.sleep(7000);
			jObject = getCallLog(checkPaging(jObject));
			writeToFile(jObject,i);	
			processForCallRecordingDownload(jObject);
		}

		fileWriter.flush();
		fileWriter.close();
		fileWriter2.flush();
		fileWriter2.close();

	}

	private void processForCallRecordingDownload(JSONObject jObject) throws JSONException, IOException {
		JSONArray records = jObject.getJSONArray("records");
		for(int i=0; i<records.length(); i++){   
			JSONObject o = records.getJSONObject(i);  
			if(o.has("recording")){
				JSONObject recording = (JSONObject) o.get("recording");
				if(recording.has("contentUri")){
					
					String contentURI = recording.getString("contentUri");
					String contentID =  recording.getString("id");
					fileWriter2.write(contentID+" "+contentURI+"\n");
				}
			}
		}
	
	}

}


























