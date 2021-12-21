const positionList = ['top', 'jg', 'mid', 'bot', 'sup']
  , listOfPosition = {}
  , listOfSummoner = {};
let worker
  , teamModal
  , leaderModal
  , flag = false
  , newTeamFlagA = true
  , newTeamFlagB = true
  , newLeaderFlag = false
  , currentGroupSeq = -1;
$(document).ready(function () {
  setItems();
  setEvents();

  initGroupList();
  initTeamTable();
  initTeamRandomModal();
  initLeaderRandomModal();
  initTierPointPlugin();
  initGameDataPlugin();
});

function setItems() {
  teamModal =  new bootstrap.Modal(document.getElementById('modalRandomTeam'), {
    keyboard: false
  });
  leaderModal = new bootstrap.Modal(document.getElementById('modalRandomLeader'), {
    keyboard: false
  });
  $groupSelector = $("#groupSelector");
}

function setEvents() {
  $("#modalRandomTeam .modal-footer [name='applyBtn']").on('click', applyRandomTeam);
  $("#modalRandomTeam .btn-close").on('click', function () {
    if (flag) {
      $randomTeamBtn.trigger('click');
    }
  })
  $("#modalRandomLeader .modal-footer [name='applyBtn']").on('click', applyLeader);

  // 그룹 선택시 이벤트
  $groupSelector.on('change', function () {
    let item = $(this).find('option:selected').data('group')
    setMyGroup(item)
    $("[name='group_list_check']").hide();
    $filterText.show().find('.form-control').val('');
    $pickCount.show();
    changePickCount(0);
    $("#groupUserList .list-group").show();
    $("[name='group_list_apply']").show();
    $(".input-group.summoner-list").remove();
    currentGroupSeq = item.groupSeq
  });
}

/* 목록에 있는 리스트 뿌려줌 */
function setMyGroup(data) {
  const $groupUserList = $("#groupUserList .list-group");
  $groupUserList.empty();
  data.customUser.forEach(function (item, index) {
    const text = item.nickname + "[" + item.summonerName + "]";
    let rate = item.total === 0? 0 : Math.round((item.win / (item.total)) * 100);
    const $span = $('<span>').html(`${item.total}전/${item.win}승/${rate}%`).css('float', 'right');
    $groupUserList.append($('<label>').addClass("list-group-item").attr('name', 'group-summoner')
      .append($("<input>").addClass("form-check-input me-1").prop('type', 'checkbox').data('name', text).on('click', fnCheck))
      .append(text)
      .append($span));
    matchRate[text] = item.positionWinRate;
    tierPoints[text] = item.tierPoint
  });

  $('#seasonName').val(data.defaultSeason.seasonName).data('seasonSeq', data.defaultSeason.seasonSeq)
}

function groupChangeFunction(groupList) {
  for (let i = 0; i < groupList.length; i++) {
    if (groupList[i].auth === GROUP_AUTH.OWNER) {
      $groupSelector.val(groupList[i].groupSeq)
      $groupSelector.trigger('change');
      return;
    }
  }
}