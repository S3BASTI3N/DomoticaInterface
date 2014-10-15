package net.cs76.projects.student10340912.DomoticaInterface.DataManagement;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sebastien on 14-10-14.
 */
class AsyncServerHttpRetriever extends AsyncTask<String, Void, String> {

    private DatabaseHelper databaseHelper_;

    public static final int REQUEST_DATABASE_INIT = 0;
    public static final int REQUEST_DATABASE_UPDATE = 1;
    public static final int REQUEST_STATE_UPDATE = 2;

    private int requestedType_;

    public AsyncServerHttpRetriever(DatabaseHelper helper, int type) {

        databaseHelper_ = helper;
        requestedType_ = type;

    }

    @Override
    protected String doInBackground(String... params) {
        HttpClient httpClient = new DefaultHttpClient();

        HttpGet get = new HttpGet( params[0] );
        HttpResponse response;

        Log.d( "AsyncServerHttpRetriever", "Url: " + params[0] );

        try {

            response = httpClient.execute( get );

            HttpEntity entity = response.getEntity();

            if( entity != null ) {

                InputStream inputStream = entity.getContent();

                return convertStreamToString( inputStream );

            }

        } catch( Exception e) {
            Log.d("AsyncServerHttpRetriever", e.getMessage());
        }
        return null;
    }

    protected void onPostExecute( String result ) {
        databaseHelper_.callBack( result, requestedType_ );
    }

    private String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
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
