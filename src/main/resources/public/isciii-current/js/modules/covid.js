import { chartColors} from "./constants.js";

export async function loadRegions(data) {
    // Most relevant values
    const firstOpts = ["ALL", "MD", "CL", "IB"];

    // All other values
    const otherOpts = Object.keys(data)
        .filter(region => !firstOpts.includes(region));

    return firstOpts.concat(otherOpts);
}

function getChartObj(chartType, data, title) {
    const chart = {
        type: "bar",
        data: {
            labels: [],
            datasets: [{
                label: '# of people',
                data: [],
                backgroundColor: [],
                borderColor: [],
                borderWidth: 1
            }]
        },
        options: {
            title: {
                text: title,
                display: true
            },
            scales: {
                yAxes: [{
                    ticks: {
                        beginAtZero: true
                    }
                }]
            }
        }
    };

    const bgColor = chartColors[chartType].bgColor;
    const borderColor = chartColors[chartType].borderColor;

    data.forEach(d => {
        chart.data.labels.push(d["dataDate"]);
        chart.data.datasets[0].data.push(d[chartType]);
        chart.data.datasets[0].backgroundColor.push(bgColor);
        chart.data.datasets[0].borderColor.push(borderColor);
    });

    return chart;
}

export async function initChart(chartType, data, title) {

    const canvas = document.getElementById(chartType);
    const ctx = canvas.getContext('2d');

    const chartConfig = getChartObj(chartType,data, title);
    const myChart = new Chart(ctx, chartConfig);

    return { type: chartType, chart: myChart}
}

export async function refreshChart(chart, chartType, data, title) {

    const newChart = getChartObj(chartType,data, title);
    chart.type = newChart.type;
    chart.data = newChart.data;
    chart.options = newChart.options;
    chart.update();
}

