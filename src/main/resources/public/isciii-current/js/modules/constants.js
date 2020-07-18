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
    pcrPositiveCases: "PCR positive cases",
    pcrPositiveCasesInc: "PCR positive cases daily variation",
    testAcPositiveCases: "Test AC positive cases",
    testAcPositiveCasesInc: "Test AC positive cases daily variation",
    otherPositiveCases: "Other positive cases",
    otherPositiveCasesInc: "Other positive cases daily vsriation",
    unknownPositiveCases: "Unknown positive cases",
    unknownPositiveCasesInc: "Unknown positive cases daily vsriation",
};

const chartColors = {
    totalCases: {bgColor: 'rgba(255, 99, 132, 0.2)', borderColor: 'rgba(255, 99, 132, 1)'},
    totalCasesInc: {bgColor: 'rgba(255, 99, 132, 0.2)', borderColor: 'rgba(255, 99, 132, 1)'},
    pcrPositiveCases: {bgColor: 'rgba(54, 162, 235, 0.2)', borderColor: 'rgba(54, 162, 235, 1)'},
    pcrPositiveCasesInc: {bgColor: 'rgba(54, 162, 235, 0.2)', borderColor: 'rgba(54, 162, 235, 1)'},
    testAcPositiveCases: {bgColor: 'rgba(255, 159, 64, 0.2)', borderColor: 'rgba(255, 159, 64, 1)'},
    testAcPositiveCasesInc: {bgColor: 'rgba(255, 159, 64, 0.2)', borderColor: 'rgba(255, 159, 64, 1)'},
    otherPositiveCases: {bgColor: 'rgba(153, 102, 255, 0.2)', borderColor: 'rgba(153, 102, 255, 1)'},
    otherPositiveCasesInc: {bgColor: 'rgba(153, 102, 255, 0.2)', borderColor: 'rgba(153, 102, 255, 1)'},
    unknownPositiveCases: {bgColor: 'rgba(75, 192, 192, 0.2)', borderColor: 'rgba(75, 192, 192, 1)'},
    unknownPositiveCasesInc: {bgColor: 'rgba(75, 192, 192, 0.2)', borderColor: 'rgba(75, 192, 192, 1)'}
}


export { regionsLit, chartTypes, chartColors }