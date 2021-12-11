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
    $switchGroup = $('.switch-group');
}

function bindEvent() {
    $("#menuTab .nav-link").on('click', function () {
        $("#menuTab .nav-link.active").removeClass('active');
        $(this).addClass('active');
        let changeMenu = $(this).data('menu');
        if (changeMenu === AUTH_MENU.INVITE) {
            callGroupInviteList(0);
        } else if (changeMenu === AUTH_MENU.MANAGE_AUTH) {
            callGroupAuthList();
        }
        switchMenu(changeMenu)
    })
    $groupSelector.on('change', changeGroupSelect)
}


function init() {
    callMyGroup('/api/group/my-manage', groupChangeFunction);
}