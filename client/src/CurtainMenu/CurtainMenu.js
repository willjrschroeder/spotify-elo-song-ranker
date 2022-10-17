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
        <div className="links">
          <Link to="/home"><button>Home</button></Link>
          <Link to="/playlistManager"><button>Manage Playlists</button></Link>
          {auth.user ? (auth.user.hasRole("admin") ? <Link to="/admin"><button>Manage Users</button></Link> : <div></div>) : <div></div>}
        </div>
        {auth.user ? <Link><button onClick={auth.logout}>Logout</button></Link> : <Link to="/login"><button>Login</button></Link>}

      </nav>
    </>
  );
}
export default CurtainMenu;