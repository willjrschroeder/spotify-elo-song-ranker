use sesr;

-- Tracks by User Id
select * from track where app_user_id = 1;

-- Track by Track Uri
select * from track as t
inner join playlist as p
on p.app_user_id = t.app_user_id
where p.playlist_uri = "spotify:playlist:38cfqZXcGK4KPtDrGUNMkI";

-- Album by Track Uri
select * from album as ab
inner join track_album as ta
on ab.album_uri = ta.album_uri
where ta.track_uri = "spotify:track:3PQfLpp4ctkcwwVg1VnMlH";

-- Genre by Track Uri
select * from genre as g
inner join genre_artist as ga
on g.genre_id = ga.genre_id
inner join track_artist as ta
on ta.artist_uri = ga.artist_uri
where ta.track_uri = 'spotify:track:3PQfLpp4ctkcwwVg1VnMlH';


-- Top 10 Artists
select avg(t.elo_score) as elo, a.artist_uri, a.artist_name, a.spotify_url, a.artist_image_link
from artist a
inner join track_artist
as ta on ta.artist_uri = a.artist_uri
inner join track as t on t.track_uri = ta.track_uri
group by a.artist_uri
order by elo desc
limit 10;

-- Each Artist ELO score
select avg(t.elo_score) as elo
from artist a
inner join track_artist
as ta on ta.artist_uri = a.artist_uri
inner join track as t on t.track_uri = ta.track_uri
where a.artist_uri = 'spotify:artist:0fA0VVWsXO9YnASrzqfmYu';


-- Top 10 Artist Uris
select avg(t.elo_score) as elo, a.artist_uri, a.artist_name
from artist as a
inner join track_artist
as ta on ta.artist_uri = a.artist_uri
inner join track as t on t.track_uri = ta.track_uri
where t.app_user_id = 1
group by a.artist_uri
order by elo desc
limit 10;


-- Master Top ten artist
select * from artist as a
inner join track_artist
as ta on ta.artist_uri = a.artist_uri
inner join track as t on t.track_uri = ta.track_uri
where t.app_user_id = 1 and a.artist_uri = 'spotify:artist:581C5Qwl87TskfBEzuoisu'
limit 1;

-- Top 10 Track Uris
select track_uri, elo_score, title from track
where app_user_id = 1
order by elo_score desc
limit 10;

-- Track Uris by Playlist uri
select t.track_uri from track as t
inner join playlist_track as pt
on pt.track_uri = t.track_uri
where pt.playlist_uri = "spotify:playlist:38cfqZXcGK4KPtDrGUNMkI"
order by elo_score desc;

-- Tracks by Playlist Uri
select * from track as t
inner join playlist as p
on p.app_user_id = t.app_user_id
where t.track_uri = 'spotify:track:3OM6qQmdFV6uy61GIqpRtf'
limit 1;

-- Top Ten Genres
select avg(t.elo_score), g.genre_id, g.genre_name
from artist a
inner join track_artist ta on ta.artist_uri = a.artist_uri
inner join (select * from track where app_user_id = 1) as t
on t.track_uri = ta.track_uri
inner join genre_artist ga on ga.artist_uri = a.artist_uri
inner join genre g on g.genre_id = ga.genre_id
group by g.genre_id
order by avg(t.elo_score) desc
limit 10;