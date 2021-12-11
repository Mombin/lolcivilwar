$(document).ready(function () {
    bindObject();
    bindEvent();
    init();
});

// 객체 바인딩
function bindObject() {
    $groupSelector = $("#groupSelector")
    $groupAuthList = $("#groupAuthList")
    $inviteHistoryTable = $("#inviteHistoryTable")
    $manageAuthTable = $("#manageAuthTable")
    $inviteHistory = $("#inviteHistory")
}

function bindEvent() {
    const MENU_INVITE = 'invite';
    const MENU_MANAGEAUTH = 'manageAuth';
    $("#position .nav-link").on('click', function () {
        $("#position .nav-link.active").removeClass('active');
        $(this).addClass('active');
        let manageMenu = $(this).data('position');
        if (manageMenu === MENU_INVITE) {
            callGroupInviteList(0);
        } else if (manageMenu === MENU_MANAGEAUTH) {
            callGroupAuthList();
        }
    })
    $groupSelector.on('change', changeGroupSelect)
}


function init() {
    callMyGroup('/api/group/my-manage', groupChangeFunction);
}