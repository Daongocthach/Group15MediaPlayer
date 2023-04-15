package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Interface.IClickItem;
import Model.Song;
import hcmute.thach.group15mediaplayer.PlayMediaActivity;
import hcmute.thach.group15mediaplayer.R;


public class NewSongAdapter extends RecyclerView.Adapter<NewSongAdapter.NewSongViewHolder> {
    private List<Song> songList;
    private IClickItem iClickItem;

    public NewSongAdapter(List<Song> songList, IClickItem iClickItem) {
        this.songList = songList;
        this.iClickItem = iClickItem;
    }

    public void setData(List<Song> songList){
        this.songList = songList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewSongAdapter.NewSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_item, parent,false);
        return new NewSongAdapter.NewSongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewSongAdapter.NewSongViewHolder holder, int position) {

        Song song = songList.get(position);
        if(song==null){
            return;
        }

        Picasso.get()
                .load(song.getImage().trim())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imgsource);
        holder.name.setText(song.getTittle());
        holder.Songan.setText(song.getSingle());

        holder.newSonglayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItem.onClickItemSong(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(songList!=null){
            return songList.size();
        }
        return 0;
    }

    public class NewSongViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout newSonglayout;
        private TextView name, Songan;
        private ImageView imgsource;
        public NewSongViewHolder(@NonNull View itemView) {
            super(itemView);
            newSonglayout = itemView.findViewById(R.id.newmusic_layout);
            name = itemView.findViewById(R.id.tvName);
            Songan = itemView.findViewById(R.id.tvmusician);
            imgsource = itemView.findViewById(R.id.imageViewItem);
        }
    }
}