package com.example.PesticideTest_0;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PesticideTest_0.drawing.CanvasView;
import com.example.PesticideTest_0.fitting.RealPathFromUriUtils;
import com.example.PesticideTest_0.share.ShareUtil;

import me.pqpo.smartcropperlib.view.CropImageView;

public class ImageActivity2 extends AppCompatActivity {
    private Button bt_sure,bt_share;
    private CropImageView picture;
    private TextView tv_result,tv_green,tv_red;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }
        //标题栏返回键
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //空指针错误注意代码顺序!!
        picture=(CropImageView)findViewById(R.id.picture);
        bt_sure = (Button)findViewById(R.id.bt_sure);
        tv_result = (TextView)findViewById(R.id.tv_result);
        tv_green = findViewById(R.id.tv_green);
        tv_red = findViewById(R.id.tv_red);
        bt_share=findViewById(R.id.bt_share);
        //获得上一个activity传来的参数
        Intent intent_ima = getIntent();
        String image_path = intent_ima.getStringExtra("image_path");
        if(image_path!=null)
        {
            bitmap= BitmapFactory.decodeFile(image_path);
            picture.setImageToCrop(bitmap);//设置待裁剪图片
        }else{
            Toast.makeText(this,"未打开指定图片，请重试",Toast.LENGTH_LONG).show();
        }
        //确定按钮
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap crop = picture.crop();
                picture.setImageBitmap(crop);
                bitmap = crop;
                double gray = calculategray(bitmap);
                String grays = String.valueOf(gray);
                float y = Float.parseFloat(grays);
                float x = (y-CanvasView.getB())/CanvasView.getK();
                if(x<0){
                    x=0;
                }
                tv_result.setText("灰度="+grays+
                        "\n根据拟合函数:灰度="+CanvasView.getK()+"浓度+"+CanvasView.getB()+
                        "\n农药浓度标准="+CanvasView.getBoundary()+"mg/L"+
                        "\n计算得农药浓度="+x+"mg/L");
                if(x>=CanvasView.getBoundary()){
                    tv_green.setText("");
                    tv_red.setText("!农药超标!");
                }
                else{
                    tv_red.setText("");
                    tv_green.setText("农药含量符合标准");
                }

            }
        });
        //分享按钮
        bt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareUtil.shotShare(ImageActivity2.this);
            }
        });
    }
    //灰度值计算，遍历裁剪后bitmap每个像素点
    private double calculategray(Bitmap bitmap)
    {
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        double [][] grayz=new double[width][height];
        double grayi=0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int color=bitmap.getPixel(i,j);
                int r = Color.red(color);//rgb分量获取
                int g = Color.green(color);
                int b = Color.blue(color);
                double gray = r * 0.3 + g * 0.59 + b * 0.11;//计算灰度值
                if((r+g+b)>50)
                {
                    grayi=gray+grayi;
                }
            }
        }
        double result=grayi/(width*height);
        return result;
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