package songranker.data;

import org.springframework.transaction.annotation.Transactional;
import songranker.models.*;

public interface SpotifyDataRepo {
    @Transactional
    Track createPlaylist(SpotifyData spotifyData);

    @Transactional
    Artist createArtist(SpotifyData spotifyData);

    @Transactional
    Album createAlbum(SpotifyData spotifyData);

    @Transactional
    Genre createGenre(SpotifyData spotifyData);
}
