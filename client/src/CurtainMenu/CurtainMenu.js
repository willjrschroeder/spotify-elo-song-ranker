import React, { useState, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
import "./CurtainMenu.css";
import Menu from "./menu.svg";
import AuthContext from "../context/AuthContext";
import userEvent from "@testing-library/user-event";

function CurtainMenu() {

  const auth = useContext(AuthContext);

  const [toggleNav, setToggleNav] = useState(false);

  const [checkWidth, setCheckWidth] = useState(window.innerWidth);

  const checkFunc = () => {
    console.log(checkWidth);
    setCheckWidth(window.innerWidth);
  };

  useEffect(() => {
    window.addEventListener("resize", checkFunc);

    return () => {
      window.removeEventListener("resize", checkFunc);
    };
  }, []);

  const toggleNavFunc = () => {
    setToggleNav(!toggleNav);
  };

  return (
    <>
      {checkWidth < 900 && (
        <button onClick={toggleNavFunc} className="floating-btn">
          <img src={Menu} />
        </button>
      )}

      <nav className={toggleNav ? "active" : ""}>

        {checkWidth < 900 && (
          <button 
          onClick={toggleNavFunc} className="close-curtain">
            X
          </button>
        )}
        <Link to="./">Landing Page</Link>
        <Link to="/home">Home</Link>
        <Link to="/playlistManager">Playlists</Link>
        <Link to="/summary">Summary</Link>
        <Link to="/eloGame">Play Game</Link>
        {auth.user.hasRoles("admin") ? <Link to="/admin">Manage Users</Link> : <></>}
        
      </nav>
    </>
  );
}
export default CurtainMenu;