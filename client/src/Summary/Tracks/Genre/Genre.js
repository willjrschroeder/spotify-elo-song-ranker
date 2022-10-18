function Genre(props) {

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
      }

    let name = props.genreName

    

    return(
        <>
        <tr>
            <td>{capitalizeFirstLetter(name)}</td>
        </tr> 
        </>
    )
}
export default Genre;