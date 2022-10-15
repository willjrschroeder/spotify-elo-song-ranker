package songranker.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import songranker.data.AppUserRepo;
import songranker.data.SpotifyDataJdbcRepo;
import songranker.models.*;
import songranker.security.UserDetailsServiceImplementation;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplementationTest {
    @Autowired
    SpotifyDataService service;

    @MockBean
    SpotifyDataJdbcRepo repository;

    private final Playlist testPlaylist1 = new Playlist(
            "38cfqZXcGK4KPtDrGUNMkI",
            "My first playlist!",
            "Here&#x27;s the playlist description",
            "https://open.spotify.com/playlist/38cfqZXcGK4KPtDrGUNMkI",
            "https://i.scdn.co/image/ab67616d0000b27368968350c2550e36d96344ee",
            1);

    private final Genre testGenre1 = new Genre("afrofuturism");

    private final Genre testGenre2 = new Genre("pop");

    private final Genre testGenre3 = new Genre("chicago rap");

    private final Genre testGenre4 = new Genre("rap");

    private final Genre testGenre5 = new Genre("antiviral pop");

    private final Genre testGenre6 = new Genre("comic");

    private final Genre testGenre7 = new Genre("meme rap");

    private final Genre testGenre8 = new Genre("social media pop");

    private final List<Genre> testGenres1 = List.of(testGenre1, testGenre2);

    private final List<Genre> testGenres2 = List.of(testGenre3, testGenre4);

    private final List<Genre> testGenres3= List.of();

    private final List<Genre> testGenres4= List.of(testGenre5, testGenre6, testGenre7, testGenre8);

    private final List<Genre> testGenres5= List.of();

    private final Artist testArtist1 = new Artist("57vWImR43h4CaDao012Ofp", "Steve Lacy", "https://open.spotify.com/artist/57vWImR43h4CaDao012Ofp","https://i.scdn.co/image/ab6761610000e5eb09ac9d040c168d4e4f58eb42", testGenres1);

    private final Artist testArtist2 = new Artist("5K4W6rqBFWDnAN6FQUkS6x", "Kanye West", "https://open.spotify.com/artist/5K4W6rqBFWDnAN6FQUkS6x", "https://i.scdn.co/image/ab6761610000e5eb867008a971fae0f4d913f63a", testGenres2);

    private final Artist testArtist3 = new Artist("2knWnzQWhtUg9J3zVsEYm8", "Tariq", "https://open.spotify.com/artist/2knWnzQWhtUg9J3zVsEYm8", "https://i.scdn.co/image/ab67616d0000b273a1d09d85651bec771fefa1cc", testGenres3);

    private final Artist testArtist4 = new Artist("0V8tQXWkKPD5SxsB2moGew", "The Gregory Brothers", "https://open.spotify.com/artist/0V8tQXWkKPD5SxsB2moGew", "https://i.scdn.co/image/ab6761610000e5eb74b7bbea0e65134152b72022", testGenres4);

    private final Artist testArtist5 = new Artist("0WSxLJRrB4L65JhNxIajE2", "Recess Therapy", "https://open.spotify.com/artist/0WSxLJRrB4L65JhNxIajE2", "https://i.scdn.co/image/ab67616d0000b273a1d09d85651bec771fefa1cc", testGenres5);

    private final List<Artist> testArtists1 = List.of(testArtist1);
    private final List<Artist> testArtists2 = List.of(testArtist2);

    private final List<Artist> testArtists3 = List.of(testArtist3, testArtist4, testArtist5);

    private final Album testAlbum1 = new Album("3Ks0eeH0GWpY4AU20D5HPD", "Gemini Rights", new Date(2022, Calendar.AUGUST,15), "https://i.scdn.co/image/ab67616d0000b27368968350c2550e36d96344ee", "https://open.spotify.com/album/3Ks0eeH0GWpY4AU20D5HPD");

    private final Album testAlbum2 = new Album("7D2NdGvBHIavgLhmcwhluK", "Yeezus", new Date(2013, Calendar.JUNE, 18), "https://i.scdn.co/image/ab67616d0000b2731dacfbc31cc873d132958af9", "https://open.spotify.com/album/7D2NdGvBHIavgLhmcwhluK");

    private final Album testAlbum3 = new Album("6UA672BEiCeihRReCCnlPb", "It's Corn", new Date(2022, Calendar.AUGUST, 28), "https://i.scdn.co/image/ab67616d0000b273a1d09d85651bec771fefa1cc", "6UA672BEiCeihRReCCnlPb");

    private final List<Album> testAlbums1 = List.of(testAlbum1);

    private final List<Album> testAlbums2 = List.of(testAlbum2);

    private final List<Album> testAlbums3 = List.of(testAlbum3);

    private final Track testTrack1 = new Track("4k6Uh1HXdhtusDW5y8Gbvy", 1,"Bad Habit", 1000, 0, 232006, 90,
            "https://open.spotify.com/track/4k6Uh1HXdhtusDW5y8Gbvy", "https://p.scdn.co/mp3-preview/b46cf3781e6cbe5bd01b8a81e333899c7f9cdfc5?cid=b055b73f53474f3e931fd58a080ca3cf",
            testArtists1, testAlbums1);
    private final Track testTrack2 = new Track ("3sNVsP50132BTNlImLx70i", 1,"Bound 2", 1000, 0, 229146, 89,
            "https://open.spotify.com/track/3sNVsP50132BTNlImLx70i", "", testArtists2, testAlbums2);
    private final Track testTrack3 = new Track("58WiMFVLbAfmS7k4T0qKtG", 1,"It's Corn", 1000, 0, 167733, 75,
            "https://open.spotify.com/track/58WiMFVLbAfmS7k4T0qKtG", "https://p.scdn.co/mp3-preview/bb70b6c247fc5e32af241a66dae9e863f37da787?cid=b055b73f53474f3e931fd58a080ca3cf",
            testArtists3, testAlbums3);

    private final List<Track> testTracks = Arrays.asList(testTrack1, testTrack2, testTrack3);



    private final SpotifyData testSpotifyData = new SpotifyData(
            testPlaylist1,
            testTracks);

    @Test
    void shouldAddValidPlaylist(){
        SpotifyData data = testSpotifyData;

        when(repository.addSpotifyData(testSpotifyData)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        System.out.println((result.getMessages()));
        assertTrue(result.isSuccess());
    }



}