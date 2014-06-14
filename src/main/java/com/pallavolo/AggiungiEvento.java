package com.pallavolo;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class AggiungiEvento extends Activity {
    Environment m_environment ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_evento);
        m_environment = (Environment)getIntent().getSerializableExtra("Environment");
        final Calendar c = Calendar.getInstance();

        final DatePicker data = (DatePicker) findViewById(R.id.datePicker ) ;

        Button btnAddEvento = (Button) findViewById(R.id.btnAddEvento);
        btnAddEvento.setBackgroundResource(R.drawable.pulsanteverde);
        final TextView Luogo = (TextView) findViewById(R.id.txtLuogo);
        final TextView Descrizione = (TextView) findViewById(R.id.txtDescEvento);

        data.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        btnAddEvento.setOnClickListener (new View.OnClickListener() {
            public void onClick(View v) {
                /* Perform action on click */
                String  dataa =  DateFormat.format("yyyy-MM-dd", data.getCalendarView().getDate()).toString();
                HTTPPostEvento PostData = new HTTPPostEvento(dataa,Luogo.getText().toString(),Descrizione.getText().toString() ,0 , m_environment.m_Utente );
                PostData.execute();
                finish();
            }
        });


    }


}
