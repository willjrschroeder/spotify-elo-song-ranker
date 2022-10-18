function Track(props) {
    return(
        <>
        <tr>
            <td>{props.title}</td>
            <td>{props.pop}</td>
            <td>{props.artists.map( a => a.artistName)}</td>
            <td>{props.elo}</td>
        </tr> 
        </>
    )
}
export default Track;