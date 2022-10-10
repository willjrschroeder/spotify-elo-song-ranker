import './App.css';
import {BrowserRouter, Route, Switch} from "react-router-dom";
import Home from './Home/Home';
import React from 'react';
import Register from './Register/Register'
import Login from './Login/Login'

function App() {
  return (

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
  );
}

export default App;
