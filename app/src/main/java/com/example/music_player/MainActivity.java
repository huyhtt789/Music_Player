package com.example.music_player;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    String [] items;
    Button setpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File filesong=new File(Environment.getExternalStorageDirectory().toString());
        listView = findViewById(R.id.ListViewSong);
        if(getIntent().hasExtra("Currentpath")) {
            String currentpath= getIntent().getStringExtra("Currentpath");
            Toast.makeText(this,currentpath,Toast.LENGTH_SHORT).show();
            filesong=new File(currentpath);
            displaySongs(filesong);
        } else Toast.makeText(this, filesong.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
        runtimePermission(filesong);
        setpath=(Button) findViewById(R.id.btnsetpath);
        setpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,FolderActivity.class);
                startActivity(intent);
            }
        });
    }

    public void runtimePermission(File file)
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySongs(file);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

    public ArrayList<File> findSong (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File [] files = file.listFiles();

        for (File singlefile: files)
        {
                if (singlefile.getName().endsWith(".mp3")||singlefile.getName().endsWith(".wav"))
                {
                    arrayList.add(singlefile);
                }
        }
        return  arrayList;
    }

    void displaySongs(File file)
    {
        final ArrayList<File> mySongs =  findSong(file);

        items = new String[mySongs.size()];
        for (int i = 0; i<mySongs.size();i++)
        {
            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

       // ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
       // listView.setAdapter(myAdapter);
        customAdapter customAdapter = new customAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String songName = (String) listView.getItemAtPosition(position);
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songname",songName)
                        .putExtra("position",position));
            }
        });
    }

    class customAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView = getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textsong = myView.findViewById(R.id.txtsongname);
            textsong.setSelected(true);
            textsong.setText(items[position]);

            return myView;

        }
    }
}