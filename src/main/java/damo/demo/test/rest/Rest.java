package damo.demo.test.rest;

import damo.demo.test.Config;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Rest {
    public Response doPost(String url, String payload, String contentType, Map<String,String> additionalHeaders) throws IOException {
        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();
        //add reuqest header
        httpClient.setRequestMethod("POST");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        if(additionalHeaders!=null) {
            for(int i=0; i<additionalHeaders.size(); i++){
                httpClient.setRequestProperty(((String)additionalHeaders.keySet().toArray()[i]), ((String)additionalHeaders.values().toArray()[i]));
            }
        }
        httpClient.setRequestProperty("Content-Type", contentType);

        // Send post request
        httpClient.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(httpClient.getOutputStream());

        if(Config.getConfig().getVerboseResults()) {
            System.out.println("post payload: " + payload);
        }
        wr.writeBytes(payload);
        wr.flush();
        int responseCode = httpClient.getResponseCode();

        if(Config.getConfig().getVerboseResults()) {
            System.out.println("Sending 'POST' request to URL : " + url);
            System.out.println("Request method: " + httpClient.getRequestMethod());

            System.out.println("Headers:");
            Iterator keys = httpClient.getHeaderFields().keySet().iterator();
            Iterator values = httpClient.getHeaderFields().values().iterator();
            while(keys.hasNext()){
                System.out.println(keys.next() + ":" + values.next());
            }
        }

        Map<String, List<String>> headers = null;
        try {
            headers = httpClient.getHeaderFields();
        } catch (Exception e){
            System.out.println("Caught exception: " + e);
            e.printStackTrace();
        }
        Response response;
        try {
            StringBuffer errorContent = new StringBuffer();
            if (httpClient.getErrorStream() != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpClient.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    errorContent.append(inputLine);
                }
                in.close();
                response = new Response(responseCode, errorContent.toString(), headers);
            } else if (httpClient.getInputStream() != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpClient.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                response = new Response(responseCode, content.toString(), headers);
            } else {
                response = new Response(responseCode, null, headers);
            }
        }catch (IOException e){
            response = new Response(responseCode, null, headers);
        }

        return response;
    }
    public Response doPost(String url, String payload, Map<String,String> additionalHeaders) throws IOException {
        return doPost(url, payload, "application/json; utf-8", additionalHeaders);
    }
    public Response doPost(String url, String payload) throws IOException {
        return doPost(url, payload, "application/json; utf-8", null);
    }
    public Response doGet(String url) throws IOException {
        return doGet(url, "");
    }
    public Response doGet(String url, String range) throws IOException {
        HttpURLConnection httpClient = (HttpURLConnection) new URL(url).openConnection();

        //add reuqest header
        httpClient.setRequestMethod("GET");
        httpClient.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpClient.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpClient.setRequestProperty("Content-Type", "application/json; utf-8");

        if(range!=null && !range.equals("")) {
            httpClient.setRequestProperty("Range", range);
        }

        if(Config.getConfig().getVerboseResults()) {
            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Request method: " + httpClient.getRequestMethod());

            System.out.println("Headers:");
            Iterator keys = httpClient.getHeaderFields().keySet().iterator();
            Iterator values = httpClient.getHeaderFields().values().iterator();
            while(keys.hasNext()){
                System.out.println(keys.next() + ":" + values.next());
            }
        }
        int responseCode = httpClient.getResponseCode();

        Map<String, List<String>> headers = httpClient.getHeaderFields();

        StringBuffer errorContent = new StringBuffer();
        Response response;
        if(httpClient.getErrorStream()!=null) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpClient.getErrorStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                errorContent.append(inputLine);
            }
            in.close();
            response = new Response(responseCode, errorContent.toString(), headers);
        } else {
            try{
                if(httpClient!=null) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(httpClient.getInputStream()));
                    String inputLine;
                    StringBuffer content = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    in.close();
                    response = new Response(responseCode, content.toString(), headers);
                } else {
                    response = new Response(responseCode, "", headers);
                }
            } catch (IOException e){response = new Response(responseCode, "", headers);}
        }

        return response;
    }

    public class Response {
        public String responseContent;
        public int responseCode;
        public Map<String, List<String>> headers;
        public Response(int responseCode, String responseContent, Map<String, List<String>> headers) {
            this.responseCode = responseCode;
            this.responseContent = responseContent;
            this.headers = headers;
        }

    }
}
