import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.squareup.okhttp.Response;

import platform.Platform;


public class DownloadRecording {


	Platform platform;
	Authorize authorize = new Authorize();
	List<String> lines ;
	String path="/Users/vyshakh.babji/Desktop/CallLogGenerator/";


	public DownloadRecording() {
		platform = authorize.authenticate();
		this.path = path;
	}

	public void loadFile(String filename) throws FileNotFoundException{

		Scanner sc = new Scanner(new File(filename));
		lines = new ArrayList<String>();

		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		
		System.out.println(lines.size());

	}

	void write(byte[] aInput, String aOutputFileName) {
		System.out.println("Writing binary file...");
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(
						aOutputFileName));
				output.write(aInput);
			} finally {
				output.close();
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File not found.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}


	public void downloadRecording()
			throws IOException, InterruptedException {

		System.out.println("downloading");

		HashMap<String, String> hm = new HashMap<String, String>();

		System.out.println("Downloading started...");
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			String [] keyValue = line.split(" ");
			String key = path + keyValue[0] + ".mp3";
			String value = keyValue[1];
			hm.put(key, value);
		}
		saveRecordings(hm);
	}

	public void saveRecordings(HashMap<String, String> mp) throws IOException,
	InterruptedException {

		File file = new File(path + Calendar.getInstance().getTime().toString());

		file.getParentFile().mkdirs();

		PrintWriter out = new PrintWriter(file);
		out.println("Files downloaded in this session: "
				+ Calendar.getInstance().getTime().toString());
		Iterator<Entry<String, String>> it = mp.entrySet().iterator();
		int i = 0;

		while (it.hasNext()) {
			Entry<String, String> pair = it.next();
			String filename = pair.getKey().toString();
			String url = pair.getValue().toString();
			Response response = platform.sendRequest("get", url, null, null);

			write(response.body().bytes(), filename);
			Thread.sleep(8000);
			System.out.println("File " + filename + "downloaded.");
			i++;
			out.println(i + ". " + filename);
			it.remove();
		}
		System.out.println("Download completed!");
		out.println("All files downloaded successfully. Number of Recordings are "
				+ i);

		out.close();

	}



}
