package com.example.music_player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;


public class CustomListItemAdapter extends ArrayAdapter<Folder> {
    Context context;
    Folder[] items;

    public CustomListItemAdapter(Context context, int layoutTobeInflated, Folder[] items){
        super(context, R.layout.list_item_lnk_img, items);
        this.context = context;
        this.items = items;
     }
    private View.OnClickListener itemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
                    int i = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(context, FolderActivity.class);
                    File file=new File(items[i].getPATH());
                    intent.putExtra("Currentpath", items[i].getPATH());
                    intent.putExtra("Parentpath", items[i].getParent_PATH());
                    context.startActivity(intent);
                    return;
        }
    };
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View row = inflater.inflate(R.layout.list_item_lnk_img, null);
        TextView foldername = (TextView) row.findViewById(R.id.tvDate);

        foldername.setText(items[position].getNAME());
        foldername.setTag(position);

        foldername.setOnClickListener(itemClickListener);
        return row;

    }


}
