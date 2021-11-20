$(document).ready(function() {
    bindObject()
    bindEvent()
    init();
});

function bindObject() {
    $list = $("#list");
    $customUserSelect = $("#customUserSelect");
    $groupSelector = $("#groupSelector");
}

function bindEvent() {
    $customUserSelect.on('change', changeCustomUser);
    $groupSelector.on('change', changeGroupSelect)
}