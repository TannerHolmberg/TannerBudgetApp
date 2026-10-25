import { useState } from "react";

interface Gauge {
    gaugeName: string;
    budgetAmount: number;
    categoryList: string[];
}

interface AddGaugeProps {
    onGaugeAdded: (gauge: Gauge) => void;
}

export default function AddGauge({ onGaugeAdded }: AddGaugeProps) {
    const [isOpen, setIsOpen] = useState(false);
    const [name, setName] = useState("");
    const [budgetAmount, setBudgetAmount] = useState("");
    const [loading, setLoading] = useState(false);

    const addGauge = async () => {
        if (!name.trim() || !budgetAmount) {
            return;
        }

        try {
            setLoading(true);

            const response = await fetch(
                `http://localhost:8080/gauge/create-gauge?gaugeName=${encodeURIComponent(
                    name
                )}&budgetAmount=${budgetAmount}`,
                {
                    method: "POST",
                }
            );

            if (!response.ok) {
                throw new Error("Failed to create gauge");
            }

            const newGauge: Gauge = await response.json();

            // Give the new gauge back to Dashboard
            onGaugeAdded(newGauge);

            // Clear form
            setName("");
            setBudgetAmount("");
            setIsOpen(false);

        } catch (error) {
            console.error("Error creating gauge:", error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{ position: "relative", display: "inline-block" }}>

            <button onClick={() => setIsOpen(!isOpen)}>
                + Add Gauge
            </button>

            {isOpen && (
                <div
                    style={{
                        position: "absolute",
                        top: "100%",
                        right: 0,
                        marginTop: "8px",
                        padding: "16px",
                        width: "220px",
                        background: "white",
                        border: "1px solid #ddd",
                        borderRadius: "8px",
                        boxShadow: "0 4px 12px rgba(0,0,0,0.15)",
                        zIndex: 10,
                    }}
                >
                    <h3>Add Gauge</h3>

                    <input
                        type="text"
                        placeholder="Name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                    />

                    <input
                        type="number"
                        placeholder="Budget Amount"
                        value={budgetAmount}
                        onChange={(e) => setBudgetAmount(e.target.value)}
                    />

                    <button
                        onClick={addGauge}
                        disabled={loading}
                    >
                        {loading ? "Adding..." : "Add"}
                    </button>
                </div>
            )}
        </div>
    );
}