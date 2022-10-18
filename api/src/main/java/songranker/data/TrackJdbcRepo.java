
package songranker.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import songranker.data.mappers.AlbumMapper;
import songranker.data.mappers.ArtistMapper;
import songranker.data.mappers.GenreMapper;
import songranker.data.mappers.TracksMapper;
import songranker.models.Album;
import songranker.models.Artist;
import songranker.models.Genre;
import songranker.models.Track;

import java.util.ArrayList;
import java.util.List;
@Repository
public class TrackJdbcRepo implements TrackRepo{

    @Autowired
    JdbcTemplate template;


    @Override
    public List<Track> getTracksByPlaylistUri(String playlistUri) {
        List<String> trackUris = getTrackUrisByPlaylistUri(playlistUri);
        List<Album> trackAlbums = getAlbumsByTrackUri(trackUris);
        List<Artist> trackArtists = getArtistsByTrackUri(trackUris);
        final String sql = "select * from track as t\n"
                +"inner join playlist_track as pt\n"
                +"on t.app_user_id = pt.app_user_id\n"
                +"inner join playlist as p\n"
                +"on p.app_user_id = pt.app_user_id\n"
                +"where p.playlist_uri = ?;";

        return template.query(sql, new TracksMapper(trackArtists, trackAlbums), playlistUri);
    }

    @Override
    public boolean updateTrackEloScore(Track updatedTrack) {

        final String sql = "update track\n"
                +"set elo_score = ?,\n"
                +"num_of_matches_played = num_of_matches_played + 1\n"
                +"where track_uri=? and app_user_id=?;";

        return (template.update(sql, updatedTrack.getEloScore(), updatedTrack.getTrack_uri(), updatedTrack.getAppUserId())) > 0;
    }

    private List<String> getTrackUrisByPlaylistUri(String playlistUri) {

        final String sql = "select track_uri from playlist_track\n"
                +"where playlist_uri = ?;";

        return template.query(sql, (resultSet, rowNum) -> {
            return resultSet.getString("track_uri");}, playlistUri);
    }

    private List<Artist> getArtistsByTrackUri(List<String> trackUris) {
        final String sql = "select * from artist as a\n"
                +"inner join track_artist as ta\n"
                +"on a.artist_uri = ta.artist_uri\n"
                +"inner join track as t\n"
                +"on t.track_uri = ta.track_uri\n"
                +"where t.track_uri = ?;";

        List<Artist> trackArtists = new ArrayList<>();

        List<Genre> artistGenres = getGenresByTrackUri(trackUris);

        for(String each : trackUris){
            List<Artist> matchingArtists = template.query(sql, new ArtistMapper(artistGenres), each);

            trackArtists.addAll(matchingArtists);
        }

        return trackArtists;
    }

    private List<Genre> getGenresByTrackUri(List<String> trackUris) {

        final String sql = "select * from genre as g\n"
                +"inner join genre_artist as ga\n"
                +"on g.genre_id = ga.genre_id\n"
                +"inner join track_artist as ta\n"
                +"on ta.artist_uri = ga.artist_uri\n"
                +"where ta.track_uri = ?;";

        List<Genre> genres = new ArrayList<>();

        for(String each : trackUris){
            List<Genre> matchingGenres = template.query(sql, new GenreMapper(), each);

            genres.addAll(matchingGenres);
        }

        return genres;
    }

    private List<Album> getAlbumsByTrackUri(List<String> trackUris) {
        final String sql = "select * from album as ab\n"
                +"inner join track_album as ta\n"
                +"on ab.album_uri = ta.album_uri\n"
                +"where ta.track_uri = ?;";

        List<Album> albums = new ArrayList<>();
        for(String each : trackUris){
            List<Album> matchingAlbums = template.query(sql, new AlbumMapper(), each);

            albums.addAll(matchingAlbums);
        }

        return albums;
    }
    @Override
    public List<Track> getTracksByAppUserId(int appUserId) {
        final String sql = "select * from track as t \n" +
                "where app_user_id = ?;";
        List<Track> tracks = new ArrayList<>();

        return null;
    }

    public List<Artist> getTop10Artists(int appUserId) {
        final String sql = "select avg(t.elo_score)\n" +
                "from artist a\n" +
                "inner join track_artist ta on ta.artist_uri = a.artist_uri\n" +
                "inner join track t on t.track_uri = ta.track_uri\n" +
                "group by a.artist_uri;";
        return null;
    }

    public List<Genre> getTop10Genres(int appUserId) {
        return null;
    }
}