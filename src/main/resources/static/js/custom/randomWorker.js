importScripts("/static/js/common/common-util.js");

let data = {};
let randomList = []
    , originalList = []
    , newTeamFlag = false;
onmessage = async function (evt) {
    data = evt.data;
    newTeamFlag =  data.newTeamFlag;
    originalList = [data.list.top, data.list.jg, data.list.mid, data.list.bot, data.list.sup];
    while(data.flag) {
        randomList = [];
        for (const [key, value] of Object.entries(data.list)) {
            randomList.push({ obj: value, randomValue: Math.random() })
        }
        randomList.sort(function(a, b) {
            return a.randomValue - b.randomValue;
        });
        if(checkSameLine()) {
            postMessage(randomList.slice());
        }

        await sleep(data.speed * (newTeamFlag ? 5 : 1));
    }
}

function checkSameLine() {
    if (newTeamFlag) {
        return true;
    }
    for (let i = 0; i < randomList.length; i++) {
        if (randomList[i].obj === originalList[i]) {
            return false;
        }
    }
    return true;
}