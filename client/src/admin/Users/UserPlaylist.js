function UserPlaylist({p}) {


    function deletePlaylist() {
        
    }

    return(
        <>
            <tr>
                <td>{p.name}</td>
                <button onClick={deletePlaylist}>Delete</button>
            </tr>
        </>
    )
}
export default UserPlaylist;