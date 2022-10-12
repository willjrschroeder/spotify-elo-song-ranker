import './App.css';
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import LandingPage from './LandingPage/LandingPage';
import Register from './Register/Register'
import Login from './Login/Login'
import AuthContext from './context/AuthContext';
import jwtDecode from "jwt-decode";
import React, { useState, useEffect } from "react";
import Home from "./Home/Home"
import SpotifyAuthorization from './SpotifyAuthorization/SpotifyAuthorization';
import CallbackPage from './SpotifyAuthorization/CallbackPage';
import SpotifyAuthContext from './context/SpotifyAuthContext';

//confused on this part, how to identify that this is the token
const LOCAL_STORAGE_TOKEN_KEY = "loginToken";
const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";

function App() {

  const [user, setUser] = useState();
  const [spotifyToken, setSpotifyToken] = useState();

  const [restoreLoginAttemptCompleted, setRestoreLoginAttemptCreated] = useState(false);
  const [restoreSpotifyTokenAttemptCompleted, setRestoreSpotifyTokenAttemptCompleted] = useState(false);

  useEffect(() => {
    const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
    console.log(token);
    if (token) {
      login(token)
    }
    setRestoreLoginAttemptCreated(true);
  }, [])

  const login = (token) => {

    localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token)

    const { sub, roles, display_name } = jwtDecode(token);

    const user = {
      username: sub,
      display_name,
      roles,
      token,
      hasRole(role) {
        return this.roles.some((r) => r.roleName === role);
      }
    };

    //for debugging
    console.log(user);

    setUser(user);

    return user;
  };

  const logout = () => {
    setUser(null);

    localStorage.removeItem(LOCAL_STORAGE_TOKEN_KEY);
  };

  const auth = {
    user: user ? { ...user } : null,
    login,
    logout
  };

  // this method is passed down to the callback page, and it is used to return the Spotify auth token to the main app page
  // the Spotify auth token is needed here to put it in a React context
  const getSpotifyAuthToken = (token) => {
    setSpotifyToken(token);
  }

  // this method is part of the SpotifyAuthContext, and it can be called to see if there is currently a Spotify auth token
  const hasValidToken = () => {
    if (spotifyToken) return true;

    return false;
  }

  // this is the value that the SpotifyAuthContext is set to. 
  // contains a spotify token (nullable) and a method to determine if the token is currently valid
  const spotifyAuth = {
    spotifyAuthToken: spotifyToken ? spotifyToken : null,
    hasValidToken
  }

  if (!restoreLoginAttemptCompleted) {
    return null;
  }

  return (
    <div className='App'>
      <AuthContext.Provider value={auth}>
        <SpotifyAuthContext.Provider value={spotifyAuth}>
          <BrowserRouter>
            <Switch>
              <Route exact path="/">
                <LandingPage />
              </Route>
              <Route exact path="/login">
                {!user ? <Login /> : <Redirect to="/" />}
              </Route>
              <Route exact path="/register">
                <Register></Register>
              </Route>
              <Route exact path="/spotify">
                <SpotifyAuthorization />
              </Route>
              <Route exact path="/callback">
                <CallbackPage passSpotifyAuthToken={getSpotifyAuthToken} />
              </Route>
              <Route exact path="/home">
                <Home></Home>
              </Route>
            </Switch>
          </BrowserRouter>
        </SpotifyAuthContext.Provider>
      </AuthContext.Provider>
    </div>
  );
}

export default App;
