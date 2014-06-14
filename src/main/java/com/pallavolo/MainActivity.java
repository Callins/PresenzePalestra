package com.pallavolo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends Activity {
    private String jsonResult;
    private String url = "http://callino.net23.net/Login.php";
    public String m_IdUtente;
    Environment m_environment = new Environment() ;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        final HTTPPostLogin PostData = new HTTPPostLogin(telephonyManager.getDeviceId() ,m_environment);
        PostData.execute();


        final Intent AnagGiocatori = new Intent(this, ListaGiocatori.class);
        final Intent AggiungiEvento = new Intent(this, AggiungiEvento.class);
        final Intent AnagCal = new Intent(this, CalendarioEventi.class);


        final Button AnagraficaGiocatori = (Button) findViewById(R.id.btnGiocatori);
        final Button Cal = (Button) findViewById(R.id.btnCalendario);
        final Button NuovoEvento = (Button) findViewById(R.id.btnNuovadata);

        AnagraficaGiocatori.setBackgroundResource(R.drawable.pulsanteblu) ;
        Cal.setBackgroundResource(R.drawable.pulsanteverde ) ;
        NuovoEvento.setBackgroundResource(R.drawable.pulsanterosso) ;

        AnagraficaGiocatori.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Perform action on click */
                do
                {
                    //Wait
                    try {
                        PostData.get(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                while (m_environment.m_Utente <=0);
               AnagGiocatori.putExtra("Environment",m_environment);
                startActivity(AnagGiocatori);
            }
        });


        NuovoEvento.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Perform action on click */
                do
                {
                    //Wait
                    try {
                        PostData.get(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                while (m_environment.m_Utente <=0);
                AggiungiEvento.putExtra("Environment",m_environment);
                startActivity(AggiungiEvento );
            }
        });
        Cal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Perform action on click */
                do
                {
                    //Wait
                    try {
                        PostData.get(1000, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                while (m_environment.m_Utente <=0);
                AnagCal.putExtra("Environment",m_environment);
                startActivity(AnagCal );
            }
        });



    }







}
