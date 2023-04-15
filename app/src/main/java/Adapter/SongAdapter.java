package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import Model.Song;
import de.hdodenhof.circleimageview.CircleImageView;
import hcmute.thach.group15mediaplayer.R;


public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {
    List<Song> songList;

    public void setData(List<Song> songList){
        this.songList   = songList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent,false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        if(song==null){
            return;
        }
        //holder.imgsource.setImageResource(song.getImage());
        Picasso.get()
                .load(song.getImage().trim())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imgsource);
        holder.name.setText(song.getTittle());
        holder.single.setText(song.getSingle());

    }

    @Override
    public int getItemCount() {
        if(songList!=null){
            return songList.size();
        }
        return 0;
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        private TextView name, single;
        private CircleImageView imgsource;
        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvName);
            single = itemView.findViewById(R.id.tvmusician);
            imgsource = itemView.findViewById(R.id.circle_image_view);
        }
    }
}