package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import songranker.models.*;

public class SpotifyDataJdbcRepo implements SpotifyDataRepo {

    @Autowired
    JdbcTemplate template;



    @Override
    @Transactional
    public Track createPlaylist(SpotifyData spotifyData){
        return null;
    }

    @Override
    @Transactional
    public Artist createArtist(SpotifyData spotifyData){
        return null;
    }

    @Override
    @Transactional
    public Album createAlbum(SpotifyData spotifyData){
        return null;
    }

    @Override
    @Transactional
    public Genre createGenre(SpotifyData spotifyData){
        return null;
    }

}
