import './App.css';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import LandingPage from './LandingPage/LandingPage';
import Register from './Register/Register'
import Login from './Login/Login'
import AuthContext from './context/AuthContext';
import jwtDecode from "jwt-decode";
import React, {useState, useEffect} from "react";

//confused on this part, how to identify that this is the token
const LOCAL_STORAGE_TOKEN_KEY = "loginToken";

function App() {

  const [user, setUser] = useState();

  const [restoreLoginAttemptCompleted, setRestoreLoginAttemptCreated] = useState(false);

useEffect(() => {
  const token = localStorage.getItem(LOCAL_STORAGE_TOKEN_KEY);
  if (token) {
    login(token)
  }
  setRestoreLoginAttemptCreated(true);
}, [])

  const login = (token) => {

    localStorage.setItem(LOCAL_STORAGE_TOKEN_KEY, token)

  const { sub: username, authorities : authoritiesString } = jwtDecode(token);

  const roles = authoritiesString.split(',');

  const user = {
    username,
    roles,
    token,
    hasRole(role) {
      return this.roles.includes(role);
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
  user: user ? {...user} : null,
  login,
  logout
};

if (!restoreLoginAttemptCompleted) {
  return null;
}

  return (
    <div className='App'>
    <AuthContext.Provider value = {auth}>
      <BrowserRouter>
          <Switch>
            <Route exact path = "/">
              <LandingPage/>
            </Route>
            <Route exact path = "/login">
              {!user ? <Login/> : <Redirect to="/"/>}
            </Route>
            <Route exact path = "/register">
              <Register></Register>
            </Route>
            <Route exact path= "/home">
              LandingPage
            </Route>
          </Switch>
      </BrowserRouter>
    </AuthContext.Provider>
    </div>
  );
}

export default App;
