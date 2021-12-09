let $groupAuthList;
let $groupAutheListHeader;
let $inviteHistoryTable;
let $manageAuthTable;
let $inviteHistory;
let userSeq;
let userId;
let lolcwTag;

// 그룹 선택 이벤트
function changeGroupSelect() {
    currentGroup = $(this).find('option:selected').data('group');
    $('#position .nav-link.active').trigger('click');
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

// 그룹 권한 리스트
function callGroupAuthList() {
    $manageAuthTable.show();
    $('#inviteForm').hide();
    $('#modifyForm').show();
    $('.table').not($manageAuthTable).hide();
    let authCall = $manageAuthTable.isAuthCall || {};
    if (authCall[currentGroup.groupSeq]) {
        return;
    }
    common_ajax.call(`/api/group/v1/${currentGroup.groupSeq}/user`, 'GET', false, {}, function(res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.error);
            return;
        }
        $.each(res.data, function(index, item) {
            let $select = $('<select>').addClass('form-select')

            $.each(GROUP_AUTH.getAuthList(), function (key, value) {
                if (item.groupAuth === GROUP_AUTH.MANAGER) {
                    if (key === GROUP_AUTH.OWNER) {
                        return;
                    }
                }
                $select.append($('<option>').val(key).html(value));
            });
            $groupAuthList.append(
                $('<tr>').append($('<th>').attr('scope', 'row').html(index + 1))
                    .append($('<td>').html(item.userId))
                    .append($('<td>').append($select))
            )
            $select.val(item.groupAuth);
            if (currentGroup.groupAuth === GROUP_AUTH.MANAGER) {
                if (item.groupAuth === GROUP_AUTH.OWNER) {
                    $select.attr('disabled', true);
                }
            }
            if (item.userId === session.name()) {
                $select.attr('disabled', true);
            }
        });
        authCall[currentGroup.groupSeq] = true;
        $manageAuthTable.isAuthCall = authCall;
    });
}

function callGroupInviteList(page){
    $inviteHistoryTable.show();
    $(".table").not($inviteHistoryTable).hide();
    $('#modifyForm').hide();
    $('#inviteForm').show();
    const param = {
        groupSeq: currentGroup.groupSeq,
        page: page
    }
    common_ajax.call(`/api/group/v1/invite-user`, 'GET', false, param, function(res) {
        console.log(res.data.list);
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.error);
            return;
        }

        $inviteHistory.empty();
        $.each(res.data.list, function (index, item) {
            let row = $('<tr>').append($('<th>').attr('scope', 'row').html(index + 1))
                .append($('<td>').html(item.invitedUserId))
                .append($('<td>').html(item.invitedDate))
                .append($('<td>').html(item.modifiedDate));
            if (item.expireResult === "N") {
                row.append($('<td>').append("<input type='button' class='btn btn-primary' name='cancelInvite' onclick='cancelInvite()' value='초대취소'/>"))
            }
            $inviteHistory.append(row)
        });
    });
}

function groupInvite(){
    const param = {
        groupSeq: currentGroup.groupSeq,
        userSeq: userSeq,
        lolcwTag: lolcwTag
    }
    common_ajax.call(`/api/group/v1/invite-user`, 'POST', false, param, function(res) {
        if (res.code === API_RESULT.SUCCESS) {
            toast.success("초대가 완료되었습니다");
            return;
        }
            toast.success(res);
            toast.error();
    });
}

function getUserByLolTag(){
        lolcwTag = $('input[name=lolcwTag]').val();
        common_ajax.call(`/api/user/by-tag/${lolcwTag}`, 'GET', false, {}, function(res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.error);
            return;
        }

        userSeq = res.data.userSeq;
        userId  = res.data.userId;
        $('input[name=userId]').val(res.data.userId);
        $('input[name=userSeq]').val(res.data.userSeq);
        $('input[name=inviteUser]').attr('disabled',false);
        $('input[name=searchUser]').attr('disabled',true);
    });
}

function cancelInvite(){

}