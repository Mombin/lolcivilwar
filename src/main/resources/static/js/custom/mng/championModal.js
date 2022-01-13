const noDataMsg = '데이터가 없습니다 =_=', userSeqs = {};
let $mostChampionCard, $highRateChampionCard, $recentChampionCard, $staticBackdropLabelChampion;

$(document).on('click', '#summonerList [name="summonerRate"]' ,function (e) {
  let customUserName = $(this).parent().find('[name="summonerName"]').val();
  if (customUserName.trim() === "") {
    return;
  }
  let customUser = customUsers[customUserName];
  let customUserSeq;
  if (Object.keys(customUser).length !== 0) {
    customUserSeq = customUser.seq;
  }

  $groupSelector.find('option:selected').data('group')
  let position = $(this).data('position');
  const param = {
    position: positionManager.getFullName(position),
    customUserSeq: customUserSeq,
    groupSeasonSeq: currentGroup.defaultSeason.seasonSeq
  }
  common_ajax.call('/api/group/played-champion', 'GET', false, param, function (res) {
    if (res.code !== API_RESULT.SUCCESS) {
      toast.error(res.message);
      return;
    }
    championModal.show();
    setChampionModalHeader(customUserName, positionManager.getKoreanName(position))
    setChampionModalData(res.data);
  })
})

function initChampionModal() {
  $staticBackdropLabelChampion = $("#staticBackdropLabelChampion");
  $mostChampionCard = $("#mostChampionCard");
  $highRateChampionCard = $("#highRateChampionCard");
  $recentChampionCard = $("#recentChampionCard")
}

function setChampionModalHeader(customUserName, krPositionName) {
  $staticBackdropLabelChampion.html(`${customUserName}의 ${krPositionName} 챔피언 정보`)
}

function setChampionModalData(data) {
  setMostChampion(data.mostChampionList);
  setHighWinRateChampion(data.highWinRateChampion)
  setRecentlyPlayedChampion(data.recentlyPlayedChampion);
}

function setMostChampion(list) {
  let cardText = $mostChampionCard.find('.card-text');
  if (list.length === 0) {
    cardText.html(noDataMsg);
    return
  }
  cardText.empty()
  $.each(list, function (idx, obj) {
    let win = obj.win
    let lose = obj.total - obj.win;
    let rate = obj.rate.toFixed(1)
    cardText.append(
      $('<div>')
        .addClass('w120px')
        .css('text-align', 'center')
        .css('display', 'inline-block')
        .css('margin-right', '1px')
        .append($('<img>').addClass('h120px w120px').attr('src', obj.championImageUrl))
        .append($('<span>').css('margin-right', '1px').css('color', 'blue').html(`${win}W`))
        .append($('<span>').css('margin-right', '1px').css('color', 'red').html(`${lose}L`))
        .append(`${rate}%`)
    )
  })
}

function setHighWinRateChampion(list) {
  let cardText = $highRateChampionCard.find('.card-text');
  if (list.length === 0) {
    cardText.html(noDataMsg);
    return
  }
  cardText.empty()
  $.each(list, function (idx, obj) {
    let win = obj.win
    let lose = obj.total - obj.win;
    let rate = obj.rate.toFixed(1)
    cardText.append(
      $('<div>')
        .addClass('w120px')
        .css('text-align', 'center')
        .css('display', 'inline-block')
        .css('margin-right', '1px')
        .append($('<img>').addClass('h120px w120px').attr('src', obj.championImageUrl))
        .append($('<span>').css('margin-right', '1px').html(`${rate}%`))
        .append($('<span>').css('margin-right', '1px').css('color', 'blue').html(`${win}W`))
        .append($('<span>').css('color', 'red').html(`${lose}L`))
    )
  })
}

function setRecentlyPlayedChampion(list) {
  let cardText = $recentChampionCard.find('.card-text');
  if (list.length === 0) {
    cardText.html(noDataMsg);
    return
  }
  cardText.empty()
  $.each(list, function (idx, obj) {
    let matchResult = obj.matchResult;
    cardText.append(
      $('<div>')
        .addClass('w120px')
        .css('text-align', 'center')
        .css('display', 'inline-block')
        .css('margin-right', '1px')
        .append($('<img>').addClass('h120px w120px').attr('src', obj.championImageUrl))
        .append($('<span>').css('margin-right', '1px').css('color', matchResult ? 'blue' : 'red').html(matchResult ? '승' : '패'))
        .append($('<span>').html(obj.createdDate.substring(0,10)))
    )
  })
}