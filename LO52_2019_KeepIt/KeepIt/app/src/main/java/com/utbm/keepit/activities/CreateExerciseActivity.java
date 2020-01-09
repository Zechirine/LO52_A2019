package com.utbm.keepit.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.utbm.keepit.R;
import com.utbm.keepit.backend.entity.Exercise;
import com.utbm.keepit.backend.entity.ExerciseDataToDesciption;
import com.utbm.keepit.backend.entity.JoinTopicExercise;
import com.utbm.keepit.backend.entity.Topic;
import com.utbm.keepit.backend.service.ExerciseService;
import com.utbm.keepit.backend.service.JoinTopicExerciseService;
import com.utbm.keepit.backend.service.TopicService;
import com.utbm.keepit.ui.StringListAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CreateExerciseActivity extends AppCompatActivity {

    private ExerciseService exerciseService = new ExerciseService();
    private JoinTopicExerciseService jteService=new JoinTopicExerciseService();
    private List<JoinTopicExercise> jtes = new ArrayList<>(); //待创建的 joinTopicExercise

    private TopicService topicService = new TopicService();
    private AlertDialog alertTopicDialog;
    private List<Topic> allTopics;
    private String[] topicItem; // 弹出框的选项内容
    private List<String> chickedTopic = new ArrayList<>(); // 弹出框中选中的 Topic
    private List<String> topicsToAdd = new ArrayList<>(); // 创建Exercise界面的 Topics
    private RecyclerView topics; // id => topics_selected
    private StringListAdapter topicsChosedAdapter;

     private EditText execName,execDesc;
     private Long tid;
    private Button take_photo,select_photo,selectTopic;

    private static List<String> topic_items = new ArrayList<>();
    private List<Long> topicIdSelected = new ArrayList<>();

    //    private Button takePhoto,selectPhoto, createSeance, cancleCreate;
    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;
    private ImageView imageview;
    private Uri imageUri;
    private Spinner spinnerType,spinnerGroup,spinnerDiff;
    List<String> typeList=new ArrayList<>();
    List<String> groupList=new ArrayList<>();
    List<String> diffList=new ArrayList<>();
    private ArrayAdapter<String> adapterType,adapterGroup,adapterDiff;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);
        take_photo = (Button) findViewById(R.id.take_photo);
        select_photo = (Button) findViewById(R.id.select_photo);
        imageview = (ImageView) findViewById(R.id.image_selected);

        selectTopic = (Button) findViewById(R.id.select_topics);
        allTopics = topicService.findAll();
        topicItem= new String[allTopics.size()];
        for(int i=0;i<allTopics.size();i++){
            topicItem[i]=allTopics.get(i).getId()+" : "+allTopics.get(i).getTopicName();
        }

//        tid=getIntent().getExtras().getLong("topicid"); //传入的 activity的id TODO; 默认选中该 topic
        execName=findViewById(R.id.input_exercise_name);
        execDesc=findViewById(R.id.input_exercise_desc);
        //ExerciseDataToDesciption.descripGroup
//        ExerciseDataToDesciption.descripDifficult
//        ExerciseDataToDesciption.descripPublic
        for( Map.Entry<Integer, String> entry : ExerciseDataToDesciption.descripPublic.entrySet()){
            typeList.add(entry.getValue());
        }
        for( Map.Entry<Integer, String> entry : ExerciseDataToDesciption.descripGroup.entrySet()){
            groupList.add(entry.getValue());
        }
        for( Map.Entry<Integer, String> entry : ExerciseDataToDesciption.descripDifficult.entrySet()){
            diffList.add(entry.getValue());
        }

        spinnerType=findViewById(R.id.input_exercise_type);
        adapterType=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,typeList);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);

        spinnerDiff=findViewById(R.id.input_exercise_diff);
        adapterDiff=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,diffList);
        adapterDiff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDiff.setAdapter(adapterDiff);

        spinnerGroup=findViewById(R.id.input_exercise_group);
        adapterGroup=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,groupList);
        adapterGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapterGroup);

        for(Topic t : topicService.findAll()){
            topic_items.add(t.toEasyString());
        }
        selectTopic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openMultiChoiDialog(v);
            }
        });

        //
//        exChoosed=root.findViewById(R.id.exercise_choosed);
//        exChoosed.setNestedScrollingEnabled(false);
//
//        exerciceListAdapter =new ExerciceChoosedListAdapter(getActivity(),tempExercises,tempSeanceExercise);
//        exChoosed.setLayoutManager(new LinearLayoutManager(getActivity()));
//        exChoosed.setAdapter(exerciceListAdapter);
        topics = findViewById(R.id.topics_selected);
        topics.setNestedScrollingEnabled(false);
        topicsChosedAdapter = new StringListAdapter(CreateExerciseActivity.this,topicsToAdd);
        topics.setLayoutManager(new LinearLayoutManager(CreateExerciseActivity.this));
        topics.setAdapter(topicsChosedAdapter);



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

    }

    public void openMultiChoiDialog(View view){
        // 多选提示框
            final String[] items = topicItem;

            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(this);
            // 设置标题
            alertDialogBuilder.setTitle("Choisir les topics");
            // 参数介绍
            // 第一个参数：弹出框的信息集合，一般为字符串集合
            // 第二个参数：被默认选中的，一个布尔类型的数组
            // 第三个参数：勾选事件监听
        //TODO : checkedItems = chickedTopic
            alertDialogBuilder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    // dialog：不常使用，弹出框接口
                    // which：勾选或取消的是第几个
                    // isChecked：是否勾选
                    if (isChecked) {
                        chickedTopic.add(items[which]);
                        Toast.makeText(CreateExerciseActivity.this, "Choose "+items[which], Toast.LENGTH_SHORT).show();
                    }else {
                        chickedTopic.remove(items[which]);
                        // 取消选中
                        Toast.makeText(CreateExerciseActivity.this, "cancel choose"+items[which], Toast.LENGTH_SHORT).show();
                    }

                }
            });
            alertDialogBuilder.setPositiveButton("Ajouter Topics", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //
                    topicsToAdd.clear();
                    for(String s: chickedTopic){
                        topicsToAdd.add(s);
                    }
                    System.out.println(topicsToAdd.toArray());
                    topicsChosedAdapter.notifyDataSetChanged();
                    chickedTopic.clear();
                    // 关闭提示框
                    alertTopicDialog.dismiss();
                }
            });
            alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // 取消的时候清空已经选择的主题，防止重复选择
                    chickedTopic.clear();
                    // 关闭提示框
                    alertTopicDialog.dismiss();
                }
            });
        alertTopicDialog = alertDialogBuilder.create();
        alertTopicDialog.show();
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

            Toast.makeText(CreateExerciseActivity.this, "没有储存卡",Toast.LENGTH_LONG).show();
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
            Toast.makeText(CreateExerciseActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CreateExerciseActivity.this,"failed to get image",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    public void onCreateExercise(View v){
        //TODO ; change all text in toast with    R.strings.(??)
       // System.out.println("==============================="+ execName.getText().toString());
        if(execName.getText().toString().length()<1){
            Toast.makeText(CreateExerciseActivity.this,"please entre nom de entraînment",Toast.LENGTH_SHORT).show();
            return;
        }else if(execDesc.getText().toString().length()<1){
            Toast.makeText(CreateExerciseActivity.this,"please entre descrption",Toast.LENGTH_SHORT).show();
            return;
        }else if(topicsToAdd.size()==0){
            Toast.makeText(CreateExerciseActivity.this,"please choose topics",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //1.exercise.name existed or not
//            2.插入exercise  返回 id
            String name=execName.getText().toString();

            if(exerciseService.findByName(name)!=null){
                Toast.makeText(CreateExerciseActivity.this,R.string.IdenExiste,Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                // if name does not existed
                String desc=execDesc.getText().toString();

                Integer diff = ExerciseDataToDesciption.getKey(
                        ExerciseDataToDesciption.descripDifficult,
                        diffList.get(spinnerDiff.getSelectedItemPosition()));

                Integer type=ExerciseDataToDesciption.getKey(
                        ExerciseDataToDesciption.descripPublic,
                        typeList.get(spinnerDiff.getSelectedItemPosition()));

                Integer group=ExerciseDataToDesciption.getKey(
                        ExerciseDataToDesciption.descripGroup,
                        groupList.get(spinnerDiff.getSelectedItemPosition()));

                Exercise exercise=new Exercise();
                exercise.setDescription(desc);
                exercise.setName(name);
                exercise.setLevelDifficult(diff);
                exercise.setLevelGroup(group);
                exercise.setTypePublic(type);
                if(imageUri!=null){
                    exercise.setImageResource(imageUri.toString());
                }
//        exercise.setImageResource();
                long newExerId =  exerciseService.createExercise(exercise);

                //插入中间表  2.插入JoinTpicExercise
                //TODO： -1
                if(newExerId != -1){
                    for(String s: topicsToAdd){
                        Long topicId = Long.valueOf(s.split(" : ")[0]);

                        JoinTopicExercise jte = new JoinTopicExercise(topicId,newExerId);
                        jteService.createJoinTopicExercise(jte);
                    }
                    Toast.makeText(CreateExerciseActivity.this,R.string.InsertSucce,Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(CreateExerciseActivity.this,MainActivity.class);
                startActivity(intent);

            }

        }
    }
}
