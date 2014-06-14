package com.pallavolo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

/**
 * Created by Callino on 17/09/13.
 */
public class CalendarioEventi extends Activity {

    Environment m_environment ;
    private String jsonResult;
    private String url;
    private ListView listView;
    private long m_id ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cal_eventi);
        m_environment = (Environment)getIntent().getSerializableExtra("Environment");
        listView  = (ListView) findViewById(R.id.listEventi);


        registerForContextMenu(listView);

        url = "http://callino.net23.net/anageventi.php?Id=" + m_environment.m_Utente;
        accessWebService();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                m_id = id;
                openContextMenu(listView);
            }

        });

            }

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                super.onCreateContextMenu(menu, v, menuInfo);
                if (v.getId() == R.id.listEventi) {
                    MenuInflater inflater = getMenuInflater();
                    inflater.inflate(R.menu.menu_evento, menu);
                }
            }


            @Override
            public boolean onContextItemSelected(MenuItem item) {

                final Intent VisualizzaDett = new Intent(getApplicationContext(), VisPartecipanti.class);

                String pkg = getPackageName();
                Evento g = null;

                switch (item.getItemId()) {

                    case R.id.ApriDettaglio:
                        // edit stuff here
                        g = m_environment.m_Eventi.get((int) m_id);
                        VisualizzaDett.putExtra("Environment", m_environment);
                        VisualizzaDett.putExtra(pkg + ".IdEvento", g);
                        startActivity(VisualizzaDett);
                        return true;


                    case R.id.delete:
                        // remove stuff here
                        g = m_environment.m_Eventi.get((int) m_id);

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
                switch (item.getItemId()) {
                    case R.id.add_giocatore:
                        Intent intent = new Intent(this, AddGiocatore.class);
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

            public void onResume() {
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
                    } catch (ClientProtocolException e) {
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
                    } catch (IOException e) {
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
                task.execute(new String[]{url});
            }

            // build hash set for list view
            public void ListDrwaer() {
                List<Map<String, String>> ListaEventi = new ArrayList<Map<String, String>>();

                try {

                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("and_Eventi");

                    m_environment.m_Eventi.clear();

                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        Integer id = jsonChildNode.optInt("Id");
                        String data = jsonChildNode.optString("Data");
                        String luogo = jsonChildNode.optString("Luogo");
                        String descrizione = jsonChildNode.optString("Descrizione");
                        m_environment.m_Eventi.add(new Evento(id, data, luogo, i, descrizione));


                        String outPut = data + " - " + luogo ;

                        ListaEventi.add(CreaGiocatore(descrizione , outPut));
                    }
                } catch (JSONException e) {
                    //Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                    //        Toast.LENGTH_SHORT).show();
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(this, ListaEventi,
                        android.R.layout.simple_list_item_1,
                        new String[]{"Descrizione","Dettagli"}, new int[]{android.R.id.text1, android.R.id.text2 });

                listView.setAdapter(simpleAdapter);

            }

            private HashMap<String, String> CreaGiocatore(String Descrizione, String Dettagli) {
                HashMap<String, String> Evento = new HashMap<String, String>();
                Evento.put("Descrizione", Descrizione);
                Evento.put("Dettagli", Dettagli);

                return Evento;
            }


        }