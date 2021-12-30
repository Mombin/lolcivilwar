let $openRandomLeaderModal;
function initTeamTable() {
  /* variable */
  $openRandomLeaderModal = $("[name='select_leader']");

  /* event */
  $("[name='group_list_apply']").on('click', function() {
    $("[name='new_team']").trigger('click');
    var customGroupList = [];
    $.each($(".form-check-input.me-1:checked"), function (index, item) {
      customGroupList.push($(item).data('name'));
    });
    $.each($(".input-group.summoner-list"), function (index, item) {
      $(item).find("[name='summonerName']").val(customGroupList[index]);
      $(item).find("[name='summonerName']").trigger('change');
    })
    $("[name='group_list_check']").show();
    $filterText.hide().val('');
    $pickCount.hide();
    changePickCount(0);
    $("#groupUserList .list-group").hide();
    $("[name='group_list_apply']").hide();
    $groupSelector.hide();
  });

  $('[name="winner_btn"]').on('click', function() {
    let winnerTeam = $(this).data('team');

    if(!confirm("정말 " + winnerTeam.toUpperCase() + "이 이겼습니까?")) {
      return;
    }

    let canCall = true;
    const matchResult = [];
    $.each($("#team .team-position .form-control"), function(index, item) {
      const $item = $(item);
      const targetTeam = $item.data('team');
      let champion = null;
      if ($toggleGameData.getToggleVal()) {
        const targetImage = $item.parent().find(`img[data-team="${targetTeam}"]`)
        let data = targetImage.data('summoner') || {};
        if (Object.keys(data).length !== 0) {
          champion = data;
        }
      }

      const param = {
        user: $item.val(),
        team: targetTeam.toUpperCase(),
        result: targetTeam === winnerTeam,
        position: $item.parent().data('position').toUpperCase(),
        champion: champion
      }
      matchResult.push(param);

      if (!$item.val()) {
        canCall = false;
      }
    });

    let bannedChampions = null;
    if ($toggleGameData.getToggleVal()) {
      bannedChampions = {a: [], b: []}
      $.each($("#banChamps img"), function (idx, obj) {
        let $imgData = $(obj).data();
        let $summonerData = $imgData.summoner || {};
        if (Object.keys($summonerData).length === 0) {
          return;
        }
        $summonerData.pickTurn = $imgData.banOrder;
        let $currentTeam = $imgData.team;
        bannedChampions[$currentTeam].push($summonerData)
      })
    }

    if (!canCall) {
      alert("빈값이 존재합니다.");
      return;
    }

    const param = {
      groupSeq: currentGroupSeq,
      matchResult: matchResult,
      seasonSeq: $("#seasonName").data('seasonSeq'),
      bannedChampions: bannedChampions
    }

    common_ajax.call("/api/custom/match", "PUT", true, param, function (res) {
      if(res.code === API_RESULT.FAIL) {
        alert(res.message);
        return;
      }
      alert("등록에 성공하였습니다.")
      if ($toggleGameData.getToggleVal()) {
        resetImages();
        runIngameApiWorker();
      }
    });
  });

  $openRandomLeaderModal.on('click', function () {
    if (!isValidationList()) {
      alert('이름을 전부 입력해주세요.');
      return;
    }
    $.each(summonerNames(), function (index, item) {
      listOfSummoner[$(item).data('index')] = {
        name: $(item).val(),
        index: index,
        tierPoint: Number($(item).parent().find('.tierPointGroup').val())
      };
    });
    $modalLeader.find("[data-team='A'] input").val('');
    $modalLeader.find("[data-team='B'] input").val('');
    leaderModal.show();
  });

  $("[name='random_team']").on('click', function () {
    const team = $(this).data('team');
    if (!isValidationName(team)) {
      alert("해당 팀에 소환사 명을 적어주세요.");
      return false;
    }
    $modalTeamBody.data('team', team);
    $.each($("[name='team_" + team + "']"), function () {
      const position = $(this).parent().data('position');
      $modalTeamBody.find("[data-position='" + position + "'] input").val($(this).val());
      listOfPosition[position] = $(this).val();
    });
    teamModal.show();
  });

  $("[name='new_team']").on('click', function () {
    newTeamFlagA = true;
    newTeamFlagB = true;
    $openRandomLeaderModal.show();
    $(".summoner-list").remove();
    $("[name='team_a']").val('');
    $("[name='team_b']").val('');
    const $summonerList = $("#summonerList");
    for (let i = 0; i < 10; i++) {
      const input = $("<input>").attr('type', 'text').attr('placeholder', '이름')
        .css('width', '30%')
        .attr('name', 'summonerName')
        .data('index', i).addClass('form-control');
      const btnA = $("<button>").addClass('btn btn-outline-primary')
        .attr('type', 'button').attr('style', 'width: 15%')
        .on('click', moveTeam).data('target', 'a').html('A팀');
      const btnB = $("<button>").addClass('btn btn-outline-success')
        .attr('type', 'button').attr('style', 'width: 15%')
        .on('click', moveTeam).data('target', 'b')
        .html('B팀');
      const $summoner = $('<div>').addClass('input-group summoner-list').attr('style', 'margin-top: 5px;')
      $summoner.append(input);
      if (Object.keys(matchRate).length > 0) {
        input.on('change', function () {
          let targetRate = makeRate(matchRate[$(this).val()]);
          $(this).next().val(targetRate);
        });
        $summoner.append($("<input>").attr('type', 'text').attr('readonly', true)
          .css('text-align', 'center')
          .css('width', '30%')
          .attr('name', 'summonerRate').addClass('form-control')).val();
      }
      if (Object.keys(tierPoints).length > 0) {
        input.on('change', function () {
          let tierPoint = tierPoints[$(this).val()] || 0;
          $(this).next().next().val(tierPoint);
        });
      }
      $summoner.append($("<input>").addClass('tierPointGroup').attr('type', 'text').attr('readonly', true).css('display', $toggleTierPoint.getToggleVal() ? 'block' : 'none').css('text-align', 'center').css('width', '5%').addClass('form-control'))
      $summoner.append(btnA).append(btnB);
      $summonerList.append($summoner);
    }
  });

  $("#team .team-position input").on('change',function () {
    if($toggleTierPoint.getToggleVal()) {
      sumTierPoints($(this).data('team'));
    }
    if($toggleGameData.getToggleVal()) {
      runIngameApiWorker()
    }
  });

  $("[name='group_list_check']").on('click', function () {
    matchRate = {};
    callMyGroup('/api/group/my-match', groupChangeFunction)
  });
}

/* function */
function isValidationList() {
  let result = true;
  $.each($("#summonerList [name='summonerName']"), function(index, item) {
    const value = $(item).val();
    if (!value || value.trim() === "") {
      result = false;
    }
  });
  return result;
}

function isValidationName(team) {
  let result = true;
  $.each($("[name='team_" + team + "']"), function () {
    const name =  $(this).val();
    $(this).val(name.trim());
    if (name.trim() === "") {
      result = false;
    }
  });
  return result;
}
