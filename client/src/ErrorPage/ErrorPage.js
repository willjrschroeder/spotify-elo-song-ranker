import { Link } from "react-router-dom"
import "./ErrorPage.css"

function ErrorPage() {
    return(
        <>
            <div className="error">
                <div className="message">404: Page not found...</div>
                <div>
                    <Link to="/home" className="return"><button>Return to home</button></Link>
                </div>
            </div>
        </>
    )
}
export default ErrorPage;
