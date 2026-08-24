import { useEffect, useState } from "react";
import { usePlaidLink } from "react-plaid-link";

interface LinkTokenResponse {
    link_token: string;
}

export default function PlaidLinkButton() {
    const [linkToken, setLinkToken] = useState<string | null>(null);

    useEffect(() => {
        console.log(linkToken);
    }, [linkToken]);

    useEffect(() => {
        const getLinkToken = async () => {
            const response = await fetch(
                "http://localhost:8080/api/plaid/link-token",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                }
            );

            const data: LinkTokenResponse = await response.json();

            setLinkToken(data.link_token);
        };

        getLinkToken();
    }, []);

    const { open, ready } = usePlaidLink({
        token: linkToken,

        onSuccess: async (publicToken, metadata) => {
            console.log("Public token:", publicToken);
            console.log("Metadata:", metadata);

            if(publicToken == null){
                return;
            }

            const response = await fetch(
                `http://localhost:8080/api/plaid/exchange-token?publicToken=${encodeURIComponent(publicToken)}`,
                {
                    method: "POST"
                }
            );

            const success = await response.json();

            if (success) {
                console.log("Token successfully exchanged and saved.");
                // Load dashboard
            } else {
                console.error("Token exchange failed.");
            }
        }
    });

    return (
        <button onClick={() => open()} disabled={!ready}>
            Connect Bank Account
        </button>
    );
}