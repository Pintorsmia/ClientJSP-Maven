package com.objets;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Trajet {
    private String DateDepart,DateArrive,Vehicule;
    private int Temps;

    public Trajet(String dateDepart, String dateArrive, int temps, String vehicule) throws ParseException {
        DateDepart = this.dateConvert(dateDepart);
        DateArrive =  this.dateConvert(dateArrive);
        Temps = temps;
        Vehicule = vehicule;
    }

    private String dateConvert (String dateInString) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
        Date date = formatter.parse(dateInString);
        String result = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(date);;
        return result;
    }
    @Override
    public String toString() {
        return  "Heure de départ :'" + DateDepart + '\'' +
                ", Heure d'arrivé :'" + DateArrive + '\'' +
                ", Durée du com.objets.Trajet :'" + Temps + '\'' +
                ", Moyen de transport :'" + Vehicule + '\'';
    }
}


