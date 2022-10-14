import "./Playlist.css"
function Playlist(props) {

    return(<>
        <label className={"card-" + props.index}>
            <img src={props.playlistData.images[0].url} alt="Playlist"></img>
        </label>
    </>
    )

}
export default Playlist;