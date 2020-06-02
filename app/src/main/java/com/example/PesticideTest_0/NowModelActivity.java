package com.example.PesticideTest_0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.example.PesticideTest_0.drawing.CanvasView;

public class NowModelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_model);
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("拟合函数:y=" + CanvasView.getK() + "x+" + CanvasView.getB());
    }
}