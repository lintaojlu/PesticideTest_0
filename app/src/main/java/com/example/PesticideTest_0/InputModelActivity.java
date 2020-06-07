package com.example.PesticideTest_0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.PesticideTest_0.controller.NetController;

public class InputModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_model);
        final EditText model_name = findViewById(R.id.model_name);
        Button bt_sure = findViewById(R.id.bt_sure);

        NetController controller = new NetController(InputModelActivity.this);
        final SharedPreferences sp = InputModelActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = sp.getString("username", null);

        String cmd_str = "cmd=5&userName=" + username;
        controller.send(cmd_str);
        //先联网获取模型名字存入本地，再从本地取出,有延迟，莫得办法
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        String string = sp.getString("all_models_names", "未获得模型");
        TextView all_models = findViewById(R.id.all_models);
        all_models.setText(string);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cmd = "cmd=6" +
                        "&userName=" + username +
                        "&modelName=" + model_name.getText();
                NetController controller1 = new NetController(InputModelActivity.this);
                controller1.send(cmd);
                //Complete and destroy activity once successful
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}