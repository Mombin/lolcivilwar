$(document).ready(function() {
    bindObject()
    bindEvent()
    init();
});

function init() {
    callMyGroup('/api/group/v1/my', groupChangeFunction);
}

function bindObject() {
    $list = $("#list");
    $customUserSelect = $("#customUserSelect");
    $groupSelector = $("#groupSelector");
}

function bindEvent() {
    $customUserSelect.on('change', changeCustomUser);
    $groupSelector.on('change', changeGroupSelect)
}