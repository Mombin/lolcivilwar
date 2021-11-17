let $groupSelector

function callMyGroup(url, changeFn, changeFnParam) {
    $groupSelector = $("#groupSelector");
    let groupList = [];
    common_ajax.call(url, 'POST', false, {}, function (res) {
        groupList = res.data;
        if (groupList.length === 0) {
            toast.warning("그룹에 속해 있지 않습니다.")
            return;
        }
        $groupSelector.empty();
        $groupSelector.append($('<option>').val(0).attr('selected', true).attr('disabled', true).html('그룹을 선택해주세요'));
        groupList.forEach((item, index) => {
            $groupSelector.append($('<option>').val(item.groupSeq).html(item.groupName).data('group', item));
            $groupSelector.show();
        });

    });
    if (typeof changeFn === 'function') {
        changeFn(groupList, changeFnParam);
    }
}