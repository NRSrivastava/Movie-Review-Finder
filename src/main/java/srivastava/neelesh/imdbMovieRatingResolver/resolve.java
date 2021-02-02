package srivastava.neelesh.imdbMovieRatingResolver;

import org.json.*;
import java.net.*;
import java.io.*;
import java.util.*;

public class resolve {
    public static void main(String[]args) throws Exception{
        try{
        System.out.println("Enter the name of the Movie/Series");
        Scanner sc=new Scanner(System.in);
        
        String name=sc.nextLine();
        if(name.length()==0){
            System.out.println("You have not entered anything");
            return;
        }
        
        String url = "https://sg.media-imdb.com/suggests/"+name.charAt(0)+"/"+name+".json";
        
        String response=getRawString(url);
        
        String jsonString=response.substring(response.indexOf("(")+1,response.lastIndexOf(")"));
        
        JSONObject json = new JSONObject(jsonString);

        JSONArray arr = json.getJSONArray("d");
        
        int index;
        if(arr.length()<=0){
            System.out.println("No movie found");
            return;
        }
        else if(arr.length()==1){
            index=0;
        }
        else{
            System.out.println("\nPlease choose your movie:\n");
            for (int i = 0; i < arr.length(); i++)
            {
                JSONObject j=arr.getJSONObject(i);
                System.out.println((i+1)+"."+
                        (j.has("l")?(" "+j.getString("l")):"")+
                        (j.has("y")?("  "+j.getInt("y")):"")+
                        (j.has("s")?("  featuring "+j.getString("s")):"")
                );
            }
            System.out.println("\nEnter the number of your choice");
            index=sc.nextInt()-1;
        }
        
        String movieID=arr.getJSONObject(index).getString("id");
        
        System.out.println("It may take a moment to load due to bandwidth limitation on IMDb-API's end . . .\n\nRatings:");
        
        url = "https://imdb-api.com/en/API/Ratings/k_loj126o8/"+movieID; //It has my api-key, please generate your own api key on https://imdb-api.com for your projects.
        
        response=getRawString(url);
        
        
        //System.out.println(response);
        
        JSONObject json2 = new JSONObject(response);
        String imdb = json2.getString("imDb");
        String metacritic=json2.getString("metacritic");
        String tmdb=json2.getString("theMovieDb");
        String rT=json2.getString("rottenTomatoes");
        String tc=json2.getString("tV_com");
        String fA=json2.getString("filmAffinity");
        
        System.out.println(qC("IMDb",imdb)+qC("MetaCritic",metacritic)+qC("TMDb",tmdb)+qC("Rotten Tomatoes",rT)+qC("TV.com",tc)+qC("Film Affinity",fA));
        }
        catch(Exception e){
            System.out.println("Unable to find your movie");
        }
    }
    static String qC(String a,String b){//quick check
        if(b.length()>0)
            return a+": "+b+"\n";
        else
            return "";
    }
    static String getRawString(String url) throws Exception{
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        //int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'GET' request to URL : " + url);
        //System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
           response.append(inputLine);
        }
        in.close();
        return response.toString();
    }
}
