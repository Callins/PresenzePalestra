package com.pallavolo;

import java.io.Serializable;

/**
 * Created by callino on 12/09/13.
 */
public class Giocatore implements Serializable {

        Integer m_id;
        String m_Nome;
        String m_mail;
        Integer m_Pagato;
        Integer m_IdLista;
        Integer m_Admin;
        Integer m_Abbonato;


        public Giocatore(Integer id,String nome,Integer Pagato,Integer idLista, Integer abbonato, Integer admin, String email) {
            this.m_id =id;
            this.m_Nome=nome;
            this.m_Pagato=Pagato;
            this.m_IdLista=idLista;
            this.m_Admin= admin;
            this.m_Abbonato= abbonato;
            this.m_mail = email;
        }

        @Override
        public String toString() {
            return "Giocatore: "+ m_id+ " "+ m_Nome + " "+ m_Pagato;
        }

    }

