import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;

import org.json.JSONException;
import org.json.JSONObject;

public class Main {
	public static void main(String[] args) {
//		CalllogDownloader d = new CalllogDownloader();
//		try {
//			d.downloadCallLog("2016-03-12T00:00:00.000Z");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		DownloadRecording r = new DownloadRecording();
		
		try {
			r.loadFile("/Users/vyshakh.babji/Desktop/CallLogGenerator/call-recording-uri.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			r.downloadRecording();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		JSONObject str = d.getCallLog("https://api.devtest.ringcentral.com/restapi/v1.0/account/131192004/call-log?dateFrom=2016-03-12T00:00:00.000Z");
//		
		
		//System.out.println(str.toString());
		
	}
}
