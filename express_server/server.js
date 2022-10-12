const express = require('express');
const SpotifyWebApi = require('spotify-web-api-node'); // This is a library for accessing the Spotify API. Simplifies all requests
const cors = require('cors');
const bodyParser = require('body-parser');
const dotenv = require('dotenv');


dotenv.config(); // allows access to .env variable from our local machine
const app = express(); // express server instance
app.use(cors()); // gets rid of cors error for accessing from localhost:3000
app.use(bodyParser.json()); // allows request body to be parsed

// this creates an API endpoint for getting a spotify access token from an authorization code
app.post('/api/spotify/login', (request, response) => {
    const code = request.body.code;
    const spotifyApi = new SpotifyWebApi({
        redirectUri: 'http://localhost:3000/callback',
        clientId: 'b055b73f53474f3e931fd58a080ca3cf',
        clientSecret: process.env.CLIENT_SECRET
    })

    spotifyApi.authorizationCodeGrant(code)
    .then(data => {
        response.json({ // set the response body to contain auth token details
            accessToken: data.body.access_token,
            refreshToken: data.body.refresh_token,
            expiresIn: data.body.expires_in
        })
    }).catch((err) => {
        console.log(err);
        response.sendStatus(400); // error that is sent when code cannot get access token
    })
});

// this creates an API endpoint for refreshing a spotify access token
app.post('/api/spotify/refresh_token', (request, response) => {
    const refreshToken = request.body.refreshToken;

    //builds a spotify API instance containing the refresh token (needed to generate a fresh token)
    const spotifyApi = new SpotifyWebApi({
        redirectUri: 'http://localhost:3000/callback',
        clientId: 'b055b73f53474f3e931fd58a080ca3cf',
        clientSecret: process.env.CLIENT_SECRET,
        refreshToken
    });

    // makes a request to the spotify API to refresh the access token
    spotifyApi.refreshAccessToken().then( (data) => {
        response.json({ // set the response body to contain auth token details
            accessToken: data.body.access_token,
            expiresIn: data.body.expires_in
        })
    }).catch((err) => {
        console.log(err);
        response.sendStatus(400); // error that is sent when code cannot get access token
    })
});

app.listen(3001); // The express server is listening on port 3001