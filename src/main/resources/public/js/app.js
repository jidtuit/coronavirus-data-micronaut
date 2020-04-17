const isDevMode = true;
const url = isDevMode ? "fakeData/data.json" : "/covid"

window.addEventListener("DOMContentLoaded", event => {
    fetchData()
        .then(data => {
            loadRegionSelector(data);
            return data;
        })
        .then(data => drawChart(data) );
});

function fetchData() {
    return fetch(url)
        .then(response => response.json());
}

function loadRegionSelector(data) {
    let select = document.getElementById("regionSelect");
    let firstOpts = ["ALL", "MD", "CL", "IB"];
    // Most relevant values
    firstOpts.map(region => addNode(region, select));

    // All other values
    Object.keys(data)
        .filter(region => !firstOpts.includes(region))
        .map(region => addNode(region, select));
}

function addNode(region, select) {
    let node = document.createElement("option");
    node.text = region;
    node.value = region;
    select.add(node);
}

function drawChart(data) {

}