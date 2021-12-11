let $groupSelector, $seasonSelector, currentGroup = {
    groupSeq: 0
}

function callMyGroup(url, changeFn, changeFnParam) {
    $groupSelector = $("#groupSelector");
    let groupList = [];
    common_ajax.call(url, 'POST', false, {}, function (res) {
        groupList = res.data;
        $groupSelector.empty();
        $groupSelector.append($('<option>').val(0).attr('selected', true).attr('disabled', true).html('그룹을 선택해주세요'));
        $groupSelector.show();
        if (groupList.length === 0) {
            toast.warning("그룹에 속해 있지 않습니다.")
            return;
        }
        groupList.forEach((item, index) => {
            $groupSelector.append($('<option>').val(item.groupSeq).html(item.groupName).data('group', item));
        });

    });
    if (typeof changeFn === 'function') {
        changeFn(groupList, changeFnParam);
    }
}

// -1 일경우 전체시즌
function getMatchAttendees(seasonSeq) {
    const param = {
        groupSeq: currentGroup.groupSeq,
        seasonSeq: seasonSeq
    }
    common_ajax.call('/api/group/v1/match_attendees', 'POST', false, param, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message)
            return;
        }
        $.each(res.data, function (idx, obj) {
            if (seasonSeq === -1) {
                currentGroup.customUser.push(obj);
            } else {
                currentGroup.customUser[seasonSeq].push(obj);
            }
        })
    });
}

function checkGroupSeq() {
    if (currentGroup.groupSeq === 0) {
        toast.error("선택한 그룹이 없습니다.")
        return false;
    }
    return true;
}