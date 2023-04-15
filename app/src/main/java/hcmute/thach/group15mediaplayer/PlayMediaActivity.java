package hcmute.thach.group15mediaplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import Model.Song;

public class PlayMediaActivity extends AppCompatActivity {
    private FloatingActionButton btn;
    private ImageView imageMusic, imgPlay;
    private TextView textViewName, textViewMusican;
    private MediaPlayer mediaPlayer;

    private RelativeLayout layout_bottom;
    private ImageView imgSong, imgPlayOrPause, imgClose;
    private TextView tvTitle, tvSingle;
    private ImageView btnStartService;
    private FloatingActionButton btnStopService;
    private Song mSong;
    private boolean isPlaying;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmedia_layout);

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));

        imageMusic = (ImageView) findViewById(R.id.imageMusic);
        textViewName = findViewById(R.id.tvName);
        textViewMusican = findViewById(R.id.tvmusician);

        btnStartService = findViewById(R.id.btn_start_service);
        btnStopService = findViewById(R.id.btn_stop_service);
        layout_bottom = findViewById(R.id.layout_bottom);
        imgSong = findViewById(R.id.img_song);
        imgPlayOrPause = findViewById(R.id.playorpause);
        imgClose = findViewById(R.id.close);
        tvTitle = findViewById(R.id.tv_tittle);
        tvSingle = findViewById(R.id.tv_single_song);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Song song = (Song) bundle.get("object_song");
            if (song != null) {
                mSong = song;
            }
        }
        btnStartService.setOnClickListener((view -> clickStartService(mSong)));
        btnStopService.setOnClickListener((view -> clickStopService()));
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            mSong = (Song) bundle.get("object_song");
            isPlaying = bundle.getBoolean("status_player");
            int actionMusic = bundle.getInt("action_music");

            handleLayoutMusic(actionMusic);
        }
    };

    private void handleLayoutMusic(int actionMusic) {
        switch (actionMusic) {
            case MyService.ACTION_START:
                layout_bottom.setVisibility(View.VISIBLE);
                showInfoSong();
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_PAUSE:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_RESUME:
                setStatusButtonPlayOrPause();
                break;
            case MyService.ACTION_CLEAR:
                layout_bottom.setVisibility(View.GONE);
                break;
        }
    }

    private void showInfoSong() {
        if (mSong == null) {
            return;
        }
        Picasso.get()
                .load(mSong.getImage().trim())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(imgSong);
        tvTitle.setText(mSong.getTittle());
        tvSingle.setText(mSong.getSingle());

        imgPlayOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    sendActionToService(MyService.ACTION_PAUSE);
                }
                else{
                    sendActionToService(MyService.ACTION_RESUME);
                }
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendActionToService(MyService.ACTION_CLEAR);
            }
        });
    }

    private void setStatusButtonPlayOrPause() {
        if (isPlaying) {
            imgPlayOrPause.setImageResource(R.drawable.pause);
        } else {
            imgPlayOrPause.setImageResource(R.drawable.play);
        }
    }

    private void sendActionToService(int action) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("action_music_service", action);

        startService(intent);
    }



    private void clickStopService() {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
    }

    private void clickStartService(Song song) {
        String str1 = "https://firebasestorage.googleapis.com/v0/b/insertdata-f6680.appspot.com/o/file_music.mp3?alt=media&token=7185e11f-c175-44ef-825f-fd90b2a53166";
        String str2 = "https://th.bing.com/th/id/OIP.iSu2RcCcdm78xbxNDJMJSgHaEo?pid=ImgDet&rs=1";
        Song song1 = new Song("JingleBell", "Christmas Song", str2, str1);
        Intent intent = new Intent(this, MyService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object_song", song1);
        intent.putExtras(bundle);
        startService(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager
                .getInstance(this)
                .unregisterReceiver(broadcastReceiver);
    }

}