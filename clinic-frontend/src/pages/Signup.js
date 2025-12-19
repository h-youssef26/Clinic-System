import { useState } from "react";
import api from "../api/axios";
import Verify from "./Verify";

function Signup() {
    const [form, setForm] = useState({
        username: "",
        email: "",
        password: ""
    });

    const [step, setStep] = useState("signup");

    const signup = async (e) => {
        e.preventDefault();
        await api.post("/auth/signup", form);
        setStep("verify");
    };

    if (step === "verify") {
        return (
            <Verify
                email={form.email}
                onSuccess={() => window.location.href = "/"}
            />
        );
    }

    return (
        <div className="auth-box">
            <h2>Create Account</h2>

            <input placeholder="Username"
                   onChange={e => setForm({...form, username: e.target.value})}
            />
            <input placeholder="Email"
                   onChange={e => setForm({...form, email: e.target.value})}
            />
            <input type="password" placeholder="Password"
                   onChange={e => setForm({...form, password: e.target.value})}
            />

            <button onClick={signup}>Sign Up</button>

            <p onClick={() => window.location.href = "/"}>
                Already have an account? Login
            </p>
        </div>
    );
}

export default Signup;
