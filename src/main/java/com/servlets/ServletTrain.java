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
import java.util.Iterator;
import com.objets.Trajet;


@WebServlet(
        name = "ServletTrain",
        urlPatterns = {"/ServletTrain"}
)
public class ServletTrain extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //Methode pour arrondir
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        // Recuperation des parametres
        /*
        Double latA = Double.parseDouble(request.getParameter("latA"));
        Double lonA= Double.parseDouble(request.getParameter("lonA"));
        Double latB = Double.parseDouble(request.getParameter("latB"));
        Double lonB = Double.parseDouble(request.getParameter("lonB"));
        */
        //Token pour api SNCF
        String TokenSNCF = "7d5b4417-f093-461b-b50b-b95d06cc06af";
        String devise = request.getParameter("devise");
        String villeDpt = request.getParameter("villeDpt");
        String villeDst = request.getParameter("villeDst");


        double [] coordDpt = this.getCoord(villeDpt,out);
        double [] coordDst = this.getCoord(villeDst,out);
        double latA = coordDpt[0];
        double lonA = coordDpt[1];
        double latB = coordDst[0];
        double lonB = coordDst[1];

        calcul.Distance service = new DistanceService().getDistancePort();
        double distance = service.distance(latA,lonA,latB,lonB);



        // out.print(latA+"  " + lonA + "   " + latB + "    " + lonB);
        out.print("Resultat = "+df.format((double) distance)+" km");

        //Requetes REST vers mon services de calcul des prix
/*
        if (distance !=0) {

            StringBuilder SB = new StringBuilder();
            URL url = new URL("http://127.0.0.1:8080/SrvPrix2_war/API/calcul/" + distance + "," + devise);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;
            while ((line = rd.readLine()) != null) {
                SB.append(line);
            }
            rd.close();
            //out.write(df.format(SB.toString()));
            out.write(" " + SB.toString());
            //Il faut modifier le service afin qu'il fasse l'arrondi avant de l'envoyer

        }

*/


/*


        //Partie recuperation des trajets
        //On recupere les UIC des 2 villes
        int UICA = this.getUIC(villeDpt);
        int UICB = this.getUIC(villeDst);
        //On recupere les 5 prochains trajets dans un arraylist
        ArrayList Trajets = this.getTrajet(UICA,UICB,TokenSNCF);
        Iterator it = Trajets.iterator();
        String affichage = new String();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

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

            double coordA = Double.valueOf(json.getJSONArray("records").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").get(0).toString());
            double coordB = Double.valueOf(json.getJSONArray("records").getJSONObject(0).getJSONObject("geometry").getJSONArray("coordinates").get(1).toString());
            coords[1] = coordA;
            coords[0] = coordB;


        } else {
            System.out.println("Pas de gares dans cette ville");
        }
        System.out.println(coords);
        conn.disconnect();
        return coords;

    }
}
