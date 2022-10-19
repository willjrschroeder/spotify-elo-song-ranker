import React from 'react';

const AUTH_URL = 'https://accounts.spotify.com/authorize?client_id=b055b73f53474f3e931fd58a080ca3cf&response_type=code&redirect_uri=http://localhost:3000/callback&scope=user-read-private%20playlist-read-private%20playlist-read-collaborative';

function SpotifyAuthorization() {
    return (
        <div className='authorize'>
            <a className='btn btn-success btn-lg' href={AUTH_URL}>Link your Spotify account</a>
        </div>
    );
}

export default SpotifyAuthorization;