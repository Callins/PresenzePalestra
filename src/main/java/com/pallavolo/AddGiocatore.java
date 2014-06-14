package com.pallavolo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddGiocatore extends Activity {
    String Nome;
    Integer m_id =0;


    Environment m_environment ;
    Integer Pagato;
    Integer abbonato;
    Integer admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_giocatore);
        m_environment = (Environment)getIntent().getSerializableExtra("Environment");
        Intent intent=getIntent();

        String pkg=getPackageName();

        Giocatore p=(Giocatore)intent.getSerializableExtra(pkg+".Giocatore");

        if (p != null){
            TextView tvnome = (TextView) findViewById(R.id.txtNome);
            TextView tvid= (TextView) findViewById(R.id.txtLabel);
            TextView email = (TextView) findViewById(R.id.txtEmail);

            tvnome.setText(p.m_Nome);
            tvid.setText(p.m_id.toString());
            email .setText(p.m_mail );
            m_id=p.m_id;

            CheckBox chkpagato = (CheckBox) findViewById(R.id.chkPagato);
            CheckBox chkabbonato = (CheckBox) findViewById(R.id.chkAbbonato);
            CheckBox chkadmin = (CheckBox) findViewById(R.id.chkAdmin);

            if (p.m_Pagato == 1){

                chkpagato.setChecked(true);
            }
            if (p.m_Abbonato  == 1){

                chkabbonato.setChecked(true);
            }
            if (p.m_Admin == 1){

                chkadmin.setChecked(true);
            }
        }


        final Button button = (Button) findViewById(R.id.btnInsGioc);
        button.setBackgroundResource(R.drawable.pulsanteverde ) ;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                TextView tvnome = (TextView) findViewById(R.id.txtNome);
                TextView email = (TextView) findViewById(R.id.txtEmail);
                CheckBox chkpagato = (CheckBox) findViewById(R.id.chkPagato);
                CheckBox chkabbonato = (CheckBox) findViewById(R.id.chkAbbonato);
                CheckBox chkadmin = (CheckBox) findViewById(R.id.chkAdmin);

                Nome = tvnome.getText().toString() ;

                if (chkpagato.isChecked()){
                    Pagato =1;
                }
                else {
                    Pagato = 0;
                }
                if (chkabbonato.isChecked()){
                    abbonato =1;
                }
                else {
                    abbonato = 0;
                }

                if (chkadmin.isChecked()){
                    admin =1;
                }
                else {
                    admin = 0;
                }


                HTTPPostData PostData = new HTTPPostData(Nome,Pagato,m_id,abbonato,admin,email.getText().toString(),0,m_environment.m_Utente);
                PostData.execute();
                TextView txtLabel = (TextView) findViewById(R.id.txtLabel);
                txtLabel.setText(PostData.Result);
                finish() ;

            }


        });
    }

}