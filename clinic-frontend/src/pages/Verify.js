import { useState } from "react";
import api from "../api/axios";

function Verify({ email, onSuccess }) {
    const [code, setCode] = useState("");

    const submit = async (e) => {
        e.preventDefault();
        await api.post("/auth/verify", {
            email,
            verificationCode: code
        });
        alert("Account verified successfully");
        onSuccess();
    };

    return (
        <div className="auth-box">
            <h2>Verify Email</h2>
            <input
                placeholder="Verification Code"
                onChange={e => setCode(e.target.value)}
            />
            <button onClick={submit}>Verify</button>
        </div>
    );
}

export default Verify;
