import './App.css';
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";
import LandingPage from './LandingPage/LandingPage';
import Register from './Register/Register';
import Login from './Login/Login';
import AuthContext from './context/AuthContext';
import jwtDecode from "jwt-decode";
import React, { useState, useEffect } from "react";
import Home from "./Home/Home";
import SpotifyAuthorization from './SpotifyAuthorization/SpotifyAuthorization';
import CallbackPage from './SpotifyAuthorization/CallbackPage';
import SpotifyAuthContext from './context/SpotifyAuthContext';
import ConfirmationPage from './SpotifyAuthorization/ConfirmationPage';
import ManagePlaylists from './ManagePlaylists/ManagePlaylists';
import EloGamePage from './EloGamePage/EloGamePage';
import ErrorPage from './ErrorPage/ErrorPage';
import ConfirmDeletePlaylist from './ConfirmDeletePlaylist/ConfirmDeletePlaylist';
import ConfirmDeleteUser from './ConfirmDeleteUser/ConfirmDeleteUser';
import Summary from './Summary/Summary';
import Curtain from './CurtainMenu/CurtainMenu';
import Admin from './Admin/Admin';
import ViewByPlaylist from './ViewByPlaylist/ViewByPlaylist';
import { library } from '@fortawesome/fontawesome-svg-core';
import { fab } from '@fortawesome/free-brands-svg-icons'
import {faCirclePlay, faCircleStop} from '@fortawesome/free-solid-svg-icons'

library.add(fab, faCirclePlay, faCircleStop);


const LOCAL_STORAGE_JWT_TOKEN_KEY = "loginToken";
const LOCAL_STORAGE_SPOTIFY_TOKEN_KEY = "spotifyToken";

function App() {

  const [user, setUser] = useState();
  const [spotifyToken, setSpotifyToken] = useState();

  const [restoreLoginAttemptCompleted, setRestoreLoginAttemptCreated] = useState(false);
  const [restoreSpotifyTokenAttemptCompleted, setRestoreSpotifyTokenAttemptCompleted] = useState(false);

  useEffect(() => {
    //this attempts to restore the JWT access token 
    const token = localStorage.getItem(LOCAL_STORAGE_JWT_TOKEN_KEY);
    if (token) {
      login(token);
    }
    setRestoreLoginAttemptCreated(true);

    //this attempts to restore the Spotify access token
    setSpotifyToken(localStorage.getItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY));
    setRestoreSpotifyTokenAttemptCompleted(true);
  }, [])

  const login = (token) => {

    const { sub, roles, display_name, id, disabled } = jwtDecode(token);

    const user = {
      id: id,
      username: sub,
      display_name,
      roles,
      disabled,
      token,
      hasRole(role) {
        return this.roles.some((r) => r.roleName === role);
      }
    };

    if(user.disabled){
      return false;
    }

    localStorage.setItem(LOCAL_STORAGE_JWT_TOKEN_KEY, token)
    setUser(user);

    return user;
  };

  const logout = () => {
    localStorage.removeItem(LOCAL_STORAGE_JWT_TOKEN_KEY);
    setUser(null);
    localStorage.removeItem(LOCAL_STORAGE_SPOTIFY_TOKEN_KEY);
    setSpotifyToken(null);
  };

  const auth = {
    user: user ? { ...user } : null,
    login,
    logout
  };

  // this is the value that the SpotifyAuthContext is set to. 
  // contains a spotify token (null if not yet set)
  const spotifyAuth = {
    spotifyAccessToken: spotifyToken ? spotifyToken : null,
  }

  if (!restoreLoginAttemptCompleted) {
    return null;
  }

  if (!restoreSpotifyTokenAttemptCompleted) {
    return null;
  }

  return (
    <div className='App'>
      <AuthContext.Provider value={auth}>
        <SpotifyAuthContext.Provider value={spotifyAuth}>
          <BrowserRouter>
          <Curtain></Curtain>
            <Switch>
              <Route exact path="/">
                {!user ? <LandingPage /> : <Redirect to="/home" />}
              </Route>
              <Route exact path="/login">
                {!user ? <Login /> : <Redirect to="/home" />}
              </Route>
              <Route exact path="/register">
                <Register />
              </Route>
              <Route exact path="/spotify">
                {user ? <SpotifyAuthorization /> : <Redirect to="/" />}
              </Route>
              <Route exact path="/callback">
                {user ? <CallbackPage setSpotifyToken={setSpotifyToken}/> : <Redirect to="/spotify" />}
              </Route>
              <Route exact path="/spotify/confirmation">
                {spotifyToken ? <ConfirmationPage /> : <Redirect to="/spotify" />}
              </Route>
              <Route exact path="/home">
                {(user && spotifyToken) ? <Home /> : <Redirect to="/spotify" />}
              </Route>
              <Route exact path="/playlistManager">
                {user ? <ManagePlaylists></ManagePlaylists> : <Redirect to="/"/>}
              </Route>
              <Route exact path="/eloGame">
              {user ? <EloGamePage></EloGamePage> : <Redirect to="/"/>}
              </Route>
              <Route exact path="/confirmDelUser">
              {user ? <ConfirmDeleteUser></ConfirmDeleteUser> : <Redirect to="/"/>}
              </Route>
              <Route exact path="/confirmDelPlaylist">
              {user ? <ConfirmDeletePlaylist></ConfirmDeletePlaylist> : <Redirect to="/"/>}
              </Route>
              <Route exact path="/summary">
              {user ? <Summary></Summary> : <Redirect to="/"/>}
              </Route>
              <Route exact path="/playlistRankings">
              {user ? <ViewByPlaylist /> : <Redirect to="/"/>}
              </Route>
              <Route exact path="/admin">
              {user ? (user.hasRole("admin") ? <Admin></Admin> : <Redirect to="/home"/>) : <Redirect to="/"/>}
                
              </Route>
              <Route>
                <ErrorPage></ErrorPage>
              </Route>
            </Switch>
          </BrowserRouter>
        </SpotifyAuthContext.Provider>
      </AuthContext.Provider>
    </div>
  );
}

export default App;
