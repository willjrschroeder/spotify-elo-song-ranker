drop database if exists sesr;
create database sesr;
use sesr;

create table app_user (
app_user_id int primary key auto_increment,
username varChar(50) unique not null,
password_hash varChar(200) not null,
display_name varChar(200) not null,
disabled bit not null default 0
);

create table app_role (
app_role_id int primary key auto_increment,
role_name varChar(100) not null
);

create table user_roles (
app_user_id int not null,
app_role_id int not null,
constraint pk_user_roles
	primary key(app_user_id, app_role_id),
constraint fk_user_roles_app_user_id
	foreign key (app_user_id)
    references app_user(app_user_id),
constraint fk_user_roles_app_role_id
	foreign key (app_role_id)
    references app_role(app_role_id)
);

create table playlist (
playlist_uri varChar(200) primary key,
playlist_name varChar(100) not null,
`description` varChar(200) not null,
playlist_url varChar(200) not null,
playlist_image_link varChar(500),
app_user_id int not null,
constraint fk_playlist_app_user_id
	foreign key (app_user_id)
    references app_user(app_user_id)
);

create table track (
track_uri varChar(200) primary key,
title varChar(100) not null,
elo_score int default 1000 not null,
num_of_matches_played int default 0 not null,
track_duration int not null,
popularity_num int default -1,
spotify_url varChar(200) not null,
preview_url varChar(200) not null
);

create table playlist_track (
track_uri varChar(200) not null,
playlist_uri varChar(200) not null,
constraint pk_playlist_track
	primary key (track_uri, playlist_uri),
constraint fk_playlist_track_track_uri
	foreign key (track_uri)
    references track(track_uri),
constraint fk_playlist_track_playlist_uri
	foreign key (playlist_uri)
    references playlist(playlist_uri)
);

create table artist (
artist_uri varChar(200) primary key unique not null,
artist_name varChar(200) not null,
spotify_url varChar(200) unique not null,
artist_image_link varChar(500)
);

create table album (
album_uri varChar(100) primary key not null,
album_name varChar(100) not null,
release_date date not null,
album_image_link varChar(500),
spotify_url varChar(100) unique not null
); 

create table genre (
genre_id int primary key auto_increment,
genre_name varChar(100) unique not null
);

create table track_artist (
track_uri varChar(200) not null,
artist_uri varChar(200) not null,
constraint pk_track_artist
	primary key (track_uri, artist_uri),
constraint fk_track_artist_track_uri
	foreign key (track_uri)
    references track(track_uri),
constraint fk_track_artist_artist_uri
	foreign key (artist_uri)
    references artist(artist_uri)
);

create table track_album (
track_uri varChar(200) not null,
album_uri varChar(200) not null,
constraint pk_track_album
	primary key (track_uri, album_uri),
constraint fk_track_album_track_uri
	foreign key (track_uri)
    references track(track_uri),
constraint fk_track_album_album_uri
	foreign key (album_uri)
    references album(album_uri)
);

create table genre_artist (
artist_uri varChar(200) not null,
genre_id int not null,
constraint pk_genre_artist
	primary key (artist_uri, genre_id),
constraint fk_genre_artist_artist_uri
	foreign key (artist_uri)
    references artist(artist_uri),
constraint fk_genre_artist_genre_id
	foreign key (genre_id)
    references genre(genre_id)
);



