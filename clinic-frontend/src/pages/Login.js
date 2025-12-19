import { useState } from "react";
import api from "../api/axios";
import Verify from "./Verify";

function Login() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [needsVerify, setNeedsVerify] = useState(false);

    const login = async (e) => {
        e.preventDefault();
        try {
            const res = await api.post("/auth/login", { email, password });
            localStorage.setItem("token", res.data.token);
            window.location.href = "/dashboard";
        } catch (err) {
            if (err.response?.status === 409) {
                setNeedsVerify(true);
            } else {
                alert("Invalid credentials");
            }
        }
    };

    if (needsVerify) {
        return (
            <Verify
                email={email}
                onSuccess={() => setNeedsVerify(false)}
            />
        );
    }

    return (
        <div className="auth-box">
            <h2>Login</h2>

            <input placeholder="Email"
                   onChange={e => setEmail(e.target.value)}
            />
            <input type="password" placeholder="Password"
                   onChange={e => setPassword(e.target.value)}
            />

            <button onClick={login}>Login</button>

            <p onClick={() => window.location.href = "/signup"}>
                Create new account
            </p>
        </div>
    );
}

export default Login;
