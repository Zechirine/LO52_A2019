package com.utbm.keepit.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.Topic;
import com.utbm.keepit.backend.service.TopicService;

import android.content.Intent;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
//TODO: 从相册选择有问题


public class CreateTopicActivity extends Activity {

    private TopicService topicService = new TopicService();
    //定义控件
    private EditText topicName;
    private Button createTopic,take_photo,select_photo;
    //    private Button takePhoto,selectPhoto, createSeance, cancleCreate;
    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;
    private ImageView imageview;
    private Uri imageUri;
    private ImageView ret;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_topic);
//
//        //获取控件
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_topic);
        take_photo = findViewById(R.id.take_photo);
        select_photo = findViewById(R.id.select_photo);
        imageview = findViewById(R.id.image_selected);
        topicName = findViewById(R.id.in_topic_name);
        createTopic = findViewById(R.id.create_topic);
        ret= findViewById(R.id.navBack);
        ret.setVisibility(true?View.VISIBLE:View.GONE);
        ret.setClickable(true);
        ret.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("添加测试user admin admin");
                Intent intent = new Intent(CreateTopicActivity.this,MainActivity.class);

                startActivity(intent);

            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照获取图片
                take_photo();
            }
        });

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从相册中选取图片
                select_photo();
            }
        });

        createTopic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                //从相册中选取图片
                insertTopic();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void insertTopic(){

        String topicName = this.topicName.getText().toString();
        if(topicName == null || imageUri==null){
            Toast.makeText(CreateTopicActivity.this, R.string.SelectImgName, Toast.LENGTH_SHORT).show();
        }else{
           // String imagePath = GetPathFromUri.getPath(CreateTopicActivity.this,imageUri);
            Topic t = new Topic(topicName,imageUri.toString());
            if(topicService.createTopic(t))
            {
                Toast.makeText(CreateTopicActivity.this, R.string.InsertSucce, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(CreateTopicActivity.this, R.string.NameExiste, Toast.LENGTH_SHORT).show();
            }
            //TODO: 创建失败的后果
        }
    }
    /**
     *拍照获取图片
     **/
    public void take_photo() {
        String status= Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)) {
            //创建File对象，用于存储拍照后的图片
            String imageFileName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            File outputImage = new File(getExternalCacheDir(), imageFileName+".jpg");

            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(this, "com.utbm.keepit.activities.MainActivity", outputImage);
            } else {
                imageUri = Uri.fromFile(outputImage);
            }
            //启动相机程序
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        }else
        {

            Toast.makeText(CreateTopicActivity.this, "没有储存卡",Toast.LENGTH_LONG).show();
        }
    }
    /**
     * 从相册中获取图片
     * */
    public void select_photo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else {
            openAlbum();
        }
    }
    /**
     * 打开相册的方法
     * */
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO :
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageview.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case SELECT_PHOTO :
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImgeOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }
    /**
     *4.4以下系统处理图片的方法
     * */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();

        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);

        String imageFileName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        File outputImage = new File(getExternalCacheDir(), imageFileName+".jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(outputImage); //获取文件流
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);  //保存成图片
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.utbm.keepit.activities.MainActivity", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }



    }
    /**
     * 4.4及以上系统处理图片的方法
     * */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImgeOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        String imageFileName = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        File outputImage = new File(getExternalCacheDir(), imageFileName+".jpg");

        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(outputImage); //获取文件流
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);  //保存成图片
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.utbm.keepit.activities.MainActivity", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        if (DocumentsContract.isDocumentUri(this,uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(uri,null);
            }else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的uri，直接获取图片路径即可
                imagePath = uri.getPath();
            }
            //根据图片路径显示图片
            displayImage(imagePath);
        }
    }
    /**
     * 根据图片路径显示图片的方法
     * */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageview.setImageBitmap(bitmap);
        }else {
            Toast.makeText(CreateTopicActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 通过uri和selection来获取真实的图片路径
     * */
    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1 :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }else {
                    Toast.makeText(CreateTopicActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


//    public void onCreateTopicClick(View view){
//
//    }

}
