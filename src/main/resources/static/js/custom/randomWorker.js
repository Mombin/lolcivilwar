importScripts("/static/js/common/common-util.js");

let data = {};
let randomList = []
    , originalList = []
    , newTeamFlag = false
    , tierOn;
onmessage = function (evt) {
    data = evt.data;
    newTeamFlag =  data.newTeamFlag;
    originalList = [data.list.top, data.list.jg, data.list.mid, data.list.bot, data.list.sup];
    tierOn = data.tierOn;
    while(data.flag) {
        randomList = [];
        for (const [key, value] of Object.entries(data.list)) {
            randomList.push({ obj: value, randomValue: Math.random(), tierPoint: value.tierPoint })
        }
        randomList.sort(function(a, b) {
            return a.randomValue - b.randomValue;
        });
        if(checkSameLine()) {
            if (tierOn && randomList.length > 2) {
                if (Math.abs(randomList[0].tierPoint - randomList[1].tierPoint) > 1) {
                    continue;
                }
            }
            postMessage(randomList.slice());
        }

        sleep(data.speed * (newTeamFlag ? 5 : 1));
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