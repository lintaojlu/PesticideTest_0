package com.example.PesticideTest_0.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.PesticideTest_0.IndexActivity;
import com.example.PesticideTest_0.drawing.CanvasView;
import com.example.PesticideTest_0.model.Net;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class NetController {
    private static Context context;

    public NetController(Context c) {
        this.context = c;
    }

    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String json_str = msg.obj.toString();
            Log.e("handler", json_str);
            try {
                JSONObject jsonObject = new JSONObject(json_str);
                String cmd = jsonObject.getString("cmd");
                String code = jsonObject.getString("code");
                //用户名密码保存到shared preference
                SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                String username;
                String password;
                //返回成功通过cmd判断各种操作
                if (code.equals("0")) {
                    Intent intent;
                    switch (cmd) {
                        //注册
                        case "1":
                            intent = new Intent(context, IndexActivity.class);
                            context.startActivity(intent);
                            username = jsonObject.getJSONObject("data").getString("username");
                            password = jsonObject.getJSONObject("data").getString("password");
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();
                            Toast.makeText(context, "注册成功", Toast.LENGTH_LONG).show();
                            break;
                        //登陆
                        case "2":
                            intent = new Intent(context, IndexActivity.class);
                            context.startActivity(intent);
                            username = jsonObject.getJSONObject("data").getString("username");
                            password = jsonObject.getJSONObject("data").getString("password");
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();
                            Log.e("username",username);
                            Toast.makeText(context, "登陆成功", Toast.LENGTH_LONG).show();
                            break;
                        //修改密码
                        case "3":
                            username = jsonObject.getJSONObject("data").getString("username");
                            password = jsonObject.getJSONObject("data").getString("password");
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();
                            Toast.makeText(context, "修改成功", Toast.LENGTH_LONG).show();
                            break;
                        //新建模型
                        case "4":
                            Toast.makeText(context, "新建模型成功", Toast.LENGTH_LONG).show();
                            break;
                        //获取全部模型名字
                        case "5":
                            String all_models = jsonObject.getString("msg");
                            editor.putString("all_models_names", all_models);
                            editor.commit();
                            break;
                        //导入模型参数
                        case "6":
                            float k = Float.parseFloat(jsonObject.getJSONObject("data").getString("modelSlope")),
                                    b = Float.parseFloat(jsonObject.getJSONObject("data").getString("modelIntercept")),
                                    boundary = Float.parseFloat(jsonObject.getJSONObject("data").getString("modelBoundary"));
                            CanvasView.changeK(k);
                            CanvasView.changeB(b);
                            CanvasView.changeBoundary(boundary);
                            Toast.makeText(context, "导入模型成功", Toast.LENGTH_LONG).show();
                            break;
                        //删除模型
                        case "7":
                            Toast.makeText(context, "删除模型成功", Toast.LENGTH_LONG).show();
                    }
                } else {
                    switch (cmd) {
                        //注册失败
                        case "1":
                            Toast.makeText(context, "注册失败，用户已存在", Toast.LENGTH_LONG).show();
                            break;
                        //登陆失败
                        case "2":
                            Toast.makeText(context, "登陆失败,用户名或密码错误", Toast.LENGTH_LONG).show();
                            break;
                        //其他
                        default:
                            Toast.makeText(context, "操作失败", Toast.LENGTH_LONG).show();

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void send(String cmd_str) {
        Log.e("send",cmd_str);
        Net.send(cmd_str);
    }
}
