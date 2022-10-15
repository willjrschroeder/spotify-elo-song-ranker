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

    private final AppUser testDisabledAppUser1 = new AppUser(1, "newTestUsername",
            "$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy", "Test Create",
            true, new ArrayList<AppRole>() {
    });



    private final SpotifyData testSpotifyData = new SpotifyData(
            testPlaylist1,
            testTracks);

    @Test
    void shouldAddValidData(){
        SpotifyData data = testSpotifyData;

        when(repository.addSpotifyData(testSpotifyData)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        assertTrue(result.isSuccess());
    }

    @Test
    void shouldNotAddNullPlaylist(){

        SpotifyData data = new SpotifyData(null, null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        assertEquals("[Playlist is required]", result.getMessages().toString());

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullOrEmptyTracksArray(){

        SpotifyData data = testSpotifyData;
        data.setTracks(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.setTracks(new ArrayList<Track>());

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All playlists must have tracks]", result.getMessages().toString());
        assertEquals("[All playlists must have tracks]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddDuplicatePlaylistUriAndUserIdCombination(){
        SpotifyData data = testSpotifyData;

        // when(repository.getPlaylistByPlaylistUriAndUserId(data.getPlaylist().getPlaylistUri(),
        // data.getPlaylist().getAppUserId()))
        // .thenReturn(testPlaylist1);
                // data.getPlaylist().getAppUserId()))
                // .thenReturn(testPlaylist1);

        Result result = service.addSpotifyData(data);

        assertEquals("[Playlist is already added to the database]", result.getMessages().toString());

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankPlaylistUri(){

        SpotifyData data = testSpotifyData;
        data.getPlaylist().setPlaylistUri(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getPlaylist().setPlaylistUri("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[Playlist URI is required]", result.getMessages().toString());
        assertEquals("[Playlist URI is required]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankPlaylistName(){

        SpotifyData data = testSpotifyData;
        data.getPlaylist().setPlaylistName(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getPlaylist().setPlaylistName("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[Playlist name is required]", result.getMessages().toString());
        assertEquals("[Playlist name is required]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankPlaylistUrl(){

        SpotifyData data = testSpotifyData;
        data.getPlaylist().setPlaylistUrl(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getPlaylist().setPlaylistUrl("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[Playlist Spotify url is required]", result.getMessages().toString());
        assertEquals("[Playlist Spotify url is required]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddLongPlaylistDescription(){

        SpotifyData data = testSpotifyData;
        data.getPlaylist().setDescription("aaaaaaaaaaaaaaaaaaaaaaaaa      " +
                "aaaaaaaaaaaa    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        assertEquals("[Description must be less than 300 characters]", result.getMessages().toString());

        assertFalse(result.isSuccess());
    }

    @Test
    void shouldAddNullOrBlankPlaylistDescription(){

        SpotifyData data = testSpotifyData;
        data.getPlaylist().setDescription(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getPlaylist().setDescription("");

        Result result2 = service.addSpotifyData(data);

        assertTrue(result.isSuccess());
        assertTrue(result2.isSuccess());
    }

    @Test
    void shouldAddNotPlaylistWithMissingOrDisabledUserId(){

        SpotifyData data = testSpotifyData;
        data.getPlaylist().setAppUserId(0);
        AppUser disabledAppUser = testDisabledAppUser1;

        // when(repository.getAppUserById(data.getPlaylist().getAppUserId())).thenReturn(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getPlaylist().setAppUserId(-1);
        Result result2 = service.addSpotifyData(data);

        data.getPlaylist().setAppUserId(1);
        // when(repository.getAppUserById(data.getPlaylist().getAppUserId())).thenReturn(disabledAppUser);
        Result result3 = service.addSpotifyData(data);

        assertEquals("[Playlist must contained an existing appUserId]", result.getMessages().toString());
        assertEquals("[Playlist must contained an existing appUserId]", result2.getMessages().toString());
        assertEquals("[Playlist must contained an non-disabled appUserId]", result3.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
        assertFalse(result3.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankTrackUri(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).setTrack_uri(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).setTrack_uri("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All tracks must have a Spotify URI]", result.getMessages().toString());
        assertEquals("[All tracks must have a Spotify URI]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankTrackTitle(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).setTitle(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).setTitle("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All tracks must have a title]", result.getMessages().toString());
        assertEquals("[All tracks must have a title]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankTrackArtistArray(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).setArtists(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).setArtists(new ArrayList<Artist>());

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All tracks must have artists]", result.getMessages().toString());
        assertEquals("[All tracks must have artists]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddNullOrBlankTrackAlbumArray(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).setAlbums(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).setAlbums(new ArrayList<Album>());

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All tracks must have an album]", result.getMessages().toString());
        assertEquals("[All tracks must have an album]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddBadTrackDuration(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).setTrackDuration(0);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).setTrackDuration(-1);

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All tracks must have a non-negative duration]", result.getMessages().toString());
        assertEquals("[All tracks must have a non-negative duration]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddArtistWithNullOrBlankUri(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getArtists().get(0).setArtistUri(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getArtists().get(0).setArtistUri("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All artists must have a Spotify URI]", result.getMessages().toString());
        assertEquals("[All artists must have a Spotify URI]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddArtistWithNullOrBlankName(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getArtists().get(0).setArtistName(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getArtists().get(0).setArtistName("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All artists must have a name]", result.getMessages().toString());
        assertEquals("[All artists must have a name]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddArtistWithNullOrBlankUrl(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getArtists().get(0).setSpotifyUrl(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getArtists().get(0).setSpotifyUrl("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All artists must have a Spotify url]", result.getMessages().toString());
        assertEquals("[All artists must have a Spotify url]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldAddArtistWithNullOrBlankImageLink(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getArtists().get(0).setArtistImageLink(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getArtists().get(0).setArtistImageLink("");

        Result result2 = service.addSpotifyData(data);

        assertTrue(result.isSuccess());
        assertTrue(result2.isSuccess());
    }

    @Test
    void shouldAddArtistWithNullOrBlankGenresArray(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getArtists().get(0).setGenres(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getArtists().get(0).setGenres(new ArrayList<Genre>());

        Result result2 = service.addSpotifyData(data);

        assertTrue(result.isSuccess());
        assertTrue(result2.isSuccess());
    }

    @Test
    void shouldNotAddArtistWithNullOrBlankGenreName(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getArtists().get(0).getGenres().get(0).setGenreName(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getArtists().get(0).getGenres().get(0).setGenreName("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All genres must have a name]", result.getMessages().toString());
        assertEquals("[All genres must have a name]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddAlbumWithNullOrBlankUri(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getAlbums().get(0).setAlbumUri(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getAlbums().get(0).setAlbumUri("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All albums must have a Spotify URI]", result.getMessages().toString());
        assertEquals("[All albums must have a Spotify URI]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddAlbumWithNullOrBlankName(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getAlbums().get(0).setAlbumName(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getAlbums().get(0).setAlbumName("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All albums must have a name]", result.getMessages().toString());
        assertEquals("[All albums must have a name]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddAlbumWithNullOrBlankUrl(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getAlbums().get(0).setSpotifyUrl(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        data.getTracks().get(0).getAlbums().get(0).setSpotifyUrl("");

        Result result2 = service.addSpotifyData(data);

        assertEquals("[All albums must have a Spotify url]", result.getMessages().toString());
        assertEquals("[All albums must have a Spotify url]", result2.getMessages().toString());

        assertFalse(result.isSuccess());
        assertFalse(result2.isSuccess());
    }

    @Test
    void shouldNotAddAlbumWithInvalidDate(){

        SpotifyData data = testSpotifyData;
        data.getTracks().get(0).getAlbums().get(0).setReleaseDate(null);

        when(repository.addSpotifyData(data)).thenReturn(true);

        Result result = service.addSpotifyData(data);

        assertEquals("[All albums must have a release date]", result.getMessages().toString());

        assertFalse(result.isSuccess());
    }

}