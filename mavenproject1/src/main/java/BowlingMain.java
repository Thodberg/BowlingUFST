/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
 
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
        int[][] result = {{10,0},{5,5},{4,5},{10,0},{2,6},{3,7},{10,0},{10,0},{10,0}};
        bowlingmain.bowlingScore(result);
        bowlingmain.webTest();

    }
    
    /**
     * tester beregning af score ved at hente og sende en  gang via endpoints
     * @return 
     */
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
    
    /**
     * henter udfald af spil via endpoints
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
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
    
    /**
     * sender udregning af score med henblik på validering
     * @param inputJson
     * @return
     * @throws IOException
     * @throws InterruptedException 
     */
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
    
    /**
     * beregner score for spil
     * @param result
     * @return 
     */
    public int[] bowlingScore(int[][] result) {
        
        int antalRuder = result.length;
        int antalRuderUdenBonusSlag = antalRuder;
        
        // fuldt spil med bonusSlag
        if(antalRuder == 11)antalRuderUdenBonusSlag = 10;        
        
        int[] totalScore = new int[antalRuderUdenBonusSlag];
        int[] rudeScore = new int[antalRuderUdenBonusSlag];
        String[] rudeType = new String[antalRuder]; 
        int sumTotal = 0;
        
        for(int i = 0; i < antalRuder; i++) {
            
            int sumScore =result[i][0] + result[i][1];  
            
            if(i==9) {
                System.out.println();
            }
            
            if(i==10) {
              rudeType[i] = "Bonus";  
              break;
            }
            else if(result[i][0] == 10) rudeType[i] = "Strike";
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
                    } else if (i == 9) {
                        sumScore = sumScore + result[i+1][1];
                    }
                }
            }
            rudeScore[i] = sumScore;
            sumTotal = sumTotal + sumScore;
            totalScore[i] = sumTotal;
        }
        
        System.out.println();
        for(int i = 0; i< antalRuder; i++) {
            if(i<=9) {
                System.out.println("Rude" + i + " " + result[i][0] + " " + result[i][1] + "  "
                        + rudeScore[i] + " " +totalScore[i] + " " + rudeType[i]);
            } else {
                System.out.println("Rude" + i + " " + result[i][0] + " " + result[i][1] + "  "
                         + rudeType[i]);
            }
        }
        return totalScore;
    }
    
    /**
     * ekstraherer int array fra responsbody
     * @param body
     * @return 
     */
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
    
    /**
     * ekstraherer token fra responsbody
     * @param body
     * @return 
     */
    String getTokenFromResponsBody(String body) {
        body = body.substring(body.lastIndexOf(":")-7, body.length()-1);
        System.out.println("Token: " + body);
        return body;
    }
    
    /**
     * laver string til post udfra token og array med score
     * @param score
     * @param token
     * @return 
     */
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

}
