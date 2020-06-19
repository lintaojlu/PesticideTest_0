package com.example.PesticideTest_0;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.PesticideTest_0.controller.NetController;

public class ShowModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_model);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        NetController controller = new NetController(ShowModelActivity.this);
        final SharedPreferences sp = ShowModelActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = sp.getString("username", null);
        String cmd_str = "cmd=5&userName=" + username;
        controller.send(cmd_str);
        String string = sp.getString("all_models_names", "未获得模型");
        TextView all_models = findViewById(R.id.all_models);
        all_models.setText(string);
    }
    //标题栏返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}