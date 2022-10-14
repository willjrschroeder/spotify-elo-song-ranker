import Playlist from "../Playlist/Playlist"
import PlaylistNames from "../Playlist/PlaylistNames/PlaylistNames"
import "./ManagePlaylists.css"
import SpotifyWebApi from 'spotify-web-api-node';
import SpotifyAuthContext from '../context/SpotifyAuthContext';
import { useEffect, useState, useContext } from "react";
import { Link } from "react-router-dom";


const spotifyApi = new SpotifyWebApi({
    clientId: 'b055b73f53474f3e931fd58a080ca3cf'
});
function ManagePlaylists() {
    const spotifyAuth = useContext(SpotifyAuthContext); 

    const [playlists, setPlaylists] = useState([]);

    function loadAllPlaylists() {

        if (!spotifyAuth.spotifyAccessToken) return; // return if the access token is not yet set

        spotifyApi.setAccessToken(spotifyAuth.spotifyAccessToken);

        spotifyApi.getMe()
        .then(function (data) {
            return spotifyApi.getUserPlaylists(data.body.id);
        })
        .then(function (data) {
            setPlaylists(data.body.items);
        });
        }
        useEffect(
            () => {
                loadAllPlaylists();
            },
            []
        )

    const [playlistPackage, setPlaylistPackage] = useState(null);

    function addPlaylistToDatabase(event) {


        event.preventDefault();

        fetch( "http://localhost:8080/api/playlists", {
            method:"POST",
            body: JSON.stringify(playlistPackage),
            headers: {
                "Content-Type": "application/json"
            }
        }).then( async response => {
            if( response.status === 201 ) {
                return response.json();
            } 
                return Promise.reject( await response.json() );
        })
        .catch( errorList => {
            if( errorList instanceof TypeError ){
                console.log( "Could not connect to api.");
            } else {
            }
        });

    }
    
        return(<>
        <Link to="/home">Home</Link>
        <div className="cards">
            {playlists.map( (p, index) => <Playlist key={p.id} playlistData={p} index={index}  />) }
        </div>
        <div className="player">
            <div class="upper-part">
                {/* <div class="play-icon">
                    <svg width="20" height="20" fill="#2992dc" stroke="#2992dc" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" class="feather feather-play" viewBox="0 0 24 24">
                    <defs/>
                    <path d="M5 3l14 9-14 9V3z"/>
                    </svg>
                </div> */}
                <div className="info-area" id="test">
                    {playlists.map( (pl, index) => <PlaylistNames key={pl.id} playlistData={pl} onPlaylistAdd={addPlaylistToDatabase} index={index}  />)}
                </div>
            </div>
            {/* <div class="progress-bar">
                <span class="progress"></span>
            </div> */}
        </div>
        </>
        )
    
    }



export default ManagePlaylists;