package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import songranker.data.mappers.*;
import songranker.models.*;

import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Repository
public class SpotifyDataJdbcRepo implements SpotifyDataRepo {

    @Autowired
    JdbcTemplate template;

    @Transactional
    public boolean deleteSpotifyData(String playlistUri, int appUserId) {
        List<String> trackUris = getTrackUrisByPlaylistUri(playlistUri);

        for (String eachUri : trackUris) {
            template.update("delete from track_album where track_uri = ?;", eachUri);
            template.update("delete from track_artist where track_uri = ?;", eachUri);
            template.update("delete from playlist_track where track_uri = ? and app_user_id = ? and playlist_uri = ?;", eachUri, appUserId, playlistUri);
            template.update("delete from track where track_uri = ? and app_user_id = ?;", eachUri, appUserId);
        }

        return (template.update("delete from playlist where playlist_uri = ? and app_user_id = ?;", playlistUri, appUserId)) > 0;
    }

    private List<String> getTrackUrisByPlaylistUri(String playlistUri) {

        final String sql = "select track_uri from playlist_track\n"
                +"where playlist_uri = ?;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("track_uri");}, playlistUri);
    }

    @Override
    @Transactional
    public boolean addSpotifyData(SpotifyData spotifyData) {

        try {
            createPlaylist(spotifyData);
            createTrack(spotifyData);
            createArtist(spotifyData);
            createAlbum(spotifyData);
            createGenre(spotifyData);

            addPlaylistTrack(spotifyData);
            addTrackArtist(spotifyData);
            addTrackAlbum(spotifyData);
            addGenreArtist(spotifyData);

            return true;
        } catch (UnsupportedOperationException ex) {
            return false;
        }
    }

    @Override
    public Playlist getPlaylistByPlaylistUri(String playlistUri, int app_user_id) {

        final String sql = "select playlist_uri, playlist_name, description, playlist_url, playlist_image_link, app_user_id "
                + "from playlist where (playlist_uri, app_user_id) = (?, ?)";


        return template.query(sql, new PlaylistMapper(), playlistUri, app_user_id).stream().findFirst().orElse(null);
    }

    private String getPlaylistUri(SpotifyData spotifyData) {

        final String sql = "select playlist_uri from playlist where playlist_uri = ?;";

        Playlist playlist = template.query(sql, new PlaylistMapper(), spotifyData.getPlaylist().getPlaylistUri()).stream().findFirst().orElse(null);


        return playlist != null ? playlist.getPlaylistUri() : spotifyData.getPlaylist().getPlaylistUri();


    }

    private List<Playlist> getUserPlaylistsById(SpotifyData spotifyData) {
        final String sql = "select * from playlist where app_user_id = ?";

        return template.query(sql, new PlaylistMapper(), spotifyData.getPlaylist().getAppUserId());
    }

    private List<String> getExistingPlaylistsById(SpotifyData spotifyData){
        final String sql = "select * from playlist where app_user_id = ?";

        List<String> uris = new ArrayList<>();

        List<Playlist> playlists = template.query(sql, new PlaylistMapper(), spotifyData.getPlaylist().getAppUserId());

        for(Playlist each : playlists){
            uris.add(each.getPlaylistUri());
        }

        return uris;
    }

    @Override
    @Transactional
    public Playlist createPlaylist(SpotifyData spotifyData) {
        Playlist playlist = new Playlist();

        List<String> existingPlaylists = getExistingPlaylistsById(spotifyData);

        final String sql = "insert into playlist (playlist_uri, playlist_name, description, playlist_url, playlist_image_link, app_user_id) "
                + "values (?,?,?,?,?,?);";

                if (!existingPlaylists.contains(spotifyData.getPlaylist().getPlaylistUri())) {
                    int rowsAffected = template.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                        ps.setString(1, spotifyData.getPlaylist().getPlaylistUri());
                        ps.setString(2, spotifyData.getPlaylist().getPlaylistName());
                        ps.setString(3, spotifyData.getPlaylist().getDescription());
                        ps.setString(4, spotifyData.getPlaylist().getPlaylistUrl());
                        ps.setString(5, spotifyData.getPlaylist().getPlaylistImageLink());
                        ps.setInt(6, spotifyData.getPlaylist().getAppUserId());
                        return ps;
                    });

                    if (rowsAffected <= 0) {
                        return null;
                    }

                    playlist = getPlaylistByPlaylistUri(spotifyData.getPlaylist().getPlaylistUri(), spotifyData.getPlaylist().getAppUserId());
                } else {
                    return null;
                }



        return playlist;
    }


    @Override
    @Transactional
    public List<Track> createTrack(SpotifyData spotifyData) {

        List<Track> tracks = new ArrayList<>();

        List<String> existingTracks = existingTracksUris(spotifyData);

        final String sql = "insert into track (track_uri, app_user_id, title, elo_score, num_of_matches_played, track_duration, popularity_num, spotify_url, preview_url) "
                + "values (?,?,?,?,?,?,?,?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            if (!existingTracks.contains(eachTrack.getTrack_uri())) {
                int rowsAffected = template.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                    ps.setString(1, eachTrack.getTrack_uri());
                    ps.setInt(2, eachTrack.getAppUserId());
                    ps.setString(3, eachTrack.getTitle());
                    eachTrack.setEloScore(1000);
                    ps.setInt(4, eachTrack.getEloScore());
                    eachTrack.setNumOfMatchesPlayed(0);
                    ps.setInt(5, eachTrack.getNumOfMatchesPlayed());
                    ps.setInt(6, eachTrack.getTrackDuration());
                    ps.setInt(7, eachTrack.getPopularityNumber());
                    ps.setString(8, eachTrack.getSpotifyUrl());
                    ps.setString(9, eachTrack.getPreviewUrl());
                    return ps;
                });

                if (rowsAffected <= 0) {
                    return null;
                }


                tracks.add(eachTrack);
                existingTracks.add(eachTrack.getTrack_uri());
            } else {
                return null;
            }
        }

        return tracks;

    }




    @Override
    @Transactional
    public List<Artist> createArtist(SpotifyData spotifyData) {

        List<String> existingArtists = existingArtistsUris(spotifyData);

        List<Artist> artists = new ArrayList<>();


        final String sql = "insert into artist (artist_uri, artist_name, spotify_url, artist_image_link) "
                + "values (?,?,?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                if (!existingArtists.contains(eachArtist.getArtistUri())) {
                    int rowsAffected = template.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                        ps.setString(1, eachArtist.getArtistUri());
                        ps.setString(2, eachArtist.getArtistName());
                        ps.setString(3, eachArtist.getSpotifyUrl());
                        ps.setString(4, eachArtist.getArtistImageLink());
                        return ps;
                    });

                    if (rowsAffected <= 0) {
                        return null;
                    }


                    artists.add(eachArtist);
                    existingArtists.add(eachArtist.getArtistUri());
                } else {
                    return null;
                }
            }
        }

        return artists;
    }


    @Override
    @Transactional
    public List<Album> createAlbum(SpotifyData spotifyData) {

        List<String> existingAlbums = existingAlbumsUris();

        List<Album> albums = new ArrayList<>();


        final String sql = "insert into album (album_uri, album_name, release_date, album_image_link, spotify_url) "
                + "values (?,?,?,?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Album eachAlbum : eachTrack.getAlbums()) {
                if (!existingAlbums.contains(eachAlbum.getAlbumUri())) {
                    int rowsAffected = template.update(connection -> {
                        PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                        ps.setString(1, eachAlbum.getAlbumUri());
                        ps.setString(2, eachAlbum.getAlbumName());
                        ps.setDate(3, eachAlbum.getReleaseDate());
                        ps.setString(4, eachAlbum.getAlbumImageLink());
                        ps.setString(5, eachAlbum.getSpotifyUrl());
                        return ps;
                    });
                    if (rowsAffected <= 0) {
                        return null;
                    }

                    albums.add(eachAlbum);
                    existingAlbums.add(eachAlbum.getAlbumUri());
                } else {
                    return null;
                }
            }

        }

        return albums;
    }

    @Override
    @Transactional
    public List<Genre> createGenre(SpotifyData spotifyData) {
        List<Genre> genres = new ArrayList<>();

        List<String> existingGenres = existingGenresNames();


        final String sql = "insert into genre (genre_name) " +
                "values (?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                for (Genre eachGenre : eachArtist.getGenres()) {
                    if (!existingGenres.contains(eachGenre.getGenreName())) {

                        KeyHolder keyHolder = new GeneratedKeyHolder();
                        int rowsAffected = template.update(connection -> {
                            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                            ps.setString(1, eachGenre.getGenreName());
                            return ps;
                        }, keyHolder);

                        if (rowsAffected <= 0) {
                            return null;
                        }

                        eachGenre.setGenreId(keyHolder.getKey().intValue());

                        genres.add(eachGenre);
                        existingGenres.add(eachGenre.getGenreName());
                    } else {
                        return null;
                    }
                }

            }
        }
        return genres;

    }

    private List<Track> existingTracks(SpotifyData spotifyData) {
        final String sql = "select * from track where app_user_id = ?";

        List<Artist> artists = existingArtists(spotifyData);


        List<Album> albums = existingAlbums();


        return template.query(sql, new TracksMapper(artists, albums), spotifyData.getPlaylist().getAppUserId());

    }

    private List<String> existingTracksUris(SpotifyData spotifyData) {

        final String sql = "select * from track where app_user_id = ?";

        List<Artist> artists = existingArtists(spotifyData);


        List<Album> albums = existingAlbums();

        List<Track> tracks = template.query(sql, new TracksMapper(artists, albums), spotifyData.getPlaylist().getAppUserId());

        List<String> uris = new ArrayList<>();

        for(Track each : tracks){
            uris.add(each.getTrack_uri());
        }
        return uris;
    }

    private List<Artist> existingArtists(SpotifyData spotifyData) {
        final String sql = "select * from artist;";


        List<Genre> artistGenres = new ArrayList<>();


        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                artistGenres.addAll(eachArtist.getGenres());
            }
        }


        return template.query(sql, new ArtistMapper(artistGenres));
    }


    private List<String> existingArtistsUris(SpotifyData spotifyData) {
        final String sql = "select * from artist;";


        List<Genre> artistGenres = new ArrayList<>();

        List<String> artistUri = new ArrayList<>();


        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                artistGenres.addAll(eachArtist.getGenres());
            }
        }

        List<Artist> artists = template.query(sql, new ArtistMapper(artistGenres));

        for (Artist each : artists) {
            artistUri.add(each.getArtistUri());
        }

        return artistUri;
    }

    private List<Album> existingAlbums() {
        final String sql = "select * from album;";


        return template.query(sql, new AlbumMapper());
    }

    private List<String> existingAlbumsUris() {
        final String sql = "select * from album;";

        List<String> uris = new ArrayList<>();

        List<Album> albums = template.query(sql, new AlbumMapper());

        for (Album each : albums) {
            uris.add(each.getAlbumUri());
        }
        return uris;
    }

    private List<Genre> existingGenres() {
        final String sql = "select * from genre;";


        return template.query(sql, new GenreMapper());

    }

    private List<String> existingGenresNames() {
        final String sql = "select * from genre;";

        List<String> names = new ArrayList<>();

        List<Genre> genres = template.query(sql, new GenreMapper());

        for (Genre each : genres) {
            names.add(each.getGenreName());
        }

        return names;

    }

    private List<Integer> existingGenresIds() {
        final String sql ="select * from genre;";

        List<Integer> ids = new ArrayList<>();

        List<Genre> genres = template.query(sql, new GenreMapper());

        for(Genre each : genres){
            ids.add(each.getGenreId());
        }

        return ids;
    }

    private void addPlaylistTrack(SpotifyData spotifyData) {
        final String sql = "insert into playlist_track (app_user_id, track_uri, playlist_uri) values (?,?,?);";

        List<String> existingTracksUris = existingTracksUris(spotifyData);

        List<String> written = new ArrayList<>();


            for (Track eachTrack : spotifyData.getTracks()) {
                if(!written.contains(eachTrack.getTrack_uri())){
                    if (existingTracksUris.contains(eachTrack.getTrack_uri())) {
                        template.update(sql, eachTrack.getAppUserId(), eachTrack.getTrack_uri(), spotifyData.getPlaylist().getPlaylistUri());
                        written.add(eachTrack.getTrack_uri());
                    }
                }

                }

            }


    private void addGenreArtist(SpotifyData spotifyData) {

        final String sql = "insert into genre_artist (artist_uri, genre_id) values (?,?);";

        List<Integer> existingGenres = existingGenresIds();

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                for (Genre eachGenre : eachArtist.getGenres()) {
                    if (existingGenres.contains(eachGenre.getGenreId())) {
                        template.update(sql, eachArtist.getArtistUri(), eachGenre.getGenreId());
                    }
                }
            }
        }

    }



    private void addTrackAlbum(SpotifyData spotifyData) {

        final String sql = "insert into track_album (track_uri, album_uri) values (?,?);";

        List<String> existing = existingAlbumsUris();

        List<String> written = new ArrayList<>();


        for (Track eachTrack : spotifyData.getTracks()) {
            if (!written.contains(eachTrack.getTrack_uri())) {
                for (Album eachAlbum : eachTrack.getAlbums()) {
                    if (existing.contains(eachAlbum.getAlbumUri())) {
                        template.update(sql, eachTrack.getTrack_uri(), eachAlbum.getAlbumUri());
                        written.add(eachTrack.getTrack_uri());
                    }
                }
            }
        }
    }

    private void addTrackArtist(SpotifyData spotifyData) {
        final String sql = "insert into track_artist (track_uri, artist_uri) values (?,?);";

        List<String> existing = existingArtistsUris(spotifyData);

        List<String> written = new ArrayList<>();

        for (Track eachTrack : spotifyData.getTracks()) {
            if (!written.contains(eachTrack.getTrack_uri())) {
                for (Artist eachArtist : eachTrack.getArtists()) {
                    if (existing.contains(eachArtist.getArtistUri())) {
                        template.update(sql, eachTrack.getTrack_uri(), eachArtist.getArtistUri());
                        written.add(eachTrack.getTrack_uri());
                    }
                }
            }
        }
    }


}
