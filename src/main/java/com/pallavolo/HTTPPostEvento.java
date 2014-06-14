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

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Callino on 17/09/13.
 */
public class HTTPPostEvento extends AsyncTask<String, Long, Object> {


    String Result = null;
    String m_data = null;
    Integer m_id  = 0;
    Integer m_del =0;
    String  m_Luogo ;
    String m_Descrizione ;
    Integer m_IdUtente;

public HTTPPostEvento(String data, String  Luogo, String Descrizione,Integer del,Integer idUtente) {

    m_data = data;
    m_del = del;
    m_Descrizione = Descrizione;
    m_Luogo =Luogo;
    m_IdUtente =idUtente;
}

@Override
protected String doInBackground(String... params) {
        byte[] Bresult = null;
HttpClient client = new DefaultHttpClient();

HttpPost post = new HttpPost("http://callino.net23.net/Addevent.php");
try {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("data", m_data.toString() ));
        nameValuePairs.add(new BasicNameValuePair("luogo", m_Luogo.toString()));
        nameValuePairs.add(new BasicNameValuePair("descrizione", m_Descrizione.toString()));
        nameValuePairs.add(new BasicNameValuePair("del", m_del.toString()));
        nameValuePairs.add(new BasicNameValuePair("Utente", m_IdUtente.toString()));

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        HttpResponse response = client.execute(post);
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK)
            {
                    Bresult = EntityUtils.toByteArray(response.getEntity());
                    Result = new String(Bresult, "UTF-8");
            }
      }
       catch (UnsupportedEncodingException e)
       {
           e.printStackTrace();
       }
       catch (Exception e)
       {
        }
            return Result;
}
        }
