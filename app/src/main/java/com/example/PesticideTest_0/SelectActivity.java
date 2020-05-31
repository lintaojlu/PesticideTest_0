package com.example.PesticideTest_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SelectActivity extends AppCompatActivity {
     int number;//采取样本的数量
     double nongdu[]=new double[100];//浓度数组
     int n=1;//循环将浓度录入数组用
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        final EditText numberselect=(EditText)findViewById(R.id.samplenumber);//样本数量编辑框
        Button ensurenum=(Button)findViewById(R.id.ensurenumber);//确认样本数量按钮
        ensurenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    String spnumber=numberselect.getText().toString();//将样本数量变成字符串
                    if(isInteger(spnumber))//确认输入是否为整数
                    {
                        number=Integer.parseInt(spnumber);
                        Toast.makeText(SelectActivity.this, "录入完毕", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SelectActivity.this, "请输入数字，重新输入", Toast.LENGTH_SHORT).show();
                        numberselect.setText("");//清空编辑框
                    }
            }
        });
        final EditText nongduselect =(EditText)findViewById(R.id.nongdu);//浓度录入编辑框
        Button ensurenongdu=(Button)findViewById(R.id.ensurenongdu);


        ensurenongdu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nongdus=nongduselect.getText().toString();
                if(isNumber(nongdus))//确认浓度是否为整数，小数
                {
                    if(n>number) {//如果输入数量大于样本数量则不会再录入
                       Toast.makeText(SelectActivity.this,"浓度样本数量超过最大数",Toast.LENGTH_SHORT).show();
                        nongduselect.setText("");
                    }else
                    {
                        nongdu[n-1] = Double.valueOf(nongdus.toString());//将textview数据录入浓度数组

                        TextView tv=(TextView)findViewById(R.id.numberhint);
                        String s = Integer.toString(n);
                        tv.setText(s);
                        n++;
                        nongduselect.setText("");

                    }
                }else {
                    Toast.makeText(SelectActivity.this, "请输入数字，重新输入", Toast.LENGTH_SHORT).show();
                   nongduselect.setText("");
                }

            }
        });
    Button gotocamera=(Button)findViewById(R.id.gotocamera);//进到相册测量灰度值按钮
    gotocamera.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(n-1==number) {//避免浓度采集不全进入灰度值采集
                Intent intent = new Intent(SelectActivity.this, ImageActivity.class);
                Bundle b = new Bundle();
                b.putDoubleArray("wrong", nongdu);//将浓度数组传递到下一界面
                intent.putExtras(b);
                intent.putExtra("Bound", number);//将样本数传递
                startActivity(intent);
            }else
            {
                Toast.makeText(SelectActivity.this,"请将浓度均录入再采集灰度值",Toast.LENGTH_SHORT).show();
            }
        }
    });


    }
    public static boolean isInteger(String str) {//判断是否整数函数
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
    public static boolean isNumber(String str){//判断是否整数小数函数
        String reg = "^[0-9]+(.[0-9]+)?$";
        return str.matches(reg);
    }

}
