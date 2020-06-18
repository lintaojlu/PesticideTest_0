package com.example.PesticideTest_0;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.PesticideTest_0.fitting.RealPathFromUriUtils;

import me.pqpo.smartcropperlib.view.CropImageView;

public class ImageActivity extends AppCompatActivity {

    public static final int CHOOSE_PHOTO=2;
    private CropImageView picture;
    private Button se;
    Bitmap bitmap1;
    int color;
    double[] nongdunumber = new double[100];
    double[] grayvalue=new double[100];
    int number;
    int m=1;

    Point[] point = new Point[4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        nongdunumber=b.getDoubleArray("wrong");
        number=intent.getIntExtra("Bound",10);

        picture=(CropImageView)findViewById(R.id.picture);

        Button choosephoto=(Button)findViewById(R.id.choose_from_album);
        choosephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(ImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(ImageActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }
            }
        });
        Button gotocal=(Button)findViewById(R.id.calculate);//建模按钮
        gotocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageActivity.this, CalculateActivity.class);
                Bundle b = new Bundle();
                b.putDoubleArray("nongdu", nongdunumber);//传递浓度
                b.putDoubleArray("grayvalue",grayvalue);//传递灰度值
                intent.putExtras(b);
                intent.putExtra("Bond", number);//传递样本数
                startActivity(intent);
            }
        });



    }
    private void openAlbum()//打开相册
    {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        }
        else
        {
            intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        }
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults)//申请运行时权限
    {
        switch (requestCode)
        {
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    openAlbum();
                }else{
                    Toast.makeText(this,"权限未申请成功，请在手机上打开权限",Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
    }
    int n=1;
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)//回显函数
    {

        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode)
        {
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK) {

                    if (m > number) {//避免所选样本大于样本数量
                        Toast.makeText(ImageActivity.this, "所选样本超过最大值", Toast.LENGTH_SHORT).show();

                    }else {
                        String realPathFromUri = RealPathFromUriUtils.getRealPathFromUri(this, data.getData());
                        displayImage(realPathFromUri);
                        final Bitmap bp = bitmap1;
                        se = (Button) findViewById(R.id.sure);//确定按钮，裁剪并计算灰度值

                        se.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bitmap crop = picture.crop();
                                picture.setImageBitmap(crop);
                                bitmap1 = crop;
                                //得到采样位置
                                Point p[] = picture.getCropPoints();

                                double gray = calculategray(bitmap1);
                                String grays = String.valueOf(gray);
                                TextView grayv = (TextView) findViewById(R.id.grayvalue);
                                grayv.setText(grays);
                                /*xuyaodagaizheng*/
                                if (m> number) {//避免所选样本大于样本数量
                                    Toast.makeText(ImageActivity.this, "所选样本超过最大值", Toast.LENGTH_SHORT).show();
                                }else {
                                    grayvalue[m - 1] = gray;

                                    m++;}
                            }
                        });
                        Button cancel = (Button) findViewById(R.id.cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                picture.setImageBitmap(bp);

                                bitmap1 = bp;

                            }

                        });


                    }
                }

                break;

            default:
                break;
        }
    }

    private String getImagePath(Uri uri,String selection)
    {
        String path =null;
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null)
        {
            if(cursor.moveToFirst())
            {
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
        }
        return path;
    }
    private void displayImage(String imagePath)//获取图片
    {
        if(imagePath!=null)
        {
            final Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageToCrop(bitmap);//设置待裁剪图片
            bitmap1=bitmap;
        }else{
            Toast.makeText(this,"未打开指定图片，请再试",Toast.LENGTH_LONG).show();
        }
    }
    public double calculategray(Bitmap bitmap)//灰度值计算，遍历裁剪后bitmap每个像素点
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
