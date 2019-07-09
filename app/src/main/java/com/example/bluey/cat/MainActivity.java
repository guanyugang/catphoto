package com.example.bluey.cat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1=(ImageView) findViewById(R.id.img1);
        imageView2=(ImageView) findViewById(R.id.img2);
        imageView3=(ImageView) findViewById(R.id.img3);
        imageView4=(ImageView) findViewById(R.id.img4);
        imageView5=(ImageView) findViewById(R.id.img5);
    }

    public void onClick(View view){
        if(view.getId()==R.id.get){
            String web="https://api.thecatapi.com/v1/images/search";
            getweb(web,imageView1);
            getweb(web,imageView2);
            getweb(web,imageView3);
            getweb(web,imageView4);
            getweb(web,imageView5);
        }
    }

    public void getweb(final String web, final ImageView imageView){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(web);
                    HttpURLConnection connection;
                    connection=(HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(2000);
                    int requesetCode=connection.getResponseCode();
                    if(requesetCode==200){
                        InputStream inputStream=connection.getInputStream();
                        StringBuffer stringBuffer=new StringBuffer();
                        String line;
                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                        while((line=bufferedReader.readLine())!=null){
                            stringBuffer.append(line);
                        }

                        String JSONObject=parseJSONWithJSONObject(stringBuffer.toString());
                        HttpURLConnection httpURLConnection=(HttpURLConnection) new URL(JSONObject).openConnection();
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setConnectTimeout(2000);
                        int requesetCode_JSON=httpURLConnection.getResponseCode();
                        if(requesetCode_JSON==200){
                            InputStream inputStream1=httpURLConnection.getInputStream();
                            Bitmap bitmap= BitmapFactory.decodeStream(inputStream1);
                            setImamge(bitmap,imageView);
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


    public String parseJSONWithJSONObject(String jsonData){
        try {
            JSONArray jsonArray=new JSONArray(jsonData);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String url=jsonObject.getString("url");
                return url;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setImamge(final Bitmap bitmap, final ImageView imageView){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }
}
