package com.t3h.hc_musicapp.manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import com.t3h.hc_musicapp.MainActivity;
import com.t3h.hc_musicapp.model.Song;
import com.t3h.hc_musicapp.service.MusicService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediaManager implements MediaPlayer.OnCompletionListener {

    private static final int IDLE = 0;
    private static final int PLAYING = 1;
    private static final int PAUSED = 2;
    private static final int STOPPED = 3;

    public static final int REPEAT_OFF = 0;
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_ON = 2;

    private int repeatMode = REPEAT_OFF;


    private Context context;
    private MediaPlayer player;
    private List<Song> listSong;

    private int state = IDLE;

    private int index=0;

    private boolean isShuffle=false;

    public MediaManager(Context context) {
        this.context = context;
        initData();
    }

    public Song getCurrentSong() {
        return getListSong().get(index);
    }

    private void initData() {
        player = new MediaPlayer();
        listSong = new ArrayList<>();

        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String projection[] = {
                MediaStore.Audio.AudioColumns.TITLE,
                MediaStore.Audio.AudioColumns.DATA,
                MediaStore.Audio.AudioColumns.ALBUM,
                MediaStore.Audio.AudioColumns.ARTIST,
                MediaStore.Audio.AudioColumns.DURATION
        };

        String where = MediaStore.Audio.AudioColumns.DISPLAY_NAME + " LIKE '%.mp3'";
        Cursor c = context.getContentResolver().query(audioUri, projection, where, null, null);

        if (c == null) {
            return;
        }

        c.moveToFirst();

        int indexTitle = c.getColumnIndex(projection[0]);
        int indexData = c.getColumnIndex(projection[1]);
        int indexAlbum = c.getColumnIndex(projection[2]);
        int indexArtist = c.getColumnIndex(projection[3]);
        int indexDuration = c.getColumnIndex(projection[4]);

        String name, path, album, singer;
        int duration;

        while (!c.isAfterLast()) {
            name = c.getString(indexTitle);
            path = c.getString(indexData);
            album = c.getString(indexAlbum);
            singer = c.getString(indexArtist);
            duration = c.getInt(indexDuration);

            listSong.add(new Song(name, path, album, singer, duration));

            c.moveToNext();
        }
        c.close();
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public boolean play() {

            if (state == IDLE || state == STOPPED) {
                Song song = listSong.get(index);
//                player.setDataSource(song.getPath());
//                player.prepare();

                player = MediaPlayer.create(context,Uri.parse(song.getPath()));

                player.setOnCompletionListener(this);

                player.start();
                state=PLAYING;
                return true;
            }else if(state == PLAYING){
                player.pause();
                state=PAUSED;
                return false;
            }else {
                player.start();
                state = PLAYING;
                return true;
            }
    }

    public void play(int position){
        index = position;
        stop();
        play();
    }


    public void stop(){
        if(state==PLAYING||state==PAUSED){
            player.stop();
            state=STOPPED;
        }
    }

    public boolean previous(){

        if (index==0){
            index=listSong.size();
        }
        index--;

        stop();
        return play();
    }

    public boolean next(){

        if(isShuffle){
            index = new Random().nextInt(listSong.size());
        }else {
//            if (index==listSong.size()){
//                index=0;
//            }else index++;
            index = (index+1)%listSong.size();
        }
        stop();
        return play();
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        switch (repeatMode){
            case REPEAT_OFF:
                if(index<(listSong.size()-1)){
                    index++;
                    stop();
                    play();
                }
                break;
            case REPEAT_ONE:
                stop();
                play();
                break;
            case REPEAT_ON:
                index++;
                if(index==listSong.size()){
                    index=0;
                }
                stop();
                play();
                break;
            default:
                break;
        }

    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public int getPositionSong() {
        return index;
    }

    public boolean isCheckPlay() {
        return state==PLAYING||state==PAUSED;
    }

    public String getTimeSong() {
        int currentTime = player.getCurrentPosition();
        int totalTime = listSong.get(index).getDuration();
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        return dateFormat.format(currentTime)+"/"+dateFormat.format(totalTime);
    }

    public int getCurrentTime() {
        return player.getCurrentPosition();
    }

    public void seek(int progress) {
        player.seekTo(progress);
    }

    public Bitmap getAlbumImage(String path) {
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) return BitmapFactory.decodeByteArray(data, 0, data.length);
        return null;
    }


}
