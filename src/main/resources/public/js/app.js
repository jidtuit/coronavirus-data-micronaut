import { fetchData } from "./modules/utils.js"
import { loadRegions, initChart, refreshChart } from "./modules/covid.js"
import { regionsLit, chartTypes } from "./modules/constants.js"

const app = new Vue({
    el: "#myApp",
    data: {
        url: "/covid",
        metadataUrl: "/covid/metadata",
        allData: {},
        selectedData: {},
        regions: [],
        selectedRegion: "ALL",
        charts: Object.keys(chartTypes),
        chartsObjs: [],
        lit: {
            regions: regionsLit,
            charts: chartTypes
        },
        metadata:{}
    },
    methods: {
        async init() {
            const data = fetchData(this.url)
                .catch(reason => console.error("Error fetching data: ", reason));

            const meta = fetchData(this.metadataUrl)
                .catch(reason => console.error("Error fetching metadata: ", reason));

            this.regions = await loadRegions(this.lit.regions)
            this.allData = await data;
            this.initCharts()
            this.metadata = await meta
        },
        async changeRegion(event) {
            this.selectedRegion = event.target.selectedOptions[0].value;
            await this.refreshChart();
        },
        async initCharts() {
            this.selectedData = this.allData[this.selectedRegion];
            const promises = this.charts.map(c =>
                initChart(c, this.selectedData, `${this.lit.charts[c]} in ${this.lit.regions[this.selectedRegion]}`)
            );

            await Promise.all(promises).then(values => {
                const ret = {};
                values.forEach(v => ret[v.type] = v.chart);
                this.chartsObjs = ret;
            });
        },
        async refreshChart() {
            this.selectedData = this.allData[this.selectedRegion];
            const promises = this.charts.map(c => {
                const title = `${this.lit.charts[c]} in ${this.lit.regions[this.selectedRegion]}`;
                return refreshChart(this.chartsObjs[c], c, this.selectedData, title);
            });

            await Promise.all(promises)
        }
    },
    computed: {
    },
    mounted() {
        this.init();
    }
});

