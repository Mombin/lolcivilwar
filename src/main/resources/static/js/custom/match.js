let currentGroupSeq = -1;

function setMatchEvents() {
    $('[name="winner_btn"]').on('click', function() {
        let winnerTeam = $(this).data('team');

        if(!confirm("정말 " + winnerTeam.toUpperCase() + "이 이겼습니까?")) {
            return;
        }

        let canCall = true;
        const matchResult = [];
        $.each($("#team .team-position .form-control"), function(index, item) {
            const $item = $(item);
            const targetTeam = $item.attr('name').substring(5, 6);
            const param = {
                user: $item.val(),
                team: targetTeam.toUpperCase(),
                result: targetTeam === winnerTeam,
                position: $item.parent().data('position').toUpperCase()
            }
            matchResult.push(param);

            if (!$item.val()) {
                canCall = false;
            }
        });

        if (!canCall) {
            alert("빈값이 존재합니다.");
            return;
        }

        const param = {
            groupSeq: currentGroupSeq,
            matchResult: matchResult
        }

        common_ajax.call("/api/custom/match", "PUT", true, param, function (res) {
            if(res.code === API_RESULT.FAIL) {
                alert(res.message);
                return;
            }
            alert("등록에 성공하였습니다.")
        });
    });
}