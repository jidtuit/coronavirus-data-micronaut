const regionsLit = {
    ALL: "SPAIN",
    MD: "MADRID",
    CL: "CASTILLA Y LEÓN",
    IB: "ISLAS BALEARES",
    AN: "ANDALUCÍA",
    AR: "ARAGÓN",
    AS: "ASTURIAS",
    CN: "CANARIAS",
    CB: "CANTABRIA",
    CM: "CASTILLA LA MANCHA",
    CT: "CATALUÑA",
    EX: "EXTREMADURA",
    GA: "GALICIA",
    RI: "LA RIOJA",
    MC: "MURCIA",
    NC: "NAVARRA",
    PV: "PAÍS VASCO",
    VC: "VALENCIA",
    CE: "CEUTA",
    ML: "MELILLA"
}

const chartTypes = {
    totalCases:"Total cases",
    totalCasesInc:"Total cases daily variation",
    uciCases:"UCI cases",
    uciCasesInc:"UCI cases daily variation",
    hospitalCases:"Hospital cases",
    hospitalCasesInc:"Hospital cases daily variation",
    deathCases:"Mortality",
    deathCasesInc:"Mortality daily variation",
    recoveredCases:"Recovered cases",
    recoveredCasesInc:"Recovered cases daily variation"
};

const chartColors = {
    totalCases: {bgColor: 'rgba(255, 99, 132, 0.2)', borderColor: 'rgba(255, 99, 132, 1)'},
    totalCasesInc: {bgColor: 'rgba(255, 99, 132, 0.2)', borderColor: 'rgba(255, 99, 132, 1)'},
    uciCases: {bgColor: 'rgba(54, 162, 235, 0.2)', borderColor: 'rgba(54, 162, 235, 1)'},
    uciCasesInc: {bgColor: 'rgba(54, 162, 235, 0.2)', borderColor: 'rgba(54, 162, 235, 1)'},
    hospitalCases: {bgColor: 'rgba(255, 159, 64, 0.2)', borderColor: 'rgba(255, 159, 64, 1)'},
    hospitalCasesInc: {bgColor: 'rgba(255, 159, 64, 0.2)', borderColor: 'rgba(255, 159, 64, 1)'},
    deathCases: {bgColor: 'rgba(153, 102, 255, 0.2)', borderColor: 'rgba(153, 102, 255, 1)'},
    deathCasesInc: {bgColor: 'rgba(153, 102, 255, 0.2)', borderColor: 'rgba(153, 102, 255, 1)'},
    recoveredCases: {bgColor: 'rgba(75, 192, 192, 0.2)', borderColor: 'rgba(75, 192, 192, 1)'},
    recoveredCasesInc: {bgColor: 'rgba(75, 192, 192, 0.2)', borderColor: 'rgba(75, 192, 192, 1)'}
}


export { regionsLit, chartTypes, chartColors }