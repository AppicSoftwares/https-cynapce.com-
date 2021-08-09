package com.alcanzar.cynapse.utils;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//import org.apache.commons.codec.binary.Base64;

public class HttpFileUploader implements Runnable {

    URL connectURL;
    String mediaType;
    //	String Token;
    String Type;
    String fileName;
    String Tag = "HTTPREQUEST";

    public  HttpFileUploader(String urlString, String mediaType, String fileName, String type) {

        try {
            Log.d("URLSTRING",urlString);
            connectURL = new URL(urlString);
            Log.e("final url", connectURL.toString());
        } catch (Exception ex) {
            Log.e("URL FORMATION", "MALFORMATED URL");
        }
        this.mediaType = mediaType;
        this.fileName = fileName;
//		this.Token = token;
        this.Type = type;
    }

    public void doStart(FileInputStream stream) {
        fileInputStream = stream;
        thirdTry();
    }

    FileInputStream fileInputStream = null;
    private static String s;





    void thirdTry() {
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            // ------------------ CLIENT REQUEST
            Log.e(Tag, "Request Start");
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type",	"multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("uploadfile", fileName);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; "+ mediaType+ "\""+ 2+ "\";"+"filename=\"" + fileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag, "Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();
            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];
            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e(Tag, "File is written");
            fileInputStream.close();
            dos.flush();
            InputStream is = conn.getInputStream();

            Log.e(Tag, "File is written done");
            // retrieve the response from server
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            s = b.toString();
            dos.close();
        } catch (IOException ioe) {
            Log.e(Tag, "error: " + ioe.getMessage(), ioe);
        }
    }

    @Override
    public void run() {
    }

    public static String updateCall() {
        String st = s;
        s="";
        return st;
    }


}