function Artist(props) {

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
            <td>{props.artist}</td>
            <td>{isEmptyOrUndefined(props.genres) ? props.genres.map(g => g.genreName + " | ") : "No genres available"}</td>
        </tr> 
        </>
    )
}
export default Artist;