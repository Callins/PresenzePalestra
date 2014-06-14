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
 * Created by callino on 12/09/13.
 */
public class HTTPPostData extends AsyncTask<String, Long, Object> {
    String Nome = null;
    Integer Pagato = null;
    String Result = null;
    String m_email = null;
    Integer m_id  = 0;
    Integer m_del =0;
    Integer m_abbonato = 0;
    Integer m_admin = 0;
    Integer m_idutente =0 ;

    public HTTPPostData(String nome,Integer pagato,Integer id,Integer Abbonato,Integer admin,String email,Integer del,Integer IdUtente) {
        Nome = nome;
        Pagato = pagato;
        m_id =id;
        m_del = del;
        m_admin = admin;
        m_abbonato =Abbonato;
        m_email= email;
        m_idutente =IdUtente;
    }

    @Override
    protected String doInBackground(String... params) {
        byte[] Bresult = null;
        HttpClient client = new DefaultHttpClient();

        HttpPost post = new HttpPost("http://callino.net23.net/addg.php");
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("name", Nome.toString()));
            nameValuePairs.add(new BasicNameValuePair("pagato", Pagato.toString()));
            nameValuePairs.add(new BasicNameValuePair("id", m_id.toString()));
            nameValuePairs.add(new BasicNameValuePair("abbonato", m_abbonato.toString()));
            nameValuePairs.add(new BasicNameValuePair("admin", m_admin.toString()));
            nameValuePairs.add(new BasicNameValuePair("mail", m_email.toString()));
            nameValuePairs.add(new BasicNameValuePair("del", m_del.toString()));
            nameValuePairs.add(new BasicNameValuePair("Utente", m_idutente.toString()));

            post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = client.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
                Bresult = EntityUtils.toByteArray(response.getEntity());
                Result = new String(Bresult, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return Result;
    }
}