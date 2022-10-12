const express = require('express');
const SpotifyWebApi = require('spotify-web-api-node');

const app = express(); // express server instance

// this creates an API endpoint for getting a spotify access token from an authorization code
app.post('/spotify_login', (request, response) => {
    const code = request.body.code;
    const spotifyApi = new SpotifyWebApi({
        redirectUri: 'http://localhost:3000/callback',
        clientId: 'b055b73f53474f3e931fd58a080ca3cf',
        clientSecret: '7b90f4b463e84c6186e70402c1d29159'
    })

    spotifyApi.authorizationCodeGrant(code)
    .then(data => {
        response.json({
            accessToken: data.body.access_token,
            refreshToken: data.body.refresh_token,
            expiresIn: data.body.expires_in
        })
    }).catch(() => {
        response.sendStatus(400); // error that is sent when code cannot get access token
    })
})