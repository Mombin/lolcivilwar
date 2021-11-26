let matchRate = {}, tierPoints = {};

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

function summonerNames() {
    return $("#summonerList [name='summonerName']");
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

function groupChangeFunction(groupList) {
    for (let i = 0; i < groupList.length; i++) {
        if (groupList[i].auth === GROUP_AUTH.OWNER) {
            $groupSelector.val(groupList[i].groupSeq)
            $groupSelector.trigger('change');
            return;
        }
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

function sumTierPoints(team) {
    if (team === undefined) {
        return;
    }
    let sum = 0;
    $.each($(`#team .team-position input[name="team_${team}"]`),function (idx, obj) {
        if ($(obj).val().trim() !== '') {
            let tierPoint = tierPoints[$(obj).val().trim()] || 0;
            sum += tierPoint;
        }
    });
    $(`#team [name="tearPointSum"][data-team="${team}"]`).val(sum);
}