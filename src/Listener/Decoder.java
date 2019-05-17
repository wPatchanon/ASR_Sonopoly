package Listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class Decoder {
	String url;
	String charset;
	File file;

	public Decoder() {
		// TODO Auto-generated constructor stub
		url = "http://localhost:8080/client/dynamic/recognize";
		charset = "UTF-8";
		file = new File("/Users/patchanon/Documents/asr/final_project/RecordAudio.wav");
	}
	
	public String compute() throws MalformedURLException, IOException {
		//System.out.println(file.exists());
		System.out.println(file.length());
		
		URLConnection connection = new URL(url).openConnection();
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "audio/x-wav");

		try (
		    OutputStream output = connection.getOutputStream();
		    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
		) {   
		    Files.copy(file.toPath(), output);
		    output.flush(); // Important before continuing with writer!  
		}

		// Request is lazily fired whenever you need to obtain information about response.
		HttpURLConnection conn = (HttpURLConnection) connection; 
		if(conn.getResponseCode() == 200) {
			InputStreamReader response = new InputStreamReader(((HttpURLConnection) connection).getInputStream());
			BufferedReader br = new BufferedReader(response);
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
		// System.out.println(sb.toString());
			return sb.toString();
		}
		else 
			return Integer.toString(conn.getResponseCode());
	}
}
