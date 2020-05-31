package com.example.PesticideTest_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ImageActivity extends AppCompatActivity {

    public static final int CHOOSE_PHOTO=2;
    private ImageView picture;
    int color;
    double[] nongdunumber = new double[100];
    double[] grayvalue=new double[100];
    int number;
    int m=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        nongdunumber=b.getDoubleArray("wrong");
        number=intent.getIntExtra("Bound",10);

        picture=(ImageView)findViewById(R.id.picture);

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
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
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

                    }else{
                        if (Build.VERSION.SDK_INT >= 19) {
                            handleImageOnkitkat(data);
                        } else {
                            handleimagebeforekitcat(data);
                        }
                        int r = Color.red(color);//rgb分量获取
                        int g = Color.green(color);
                        int b = Color.blue(color);
                        double gray = r * 0.3 + g * 0.59 + b * 0.11;//计算灰度值
                        String grays = String.valueOf(gray);
                        TextView grayv = (TextView) findViewById(R.id.grayvalue);
                        grayv.setText(grays);
                        grayvalue[m-1]=gray;
                        m++;


                    }
                }
                break;

            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnkitkat(Intent data)//获取路径
    {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri))
        {
            String Docid=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=Docid.split(":")[1];
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.prociders.downloads.documents".equals(uri.getAuthority())){
                Uri contenturi= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(Docid));
                imagePath=getImagePath(contenturi,null);
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                imagePath=getImagePath(uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme()))
            {
                imagePath=uri.getPath();
            }

        }
        displayImage(imagePath);
    }

    private void handleimagebeforekitcat(Intent data)
    {
        Uri uri=data.getData();
        String imagepath=getImagePath(uri,null);
        displayImage(imagepath);
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
            Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
            color=bitmap.getPixel(1512,2116);//采取固定位置像素的的color
        }else{
            Toast.makeText(this,"未打开指定图片，请再试",Toast.LENGTH_LONG).show();
        }
    }
}
