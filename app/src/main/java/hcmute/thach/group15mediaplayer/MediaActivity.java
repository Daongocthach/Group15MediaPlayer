package hcmute.thach.group15mediaplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import Adapter.CategoryAdapter;
import Adapter.NewSongAdapter;
import Interface.IClickItem;
import Model.Category;
import Model.Song;

public class MediaActivity extends AppCompatActivity {
    ImageView img_home;
    RecyclerView recyclerView, recyclerViewNew;
    private CategoryAdapter categoryAdapter;
    private NewSongAdapter newSongAdapter;

    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    List<Song> listSong;
    List<Category> listCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_layout);
        img_home = findViewById(R.id.imageViewhome);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setData(getListCategory());
//        recyclerView.setAdapter(categoryAdapter);

//        recyclerViewNew = (RecyclerView) findViewById(R.id.recycle_view_new);
//        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
//        recyclerViewNew.setLayoutManager(linearLayoutManager1);
//        newSongAdapter = new NewSongAdapter(getListSong(), new IClickItem() {
//            @Override
//            public void onClickItemSong(Song song) {
//                onCLickGoToPlayMedia(song);
//            }
//        });
        newSongAdapter.setData(getListSong());
        recyclerViewNew.setAdapter(newSongAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
//
//        img_home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

//        mStorage = FirebaseStorage.getInstance();
//        mDatabaseRef = FirebaseDatabase.getInstance().getReference("audios");
//
//        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                listSong.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Song song = postSnapshot.getValue(Song.class);
//                    song.setKey(postSnapshot.getKey());
//                    listSong.add(song);
//                }
//                newSongAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(MediaActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private List<Category> getListCategory(){

        String str1 = "https://firebasestorage.googleapis.com/v0/b/insertdata-f6680.appspot.com/o/file_music.mp3?alt=media&token=7185e11f-c175-44ef-825f-fd90b2a53166";
        String str2 = "https://th.bing.com/th/id/OIP.iSu2RcCcdm78xbxNDJMJSgHaEo?pid=ImgDet&rs=1";

        listSong.add(new Song("JingleBell", "Christmas Song", str2, str1));
        listSong.add(new Song("Moonlight Sonata", "Beethoven", str2, str1));
        listSong.add(new Song("Requiem", "Mozart", str2, str1));
        listSong.add(new Song("Nocturne", "Christmas Song", str2, str1));
        listSong.add(new Song("Sonata", "Christmas Song", str2, str1));
        listSong.add(new Song("Tom and Jerry", "Christmas Song", str2, str1));
        listSong.add(new Song("JingleBell", "Christmas Song", str2, str1));


        listCategory.add(new Category("Danh sách nhạc",listSong));
        listCategory.add(new Category("Nhạc nổi bật",listSong));
        listCategory.add(new Category("Bảng xếp hạng tuần",listSong));
        return listCategory;
    }
    private List<Song> getListSong() {
        String str1 = "https://firebasestorage.googleapis.com/v0/b/insertdata-f6680.appspot.com/o/file_music.mp3?alt=media&token=7185e11f-c175-44ef-825f-fd90b2a53166";
        String str2 = "https://th.bing.com/th/id/OIP.iSu2RcCcdm78xbxNDJMJSgHaEo?pid=ImgDet&rs=1";
        listSong.add(new Song("JingleBell", "Christmas Song", str2, str1));
        listSong.add(new Song("Moonlight Sonata", "Beethoven", str2, str1));
        listSong.add(new Song("Requiem", "Mozart", str2, str1));
        listSong.add(new Song("Nocturne", "Christmas Song", str2, str1));
        listSong.add(new Song("Sonata", "Christmas Song", str2, str1));
        listSong.add(new Song("Tom and Jerry", "Christmas Song", str2, str1));
        listSong.add(new Song("JingleBell", "Christmas Song", str2, str1));
        return listSong;
    }
    private void onCLickGoToPlayMedia(Song song){
        Intent intent = new Intent(this, PlayMediaActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
