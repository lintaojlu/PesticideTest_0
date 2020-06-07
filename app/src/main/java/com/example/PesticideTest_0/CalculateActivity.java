package com.example.PesticideTest_0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

import com.example.PesticideTest_0.controller.NetController;
import com.example.PesticideTest_0.drawing.CanvasView;
import com.example.PesticideTest_0.fitting.Arithmetic;
import com.example.PesticideTest_0.fitting.DataPoint;

public class CalculateActivity extends AppCompatActivity {
    double[] nongdux = new double[100];
    double[] grayvaluey = new double[100];
    int number;
    private static final int MAX_POINTS = 10;
    private float cal_k, cal_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        nongdux = b.getDoubleArray("nongdu");//接受浓度
        grayvaluey = b.getDoubleArray("grayvalue");//接受灰度值
        int n = 100;
        Arrays.sort(nongdux);//浓度排序
        Arrays.sort(grayvaluey);//灰度值排序，由于浓度和灰度值数组上线为100，因此排序后前面会有很多0，排序目的在于浓度从小到大，灰度值从大到小，可以按顺序确定点xy值
        number = intent.getIntExtra("Bond", 10);//接受样本数
        Arithmetic line = new Arithmetic();
        for (int i = 0; i < number; i++) {
            line.addDataPoint(new DataPoint(nongdux[100 - number + i], grayvaluey[n - 1]));//浓度从小到大，灰度值从大到小，由于前面都是0，所以从后往前计
            n--;
        }


        printSums(line);//此处为拟合成线性回归
        printLine(line);
        String s = String.valueOf(line.getA1());
        String s1 = String.valueOf(line.getA0());
        TextView tv4 = (TextView) findViewById(R.id.text);
        tv4.setText("数据点个数=" + line.getDataPointCount() +
                ",误差:R^2=" + line.getR() +
                "\n拟合函数:y=" + s + "x+" + s1);

        cal_k = Float.parseFloat(s);
        cal_b = Float.parseFloat(s1);
        CanvasView.changeK(cal_k);
        CanvasView.changeB(cal_b);

        SharedPreferences sp = CalculateActivity.this.getSharedPreferences("user", Context.MODE_PRIVATE);
        final String username = sp.getString("username", null);
        final EditText editText = findViewById(R.id.editText);
        final EditText editText1 = findViewById(R.id.editText1);
        Button bt_save = findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String boundary_str = editText1.getText().toString();
                CanvasView.changeBoundary(Float.parseFloat(boundary_str));
                String cmd_str = "cmd=4" +
                        "&userName=" + username +
                        "&modelName=" + editText.getText() +
                        "&modelSlope=" + CanvasView.getK() +
                        "&modelIntercept=" + CanvasView.getB() +
                        "&modelBoundary=" + boundary_str;
                NetController netController = new NetController(CalculateActivity.this);
                netController.send(cmd_str);
                //Complete and destroy activity once successful
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    /**
     * Print the computed sums.
     *
     * @param line the regression line
     */
    private static void printSums(Arithmetic line) {
        System.out.println("\n数据点个数 n = " + line.getDataPointCount());
        System.out.println("\nSum x  = " + line.getSumX());
        System.out.println("Sum y  = " + line.getSumY());
        System.out.println("Sum xx = " + line.getSumXX());
        System.out.println("Sum xy = " + line.getSumXY());
        System.out.println("Sum yy = " + line.getSumYY());

    }

    /**
     * Print the regression line function.
     *
     * @param line the regression line
     */
    private static void printLine(Arithmetic line) {
        System.out.println("\n拟合函数:  y = " + line.getA1() + "x + "
                + line.getA0());
        System.out.println("误差：     R^2 = " + line.getR());
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

