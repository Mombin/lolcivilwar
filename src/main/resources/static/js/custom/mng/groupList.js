let matchRate = {}
  , $pickCount
  , $filterText;

function initGroupList() {
  /* variable */
  $pickCount = $('[name="pick-count"]')
  $filterText = $('[name="filter_text"]')

  /* event */
  $filterText.find('.form-control').on('keyup', function () {
    let filteringText = $(this).val()
      , groupSummoners = $('[name="group-summoner"]')
    if (filteringText === "") {
      groupSummoners.show();
      return;
    }
    $.each(groupSummoners, function (index, item) {
      let text = $(item).clone().children().remove().end().text();
      if (text.toUpperCase().indexOf(filteringText.toUpperCase()) === -1) {
        $(item).hide();
      } else {
        $(item).show()
      }
    })
  });
}

/* 10명 초과 체크시 해제 */
function fnCheck() {
  let checkedCount = $(".form-check-input.me-1:checked").length;
  if (checkedCount > 10) {
    $(this).prop('checked', false);
    alert("10명 초과하여 선택할수 없습니다.");
    return;
  }
  changePickCount(checkedCount);
}

/* 포지션별 승률 */
function makeRate(rateObj) {
  if (!rateObj) {
    return "";
  }
  const positionListSmall = ['T', 'J', 'M', 'B', 'S'];
  let result = "";
  $.each(positionList, function(index, item) {
    let positionResult = rateObj[item.toUpperCase()]
    if (!positionResult) {
      positionResult = { first:1, second: 0 };
    }
    result += `${positionListSmall[index]}:${Math.round((positionResult.second/positionResult.first) * 100)}% / `
  });
  return result.slice(0, result.length - 2).trim();
}

// pickCount의 숫자변경
function changePickCount(count) {
  if (!isNaN(Number(count))) {
    $pickCount.html(`선택된 유저 : ${count}`)
  }
}

function moveTeam() {
  const positionName = ['top', 'jg', 'mid', 'bot', 'sup'];
  const target = $(this).data('target');
  let flag = false;
  for (let i = 0; i < positionName.length; i++) {
    const $position = $('[data-position="' + positionName[i] + '"]').find('[name="team_'+target+'"]')
    if ($position.val().trim() === "") {
      $position.val($(this).parent().find('input').val());
      flag = true;
      break;
    }
  }
  if (flag) {
    $(this).parent().remove();
  }

  if ($toggleTierPoint.getToggleVal()) {
    sumTierPoints(target)
  }
  if($toggleGameData.getToggleVal()) {
    runIngameApiWorker()
  }
}

function summonerNames() {
  return $("#summonerList [name='summonerName']");
}