$(document).ready(function() {
    bindObject();
    bindEvent();
    init();
});

function init() {
    callMyGroup('/api/group/my', groupChangeFunction);
}

function bindObject() {
    $customUserSelect = $("#customUserSelect");
    $groupSelector = $("#groupSelector");
}

function bindEvent() {
    $customUserSelect.on('change', changeCustomUser);
    $groupSelector.on('change', changeGroupSelect)
}