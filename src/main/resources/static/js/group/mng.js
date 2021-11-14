let currentGroup
    , selectedUser = {}
    , openerData = {};
function init() {
    callMyGroup('/api/group/my', groupChangeFunction);
    console.log("최신화됨?");
}

/* 그룹에 추가할 소환사명 */
function isValidationInput() {
    if(!currentGroup) {
        alert("생성한 그룹이 없습니다.");
        return false;
    }

    const nickname = $("#nickname").val();
    if(!nickname || nickname.trim() === "") {
        alert("홈페이지에서 구분할 닉네임을 채워주세요");
        return false;
    }

    const summonerId = $("#summonerId").val();
    if(!summonerId || summonerId.trim() === "") {
        alert("소환사명을 입력해주세요")
        return false;
    }
    return true;
}

/* 내 그룹원 세팅 */
function setList() {
    if(!currentGroup) {
        return
    }

    const container = $("#myGroupList .list-group");
    container.empty();
    currentGroup.customUser.forEach(function (item, index) {
        const $span = $('<span>').html(`${item.total}전/${item.win}승/${Math.round((item.win / (item.total === 0 ? 1 : item.total)) * 100)}%`).css('float', 'right');
        let $li = $("<li>").addClass("list-group-item");
        if (GROUP_AUTH.isManageable(currentGroup.auth)) {
            $li.append($("<input>").addClass("form-check-input me-1").prop("type", "checkbox").data('seq', item.seq))
        }
        $li.append($("<span>").html(item.nickname + "["+ item.summonerName +"]").on('click', setModifyTarget).data(item).addClass('hover-underline')).append($span)

        if (item.accountId === "") {
            $span.append('  ')
                .append(
                    $("<button>").on('click', openLinkPopup).addClass('btn btn-success btn-sm').html("인증하기")
                        .data('seq', item.seq)
                        .data('summonerName', item.summonerName)
                        .data('groupSeq', item.groupSeq)
                )
        } else {
            $span.append('  ')
                .append($('<img>').attr('src', riotData.profileIcon(item.profileIconId)).css('width', '35px'))
                .append(`   LEVEL : ${item.summonerLevel}`)
        }

        container.append($li);
    });
}

/* 해당 사항을 수정하기 위해 클릭 */
function setModifyTarget() {
    const item = $(this).data();
    if (!item) {
        return;
    }
    selectedUser = item;
    $("#nickname").val(item.nickname);
    $("#summonerId").val(item.summonerName);
    $(".modify-group").show();
    $(".new-group").hide();
}

function saveGroup() {
    const groupName = $("#groupName").val();
    console.log(groupName);
    if(groupName.trim() === "") {
        alert("그룹 이름을 지정해주세요");
        return;
    }
    const param = {
        groupName: groupName.trim()
    }
    let result = true;
    common_ajax.call("/api/group", 'POST', false, param, function(res) {
        if(res.code === API_RESULT.FAIL) {
            if (res.message) {
                alert(res.message);
                return;
            }
            alert("그룹 생성에 실패하였습니다. 자세한 사항은 관리자에게 문의바랍니다.")
        } else {
            callMyGroup('/api/group/my', groupChangeFunction);
        }
    });
}

// 그룹 선택
function changeGroupSelect() {
    currentGroup = $(this).find('option:selected').data('group');
    if (GROUP_AUTH.isManageable(currentGroup.auth)) {
        $("#addGroupForm").show();
        $("#deleteUser").show();
    } else {
        $("#addGroupForm").hide();
        $("#deleteUser").hide();
    }
    setList();
}

// 그룹 유저 추가
function fnAddUser() {
    if (!isValidationInput()) {
        return;
    }
    const param = {
        groupSeq: currentGroup.groupSeq,
        nickname: $("#nickname").val().trim(),
        summonerId: $("#summonerId").val().trim()
    }
    common_ajax.call('/api/group/user', 'POST', false, param, function (res) {
        if (res.code === API_RESULT.FAIL) {
            alert(res.message);
            return;
        }
        toast.success("추가되었습니다")
        $("#nickname").val("");
        $("#summonerId").val("");
        callMyGroup('/api/group/my', groupChangeFunction, currentGroup.groupSeq);
    });
}


function groupChangeFunction(groupList, groupSeq) {
    let myGroupFlag = false;

    for (let i = 0; i < groupList.length; i++) {
        if (groupList[i].auth === GROUP_AUTH.OWNER) {
            myGroupFlag = true;
            if (groupSeq) {
                $groupSelector.val(groupSeq);
            } else {
                $groupSelector.val(groupList[i].groupSeq)
            }
            $groupSelector.trigger('change');
            return;
        }
    }

    console.log(myGroupFlag);
    if (!myGroupFlag) {
        $('#myGroup').html("그룹을 생성할수 있습니다.");
        $("#addGroupForm").hide();
        $("#createGroup").show();
        $("#deleteUser").hide();
    }

    if (groupSeq) {
        $groupSelector.val(groupSeq);
        $groupSelector.trigger('change');
    }
}

function openLinkPopup() {
    let popup = popupOpen('/popup/link/summoner', 400, 550);
    let targetData = $(this).data();
    let form = $("#openerData");
    form.empty();
    $.each(targetData, function (key, value) {
       form.append($('<input>').attr('type', 'text').attr('name', key).val(value))
    });
}

function refresh() {
    callMyGroup('/api/group/my', groupChangeFunction, currentGroup.groupSeq)
}