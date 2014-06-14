package com.pallavolo;

import java.io.Serializable;

/**
 * Created by Callino on 17/09/13.
 */
public class Evento implements Serializable {

    Integer m_id;
    String m_Data;
    String m_Luogo;
    String m_Descrizione;
    Integer m_IdLista;

    public Evento(Integer id,String data,String luogo,Integer idLista, String descrizione) {
        this.m_id =id;
        this.m_Data=data;
        this.m_Luogo=luogo;
        this.m_IdLista=idLista;
        this.m_Descrizione= descrizione;

    }

    @Override
    public String toString() {
        return "Giocatore: "+ m_id+ " "+ m_Data + " "+ m_Luogo;
    }

}