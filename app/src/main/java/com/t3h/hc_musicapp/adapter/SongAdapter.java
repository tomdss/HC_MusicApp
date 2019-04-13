package com.t3h.hc_musicapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.t3h.hc_musicapp.R;
import com.t3h.hc_musicapp.model.Song;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {


    private List<Song> listSong;
    private SongListener listener;
    private LayoutInflater inflater;

    public SongAdapter(Context context, List<Song> listSong) {
        this.listSong = listSong;
        inflater = LayoutInflater.from(context);
    }

    public void setListener(SongListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = inflater.inflate(R.layout.item_song,viewGroup,false);
        SongHolder holder = new SongHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongHolder songHolder, final int i) {
        Song s = listSong.get(i);
        songHolder.bindData(s);
        songHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listSong.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder{

        private TextView textViewName;
        private TextView textViewSinger;

        public SongHolder(@NonNull View itemView) {
            super(itemView);
            textViewName=itemView.findViewById(R.id.text_view_name);
            textViewSinger=itemView.findViewById(R.id.text_view_singer);
        }

        public void bindData(Song song){
            textViewName.setText(song.getName());
            textViewSinger.setText(song.getSinger());
        }

    }

    public interface SongListener{
        void onClick(int position);
    }

}
