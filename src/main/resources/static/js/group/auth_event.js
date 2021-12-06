$(document).ready(function () {
    bindObject();
    bindEvent();
    init();
});

// 객체 바인딩
function bindObject() {
    $groupSelector = $("#groupSelector")
    $groupAuthList = $("#groupAuthList")
    $groupAutheListHeader = $("#groupAutheListHeader")
    $inviteHistoryTable = $("#inviteHistoryTable")
    $manageAuthTable = $("#manageAuthTable")
}

function bindEvent() {
    $("#position .nav-link").on('click', function () {
        $("#position .nav-link.active").removeClass('active');
        $(this).addClass('active');
        let manageMenu = $(this).data('position');
        if (manageMenu === 'invite') {
            callGroupInviteList();
        } else {
            callGroupAuthList();
        }
    })
    $groupSelector.on('change', changeGroupSelect)
}


function init() {
    callMyGroup('/api/group/my-manage', groupChangeFunction);
}