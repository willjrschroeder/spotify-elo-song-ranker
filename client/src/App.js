import './App.css';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Home from './Home/Home';
import Register from './Register/Register'
import Login from './Login/Login'
import AuthContext from './context/AuthContext';
import jwtDecode from "jwt-decode";
import React, {useState} from "react";

function App() {

  const [user, setUser] = useState();

const login = (token) => {

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
};

const auth = {
  user: user ? {...user} : null,
  login,
  logout
};



  return (
    <div className='App'>
    <AuthContext.Provider value = {auth}>
      <BrowserRouter>
          <Switch>
            <Route exact path = "/">
              <Home/>
            </Route>
            <Route exact path = "/login">
              <Login></Login>
            </Route>
            <Route exact path = "/register">
              <Register></Register>
            </Route>
          </Switch>
      </BrowserRouter>
    </AuthContext.Provider>
    </div>
  );
}

export default App;
