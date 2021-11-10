$(document).ready(function() {
    bindObject();
    bindEvent();
    init();
});
// 객체 바인딩
function bindObject() {
    $groupSelector = $("#groupSelector")
    $groupAuthList = $("#groupAuthList")
}

function bindEvent() {
    $groupSelector.on('change', callGroupAuthList)
}

function init() {
    callMyGroup('/api/group/my-manage', groupChangeFunction);
}