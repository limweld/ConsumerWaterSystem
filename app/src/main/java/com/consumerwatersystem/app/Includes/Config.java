package com.consumerwatersystem.app.Includes;

/**
 * Created by Hilsoft on 23/06/2017.
 */
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Config {

    private String url_User_data;
    private String url_Reading_data;
    private String url_Payment_data;

    public void setUrl_User_data(String IP) {
        this.url_User_data = "http://"+IP+"/mssqlwater/mobile/consumer_login_json.php";
    }

    public void setUrl_Reading_data(String IP) {
        this.url_Reading_data = "http://"+IP+"/mssqlwater/mobile/reading_list_row_by_consumer.php";
    }

    public void setUrl_Payment_data(String IP) {
        this.url_Payment_data = "http://"+IP+"/mssqlwater/mobile/payment_list_row_by_consumer.php";
    }
    public String getUrl_User_data() { return url_User_data; }

    public String getUrl_Reading_data() { return url_Reading_data; }

    public String getUrl_Payment_data() { return url_Payment_data; }

    public Config() {
    }

    public static String getUrl(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }


}
