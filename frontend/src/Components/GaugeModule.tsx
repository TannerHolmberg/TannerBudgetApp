import { Gauge } from "@mui/x-charts/Gauge";
import "./CSS/Gauge.css";

interface GaugeModuleProps {
    title: string;
    value: number;
    max: number;
    onClick: () => void;
}

export default function GaugeModule({
                                        title,
                                        value,
                                        max,
                                        onClick,
                                    }: GaugeModuleProps) {

    return (
        <div className={"GaugeContainer"}
             onClick={onClick}
             role="button"
             tabIndex={0}
             onKeyDown={(e) => {
                 if (e.key === "Enter" || e.key === " ") {
                     onClick();
                 }
             }}>
            <h2 className={"GaugeTitle"}>
                {title}
            </h2>

            <div
                style={{
                    width: "220px",
                    height: "220px",
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    color: "red",
                }}
            >
                <Gauge
                    value={value}
                    valueMax={max}
                    startAngle={-110}
                    endAngle={110}
                    sx={{
                        [`& .MuiGauge-valueText`]: {
                            fontSize: 28,
                            transform: "translate(0px, 0px)",
                        },
                        [`& .MuiGauge-valueText text`]: {
                            color: "#D3DAD9",
                        },
                    }}

                    text={({ value, valueMax }) =>
                        `${value} / ${valueMax}`
                    }
                />
            </div>
        </div>
    );
}