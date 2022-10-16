function Track(props) {
    return(
        <>
        <tr>
            <td>{props.t.title}</td>
            <td>{props.t.popularityNum}</td>
            <td>{props.t.artists}</td>
            <td>{props.t.eloScore}</td>
        </tr> 
        </>
    )
}
export default Track;