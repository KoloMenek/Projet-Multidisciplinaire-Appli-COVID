package com.example.projetmultidisciplinaire_applicovid.modele;

import java.util.Date;

public class User {
    public String name;
    public String firstname
    public Date dateNaissance;
    public int numeroAdresse;
    public String voie;
    public int codePostal;
    public String ville;

    public User(String name, String firstname, Date dateNaissance, int numeroAdresse, String voie, int codePostal, String ville){
        this.name = name;
        this.firstname = firstname;
        this.dateNaissance = dateNaissance;
        this.numeroAdresse = numeroAdresse;
        this.voie = voie;
        this.codePostal = codePostal;
        this.ville = ville;
    }
}
