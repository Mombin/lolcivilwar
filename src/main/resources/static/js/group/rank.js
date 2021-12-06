function setList(list) {
    const $list = $("#list")
    $list.empty();

    let number = 1;
    $.each(list, function (index, item) {
        let rank;
        if (item.total === 0) {
            return;
        } else if (item.rate > 55) {
            rank = 'table-danger';
        } else if (item.rate < 45) {
            rank = 'table-secondary';
        }
        let $tr = $('<tr>')
            .addClass(rank)
            .append($('<th>').attr('scope', 'row').html(number))
            .append($('<td>').html(item.nickname))
            .append($('<td>').html(item.summonerName))
            .append($('<td>').html(item.total))
            .append($('<td>').html(item.win))
            .append($('<td>').html(item.rate))
            .append($('<td>').html(item.lastDate));

        $list.append($tr);
        number++;
    });
}

// 그룹 선택 이벤트
function changeGroupSelect() {
    currentGroup = $(this).find('option:selected').data('group');
    setSeasons();
    $('#position .nav-link.active').trigger('click');
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

function callRankData() {
    const param = {
        groupSeq: currentGroup.groupSeq,
        seasonSeq: $seasonSelector.val()
    }
    common_ajax.call('/api/group/v1/rank', 'POST', false, param, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message)
            return;
        }
        $.each(res.data, function (idx, obj) {
            currentGroup.customUser[$seasonSelector.val()].push(obj);
        })
    });
}