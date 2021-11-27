let $customUserSelect;

function changeCustomUser() {
    const param = {
        groupSeq: currentGroup.groupSeq,
        customUserSeq: $(this).val(),
        seasonSeq: $seasonSelector.val()
    }
    common_ajax.call('/api/group/v1/synergy', 'GET', true, param, function (res) {
        const data = res.data;
        const synergy = data.synergy;
        const badSynergy = data.badSynergy;
        sort(synergy);
        sort(badSynergy);
        tableSet($("#synergy"), synergy);
        tableSet($("#badSynergy"), badSynergy)
    });
}

function sort(list) {
    list.sort(function(a, b) {
        let check = b.rate - a.rate;
        if (check !== 0) {
            return check;
        }
        return b.total - a.total;
    });
    return list;
}

function tableSet($id, list) {
    $id.empty();
    $.each(list, function(index, item) {
        let $tr = $('<tr>')
            .append($('<th>').attr('scope', 'row').html(index+1))
            .append($('<td>').html(item.nickname))
            .append($('<td>').html(item.summonerName))
            .append($('<td>').html(item.total))
            .append($('<td>').html(item.win))
            .append($('<td>').html(item.rate))
            .append($('<td>').html(item.lastDateString));
        $id.append($tr);
    });
}

// 그룹 변경시 데이터 세팅
function changeGroupSelect() {
    currentGroup = $(this).find('option:selected').data('group');
    setSeasons();
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

function setSeasons() {
    $seasonSelector.empty();
    $.each(currentGroup.seasons, function (idx, season) {
        let option = $('<option>').val(season.seasonSeq).html(season.seasonName);
        if (season.seasonSeq === currentGroup.defaultSeason.seasonSeq) {
            option.attr('selected', true);
        }
        $seasonSelector.append(option);
    });
    $seasonSelector.trigger('change')
}

function changeSeasonSelect() {
    $customUserSelect.empty();
    $customUserSelect.append($('<option>').val(0).attr('selected', true).attr('disabled', true).html('닉네임을 선택해주세요'));
    $("#synergy").empty();
    $("#badSynergy").empty();
    if (currentGroup.customUser[$seasonSelector.val()] === undefined) {
        currentGroup.customUser[$seasonSelector.val()] = [];
        getMatchAttendees($seasonSelector.val())
    }
    $.each(currentGroup.customUser[$seasonSelector.val()], function(index, item) {
        $customUserSelect.append($('<option>').val(item.seq).html(`${item.nickname}[${item.summonerName}]`))
    });
}