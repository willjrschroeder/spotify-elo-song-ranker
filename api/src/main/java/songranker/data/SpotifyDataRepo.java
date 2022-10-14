package songranker.data;

import org.springframework.transaction.annotation.Transactional;
import songranker.models.*;

import java.util.List;

public interface SpotifyDataRepo {


    @Transactional
    Playlist createPlaylist(SpotifyData spotifyData);

    @Transactional
    List<Track> createTrack(SpotifyData spotifyData);

    @Transactional
    List<Artist> createArtist(SpotifyData spotifyData);

    @Transactional
    List<Album> createAlbum(SpotifyData spotifyData);

    @Transactional
    List<Genre> createGenre(SpotifyData spotifyData);
}
