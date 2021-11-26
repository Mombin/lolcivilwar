$(document).ready(function () {
    bindObject();
    bindEvent();
    init();
});

// 객체 바인딩
function bindObject() {
    $groupSelector = $("#groupSelector")
    $seasonSelector = $('#season');
}

// 이벤트 바인딩
function bindEvent() {
    $("#position .nav-link").on('click', function () {
        $("#position .nav-link.active").removeClass('active');
        $(this).addClass('active');
        let position = $(this).data('position');
        let list = [];

        if (currentGroup.customUser[$seasonSelector.val()] === undefined) {
            currentGroup.customUser[$seasonSelector.val()] = [];
            callRankData()
        }
        $.each(currentGroup.customUser[$seasonSelector.val()], function (index, item) {
            let date = (item.lastDate || []).slice();
            let lastDate;
            if (date) {
                date[1] = date[1] - 1;
                lastDate = moment(date.slice(0, 6));
                if (moment().diff(lastDate, 'day') > 10) {
                    return;
                }
            } else {
                return;
            }
            let total = 0
                , win = 0;
            if (position === 'total') {
                $.each(item.positionWinRate, function (key, value) {
                    total += value.first || 0;
                    win += value.second || 0;
                });
            } else {
                let positionWinRate = item.positionWinRate[position.toUpperCase()] || {};
                total = positionWinRate.first || 0;
                win = positionWinRate.second || 0;
            }
            let rate = ((win / (total === 0 ? 1 : total)) * 100).toFixed(1);
            if (isNaN(rate)) {
                rate = 0;
            }

            list.push({
                nickname: item.nickname,
                summonerName: item.summonerName,
                total: total,
                win: win,
                rate: rate,
                lastDate: lastDate.format('YYYY-MM-DD')
            })
        });
        list.sort(function (a, b) {
            let check = b.rate - a.rate;
            if (check !== 0) {
                return check;
            }
            return b.total - a.total;
        });
        setList(list);
    });
    $seasonSelector.on('change', function () {
        $("#position .nav-link.active").trigger('click');
    });

    $groupSelector.on('change', changeGroupSelect);
}

function init() {
    callMyGroup('/api/group/v1/my', groupChangeFunction);
}