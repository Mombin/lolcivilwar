let myGroup
    , $list
    , currentPage = 0;

function init() {
    callMyGroup('/api/group/my', groupChangeFunction);
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

function getList(page) {
    if (!myGroup) {
        toast.warning("소속된 그룹이 없습니다.")
    }
    common_ajax.call(`/api/group/match/${myGroup.groupSeq}/${page}`, 'POST', true, {}, function (res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error("관리자에게 문의부탁드립니다.")
            return;
        }

        let result = res.data.list;
        $list.empty();
        if (GROUP_AUTH.isManageable(myGroup.auth)) {
            $('.deleteCol').show();
        } else {
            $('.deleteCol').hide();
        }
        $.each(result, function (index, item) {

            let $tr = $('<tr>')
                .append($('<td>').html(item.matchNumber))
                .append($('<td>').html(item.date))
                .append($('<td>').html(`${item.winner}팀`))
                .append($('<td>').addClass(item.winner === 'A' ? 'table-primary' : '').html(item.alist.join(', ')))
                .append($('<td>').addClass(item.winner === 'B' ? 'table-danger' : '').html(item.blist.join(', ')))
            if (GROUP_AUTH.isManageable(myGroup.auth)) {
                $tr.append($('<td>').html($('<button>')
                    .html('삭제')
                    .addClass('btn btn-danger btn-sm')
                    .data('match_seq', item.matchSeq)
                    .on('click', deleteMatchResult)))
            }
            $list.append($tr);
        });
        currentPage = page;
        common_page.createPage($("#pagination"), currentPage, res.data.totalPage, getPage);
        if (result.length === 0 && currentPage !== 0) {
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

function deleteMatchResult() {
    if (!myGroup) {
        toast.warning("소속되 그룹이 없습니다.")
    }
    if (!confirm("정말 삭제하시겠습니까?")) {
        return;
    }

    const matchSeq = $(this).data('match_seq');

    common_ajax.call(`/api/group/match/${matchSeq}`, 'DELETE', true, {}, function (res) {
        if (res.code === API_RESULT.SUCCESS) {
            toast.success("삭제하였습니다.")
            getList(currentPage);
        } else {
            toast.error("삭제에 실패하였습니다.")
        }
    })
}