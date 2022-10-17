import { Link } from "react-router-dom"
import "./ErrorPage.css"

function ErrorPage() {
    return(
        <>
            <div>
                <h2 className="header">404</h2>
                <div>
                    <Link to="/home">Return to home</Link>
                </div>
            </div>
        </>
    )
}
export default ErrorPage;
