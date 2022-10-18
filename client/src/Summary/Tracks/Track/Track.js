function Track(props) {

    let artists = props.artists.map(a => a.artistName);

    function isEmptyOrUndefined(array) {
        if (array.length == 0 || array === undefined) {
            return false;
          } else {
            return true;
          }
    }

    return(
        <>
        <tr>
            <td>{props.title}</td>
            <td>{props.pop}</td>
            <td>{isEmptyOrUndefined ? artists.join(", ") : "No "}</td>
            <td>{props.elo}</td>
        </tr> 
        </>
    )
}
export default Track;