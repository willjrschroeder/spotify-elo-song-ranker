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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@Repository
public class SpotifyDataJdbcRepo implements SpotifyDataRepo {

    @Autowired
    JdbcTemplate template;

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

    @Override
    @Transactional
    public Playlist createPlaylist(SpotifyData spotifyData) {
        Playlist playlist = new Playlist();

        List<Playlist> existingPlaylists = getUserPlaylistsById(spotifyData);

        final String sql = "insert into playlist (playlist_uri, playlist_name, description, playlist_url, playlist_image_link, app_user_id) "
                + "values (?,?,?,?,?,?);";
        if (existingPlaylists.size() > 0) {
            for (Playlist each : existingPlaylists) {
                if (each.getAppUserId() != spotifyData.getPlaylist().getAppUserId()) {
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
                }
            }
        } else {
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
        }


        return playlist;
    }


    @Override
    @Transactional
    public List<Track> createTrack(SpotifyData spotifyData) {

        List<Track> tracks = new ArrayList<>();

        List<Track> existingTracks = existingTracks(spotifyData);

        final String sql = "insert into track (track_uri, app_user_id, title, elo_score, num_of_matches_played, track_duration, popularity_num, spotify_url, preview_url) "
                + "values (?,?,?,?,?,?,?,?,?);";

        if (existingTracks.size() > 0) {
            for (Track eachTrack : spotifyData.getTracks()) {
                for (Track each : existingTracks) {
                    if (each.getAppUserId() != eachTrack.getAppUserId()) {
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
                            ps.setInt(7, eachTrack.getPopularityNum());
                            ps.setString(8, eachTrack.getSpotifyUrl());
                            ps.setString(9, eachTrack.getPreviewUrl());
                            return ps;
                        });

                        if (rowsAffected <= 0) {
                            return null;
                        }


                        tracks.add(eachTrack);
                    }


                }
            }


        } else {
            for (Track eachTrack : spotifyData.getTracks()) {
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
                    ps.setInt(7, eachTrack.getPopularityNum());
                    ps.setString(8, eachTrack.getSpotifyUrl());
                    ps.setString(9, eachTrack.getPreviewUrl());
                    return ps;
                });

                if (rowsAffected <= 0) {
                    return null;
                }


                tracks.add(eachTrack);
            }

        }

        return tracks;

    }


    @Override
    @Transactional
    public List<Artist> createArtist(SpotifyData spotifyData) {

        List<Artist> existingArtists = existingArtists(spotifyData);

        List<Artist> artists = new ArrayList<>();

        final String sql = "insert into artist (artist_uri, artist_name, spotify_url, artist_image_link) "
                + "values (?,?,?,?);";
        if (existingArtists.size() > 0) {
            for (Track eachTrack : spotifyData.getTracks()) {
                for (Artist eachArtist : eachTrack.getArtists()) {
                    for (Artist existing : existingArtists) {
                        if (!existing.getArtistUri().equals(eachArtist.getArtistUri())) {
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


                            artists.addAll(eachTrack.getArtists());
                        }
                    }
                }
            }
        } else {
            for (Track eachTrack : spotifyData.getTracks()) {
                for (Artist eachArtist : eachTrack.getArtists()) {
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


                    artists.addAll(eachTrack.getArtists());
                }
            }
        }

        return artists;
    }


    @Override
    @Transactional
    public List<Album> createAlbum(SpotifyData spotifyData) {

        List<Album> existingAlbums = existingAlbums(spotifyData);

        List<Album> albums = new ArrayList<>();

        final String sql = "insert into album (album_uri, album_name, release_date, album_image_link, spotify_url) "
                + "values (?,?,?,?,?);";

        if (existingAlbums.size() > 0) {
            for (Track eachTrack : spotifyData.getTracks()) {
                for (Album eachAlbum : eachTrack.getAlbums()) {
                    for (Album existing : existingAlbums) {
                        if (!existing.getAlbumUri().equals(eachAlbum.getAlbumUri())) {
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
                        }
                    }
                }
            }
        } else {
            for (Track eachTrack : spotifyData.getTracks()) {
                for (Album eachAlbum : eachTrack.getAlbums()) {
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
                }

            }
        }
        return albums;
    }

    @Override
    @Transactional
    public List<Genre> createGenre(SpotifyData spotifyData) {
        List<Genre> genres = new ArrayList<>();

        List<Genre> existingGenres = existingGenres(spotifyData);

        final String sql = "insert into genre (genre_name) " +
                "values (?);";

        if (existingGenres.size() > 0) {
            for (Track eachTrack : spotifyData.getTracks()) {
                for (Artist eachArtist : eachTrack.getArtists()) {
                    for (Genre eachGenre : eachArtist.getGenres()) {
                        for (Genre existing : existingGenres) {
                            if (!existing.getGenreName().equals(eachGenre.getGenreName())) {
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
                            }
                        }
                    }
                }
            }
        }
        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                for (Genre eachGenre : eachArtist.getGenres()) {
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
                }

            }
        }
        return genres;

    }

    private List<Track> existingTracks(SpotifyData spotifyData) {
        final String sql = "select * from track where app_user_id = ?";

        List<Artist> artists = existingArtists(spotifyData);


        List<Album> albums = existingAlbums(spotifyData);


        return template.query(sql, new TracksMapper(artists, albums), spotifyData.getPlaylist().getAppUserId());

    }

    private List<Artist> existingArtists(SpotifyData spotifyData) {
        final String sql = "select * from artist where artist_name = ?";


        List<Genre> artistGenres = new ArrayList<>();

        List<String> artistsNames = new ArrayList<>();

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                artistGenres.addAll(eachArtist.getGenres());
                artistsNames.add(eachArtist.getArtistName());
            }
        }

        return template.query(sql, new ArtistMapper(artistGenres), artistsNames);
    }

    private List<Album> existingAlbums(SpotifyData spotifyData) {
        final String sql = "select * from album where album_name = ?";

        List<String> albumNames = new ArrayList<>();

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Album eachAlbum : eachTrack.getAlbums()) {
                albumNames.add(eachAlbum.getAlbumName());
            }
        }

        return template.query(sql, new AlbumMapper(), albumNames);
    }

    private List<Genre> existingGenres(SpotifyData spotifyData) {
        final String sql = "select * from genre where genre_id = ?";

        List<Integer> genreIds = new ArrayList<>();

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                for (Genre eachGenre : eachArtist.getGenres()) {
                    genreIds.add(eachGenre.getGenreId());
                }

            }
        }
        return template.query(sql, new GenreMapper(), genreIds);

    }

    private void addPlaylistTrack(SpotifyData spotifyData) {
        final String sql = "insert into playlist_track (app_user_id, track_uri, playlist_uri) values (?,?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            template.update(sql, eachTrack.getAppUserId(), eachTrack.getTrack_uri(), spotifyData.getPlaylist().getPlaylistUri());
        }
    }

    private void addGenreArtist(SpotifyData spotifyData) {

        final String sql = "insert into genre_artist (artist_uri, genre_id) values (?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                for (Genre eachGenre : eachArtist.getGenres()) {
                    template.update(sql, eachArtist.getArtistUri(), eachGenre.getGenreId());
                }

            }
        }
    }

    private void addTrackAlbum(SpotifyData spotifyData) {

        final String sql = "insert into track_album (track_uri, album_uri) values (?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Album eachAlbum : eachTrack.getAlbums()) {
                template.update(sql, eachTrack.getTrack_uri(), eachAlbum.getAlbumUri());
            }
        }
    }

    private void addTrackArtist(SpotifyData spotifyData) {
        final String sql = "insert into track_artist (track_uri, artist_uri) values (?,?);";

        for (Track eachTrack : spotifyData.getTracks()) {
            for (Artist eachArtist : eachTrack.getArtists()) {
                template.update(sql, eachTrack.getTrack_uri(), eachArtist.getArtistUri());
            }
        }

    }

}
