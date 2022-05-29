package com.example.music_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PlayerActivity extends AppCompatActivity {

    Button btnplay,btnnext,btnforward,btnpre,btnrewind,imgrepeat,imgrandom;
    TextView txtsstart, txtsstop,txtsname;
    SeekBar seekmusic;
    BarVisualizer visualizer;
    ImageView imageView;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position=0;
    ArrayList<File> mySongs;
    Thread updateseekbar;
    boolean repeat=false;
    boolean checkrandoom=false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (visualizer !=null)
        {
            visualizer.release();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnforward = findViewById(R.id.btnforward);
        btnnext = findViewById(R.id.btnnext);
        btnplay = findViewById(R.id.btnplay);
        btnpre = findViewById(R.id.btnprevious);
        btnrewind = findViewById(R.id.btnrewind);
        txtsname = findViewById(R.id.txtsn);
        txtsstart = findViewById(R.id.txtsstart);
        txtsstop = findViewById(R.id.txtsstop);
        seekmusic = findViewById(R.id.seekbar);
        visualizer = findViewById(R.id.blast);
        imageView = findViewById(R.id.imageview);
        imgrepeat=findViewById(R.id.imagebuttonrepeat);
        imgrandom=findViewById(R.id.imagebuttonsuffle);
       

        if (mediaPlayer != null )
        {

            mediaPlayer.stop();
            mediaPlayer.release();

        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList)bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("position",0);
        txtsname.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        sname = mySongs.get(position).getName();
        txtsname.setText(sname);

        mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
        mediaPlayer.start();

        //xu ly thanh time nhac
        updateseekbar = new Thread()
        {
            @Override
            public void run() {
                int totaDuration = mediaPlayer.getDuration();
                int currentposition=0;

                while (currentposition<totaDuration)
                {
                    try {
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    }
                    catch (InterruptedException | IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        String endTime = createTime(mediaPlayer.getDuration());
        txtsstop.setText(endTime);
        final Handler handler = new Handler();
        final int deplay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run()
            {
                String currenTime = createTime(mediaPlayer.getCurrentPosition());
                txtsstart.setText(currenTime);
                handler.postDelayed(this,deplay);
            }
        },2000);

        //Xu ly nut play va pause
        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) // neu dang la nut play la music dang pause . else
                {
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }
                else
                {
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        //next listener
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                btnnext.performClick();
            }
        });

        int audiosessionId = mediaPlayer.getAudioSessionId();
        if (audiosessionId !=-1)
        {
            visualizer.setAudioSessionId(audiosessionId);
        }

        //xu ly nut next chuyen bai hat tiep theo
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
               if(repeat==true){
                   if (position==0){
                       position=mySongs.size();
                   }
                   position-=1;
               }
                if(checkrandoom==true){
                    Random random=new Random();
                    int index=random.nextInt(mySongs.size());
                    if(index==position){
                        position=index-1;
                    }
                    position=index;
                }
                if(position>(mySongs.size()-1)){
                    position=0;
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if (audiosessionId !=-1)
                {
                    visualizer.setAudioSessionId(audiosessionId);
                }
                btnpre.setClickable(false);
                btnnext.setClickable(false);
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnpre.setClickable(true);
                        btnnext.setClickable(true);
                    }
                },3000);
            }
        });
        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat==false){
                    if(checkrandoom==true){
                        checkrandoom=false;
                        imgrepeat.setBackgroundResource(R.drawable.iconsyned);
                        imgrandom.setBackgroundResource(R.drawable.iconsuffle);
                    }
                    imgrepeat.setBackgroundResource(R.drawable.iconsyned);
                    repeat=true;

                }else {
                    imgrepeat.setBackgroundResource(R.drawable.iconrepeat);
                    repeat=false;
                }
            }
        });
        imgrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkrandoom==false){
                    if(repeat==true){
                        repeat=false;
                        imgrandom.setBackgroundResource(R.drawable.iconshuffled);
                        imgrepeat.setBackgroundResource(R.drawable.iconrepeat);
                    }
                    imgrandom.setBackgroundResource(R.drawable.iconshuffled);
                    checkrandoom=true;

                }else {
                    imgrandom.setBackgroundResource(R.drawable.iconsuffle);
                    checkrandoom=false;
                }
            }
        });


        //xu ly nut previous quay lai bai hat truoc
        btnpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                if(repeat==true){

                    position+=1;
                }
                if(checkrandoom==true){
                    Random random=new Random();
                    int index=random.nextInt(mySongs.size());
                    if(index==position){
                        position=index-1;
                    }
                    position=index;
                }

                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
                sname = mySongs.get(position).getName();
                txtsname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if (audiosessionId !=-1)
                {
                    visualizer.setAudioSessionId(audiosessionId);
                }
                btnpre.setClickable(false);
                btnnext.setClickable(false);
                Handler handler1=new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnpre.setClickable(true);
                        btnnext.setClickable(true);
                    }
                },3000);
            }
        });

        //xu ly nut forward tua nhanh bai hat
        btnforward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);// tua nha 10.000ms = 10s
                }
            }
        });

        //xu ly nut rewind tua nguoc lai bai hat
        btnrewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                }
            }
        });

    }

    //xu ly animation hieu ung song nhac
    public void startAnimation(View view)
    {
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView,"rotation",0f,360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet =  new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    //tinh thoi gian bai hat
    public String createTime(int duration)
    {
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time +=min+":";

        if (sec<10)
        {
            time+="0";
        }
        time += sec;

        return time;
    }
}