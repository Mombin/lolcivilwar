$(document).ready(function() {
    bindObject();
    bindEvent();
    init();
});

function init() {
    callMyGroup('/api/group/v1/my', groupChangeFunction);
}

function bindObject() {
    $customUserSelect = $("#customUserSelect");
    $groupSelector = $("#groupSelector");
    $seasonSelector = $("#season")
}

function bindEvent() {
    $customUserSelect.on('change', changeCustomUser);
    $groupSelector.on('change', changeGroupSelect)
    $seasonSelector.on('change', changeSeasonSelect)
}