package com.pallavolo;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaGiocatori extends Activity {
    private String jsonResult;
    private ListView listView;
    Environment m_environment ;
    private String url ;
    private long m_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_giocatori);

        m_environment = (Environment)getIntent().getSerializableExtra("Environment");
        listView  = (ListView) findViewById(R.id.listView1);

        registerForContextMenu(listView);




        if(m_environment.m_Utente > 0){
            url = "http://callino.net23.net/ag.php?Id=" + m_environment.m_Utente.toString();
            accessWebService();
        }
        else
        {
            Context context = getApplicationContext();
            CharSequence text = "Sto Raccogliendo informazioni sull'account Riprova tra un attimo!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                m_id = id;
                openContextMenu(listView);
            }

        });
    }
    /**
     * MENU
     */

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listView1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final Intent AddGioca = new Intent(getApplicationContext(), AddGiocatore.class);
        String pkg=getPackageName();
        Giocatore g;
        switch(item.getItemId()) {

            case R.id.edit:
                // edit stuff here
                 g = m_environment.m_giocatori.get((int) m_id);

                AddGioca.putExtra(pkg + ".Giocatore",g );
                AddGioca.putExtra("Environment",m_environment);
                startActivity(AddGioca);

                return true;

            case R.id.delete:
                // remove stuff here
                g = m_environment.m_giocatori.get((int) m_id);

                HTTPPostData PostData = new HTTPPostData(g.m_Nome,g.m_Pagato ,g.m_id,g.m_Abbonato,g.m_Admin,g.m_mail ,1,m_environment.m_Utente);
                PostData.execute();
                accessWebService();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

      @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lista_giocatori, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_giocatore:
                Intent intent = new Intent(this, AddGiocatore.class);
                intent.putExtra("Environment",m_environment);
                this.startActivity(intent);
                break;
            case R.id.Aggiorna:
                accessWebService();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    public void onResume()
    {
        super.onResume();
        accessWebService();
    }

    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
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
            JSONArray jsonMainNode = jsonResponse.optJSONArray("and_Giocatori");


            m_environment.m_giocatori.clear() ;

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String nome = jsonChildNode.optString("Nome");
                String email = jsonChildNode.optString("email");
                Integer numero = jsonChildNode.optInt("Id");
                Integer CPagato =jsonChildNode.optInt("Pagato");
                Integer CFisso = jsonChildNode.optInt("Abbonato");
                Integer CPrivilegi = jsonChildNode.optInt("Privilegi") ;
                String pagato="";
                String abbonato="";
                m_environment.m_giocatori.add(new Giocatore(numero, nome,CPagato,i,CFisso,CPrivilegi,email ));


                if (CPagato == 0) {
                    pagato = "Non Pagato";
                }
                else{
                    pagato = "Pagato";
                }

                if(CFisso== 1){
                    abbonato ="Abbonato";
                }
                else {
                    abbonato ="Saltuario";
                }

                String outPut =  pagato +  " - " + abbonato;

                ListaGiocatori.add(CreaGiocatore( nome, outPut));
            }
        } catch (JSONException e) {
            //Toast.makeText(getApplicationContext(), "Error" + e.toString(),
            //        Toast.LENGTH_SHORT).show();
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ListaGiocatori,
                 android.R.layout.simple_list_item_2,
                new String[] { "Nome","Descrizione"}, new int[] { android.R.id.text1, android.R.id.text2 });

               listView.setAdapter(simpleAdapter);

    }



    private HashMap<String, String> CreaGiocatore(String nome, String descrizione) {
        HashMap<String, String> GiocatoreNo = new HashMap<String, String>();
        GiocatoreNo.put("Nome",nome);
        GiocatoreNo.put("Descrizione",descrizione );
        return GiocatoreNo;
    }
}

