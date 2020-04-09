package com.servlets;

import calcul.DistanceService;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import com.objets.Trajet;


@WebServlet(
        name = "ServletTrain",
        urlPatterns = {"/ServletTrain"}
)
public class ServletTrain extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //Methode pour arrondir
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        //Token pour api SNCF
        String TokenSNCF = "7d5b4417-f093-461b-b50b-b95d06cc06af";
        String devise = request.getParameter("devise");
        String villeDpt = request.getParameter("villeDpt");
        String villeDst = request.getParameter("villeDst");

        //Récupération des coordonées avec la fonction juste en dessous (utilisation de l'api SNCF)
        double [] coordDpt = this.getCoord(villeDpt,out);
        double [] coordDst = this.getCoord(villeDst,out);
        double latA = coordDpt[0];
        double lonA = coordDpt[1];
        double latB = coordDst[0];
        double lonB = coordDst[1];

        //Calcul de la distance avec soap
        calcul.Distance service = new DistanceService().getDistancePort();
        double distance = service.distance(latA,lonA,latB,lonB);
        out.print("Resultat = "+df.format(distance)+" km");

        //Requetes REST vers mon services de calcul des prix

        if (distance !=0) {

            StringBuilder SB = new StringBuilder();
            URL url = new URL("https://api-py-train-bourget.herokuapp.com/API/calcul/" + distance + "/" + devise);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line, toprint;
            while ((line = rd.readLine()) != null) {
                SB.append(line);
            }
            rd.close();
            JSONObject json = new JSONObject(SB.toString());
            System.out.println(json);
            //verif qu'il y'a une gare dans la ville indique
            if (json.getInt("message") == 0) {
                double prixAPI = Double.parseDouble(json.get("prix").toString());
                String deviseAPI = json.get("devise").toString();
                toprint = " Prix : " + prixAPI + " " + deviseAPI;
            }else{
                toprint = " Erreur avec l'api calcul prix";
            }
            //out.write(df.format(SB.toString()));
            out.write(toprint);
            //Il faut modifier le service afin qu'il fasse l'arrondi avant de l'envoyer

        }

        //Partie recuperation des trajets
        //On recupere les UIC des 2 villes
        int UICA = this.getUIC(villeDpt);
        int UICB = this.getUIC(villeDst);
        //On recupere les 5 prochains trajets dans un arraylist
        ArrayList Trajets = this.getTrajet(UICA,UICB,TokenSNCF);
        Iterator it = Trajets.iterator();
        String affichage = "";
        while (it.hasNext()){
            com.objets.Trajet tmptrajet = (com.objets.Trajet) it.next();
            String message = tmptrajet.toString();
            affichage += "<br>" + message;
        }
        out.write(affichage);
        /*
        if(pass.equals("pass"))
        {
            RequestDispatcher rd=request.getRequestDispatcher("servlet2");
            rd.forward(request, response);
        }
        else
        {
            out.print("Sorry UserName or Password Error!");
            RequestDispatcher rd=request.getRequestDispatcher("/index.jsp");
            rd.include(request, response);

         */
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    public double[] getCoord(String ville, PrintWriter out) throws IOException {

        StringBuilder result = new StringBuilder();
        double[] coords = new double[2];
        URL url = new URL("https://data.sncf.com/api/records/1.0/search//?dataset=referentiel-gares-voyageurs&q="+ville+"&lang=FR");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream in = new BufferedInputStream(conn.getInputStream());
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()) {
            result.append(line);
        }
        in.close();
        r.close();
        JSONObject json = new JSONObject(result.toString());
        System.out.println(json);
        //verif qu'il y'a une gare dans la ville indique
        if (json.getInt("nhits") > 0) {

            double coordA = Double.parseDouble(json.getJSONArray("records").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString());
            double coordB = Double.parseDouble(json.getJSONArray("records").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString());
            coords[1] = coordA;
            coords[0] = coordB;


        } else {
            System.out.println("Pas de gares dans cette ville");
        }
        System.out.println(Arrays.toString(coords));
        conn.disconnect();
        return coords;

    }
    public int getUIC(String ville) throws IOException {
        StringBuilder result = new StringBuilder();
        int UIC = -1;
        URL url = new URL("https://data.sncf.com/api/records/1.0/search//?dataset=referentiel-gares-voyageurs&q=" + ville + "&lang=FR");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                result.append(line);
            }
            in.close();
            JSONObject json = new JSONObject(result.toString());
            System.out.println(json);
            //verif qu'il y'a une gare dans la ville indique
            if (json.getInt("nhits") > 0) {
                UIC = Integer.parseInt(json.getJSONArray("records").getJSONObject(0).getJSONObject("fields").get("pltf_uic_code").toString());
                System.out.println(UIC);
            } else {
                System.out.println("Pas de gares dans cette ville");
            }
        } finally {
            conn.disconnect();
            return UIC;
        }
    }
    public ArrayList getTrajet (int UICA, int UICB, String token) throws IOException {
        StringBuilder result = new StringBuilder();
        ArrayList Trajets = new ArrayList();

        URL url = new URL("https://api.sncf.com/v1/coverage/sncf/journeys?from=stop_area:OCE:SA:"+UICA+"&to=stop_area:OCE:SA:"+UICB+"&min_nb_journeys=5&key="+token);
        System.out.println(url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        /*int responseCode = conn.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);*/
        try {
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in), 1000);
            for (String line = r.readLine(); line != null; line = r.readLine()) {
                result.append(line);
            }
            in.close();
            JSONObject json = new JSONObject(result.toString());
            System.out.println(json);
            System.out.println((String) json.getJSONArray("journeys").getJSONObject(0).get("departure_date_time"));
            System.out.println(json.getJSONArray("journeys").length());
            //verif qu'il y'a au moins 1 voyage
            if (json.getJSONArray("journeys").length() > 2) {
                for (int nbvoyage = 0; nbvoyage < json.getJSONArray("journeys").length() ; nbvoyage++) {
                    System.out.println("journey " + nbvoyage);
                    Trajets.add(new Trajet(
                            (String) json.getJSONArray("journeys").getJSONObject(nbvoyage).get("departure_date_time"),
                            (String) json.getJSONArray("journeys").getJSONObject(nbvoyage).get("arrival_date_time"),
                            Integer.parseInt(json.getJSONArray("journeys").getJSONObject(nbvoyage).get("duration").toString()),
                            (String) json.getJSONArray("journeys").getJSONObject(nbvoyage).getJSONArray("sections").getJSONObject(1).getJSONObject("display_informations").get("physical_mode")
                    ));

                }
            } else {
                System.out.println("Pas de voyage disponible");
            }
        } finally {
            conn.disconnect();
            // System.out.println(Trajets[0].toString());
            return Trajets;
        }
    }
}
