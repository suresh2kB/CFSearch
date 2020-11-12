package com.example.cfsearch;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    public QueryUtils() {

    }

    public static List<Code> fetchCodeData(String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Code> codes = extractFeatureFromJson(jsonResponse);
        return codes;
    }

    private static List<Code> extractFeatureFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        List<Code> code = new ArrayList<>();
        try {
            JSONObject baseJsonObject = new JSONObject(jsonResponse);
            JSONArray coderesultsArray = baseJsonObject.getJSONArray("result");
            Code code1 = null;
            for(int i=0;i<coderesultsArray.length();i++){
                JSONObject p = coderesultsArray.getJSONObject(i);
                String firstName;
                if(p.has("firstName")){
                    firstName = p.getString("firstName");
                }
                else{
                    firstName = "NA";
                }
                String lastName;
                if(p.has("lastName")){
                    lastName = p.getString("lastName");
                }
                else{
                    lastName = "NA";
                }
                String currentReting;
                if(p.has("rating")){
                    currentReting = p.getString("rating");
                }
                else{
                    currentReting = "NA";
                }
                String maxRating;
                if(p.has("maxRating")){
                    maxRating = p.getString("maxRating");
                }
                else{
                    maxRating = "NA";
                }
                long unix_secondsForOnline;
                String timeLastOnline;
                if(p.has("lastOnlineTimeSeconds")){
                    unix_secondsForOnline = p.getLong("lastOnlineTimeSeconds");
                    //long unix_seconds = 1372339860;
                    //convert seconds to milliseconds
                    Date date = new Date(unix_secondsForOnline*1000L);
                    // format of the date
                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                    timeLastOnline = jdf.format(date);
                }
                else{
                    timeLastOnline = "NA";
                }
                long unix_secondsForReg;
                String timeReg;
                if(p.has("registrationTimeSeconds")){
                    unix_secondsForReg = p.getLong("registrationTimeSeconds");
                    //long unix_seconds = 1372339860;
                    //convert seconds to milliseconds
                    Date date = new Date(unix_secondsForReg*1000L);
                    // format of the date
                    SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                    jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                    timeReg = jdf.format(date);
                }
                else{
                    timeReg = "NA";
                }
                String contribution = p.getString("contribution");
                String totalFriends = p.getString("friendOfCount");
                String currentRank;
                if(p.has("rank")){
                    currentRank = p.getString("rank");
                }
                else{
                    currentRank = "NA";
                }
                String MaxRank;
                if(p.has("maxRank")){
                    MaxRank = p.getString("maxRank");
                }
                else{
                    MaxRank = "NA";
                }
                code1 = new Code(firstName,lastName,currentReting,maxRating,timeLastOnline,timeReg,contribution,totalFriends,currentRank,MaxRank);
                code.add(code1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        }
        return code;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonresponse = "";
        if(url==null){
            return null;
        }
        HttpsURLConnection httpsURLConnection = null;
        InputStream inputStream = null;
        try {
            httpsURLConnection = (HttpsURLConnection)url.openConnection();
            httpsURLConnection.setReadTimeout(10000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.connect();
            if(httpsURLConnection.getResponseCode()==200){
                inputStream = httpsURLConnection.getInputStream();
                jsonresponse = readFromStream(inputStream);
            }
            else{
                Log.e("url",String.valueOf(url));
                Log.e(LOG_TAG,"Error Response code : "+httpsURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Problem retrieving the User JSON results.", e);
        }
        finally {
            if(httpsURLConnection!=null){
                httpsURLConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return  jsonresponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Problem building the URL ",e);
        }
        return url;
    }
}
