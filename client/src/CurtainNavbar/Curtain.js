function Curtain() {

    function openNav() {
        document.getElementById("myNav").style.width = "20%";
      }
      
      function closeNav() {
        document.getElementById("myNav").style.width = "0%";
      }

    return(
        <>
        <div id="myNav" className="overlay">
            <a href="javascript:void(0)" class="closebtn" onclick={closeNav}>&times;</a>
            <div className="overlay-content">
                <a href="#">Home</a>
                <a href="#">Manage playlists</a>
                <a href="#">View all songs</a>
                <a href="#">Contact</a>
            </div>
            <span style="font-size:70px;cursor:pointer" onclick={openNav}>&#9776;</span>

            <br></br>
            <div>
            <h2>Click on the element above to open the fullscreen overlay navigation menu.</h2>
            <p>In this example, the navigation menu will slide in, from left to right:</p>
            </div>
        </div>
        </>
    )
}