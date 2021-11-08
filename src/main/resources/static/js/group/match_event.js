$(document).ready(function() {
    bindObject();
    bindEvent();
    init();
});

function bindObject() {
    $list = $("#list");
    $groupSelector = $("#groupSelector")
}

function bindEvent() {
    $groupSelector.on('change', function () {
        let item = $(this).find('option:selected').data('group');
        $('#myGroup').html(item.groupName);
        myGroup = item;
        getList(0);
    });
}