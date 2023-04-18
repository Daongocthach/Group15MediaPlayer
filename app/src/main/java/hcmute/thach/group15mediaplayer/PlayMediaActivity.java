package hcmute.thach.group15mediaplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Model.Song;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMediaActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 10;
    private FloatingActionButton btn;
    private CircleImageView imageMusic;
    private TextView textViewName, textViewMusican;

    private RelativeLayout layout_bottom;
    private CircleImageView imgSong;
    private ImageView imgPlayOrPause, imgClose, btnStartStopService, btnNext, btnPrev;
    private TextView tvTitle, tvSingle;
    private FloatingActionButton btnStopService;
    private Song mSong;
    private boolean isPlaying;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playmedia_layout);

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(broadcastReceiver, new IntentFilter("send_data_to_activity"));

        imageMusic = findViewById(R.id.imageMusic);
        btnNext = findViewById(R.id.next_button);
        btnPrev = findViewById(R.id.previous_button);
        textViewName = findViewById(R.id.tvName);
        textViewMusican = findViewById(R.id.tvmusician);
        btnStartStopService = findViewById(R.id.btn_start_stop_service);
        btnStopService = findViewById(R.id.btn_stop_service);
        layout_bottom = findViewById(R.id.layout_bottom);
        imgSong = findViewById(R.id.img_song);
        imgPlayOrPause = findViewById(R.id.playorpause);
        imgClose = findViewById(R.id.close);
        tvTitle = findViewById(R.id.tv_tittle);
        tvSingle = findViewById(R.id.tv_single_song);
        ArrayList<Song> songList = (ArrayList<Song>) getIntent().getSerializableExtra("songList");
        pos = getIntent().getIntExtra("pos", 0);
        loadImage(songList, pos);

        btnStartStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    clickStopService();
                } else {
                    clickStartService(songList, pos);
                }
            }
        });
        btnStopService.setOnClickListener((view -> checkPermission()));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos++;
                loadImage(songList, pos);
                clickStopService();
                clickStartService(songList, pos);
            }
        });

    }

    private void loadImage(ArrayList<Song> songList, int pos) {
        if (!songList.isEmpty()) {
            mSong = songList.get(pos);
            textViewMusican.setText(mSong.getSingle());
            textViewName.setText(mSong.getTittle());
            Picasso.get()
                    .load(mSong.getImage().trim())
                    .into(imageMusic);
        }
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_PERMISSION_CODE);
            } else {
                startDowloadFile();
            }
        } else {
            startDowloadFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startDowloadFile();
            } else {
                Toast.makeText(this, "Permisson Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDowloadFile() {
        String urlFile = mSong.getResource();
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlFile));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download");
        request.setDescription("Download file...");

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(System.currentTimeMillis()));

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
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
                } else {
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
        sendActionToService(MyService.ACTION_CLEAR);
        stopService(intent);

    }

    private void clickStartService(ArrayList<Song> songList, int pos) {
        Intent intent = new Intent(this, MyService.class);
        intent.putExtra("songList", songList);
        intent.putExtra("pos", pos);
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