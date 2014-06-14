package com.pallavolo;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callino on 17/09/13.
 */
public class HTTPPostLogin extends AsyncTask<String, Long, Object> {
    public int numero ;
    String Result = null;
    String m_Imei ;
    Environment m_Environment;

    public HTTPPostLogin(String Imei,Environment environment) {
       m_Imei = Imei;
       m_Environment = environment;
    }

    @Override
    protected Integer doInBackground(String... params) {
            byte[] Bresult = null;
    HttpClient client = new DefaultHttpClient();

    HttpPost post = new HttpPost("http://callino.net23.net/Login.php");

    try {
            m_Environment.m_Utente  =0;
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("imei", m_Imei.toString() ));


            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK)
                {
                        Bresult = EntityUtils.toByteArray(response.getEntity());
                        Result = new String(Bresult, "UTF-8");
                }

                JSONObject jsonResponse = new JSONObject(Result);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("Utente");



                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                    numero = jsonChildNode.optInt("id") ;
                    m_Environment.m_Utente = numero;
                }

            }
               catch (UnsupportedEncodingException e)
               {
                   e.printStackTrace();
               } catch (JSONException e) {

               } catch (Exception e)
               {
                }


        return numero;


    }







}
