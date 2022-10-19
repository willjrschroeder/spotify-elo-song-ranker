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
  function doLogout() {
    toggleNavFunc();
    auth.logout();
  }
  function doLogin() {
    toggleNavFunc();
    auth.login();
  }

  return (
    <>





      <div className="container-fluid">
        <img src="/sortify_logo.png" className="img-fluid rounded-top float-start" alt="Sortify Logo" />
        <h1 className={toggleNav ? "title-active " : "title"}>Spotify Leaderboards</h1>




        <button onClick={toggleNavFunc} className="floating-btn">
          <img src={Menu} />
        </button>


        <nav className={toggleNav ? "active" : ""}>


          <div className="links">
            {auth.user ? <div>
              <Link to="/home"><button onClick={toggleNavFunc}>Home</button></Link>
              <Link to="/playlistManager"><button onClick={toggleNavFunc}>Manage Playlists</button></Link>
              <Link to="/summary"><button onClick={toggleNavFunc}>Summary</button></Link>
              {auth.user ? (auth.user.hasRole("admin") ? <Link to="/admin"><button>Manage Users</button></Link> : <div></div>) : <div></div>}
              <Link to="/"><button onClick={doLogout}>Logout</button></Link>
            </div> : <div><Link to="/login"><button onClick={doLogin}>Login</button></Link></div>}


          </div>


        </nav>
      </div>
    </>
  );
}
export default CurtainMenu;