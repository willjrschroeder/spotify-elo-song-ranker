package songranker.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import songranker.data.AppUserRepo;
import songranker.data.SpotifyDataJdbcRepo;
import songranker.models.Playlist;
import songranker.models.Result;
import songranker.models.SpotifyData;
import songranker.models.Track;
import songranker.security.UserDetailsServiceImplementation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDetailsServiceImplementationTest {
    @Autowired
    SpotifyDataService service;

    @MockBean
    SpotifyDataJdbcRepo repository;

    @Test
    void shouldAddValidPlaylist(){
        SpotifyData data = createValidTestData();

        Result result = service.addSpotifyData(data);


    }

    private SpotifyData createValidTestData(){ //TODO: add data
        Playlist playlist = new Playlist();
        List<Track> tracks = new ArrayList<>();

        SpotifyData data = new SpotifyData(playlist, tracks);

        return data;
    }


}