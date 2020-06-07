package com.example.PesticideTest_0.model;

import android.os.Looper;
import android.os.Message;

import com.example.PesticideTest_0.controller.NetController;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Net {
    public static void send(final String cmd_str){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //define url
                String url_str = "http://45.76.188.45:8080/androidTestLogin/servlet/DealCmd?"+ cmd_str;
                //open url
                try {
                    URL url = new URL(url_str);
                    try {
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        //response based on the result
                        if(connection.getResponseCode()==200){
                            InputStream inputStream = connection.getInputStream();
                            byte[] buff = new byte[1024];
                            int len=-1;
                            StringBuilder stringBuilder = new StringBuilder();
                            while((len=inputStream.read(buff))!=-1){
                                stringBuilder.append(new String(buff,0,len,"utf-8"));
                            }
                            String res_str = stringBuilder.toString();
                            //call back method
                            Message message = new Message();
                            message.obj=res_str;
                            Looper.prepare();//新线程中调用handler必须用looper包裹
                            NetController.handler.handleMessage(message);
                            Looper.loop();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
