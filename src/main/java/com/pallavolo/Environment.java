package com.pallavolo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Callino on 28/10/13.
 */
public class Environment implements Serializable {

    ArrayList<Giocatore> m_giocatori = new ArrayList<Giocatore>();
    ArrayList<Evento> m_Eventi = new ArrayList<Evento>();
    Integer m_Utente;

}
