let $groupAuthList;
let $groupAutheListHeader;
let currentGroup = {}
let $inviteHistoryTable;
let $manageAuthTable;

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
    });
}

function callGroupInviteList(){
    $inviteHistoryTable.show();
    $(".table").not($inviteHistoryTable).hide();
}