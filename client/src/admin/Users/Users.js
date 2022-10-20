function Users(props) {
    return(
        <>
            <option value={props.u.username}>{props.u.username}</option>
        </>
    )
}
export default Users;