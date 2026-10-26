import { useEffect, useState } from "react";
import GaugeModule from "./GaugeModule";
import AddGauge from "./AddGauge";
import "./CSS/Dashboard.css";


interface Gauge {
    gaugeName: string;
    budgetAmount: number;
    categoryList: string[];
}

interface Transaction {
    transactionId: string;
    userId: string;
    accountId: string;
    merchantName: string | null;
    name: string;
    amount: number;
    category: string;
    date: string;
    pending: boolean;
    removed: boolean;
}

interface GaugeTransactionResponse {
    gaugeMap: Record<string, number>;
    uncategorizedTransactions: Transaction[];
    gaugeFilteredTxn: Record<string, Transaction[]>;
    moneyIn: number | null;
    moneyOut: number | null;
}


export default function Dashboard() {

    const [transactionData, setTransactionData] =
        useState<GaugeTransactionResponse>({
            gaugeMap: {},
            uncategorizedTransactions: [],
            gaugeFilteredTxn: {},
            moneyIn: 0,
            moneyOut: 0,
        });

    const [gauges, setGauges] = useState<Gauge[]>([]);

    const [selectedGauge, setSelectedGauge] = useState<string | null>(null);

    const loadGauges = async () => {
        try {
            const response = await fetch(
                "http://localhost:8080/gauge/get-gauges"
            );

            if (!response.ok) {
                throw new Error("Failed to load gauges");
            }

            const data: Gauge[] = await response.json();

            console.log("Gauges:", data);

            setGauges(data);

        } catch (error) {
            console.error("Error loading gauges:", error);
        }
    };

    useEffect(() => {
        loadGauges();
    }, []);

    useEffect(() => {
        const loadTransactions = async () => {
            try {
                const response = await fetch(
                    "http://localhost:8080/api/plaid/get-transaction-data"
                );

                if (!response.ok) {
                    throw new Error("Failed to load transaction data");
                }

                const data: GaugeTransactionResponse =
                    await response.json();

                console.log("Transactions:", data);

                setTransactionData(data);

            } catch (error) {
                console.error("Error loading transactions:", error);
            }
        };

        loadTransactions();
    }, []);

    return (
        <div className={"background"}>
            <div className = "gaugeCluster">
                <AddGauge
                    onGaugeAdded={(newGauge) => {
                        setGauges((currentGauges) => [
                            ...currentGauges,
                            newGauge
                        ]);
                    }}
                />
                <h3> Money In: $ {transactionData.moneyIn?.toFixed(2) ?? "0.00"} </h3>
                {gauges.map((gauge) => (
                    <GaugeModule
                        key={gauge.gaugeName}
                        title={gauge.gaugeName}
                        value={transactionData.gaugeMap[gauge.gaugeName] ?? 0}
                        max={gauge.budgetAmount}
                        onClick={() => setSelectedGauge(gauge.gaugeName)}
                    />
                ))}
            </div>
            <div className="transactions-panel">

                {selectedGauge === null ? (
                    <div className="transaction-placeholder">
                        <h2>Select a gauge</h2>
                        <p>
                            Select a gauge to see the transactions
                            contributing to its total.
                        </p>
                    </div>
                ) : (
                    <>
                        <h2>{selectedGauge}</h2>

                        {transactionData
                            .gaugeFilteredTxn[selectedGauge]
                            ?.map((transaction) => (
                                <div
                                    key={transaction.transactionId}
                                    className="transaction"
                                >
                                    <div>
                                        <strong>
                                            {transaction.merchantName ??
                                                transaction.name}
                                        </strong>

                                        <small>
                                            {transaction.date}
                                        </small>
                                    </div>

                                    <strong>
                                        ${transaction.amount.toFixed(2)}
                                    </strong>
                                </div>
                            ))}
                    </>
                )}

            </div>
        </div>
    );
}