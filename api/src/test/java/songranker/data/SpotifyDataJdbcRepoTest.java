package songranker.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import songranker.models.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpotifyDataJdbcRepoTest {

    @Autowired
    SpotifyDataJdbcRepo repo;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup(){knownGoodState.set();}

    private final Playlist testPlaylist1 = new Playlist(
            "38cfqZXcGK4KPtDrGUNMkI",
            "My first playlist!",
            "Here&#x27;s the playlist description",
            "https://open.spotify.com/playlist/38cfqZXcGK4KPtDrGUNMkI",
            "https://i.scdn.co/image/ab67616d0000b27368968350c2550e36d96344ee",
            1);

    private List<Genre> testGenres1 = new ArrayList<>();

    private List<Genre> testGenres2 = new ArrayList<>();

    private List<Genre> testGenres3= new ArrayList<>();

    private List<Artist> testArtists1 = new ArrayList<>();

    private Artist testArtist1 = new Artist("57vWImR43h4CaDao012Ofp", "Steve Lacy", "https://open.spotify.com/artist/57vWImR43h4CaDao012Ofp","", testGenres1);

    private List<Artist> testArtists2 = new ArrayList<>();

    private List<Artist> testArtists3 = new ArrayList<>();

    private List<Album> testAlbums1 = new ArrayList<>();

    private List<Album> testAlbums2 = new ArrayList<>();

    private List<Album> testAlbums3 = new ArrayList<>();

    private List<Track> testTracks = new ArrayList<>();
        Track testTrack1 = new Track("4k6Uh1HXdhtusDW5y8Gbvy", "Bad Habit", 1000, 0, 232006, 90,
                "https://open.spotify.com/track/4k6Uh1HXdhtusDW5y8Gbvy", "https://p.scdn.co/mp3-preview/b46cf3781e6cbe5bd01b8a81e333899c7f9cdfc5?cid=b055b73f53474f3e931fd58a080ca3cf",
                testArtists1, testAlbums1);
        Track testTrack2;


    private final SpotifyData testSpotifyData = new SpotifyData(
            testPlaylist1,
            testTracks);

    @Test
    void shouldCreateValidPlaylist() {

    }

    @Test
    void createTrack() {
    }

    @Test
    void createArtist() {
    }

    @Test
    void createAlbum() {
    }

    @Test
    void createGenre() {
    }
}