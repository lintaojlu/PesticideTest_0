package com.example.PesticideTest_0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.PesticideTest_0.controller.NetController;

public class CPWActivity extends AppCompatActivity {

    private EditText username, old_password, new_password;
    private Button bt_sure;
    private String cmd_str="cmd=3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_p_w);
        username = findViewById(R.id.username);
        old_password = findViewById(R.id.oldPassword);
        new_password = findViewById(R.id.newPassword);
        bt_sure = findViewById(R.id.bt_sure);
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cmd_str=cmd_str+
                        "&userName="+username.getText().toString()+
                        "&oldPassword="+old_password.getText().toString()+
                        "&newPassword="+new_password.getText().toString();
                NetController controller = new NetController(CPWActivity.this);
                controller.send(cmd_str);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}