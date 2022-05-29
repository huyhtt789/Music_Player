package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Date;

public class FolderActivity extends AppCompatActivity {
    TextView Back;
    Button choose;
    ListView _myList;
    Folder[] items;
    String currentpath;
    ProgressDialog progressDialog;
    CustomListItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Intent intent=getIntent();
        if(getIntent().hasExtra("Currentpath")) {
            currentpath= getIntent().getStringExtra("Currentpath");
            getlistFolder(currentpath);
        }else {
            currentpath=Environment.getExternalStorageDirectory().toString();
            getlistFolder(Environment.getExternalStorageDirectory().toString());
        }
        _myList = (ListView) findViewById(R.id.myList);
        adapter = new CustomListItemAdapter(this, R.layout.list_item_lnk_img, items);
        _myList.setAdapter(adapter);
        Back=(TextView) findViewById(R.id.tvBack);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Currentpath", getIntent().getStringExtra("Parentpath"));
                currentpath= getIntent().getStringExtra("Currentpath");
                getlistFolder(currentpath);
                adapter = new CustomListItemAdapter(FolderActivity.this, R.layout.list_item_lnk_img, items);
                _myList.setAdapter(adapter);
            }
        });
        choose=(Button) findViewById(R.id.btnChoose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FolderActivity.this, MainActivity.class);
                intent.putExtra("Currentpath", currentpath);
                startActivity(intent);
            }
        });
    }
    public void getlistFolder(String currentpath){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang lấy dữ liệu.... ");
        progressDialog.show();
        Log.d("Files", "Path: " + currentpath);
        File directory = new File(currentpath);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        items=new Folder[files.length];
        Intent intent=getIntent();
        intent.putExtra("ParentPath", directory.getParent());
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
            String filePath = files[i].getPath();
            items[i]=new Folder();
            items[i].setNAME(files[i].getName());
            items[i].setParent_PATH(files[i].getParent());
            items[i].setPATH(files[i].getPath());
        }
        progressDialog.dismiss();
    }
}