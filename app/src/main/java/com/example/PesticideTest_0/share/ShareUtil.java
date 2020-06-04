package com.example.PesticideTest_0.share;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareUtil extends Activity {

    public static void shotShare(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        //截图
        String path = screenShot(context);
        //分享
        if (path != null)
            ShareImage(context, path);
    }

    //获取截屏
    private static String screenShot(Context context) {
        Bitmap bitmap = getScreen((Activity) context);
        if (bitmap != null) {
            try {
                //图片文件路径
                String imagePath = getPath();
                Log.e("1", imagePath);
                File file = new File(imagePath);
                if (file.exists()) {
                    file.delete();
                    Log.e("1", "失败");
                }
                if(!file.createNewFile()){
                    Log.e("123","创建文件失败");
                }
                Log.e("2", imagePath);
                FileOutputStream os = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.flush();
                os.close();
                return imagePath;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("screenShot", "保存文件失败");
            }
        }
        return null;
    }

    private static Bitmap getScreen(Activity activity) {
        //获取windows中最顶层的view
        View view = activity.getWindow().getDecorView();

        //允许当前窗口保存缓存信息
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        //获取状态栏高度
        Rect rect = new Rect();
        view.getWindowVisibleDisplayFrame(rect);
        int statusBarHeights = rect.top;

        WindowManager windowManager = activity.getWindowManager();
        //获取屏幕宽和高
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        //去掉状态栏
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeights, width, height - statusBarHeights);

        //销毁缓存信息
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(false);

        return bitmap;

    }

    private static String getPath() {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"photo";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
            Log.e("mkdir", "未创建");
        }
        String filePath=storePath+File.separator+System.currentTimeMillis()+".jpg";
        return filePath;
    }

    //分享
    private static void ShareImage(Context context, String imagePath) {
        if (imagePath != null) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(new File(imagePath));
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.setType("image/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            Intent chooser = Intent.createChooser(intent, "分享截图");
                context.startActivity(chooser);

        } else {
            Log.e("ShareImage", "截图失败");
        }
    }
}
/*
    // Intent intent=new Intent();     //创建一个意图
    // intent.setAction(Intent.ACTION_SEND);       //查找提供分享的接口程序，并询问把数据分享到那里
    // intent.setType("text/plain");               //设置分享类型
    // intent.putExtra(Intent.EXTRA_TEXT,"文字分享");  //放入分享内容
    //startActivity(intent);                      //系统默认该内容一直使用选定的程序进行，不符合分享需求
    // startActivity(Intent.createChooser(intent,"选择分享应用"));

    Uri uri = Uri.parse("file://");

    Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                PackageManager pm = getPackageManager();
                List<ResolveInfo> resInfo = pm.queryIntentActivities(intent, 0);
        if (resInfo.isEmpty()) {
        Toast.makeText(MainActivity.this, "没有可以分享的应用", Toast.LENGTH_SHORT).show();
        return;
        }

        List<Intent> targetIntents = new ArrayList<>();
        for (ResolveInfo resolveInfo : resInfo) {
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        if (!(activityInfo.packageName.contains("com.tencent.mm")
        || activityInfo.packageName.contains("com.tencent.mobileqq"))) {
        continue;
        }
        Intent target = new Intent();
        target.setAction(Intent.ACTION_SEND);
        target.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name));
        target.putExtra(Intent.EXTRA_STREAM, uri);
        target.setType("image/*");//必须设置，否则选定分享类型后不能跳转界面
        targetIntents.add(new LabeledIntent(target, activityInfo.packageName, resolveInfo.loadLabel(pm), resolveInfo.icon));
        }
        if (targetIntents.size() <= 0) {
        Toast.makeText(MainActivity.this, "没有可以分享的应用", Toast.LENGTH_SHORT).show();
        return;
        }

        Intent chooser =Intent.createChooser(targetIntents.remove(targetIntents.size()-1),"选择分享");
        if(chooser==null)
        return;
        LabeledIntent[] labeledIntents=targetIntents.toArray(new LabeledIntent[targetIntents.size()]);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,labeledIntents);
        startActivity(chooser);
*/