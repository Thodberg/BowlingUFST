/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;


//import org.testng.annotations.Test;

    //@Test
 
/**
 *
 * @author sq
 */
public class BowlingMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // test af algoritme
        BowlingMain bowlingmain = new BowlingMain();
        int[][] result = {{10,0},{5,5},{4,5},{10,0},{2,6},{3,7},{10,0},{10,0},{10,0},{10,0}};
        bowlingmain.bowlingScore(result);
        bowlingmain.webTest();

    }
    
    public boolean webTest() {
        BowlingMain bowlingmain = new BowlingMain();
        HttpResponse <String> response;
       try {
            response = bowlingmain.getResult();
            System.out.println("Status Code: " + response.statusCode());
            System.out.println("Responsebody: " + response.body());
            System.out.println("Response: " + response );
            System.out.println("Headers: " + response);
            int[][] result2 = this.getIntArrayFromResponsBody(response.body());
            int[] score = this.bowlingScore(result2);
            String token = this.getTokenFromResponsBody(response.body());
            String post = this.makeStringForPost(score, token);
            boolean succes = this.PostAndGetValidationOfScore(post);
            return succes;
       } catch(Exception e) {
           e.printStackTrace();
           return false;
       }
        
    }
    
    public HttpResponse <String> getResult() throws IOException, InterruptedException {

     String getEndpoint;
        getEndpoint = "http://13.74.31.101/api/points";

     HttpRequest request = HttpRequest.newBuilder()
         .uri(URI.create(getEndpoint))
         .GET()
         .build();
     
     HttpClient client = HttpClient.newHttpClient();

     HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
     return response;
    }
    
    public boolean PostAndGetValidationOfScore(String inputJson) throws IOException, InterruptedException {
 
        String postEndpoint = "http://13.74.31.101/api/points";
 
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(postEndpoint))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(inputJson))
            .build();
 
        System.out.println("Body: " + request.bodyPublisher().toString());
        HttpClient client = HttpClient.newHttpClient();
 
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
 
        System.out.println("StatusCode: " + response.statusCode());
        String body = response.body().toString();
        System.out.println("Body: " + body);
        String test = body.substring(1, body.indexOf(","));
        System.out.println("Test: " + test);
        boolean succes;
        if(test.equals("\"success\":true")) succes = true;
        else succes = false;
        System.out.println("Succes: " + succes);
        return succes;
 
    }
    
    public int[] bowlingScore(int[][] result) {
        
        int antalRuder = result.length;
        int[] totalScore = new int[result.length];
        int[] rudeScore = new int[antalRuder];
        String[] rudeType = new String[antalRuder]; 
        int sumTotal = 0;
        
        for(int i = 0; i < antalRuder; i++) {
            
            int sumScore =result[i][0] + result[i][1];            
            
            if(result[i][0] == 10) rudeType[i] = "Strike";
            else if(sumScore == 10) rudeType[i] = "Spare";
            else rudeType[i] = "Normal";
            
            // Bonus for strike eller spare beregnes
            if(sumScore == 10 && i < antalRuder - 1) {
                sumScore = sumScore + result[i+1][0];
                
                // Bonus for strike beregnes
                if(result[i][0] == 10) {
                     if(result[i+1][0] != 10) {
                        sumScore = sumScore + result[i+1][1];
                    }
                    else if(i < antalRuder - 2) {
                        sumScore = sumScore + result[i+2][0];
                    }
                }
            }
            rudeScore[i] = sumScore;
            sumTotal = sumTotal + sumScore;
            totalScore[i] = sumTotal;
        }
        
        System.out.println();
        for(int i = 0; i< antalRuder; i++) {
            System.out.println("Rude" + i + " " + result[i][0] + " " + result[i][1] + "  "
                    + rudeScore[i] + " " +totalScore[i] + " " + rudeType[i]);
        }
        return totalScore;
    }
    
    int[][] getIntArrayFromResponsBody(String body) {        
        body = body.replace("]", "");
        body = body.replace("[", "");
        body = body.substring(body.indexOf(":")+1);
        String[] Array = body.split(",");
        int[][] value = new int[Array.length/2][2];
        for(int i = 0; i < value.length; i++) {
            //System.out.println(Array[i*2]);
            //System.out.println(Array[i*2+1]);
            value[i][0] = Integer.parseInt(Array[i*2]);
            value[i][1] = Integer.parseInt(Array[i*2+1]);
        }
        return value;
    }
    
    String getTokenFromResponsBody(String body) {
        body = body.substring(body.lastIndexOf(":")-7, body.length()-1);
        System.out.println("Token: " + body);
        return body;
    }
    
    String makeStringForPost(int[] score, String token) {
        //score[0] = score[0] + 10;
        String post = "";
        post = "{"+token+ ",\"points\":[";        
        for(int i = 0; i < score.length; i++) {
            if(i == 0) post = post + score[i];
            else post = post = post + "," + score[i];
        }  
        post = post + "]}";
        System.out.println("Post: " +post);
        return post;
    }

    

  /** private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
  }*/
  
    
}
