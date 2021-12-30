let $toggleGameData, ingameApiWorker, encryptIds = {}, parsedIngameData = {}, nicknames = {};
const blackImage = "data:image/jpeg;base64,iVBORw0KGgoAAAANSUhEUgAAADwAAAA9CAIAAAB+wp2AAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAABeSURBVGhD7c4BCQAxDACxeZp/CdX0z0wcFBIFOd9CZxZ66buKdEW6Il2RrkhXpCvSFemKdEW6Il2RrkhXpCvSFemKdEW6Il2RrkhXpCvSFemKdEW6Il2RrmxOLzPzA3ZtxVt56UJ2AAAAAElFTkSuQmCC"

function initGameDataPlugin() {
  /* variable */
  $toggleGameData = $('#toggleGameData')

  /* event */
  $toggleGameData.on('click', function() {
    let gameToggle = $(this).data('gameToggle');
    if (gameToggle === 'off') {
      $(this).data('gameToggle', 'on')
      $(this).val('게임데이터 ON')
      $('.gameDataGroup').show()
      runIngameApiWorker();
      resetImages()
    } else {
      $(this).data('gameToggle', 'off')
      $(this).val('게임데이터 OFF')
      $('.gameDataGroup').hide()
      ingameApiWorker.terminate();
    }
    $(this).toggleClass('btn-danger').toggleClass('btn-primary')
  })

  /* function */
  $toggleGameData.getToggleVal = function () {
    return $(this).data('gameToggle') === 'on';
  }
}

function runIngameApiWorker() {
  if (ingameApiWorker) {
    ingameApiWorker.terminate();
  }
  ingameApiWorker = new Worker('/static/js/custom/mng/ingameApiWorker.js');
  ingameApiWorker.onmessage = function (evt) {
    parseGameData(evt.data)
  }
  let currentEncryptIds = getCurrentEncryptIds();
  if (currentEncryptIds.length > 0) {
    ingameApiWorker.postMessage({ids: currentEncryptIds})
  }
}

function resetImages() {
  $("img.gameDataGroup, .gameDataGroup img").attr('src', blackImage).data('summoner', null);
}

function getCurrentEncryptIds() {
  let ids = [];
  let hasUser = 0;
  let $userNameInput = $('#team .team-position input');
  $.each($userNameInput,function (idx, obj) {
    if ($(obj).val().trim() !== '') {
      hasUser++;
    }
  });
  if (hasUser !== 10) {
    return ids;
  }
  $.each($userNameInput,function (idx, obj) {
    if ($(obj).val().trim() !== '') {
      let encryptId = encryptIds[$(obj).val().trim()] || '';
      if (encryptId) {
        ids.push(encryptId)
      }
    }
  });
  return ids;
}

function parseGameData(ingameData) {
  parsedIngameData = {};
  findTeam(ingameData.participants);
  parseParticipants(ingameData.participants);
  parseBannedChampions(ingameData.bannedChampions);
  setIngameDatas()
}

function findTeam(participants) {
  let $userInput = $('#team .team-position input');
  for(let i = 0; i < $userInput.length; i++) {
    let userNick = $userInput.eq(i).val();
    let encryptId = encryptIds[userNick];
    let team = $userInput.eq(i).data('team');
    for(let j = 0; j < participants.length; j++) {
      let participant = participants[j];
      if (encryptId === participant.summonerId) {
        parsedIngameData[String(participant.teamId)] = { team: team, summoner: {}, bannedChampions: [] };
        parsedIngameData[String(participant.teamId === 100 ? 200 : 100)] = {team :(team === 'a' ? 'b' : 'a'), summoner: {}, bannedChampions: []}
        return;
      }
    }
  }
}

function parseParticipants(participants) {
  $.each(participants, function (idx, obj) {
    obj.nickname = nicknames[obj.summonerId] || "";
    if (obj.nickname === "") {
      return;
    }
    parsedIngameData[String(obj.teamId)].summoner[obj.nickname] = obj;
  })
}

function parseBannedChampions(bannedChampions) {
  $.each(bannedChampions, function (idx, obj) {
    parsedIngameData[String(obj.teamId)].bannedChampions.push(obj);
  })
}

function setIngameDatas() {
  $.each(parsedIngameData, function ($key, $team) {
    let teamKey = $team.team;
    let summoner = $team.summoner;
    let bannedChampions = $team.bannedChampions;
    $.each($("div.team-position"), function (idx, position) {
      let nickname = $(position).find(`input[data-team='${teamKey}']`).val();
      let summonerElement = summoner[nickname];
      if (summonerElement === undefined) {
        return;
      }
      $(position).find(`img[data-team="${teamKey}"]`).data('summoner', summonerElement).attr('src', summonerElement.championImage);
    })

    $.each(bannedChampions, function (idx, obj) {
      $(`.gameDataGroup img[data-team="${teamKey}"][data-ban-order="${idx}"]`).data('summoner', obj).attr('src', obj.championImage);
    });
  })
}