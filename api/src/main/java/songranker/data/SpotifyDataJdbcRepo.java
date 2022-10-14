package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import songranker.data.mappers.AlbumMapper;
import songranker.data.mappers.ArtistMapper;
import songranker.data.mappers.GenreMapper;
import songranker.models.*;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SpotifyDataJdbcRepo implements SpotifyDataRepo {

    @Autowired
    JdbcTemplate template;



    @Override
    @Transactional
    public Playlist createPlaylist(SpotifyData spotifyData){

        Playlist playlist = new Playlist();

        final String sql = "insert into playlist (playlist_uri, playlist_name, description, playlist_url, playlist_image_link, app_user_id) "
                + "values (?,?,?,?,?,?);";

        int rowsAffected = template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                    ps.setString(0, spotifyData.getPlaylist().getPlaylistUri());
                    ps.setString(1, spotifyData.getPlaylist().getPlaylistName());
                    ps.setString(2, spotifyData.getPlaylist().getDescription());
                    ps.setString(3, spotifyData.getPlaylist().getPlaylistUrl());
                    ps.setString(4, spotifyData.getPlaylist().getPlaylistImageLink());
                    ps.setInt(5, spotifyData.getPlaylist().getAppUserId());
                    return ps;
        });

        if(rowsAffected <= 0){
            return null;
        }

        addPlaylistTrack(spotifyData);

        return playlist;
    }

    private void addPlaylistTrack(SpotifyData spotifyData) {
        final String sql = "insert into playlist_track (track_uri, playlist_uri) values (?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            template.update(sql, eachTrack.getTrack_uri() ,spotifyData.getPlaylist().getPlaylistUri());
        }
    }

    @Override
    @Transactional
    public List<Track> createTrack(SpotifyData spotifyData) {

        List<Track> playlistTracks = new ArrayList<>();

        final String sql = "insert into track (track_uri, title, elo_score, num_of_matches_played, track_duration, popularity_num, spotify_url, preview_url) "
                + "values (?,?,?,?,?,?,?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            int rowsAffected = template.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                ps.setString(0, eachTrack.getTrack_uri());
                ps.setString(1, eachTrack.getTitle());
                eachTrack.setEloScore(1000);
                ps.setInt(2, eachTrack.getEloScore());
                eachTrack.setNumOfMatchesPlayed(0);
                ps.setInt(3, eachTrack.getNumOfMatchesPlayed());
                ps.setInt(4, eachTrack.getTrackDuration());
                ps.setInt(5, eachTrack.getPopularityNum());
                ps.setString(6, eachTrack.getSpotifyUrl());
                ps.setString(7, eachTrack.getPreviewUrl());
                return ps;
            });


            if(rowsAffected <=0){
                return null;
            }

            playlistTracks.add(eachTrack);
        }

        addTrackAlbum(spotifyData);

        addTrackArtist(spotifyData);

        return playlistTracks;

    }

    private void addTrackArtist(SpotifyData spotifyData) {
        final String sql = "insert into track_artist (track_uri, artist_uri) values (?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            for(Artist eachArtist : eachTrack.getArtists()) {
                template.update(sql, eachTrack.getTrack_uri(), eachArtist.getArtistUri());
            }
        }

    }

    private void addTrackAlbum(SpotifyData spotifyData) {

        final String sql = "insert into track_album (track_uri, album_uri) values (?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            for(Album eachAlbum : eachTrack.getAlbums()) {
                template.update(sql, eachTrack.getTrack_uri(), eachAlbum.getAlbumUri());
            }
        }
    }

    @Override
    @Transactional
    public List<Artist> createArtist(SpotifyData spotifyData){

        List<Artist> artists = new ArrayList<>();

        final String sql = "insert into artist (artist_uri, artist_name, spotify_url, artist_image_link) "
                + "values (?,?,?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            for(Artist eachArtist : eachTrack.getArtists()){
                for(Artist existingArtist : existingArtists(spotifyData)){
                    if(!eachArtist.getArtistUri().equals(existingArtist.getArtistUri())){
                        int rowsAffected = template.update(connection -> {
                            PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                            ps.setString(0, eachArtist.getArtistUri());
                            ps.setString(1, eachArtist.getArtistName());
                            ps.setString(2, eachArtist.getSpotifyUrl());
                            ps.setString(3, eachArtist.getArtistImageLink());
                            return ps;
                        });

                        if(rowsAffected <= 0){
                            return null;
                        }
                    }
                }
                artists.add(eachArtist);
            }
        }
        
        addGenreArtist(spotifyData);

        return artists;
    }

    private List<Artist> existingArtists(SpotifyData spotifyData) {
        final String sql = "select * from artist;";


        List<Genre> existingGenres = new ArrayList<>();

        for(Track eachTrack : spotifyData.getTracks()){
            for(Artist eachArtist : eachTrack.getArtists()){
                existingGenres.addAll(eachArtist.getGenres());
            }
        }

        return template.query(sql, new ArtistMapper(existingGenres), spotifyData);
    }

    private void addGenreArtist(SpotifyData spotifyData) {

        final String sql = "insert into genre_artist (artist_uri, genre_id) values (?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            for(Album eachAlbum : eachTrack.getAlbums()) {
                template.update(sql, eachTrack.getTrack_uri(), eachAlbum.getAlbumUri());
            }
        }
    }

    @Override
    @Transactional
    public List<Album> createAlbum(SpotifyData spotifyData){

        List<Album> albums = new ArrayList<>();

        final String sql = "insert into album (album_uri, album_name, release_date, album_image_link, spotify_url) "
                + "values (?,?,?,?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            for(Album eachAlbum : eachTrack.getAlbums()){
                for(Album existingAlbum : existingAlbums(spotifyData)){
                    if(!eachAlbum.getAlbumUri().equals(existingAlbum.getAlbumUri())){
                        int rowsAffected = template.update(connection -> {
                            PreparedStatement ps = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
                            ps.setString(0, eachAlbum.getAlbumUri());
                            ps.setString(1, eachAlbum.getAlbumName());
                            ps.setDate(2, (Date) eachAlbum.getReleaseDate());
                            ps.setString(3, eachAlbum.getAlbumImageLink());
                            ps.setString(4, eachAlbum.getSpotifyUrl());
                            return ps;
                        });
                        if(rowsAffected <=0){
                            return null;
                        }
                    }

                }

                albums.add(eachAlbum);
            }
        }
        return albums;
    }


    private List<Album> existingAlbums(SpotifyData spotifyData) {
        final String sql = "select * from album;";


        return template.query(sql, new AlbumMapper(), spotifyData);
    }

    @Override
    @Transactional
    public List<Genre> createGenre(SpotifyData spotifyData){
        List<Genre> genres = new ArrayList<>();

        final String sql = "insert into genre (genre_id, genre_name) "+
                "values (?,?);";

        for(Track eachTrack : spotifyData.getTracks()){
            for(Artist eachArtist : eachTrack.getArtists()){
                for(Artist existingArtist : existingArtists(spotifyData)){
                    if(!eachArtist.getArtistUri().equals(existingArtist.getArtistUri())){
                        for(Genre eachGenre : eachArtist.getGenres()){
                            for(Genre existingGenre : existingGenres(spotifyData)){
                                if(!eachGenre.getGenreName().equals(existingGenre.getGenreName())){
                                    KeyHolder keyHolder = new GeneratedKeyHolder();
                                    int rowsAffected = template.update(connection -> {
                                        PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                                        ps.setString(1, eachGenre.getGenreName());
                                        return ps;
                                    }, keyHolder);

                                    if(rowsAffected <=0){
                                        return null;
                                    }

                                    eachGenre.setGenreId(keyHolder.getKey().intValue());
                                }

                                genres.add(eachGenre);
                            }

                        }

                    }

                }
            }
        }
        return genres;

    }

    private List<Genre> existingGenres(SpotifyData spotifyData) {
        final String sql = "select * from genre;";


        return template.query(sql, new GenreMapper(), spotifyData);

    }

}