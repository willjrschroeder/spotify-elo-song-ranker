drop database if exists sesr_test;
create database sesr_test;
use sesr_test;

drop table if exists genre_artist;
drop table if exists track_album;
drop table if exists track_artist;
drop table if exists genre;
drop table if exists album;
drop table if exists artist;
drop table if exists playlist_track;
drop table if exists track;
drop table if exists playlist;
drop table if exists user_roles;
drop table if exists app_role;
drop table if exists app_user;

-- create tables and relationships
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

delimiter //
create procedure set_known_good_state()
begin
	
    -- This order may have to be altered depending on the order of inserts. deletion must satisfy FK restraints
    delete from genre_artist;
    delete from track_album;
    delete from track_artist;
    delete from genre;
    alter table genre auto_increment = 1;
    delete from album;
    delete from artist;
    delete from playlist_track;
    delete from track;
    delete from playlist;
    delete from user_roles;
    delete from app_role;
    alter table app_role auto_increment = 1;
    delete from app_user;
    alter table app_user auto_increment = 1;
    
    insert into app_user (app_user_id, username, password_hash, display_name, disabled) values
		-- all pw_hashes were generated w/ bcrypt using 10 rounds
        
        -- original pw was 'password'
        -- this user has not been assigned a role
		(1, 'testUsername', '$2a$10$VtVK8vKTeFblMnmzLEP6AucvOG.HveI/ZohIlrmQ7s3qUaGmIkPvy', 'John Smith', 0),
        -- original pw was 'password2'
        -- this user has the role of admin and user
		(2, 'testUsername2', '$2a$10$AEDyRKVpyyI4XQyzfudtSeCmJN3u2DOG04ueHEI4bRA43rRjO3i1a', 'Jane Admin Smith', 0),
        -- original pw was 'P@ssw3rd'
        -- this user has the role of user
		(3, 'testUsername3', '$2a$10$Y2GsuViVefCtNy1puv4XhOArHAamTPZByEf2XfUsqdjXNu9VJix8q', 'George User Smith', 0);
        
	insert into app_role (app_role_id, role_name) values
		(1, 'user'),
        (2, 'admin');
        
	insert into user_roles (app_user_id, app_role_id) values
		(1, 1),
		(2, 2),
        (2, 1),
        (3, 1);
        
end //

delimiter ;

call set_known_good_state();