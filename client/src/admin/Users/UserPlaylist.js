function UserPlaylist({p}) {

    return(
        <>
            <tr>
            <td><img className="img-fluid ${3|rounded-top,rounded-right,rounded-bottom,rounded-left,rounded-circle,|}" src={p.playlistImageLink} alt="img"></img></td>
                <td>{p.playlistName}</td>
            </tr>
        </>
    )
}
export default UserPlaylist;