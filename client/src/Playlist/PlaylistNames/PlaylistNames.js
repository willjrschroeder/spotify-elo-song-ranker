import "./PlaylistNames.css"
function PlaylistNames() {
return(<>
    <label class="song-info" id="song-info-1">
        <div class="title">Bunker</div>
        <div class="sub-line">
          <div class="subtitle">Balthazar</div>
          <div class="time">4.05</div>
          <button onClick={props.addPlaylistToDatabase}>Add all tracks from this playlist to rank</button>
        </div>
    </label>
    </>
)

}