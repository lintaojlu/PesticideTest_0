package com.example.PesticideTest_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.PesticideTest_0.drawing.CanvasView;
import com.example.PesticideTest_0.fitting.RealPathFromUriUtils;

import me.pqpo.smartcropperlib.view.CropImageView;

public class ImageActivity2 extends AppCompatActivity {
    private Button bt_sure;
    private CropImageView picture;
    private TextView tv_result,tv_green,tv_red;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image2);
        //原来一直就是定义位置错了！！！该注释空行不能省啊
        picture=(CropImageView)findViewById(R.id.picture);
        bt_sure = (Button)findViewById(R.id.bt_sure);
        tv_result = (TextView)findViewById(R.id.tv_result);
        tv_green = findViewById(R.id.tv_green);
        tv_red = findViewById(R.id.tv_red);
        Intent intent_ima = getIntent();
        String image_path = intent_ima.getStringExtra("image_path");
        if(image_path!=null)
        {
            bitmap= BitmapFactory.decodeFile(image_path);
            picture.setImageToCrop(bitmap);//设置待裁剪图片
        }else{
            Toast.makeText(this,"未打开指定图片，请重试",Toast.LENGTH_LONG).show();
        }
        bt_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap crop = picture.crop();
                picture.setImageBitmap(crop);
                bitmap = crop;
                double gray = calculategray(bitmap);
                String grays = String.valueOf(gray);
                float x = Float.parseFloat(grays);
                float y = CanvasView.getK()*x+CanvasView.getB();
                tv_result.setText("灰度值="+grays+
                        "\n根据拟合函数:y="+CanvasView.getK()+"x+"+CanvasView.getB()+
                        "\n计算得农药浓度="+y+"mg/L");
                if(y>=CanvasView.getBoundary()){
                    tv_red.setText("!农药超标!");
                }
                else{
                    tv_green.setText("农药含量符合标准");
                }
            }
        });
    }
    private double calculategray(Bitmap bitmap)//灰度值计算，遍历裁剪后bitmap每个像素点
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
}