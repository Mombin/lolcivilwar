let $groupAuthList, $inviteHistoryTable, $manageAuthTable, $inviteHistory, $switchGroup ,$lolcwTag;
let userSeq, userId, lolcwTag, inviteSeq, currentPage = 0;
const INVITE = {
    APPROVE:"초대승인",
    REJECT: "초대거절",
    EXPIRED: "만료",
    NOT_REPLY: "미응답"
}

// 그룹 선택 이벤트
function changeGroupSelect() {
    currentGroup = $(this).find('option:selected').data('group');
    $('#menuTab .nav-link.active').trigger('click');
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
    if (!checkGroupSeq()) {
        return;
    }

    let authCall = $manageAuthTable.isAuthCall || {};
    if (authCall[currentGroup.groupSeq]) {
        return;
    }
    common_ajax.call(`/api/group/v1/${currentGroup.groupSeq}/user`, 'GET', false, {}, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message);
            return;
        }
        $.each(res.data, function (index, item) {
            let $select = $('<select>').addClass('form-select')

            $.each(GROUP_AUTH.getAuthList(), function (key, value) {
                $select.append($('<option>').val(key).html(value));
            });
            $groupAuthList.append(
                $('<tr>').append($('<th>').attr('scope', 'row').html(index + 1))
                    .append($('<td>').data('userSeq', item.userSeq).html(item.userId))
                    .append($('<td>').data('currentAuth', item.groupAuth).append($select))
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

function callGroupInviteList(page) {
    if (!checkGroupSeq()) {
        return;
    }
    const param = {
        groupSeq: currentGroup.groupSeq,
        page: page
    }
    common_ajax.call(`/api/group/v1/invite-user`, 'GET', false, param, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message);
            return;
        }

        $inviteHistory.empty();
        $.each(res.data.list, function (index, item) {
            let Seq = item.inviteSeq;
            let inviteResult = item.inviteResult;

            if (inviteResult === "Y") {
                inviteResult = INVITE.APPROVE;
            } else if (inviteResult === 'N') {
                inviteResult = INVITE.REJECT;
            } else if (inviteResult === '') {
                inviteResult =  INVITE.NOT_REPLY;
                if (item.expireResult ==="Y") {
                    inviteResult = INVITE.EXPIRED;
                }
            }

            let $cancelBtnTd = $('<td>');
            if (item.expireResult === "N") {
                $cancelBtnTd.append(`<input type="button" class="btn btn-primary" name="cancelInvite" onClick="cancelInvite(${Seq})" value="초대취소"/>`)
            }

            let row = $('<tr>').css('height','55px').append($('<th>').attr('scope', 'row').html(item.invitedUserId))
                .append($('<td>').addClass('ta-c').html(inviteResult))
                .append($('<td>').addClass('ta-c').html(item.invitedDate))
                .append($('<td>').addClass('ta-c').html(item.inviteResult === "" ? "" : item.modifiedDate))
                .append($cancelBtnTd);

            $inviteHistory.append(row)
        });
        currentPage = page;
        common_page.createPage($("#pagination"), currentPage, res.data.totalPage, getPage);
    });
}

function getPage() {
    let page = $(this).data('page');
    if (page === 'next') {
        callGroupInviteList(currentPage + 1);
    } else if (page === 'prev') {
        callGroupInviteList(currentPage - 1);
    } else if (currentPage !== page - 1) {
        callGroupInviteList(page - 1);
    }
}

function groupInvite() {
    if (!checkGroupSeq()) {
        return;
    }
    const param = {
        groupSeq: currentGroup.groupSeq,
        userSeq: userSeq,
        lolcwTag: lolcwTag
    }
    common_ajax.call(`/api/group/v1/invite-user`, 'POST', false, param, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message);
            return;
        }
        toast.success("초대가 완료되었습니다");
        resetInviteForm();
        $('input[name=lolcwTag]').val();
        $inviteHistory.empty();
        callGroupInviteList(0);
    });
}

function resetInviteForm(){
    $('input[name=userId]').val('');
    $('input[name=userSeq]').val('');
    $('input[name=inviteUser]').attr('disabled', true);
    $('input[name=searchUser]').attr('disabled', false);
}

function getUserByLolTag() {
    lolcwTag = $('input[name=lolcwTag]').val();
    common_ajax.call(`/api/user/by-tag/${lolcwTag}`, 'GET', false, {}, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message);
            return;
        }

        userSeq = res.data.userSeq;
        userId = res.data.userId;
        $('input[name=userId]').val(res.data.userId);
        $('input[name=userSeq]').val(res.data.userSeq);
        $('input[name=inviteUser]').attr('disabled', false);
        $('input[name=searchUser]').attr('disabled', true);
    });
}

function cancelInvite(inviteSeq) {
    common_ajax.call(`/api/group/v1/invite-result/${inviteSeq}`, 'DELETE', false, {}, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error(res.message);
            return;
        }

        toast.success("초대가 취소되었습니다.");
        $inviteHistory.empty();
        callGroupInviteList(0);
    });
}