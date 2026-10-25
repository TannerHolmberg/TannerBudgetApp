import PlaidLinkButton from "./Components/PlaidLinkButton";
import {useEffect, useState} from "react";
import Dashboard from "./Components/Dashboard.tsx";

function App() {
    const [loading, setLoading] = useState(true);
    const [hasToken, setHasToken] = useState(false);

    useEffect(() => {

        const checkForToken = async () => {
            try {
                const response = await fetch(
                    "http://localhost:8080/user-tokens/does-token-exist"
                );

                const exists = response;

                if(exists){
                    setHasToken(true);
                }else{
                    setHasToken(false);
                }

            } catch (error) {
                console.error("Failed to check token:", error);

            } finally {
                setLoading(false);
            }
        };

        checkForToken();

    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    if (hasToken) {
        return <Dashboard />;
    }

    return (
        <PlaidLinkButton
            onConnectionSuccess={() => setHasToken(true)}
        />
    );
}

export default App;

