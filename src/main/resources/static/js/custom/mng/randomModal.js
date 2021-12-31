let $modalTeam
  , $modalTeamBody
  , $randomTeamBtn
  , $modalRandomLeader
  , $modalLeader
  , $modalLeaderClose
  , $randomLeaderBtn;

function initTeamRandomModal() {
  $modalTeam = $('#modalRandomTeam')
  $modalTeamBody = $modalTeam.find(".modal-body.team")
  $randomTeamBtn = $modalTeam.find(".modal-footer [name='randomBtn']")
  /* Event */
  $randomTeamBtn.on('click',  function() {
    flag = !flag;
    let param = {
      list: [],
      newTeamFlag: ($modalTeamBody.data('team') === 'a' ? newTeamFlagA : newTeamFlagB)
    }
    $.each(listOfPosition, function (key, value) {
      param.list.push({position: key, name: value})
    });
    if (!flag) {
      let result = dice(param)
      $randomTeamBtn.html('랜덤');
      worker.terminate();
      $.each(result, function (index, item) {
        $modalTeamBody.find("[data-position='" + item.position.toLowerCase() + "'] input").val(item.name);
      })
      return;
    } else {
      $randomTeamBtn.html('스탑');
    }
    worker = new Worker('/static/js/custom/randomWorker.js');
    worker.postMessage({
      flag: flag, list: listOfPosition, speed: 10
      , newTeamFlag: ($modalTeamBody.data('team') === 'a' ? newTeamFlagA : newTeamFlagB)
    });
    worker.onmessage = function (evt) {
      const tempList = evt.data;
      $.each(tempList, function (index, item) {
        $modalTeamBody.find("[data-position='" + positionList[index] + "'] input").val(item.obj);
      })
    }
  });
}

function initLeaderRandomModal() {
  /* variable */
  $modalRandomLeader = $('#modalRandomLeader')
  $modalLeader = $modalRandomLeader.find(".modal-body.team")
  $modalLeaderClose = $modalRandomLeader.find('.btn-close')
  $randomLeaderBtn = $modalRandomLeader.find(".modal-footer [name='randomBtn']")

  /* event */
  $modalLeaderClose.on('click', function () {
    if (flag) {
      $randomLeaderBtn.trigger('click');
    }
  });

  $randomLeaderBtn.on('click', function () {
    if (!newLeaderFlag) {
      newLeaderFlag = true;
    }
    flag = !flag;
    if (!flag) {
      $randomLeaderBtn.html('랜덤');
      worker.terminate();
      return;
    } else {
      $randomLeaderBtn.html('스탑');
    }
    worker = new Worker('/static/js/custom/randomWorker.js');
    worker.postMessage({flag: flag, list: listOfSummoner, speed: 50, tierOn: $toggleTierPoint.getToggleVal()});
    worker.onmessage = function (evt) {
      const tempList = evt.data;
      $modalLeader.find("[data-team='A'] [name='summonerName']").val(tempList[0].obj.name).data('index', tempList[0].obj.index);
      $modalLeader.find("[data-team='A'] .tierPointGroup").val(tempList[0].obj.tierPoint);
      $modalLeader.find("[data-team='B'] [name='summonerName']").val(tempList[1].obj.name).data('index', tempList[1].obj.index);
      $modalLeader.find("[data-team='B'] .tierPointGroup").val(tempList[1].obj.tierPoint);
    }
  });
}

function applyRandomTeam() {
  const team = $modalTeamBody.data('team')
  if (team === 'a') {
    newTeamFlagA = false
  } else {
    newTeamFlagB = false
  }
  $.each($modalTeamBody.find(".input-group"), function (index, item) {
    const position = $(item).data('position');
    const name = $(item).find('input').val();
    $("#team [data-position='" + position + "'] [name='team_" + team + "']").val(name);
  });
  $("#modalRandomTeam .btn-close").trigger('click');
}

function applyLeader() {
  if (!newLeaderFlag) {
    alert("팀장을 정해주세요");
    return;
  }
  const teamAleader = $modalLeader.find("[data-team='A'] input[name='summonerName']");
  const teamBleader = $modalLeader.find("[data-team='B'] input[name='summonerName']");
  let gap = 0;
  if (teamBleader.data().index > teamAleader.data().index) {
    gap--;
  }

  summonerNames().eq(teamAleader.data().index).parent().remove();
  summonerNames().eq(teamBleader.data().index + gap).parent().remove();
  $("[data-position='top']").find("[name='team_a']").val(teamAleader.val())
  $("[data-position='top']").find("[name='team_b']").val(teamBleader.val())

  $modalLeaderClose.trigger('click');
  $("[name='select_leader']").hide();
  newLeaderFlag = false;

  if ($toggleTierPoint.getToggleVal()) {
    sumTierPoints('a');
    sumTierPoints('b');
  }
}

function dice(param) {
  let result = [];
  common_ajax.call('/api/custom/random-dice', 'POST', false, param, function (res) {
    if (res.code !== API_RESULT.SUCCESS) {
      return;
    }
    result = res.data;
  });
  return result;
}