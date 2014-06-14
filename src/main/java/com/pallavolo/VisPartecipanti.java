package com.pallavolo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by Callino on 17/09/13.
 */
public class VisPartecipanti extends Activity {
    Environment m_environment ;
    private static final String username = "luca.callegaro@gmail.com";
    private static final String password = "lucaegraziano@@@";
    private Integer m_id;
    private String jsonResult;
    private String url;
    private ListView listView;
    private String Messaggio;

    ArrayList<Giocatore> m_giocatori = new ArrayList<Giocatore>();
    //
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dett_partecipanti);
        m_environment = (Environment)getIntent().getSerializableExtra("Environment");
        listView  = (ListView) findViewById(R.id.listDettEventi);

        Intent intent=getIntent();

        String pkg=getPackageName();

        Evento e=(Evento)intent.getSerializableExtra(pkg+".IdEvento");
        url = "http://callino.net23.net/dettPartecipanti.php" ;
        m_id = e.m_id;

        Messaggio ="Vuoi partecipare all'evento " + e.m_Descrizione.toString() + " in data " + e.m_Data + " a " + e.m_Luogo +" Allora ";

        accessWebService();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_inviamail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.SendEmail:
              Integer k;
                for(k =0;k< m_environment.m_giocatori.toArray().length ;k++){
                    if(m_environment.m_giocatori.get(k).m_mail != ""){
                        sendMail(m_environment.m_giocatori.get(k).m_mail, "Palestra",Messaggio + "<a href=http://callino.net23.net/cp.php?id="+ m_environment.m_giocatori.get(k).m_id.toString() +"&Id_evento=" + m_id.toString() + "> Clicca Qua </a>")  ;
                    }
                }

               break;
            default:
                return super.onOptionsItemSelected(item);
        }


        return true;
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(params[0]);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("id", m_id.toString()));
            nameValuePairs.add(new BasicNameValuePair("Utente", m_environment .m_Utente .toString()));


            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            jsonResult = inputStreamToString(
                    response.getEntity().getContent()).toString();
        }

        catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            // e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }

    @Override
    protected void onPostExecute(String result) {
        ListDrwaer();
    }
}// end async task

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        // passes values for the urls string array
        task.execute(new String[] { url });
    }

    // build hash set for list view
    public void ListDrwaer() {
        List<Map<String, String>> ListaGiocatori = new ArrayList<Map<String, String>>();

        try {

            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("and_Eventi_dett");


            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String nome = jsonChildNode.optString("Nome");
                Integer partecipa = jsonChildNode.optInt("Partecipa");
                String email = jsonChildNode.optString("email");
                Integer numero = jsonChildNode.optInt("Id");
                Integer CPagato =jsonChildNode.optInt("Pagato");
                Integer CFisso = jsonChildNode.optInt("Abbonato");
                Integer CPrivilegi = jsonChildNode.optInt("Privilegi") ;
                String Part="";
                if (partecipa ==0){
                    Part="Non Partecipa";
                }
                else{
                    Part="Partecipa";
                };

                String outPut = nome  +  " - " + Part;
                m_environment.m_giocatori.add(new Giocatore(numero, nome,CPagato,i,CFisso,CPrivilegi,email));
                ListaGiocatori.add(CreaGiocatore(outPut, Part));
            }
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter mioAdapter = new SimpleAdapter (
                this, 			// The context where the View associated with this SimpleAdapter is running
                ListaGiocatori,		// A List of Maps. Each entry in the List corresponds to one row in the list.
                android.R.layout.simple_list_item_1,		// Resource identifier of a view layout
                new String[] {"nome"},	 		// A list of column names that will be added to the Map associated with each item.
                new int[] { android.R.id.text1 } 			// The views that should display data
        );

        try {

              listView.setAdapter(mioAdapter);
        } catch (Exception  e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String> CreaGiocatore(String nome, String Part) {
        HashMap<String, String> GiocatoreNo = new HashMap<String, String>();
        GiocatoreNo.put("nome",nome);
        GiocatoreNo.put("Partecipa",Part);
        return GiocatoreNo;
    }

    // Mail
    private void sendMail(String email, String subject, String messageBody) {
        Session session = createSessionObject();

        try {
            Message message = createMessage(email, subject, messageBody, session);
            new SendMailTask().execute(message);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Session createSessionObject() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("luca.callegaro@gmail.com" , "Palestra"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);

        //  message.setText(messageBody);
        message.setContent(messageBody, "text/html");
        return message;
    }


    private class SendMailTask extends AsyncTask<Message, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(VisPartecipanti.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Message... messages) {
            try {
                Transport.send(messages[0]);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
