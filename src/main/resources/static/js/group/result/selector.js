let currentUserSeq, currentPage = 0, $list, $customUserSelect;

function groupChangeFunction(groupList) {
    for (let i = 0; i < groupList.length; i++) {
        if (groupList[i].auth === GROUP_AUTH.OWNER) {
            $groupSelector.val(groupList[i].groupSeq)
            $groupSelector.trigger('change');
            return;
        }
    }
}

// 그룹 변경시 데이터 세팅
function changeGroupSelect() {
    currentUserSeq = -1;
    currentGroup = $(this).find('option:selected').data('group');
    if (currentGroup.customUser === undefined || currentGroup.customUser.length === 0) {
        getMatchAttendees(-1)
    }
    $customUserSelect.empty();
    $customUserSelect.append($('<option>').val(0).attr('selected', true).attr('disabled', true).html('닉네임을 선택해주세요'));
    $("#synergy").empty();
    $("#badSynergy").empty();
    $.each(currentGroup.customUser, function(index, item) {
        $customUserSelect.append($('<option>').val(item.seq).html(`${item.nickname}[${item.summonerName}]`))
    });
}

function changeCustomUser() {
    currentUserSeq = $(this).val();
    getList(0);
}

function getList(page) {
    currentPage = page;
    const param = {
        groupSeq: currentGroup.groupSeq,
        customUserSeq: currentUserSeq,
        page: page
    }
    common_ajax.call('/api/group/v1/personal/result', 'GET', true, param, function (res) {
        const data = res.data;
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error("관리자에게 문의부탁드립니다.")
            return;
        }

        $list.empty();
        $.each(data.list, function (index, item) {

            let $tr = $('<tr>')
                .append($('<td>').css('text-align', 'center').html(item.date))
                .append($('<td>').css('text-align', 'center').html(item.seasonName))
                .append($('<td>').css('text-align', 'center').html(POSITION[item.position]))
                .append($('<td>').css('text-align', 'center').html(item.matchUser))
                .append($('<td>').css('text-align', 'center').addClass(item.winYn === 'Y' ? 'table-primary' : 'table-danger').html(item.winYn === 'Y' ? '승' : '패'))
            $list.append($tr);
        });
        common_page.createPage($("#pagination"), currentPage, data.totalPage, getPage);
        if (data.length === 0 && currentPage !== 0) {
            getList(currentPage - 1);
        }
    });
}

function getPage() {
    let page = $(this).data('page');
    if (page === 'next') {
        getList(currentPage + 1);
    } else if (page === 'prev') {
        getList(currentPage - 1);
    } else if (currentPage !== page - 1) {
        getList(page - 1);
    }
}