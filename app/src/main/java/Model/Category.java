package Model;

import java.util.List;

public class Category {
    String title;
    List<Song> songList;

    public Category(String title, List<Song> songList) {
        this.title = title;
        this.songList = songList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }
}
