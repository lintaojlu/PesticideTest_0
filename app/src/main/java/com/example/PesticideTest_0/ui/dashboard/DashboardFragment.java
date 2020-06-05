package com.example.PesticideTest_0.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.PesticideTest_0.ImageActivity2;
import com.example.PesticideTest_0.R;
import com.example.PesticideTest_0.fitting.RealPathFromUriUtils;

import java.io.File;
import java.io.IOException;

import me.pqpo.smartcropperlib.view.CropImageView;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    public static final int CHOOSE_CAMERA = 1;
    public static final int CHOOSE_ALBUM = 2;
    private File currentImageFile = null;    //定义一个保存图片的File变量
    private Uri photoUri = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //拍照检测
        Button bt_camera = root.findViewById(R.id.bt_camera);
        bt_camera.setOnClickListener(new View.OnClickListener() {
            //在按钮点击事件处写上这些东西，这些是在SD卡创建图片文件的:
            @Override
            public void onClick(View v) {
                //创建图片路径
                File dir = new File(Environment.getExternalStorageDirectory(),"pictures");
                if(dir.exists()){
                    dir.mkdirs();
                }
                //创建图片文件
                currentImageFile = new File(dir,System.currentTimeMillis() + ".jpg");
                if(!currentImageFile.exists()){
                    try {
                        currentImageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //图片要使用provider
                photoUri = FileProvider.getUriForFile(
                        getActivity(),
                        "com.example.PesticideTest_0.file_provider",
                        currentImageFile);
                it.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);//图片保存到sd卡
                startActivityForResult(it, CHOOSE_CAMERA);
            }
        });
        //从相册选择
        Button bt_album = root.findViewById(R.id.bt_album);
        bt_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intent;
                    if (Build.VERSION.SDK_INT < 19) {
                        intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                    } else {
                        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    }
                    startActivityForResult(intent, CHOOSE_ALBUM);
                }
            }
        });
        return root;
    }

    //重写onActivityResult方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent_ima = new Intent(getActivity(), ImageActivity2.class);
        String image_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CHOOSE_CAMERA:
                    //返回相机照片
                    image_path = RealPathFromUriUtils.getRealPathFromUri(getActivity(), Uri.fromFile(currentImageFile));
                    intent_ima.putExtra("image_path",image_path );//将图片路径传给下一个activity
                    startActivity(intent_ima);
                    break;
                case CHOOSE_ALBUM:
                    //返回相册照片
                    image_path = RealPathFromUriUtils.getRealPathFromUri(getActivity(), data.getData());
                    intent_ima.putExtra("image_path", image_path);//将图片路径传给下一个activity
                    startActivity(intent_ima);
            }
        }
    }
}