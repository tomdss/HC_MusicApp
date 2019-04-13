package com.t3h.hc_musicapp;

import android.Manifest;
import android.content.pm.PackageManager;

import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.t3h.hc_musicapp.adapter.SongAdapter;
import com.t3h.hc_musicapp.manager.MediaManager;
import com.t3h.hc_musicapp.model.Song;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, SongAdapter.SongListener, SeekBar.OnSeekBarChangeListener {

    private static final int LEVEL_SHUFFLE_OFF = 0;
    private static final int LEVEL_SHUFFLE_ON = 1;
    private static final int LEVEL_REPEAT_OFF = 0;
//    private static final int LEVEL_REPEAT_ONE = 1;
//    private static final int LEVEL_REPEAT_ON = 2;
    private static final int LEVEL_PLAY_OFF = 0;
    private static final int LEVEL_PLAY_ON = 1;


    private ImageView imShuffle,imPrevious,imNext,imPlay,imRepeat;
    private TextView tvNameSong,tvSinger,tvSerial,tvDuration;
    private ImageView imSong;
    private RecyclerView lvListMusic;

    private MediaManager mediaManager;
    private List<Song> listSong;

    private SongAdapter songAdapter;

    private SeekBar sbSong;
    private int progressSeekBar;


//    private int levelShuffle = LEVEL_SHUFFLE_OFF;
//    private int levelRepeat = LEVEL_REPEAT_OFF;
//    private int levelPlay = LEVEL_PLAY_OFF;

//    private MediaPlayer media;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().hide();

        checkAndRequestPermissions();


        initData();

        initViews();

        initFirstSongView();

    }

    private void initFirstSongView() {
        tvNameSong.setText(mediaManager.getListSong().get(0).getName());
        tvSinger.setText(mediaManager.getListSong().get(0).getSinger());
//        imSong.setImageBitmap(mediaManager.getAlbumImage(mediaManager.getListSong().get(0).getPath()));
    }

    private void initData() {
        mediaManager = new MediaManager(this);
        listSong = mediaManager.getListSong();

    }

    private void initViews() {

        lvListMusic=findViewById(R.id.lv_list_music);

        tvNameSong = findViewById(R.id.tv_name_song);
        tvSinger = findViewById(R.id.tv_singer);
        tvSerial = findViewById(R.id.tv_serial);
        tvDuration = findViewById(R.id.tv_duration);
        imSong = findViewById(R.id.im_song);

        imShuffle = findViewById(R.id.im_shuffle);
        imPrevious = findViewById(R.id.im_previous);
        imPlay = findViewById(R.id.im_play);
        imNext = findViewById(R.id.im_next);
        imRepeat = findViewById(R.id.im_repeat);

        imShuffle.setOnClickListener(this);
        imPrevious.setOnClickListener(this);
        imPlay.setOnClickListener(this);
        imNext.setOnClickListener(this);
        imRepeat.setOnClickListener(this);

        songAdapter = new SongAdapter(this,listSong);

        lvListMusic.setAdapter(songAdapter);

        songAdapter.setListener(this);

        sbSong = findViewById(R.id.sb_song);
        sbSong.setMax(mediaManager.getCurrentSong().getDuration());
        sbSong.setOnSeekBarChangeListener(this);



//        media = MediaPlayer.create(this, Uri.parse(listSong.get(0).getPath()));

    }

    private void updateSong(){

        Song song = mediaManager.getCurrentSong();
        setInfoSong(song);
        lvListMusic.getLayoutManager().scrollToPosition(mediaManager.getPositionSong());

        new UpdateSeekBarSong().execute();

    }

    private void setInfoSong(Song song) {

        tvNameSong.setText(song.getName());
        tvSinger.setText(song.getSinger());
//        tvSerial.setText(song.+"/"+songAdapter.getItemCount());
        tvSerial.setText(String.valueOf(mediaManager.getPositionSong()+1)+"/"+songAdapter.getItemCount());
//        BitmapDrawable bd = new BitmapDrawable(getResources(),mediaManager.getAlbumImage(song.getPath()));
        imSong.setImageBitmap(mediaManager.getAlbumImage(song.getPath()));


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.im_shuffle:
                doShuffle();
                break;
            case R.id.im_previous:
                doPrevious();
                break;
            case R.id.im_play:
                doPlay();
                break;
            case R.id.im_next:
                doNext();
                break;
            case R.id.im_repeat:
                doRepeat();
                break;
            default:
                break;
        }
    }

    private void doNext() {

        if(mediaManager.next()){
            imPlay.setImageLevel(LEVEL_PLAY_ON);
            updateSong();
        }

    }

    private void doPrevious() {
        if(mediaManager.previous()){
            imPlay.setImageLevel(LEVEL_PLAY_ON);
            updateSong();
        }
    }


    private void doPlay() {

//        if(levelPlay==LEVEL_PLAY_OFF){
//            levelPlay=LEVEL_PLAY_ON;
//            imPlay.setImageLevel(LEVEL_PLAY_ON);
//        }else {
//            levelPlay=LEVEL_PLAY_OFF;
//            imPlay.setImageLevel(LEVEL_PLAY_OFF);
//        }

        if(mediaManager.play()){
            imPlay.setImageLevel(LEVEL_PLAY_ON);
            updateSong();
        }else {
            imPlay.setImageLevel(LEVEL_PLAY_OFF);
            //van bai hat cu nen ko update song
        }

    }

    private void doShuffle() {

//        if(levelShuffle==LEVEL_SHUFFLE_OFF){
//            levelShuffle=LEVEL_SHUFFLE_ON;
//            imShuffle.setImageLevel(LEVEL_PLAY_ON);
//        }else {
//            levelShuffle=LEVEL_SHUFFLE_OFF;
//            imShuffle.setImageLevel(LEVEL_PLAY_OFF);
//        }
        if(mediaManager.isShuffle()){
            mediaManager.setShuffle(false);
            imShuffle.setImageLevel(LEVEL_SHUFFLE_OFF);
        }else {
            mediaManager.setShuffle(true);
            imShuffle.setImageLevel(LEVEL_SHUFFLE_ON);
        }


    }

    private void doRepeat() {

//        if(levelRepeat==LEVEL_REPEAT_OFF){
//            levelRepeat=LEVEL_REPEAT_ONE;
//            imRepeat.setImageLevel(LEVEL_REPEAT_ONE);
//        }else if(levelRepeat==LEVEL_REPEAT_ONE){
//            levelRepeat=LEVEL_REPEAT_ON;
//            imRepeat.setImageLevel(LEVEL_REPEAT_ON);
//        }else {
//            levelRepeat=LEVEL_REPEAT_OFF;
//            imRepeat.setImageLevel(LEVEL_REPEAT_OFF);
//        }

        if(mediaManager.getRepeatMode()== MediaManager.REPEAT_OFF){
            mediaManager.setRepeatMode(MediaManager.REPEAT_ONE);
            imRepeat.setImageLevel(MediaManager.REPEAT_ONE);
        }else if(mediaManager.getRepeatMode()== MediaManager.REPEAT_ONE){
            mediaManager.setRepeatMode(MediaManager.REPEAT_ON);
            imRepeat.setImageLevel(MediaManager.REPEAT_ON);
        }else {
            mediaManager.setRepeatMode(MediaManager.REPEAT_OFF);
            imRepeat.setImageLevel(MediaManager.REPEAT_OFF);
        }


    }

    @Override
    public void onClick(int position) {
//        Toast.makeText(this, "onClick", Toast.LENGTH_SHORT).show();
//        media.stop();



        mediaManager.play(position);
        imPlay.setImageLevel(LEVEL_PLAY_ON);

        updateSong();

//        tvNameSong.setText(listSong.get(position).getName());
//        tvSinger.setText(listSong.get(position).getSinger());
//        tvSerial.setText(position+"/"+songAdapter.getItemCount());
//        tvSerial.setText(position+"/"+listSong.size());
//        tvDuration.setText(listSong.get(position).getDuration());

//        media = MediaPlayer.create(this, Uri.parse(listSong.get(position).getPath()));
//        media.setNextMediaPlayer(MediaPlayer.create(this, Uri.parse(listSong.get(position+1).getPath())));
//        media.start();

    }

    private void checkAndRequestPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(permission);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
        }
    }



    //Override SEEK BAR LISTENER
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        progressSeekBar = progress;

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        mediaManager.seek(progressSeekBar);
        seekBar.setProgress(seekBar.getProgress());

    }



    private class UpdateSeekBarSong extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            while (mediaManager.isCheckPlay()){
                try {
                    Thread.sleep(100);
                    publishProgress();//void nen ko can truyen tham so
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
//            super.onProgressUpdate(values);
            tvDuration.setText(mediaManager.getTimeSong());
            sbSong.setProgress(mediaManager.getCurrentTime());
            if(mediaManager.getCurrentTime()==0)
            updateSong();
        }
    }


}
