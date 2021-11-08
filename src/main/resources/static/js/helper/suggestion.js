const textLimit = 250;
$(document).ready(function () {
    $("#suggestionText").on('keyup', function () {
        let textLength = $(this).val().length;
        const $textLimit = $("#suggestionTextLimit");
        $textLimit.html(textLength + " / " + textLimit);
        if (textLength > textLimit) {
            $textLimit.css('color', 'red');
        } else {
            $textLimit.css('color', '');
        }
    });

    $("#suggestionSubmit").on('click', function () {
        let $text = $("#suggestionText");
        $text.val($text.val().trim());
        if ($text.val().length > 250) {
            alert("건의는 250자 이상입력하실수 없습니다.");
            return;
        }

        const param = {
            context: $text.val()
        }

        common_ajax.call('/api/helper/save', 'PUT', true, param, function (res) {
            if (res.code === API_RESULT.SUCCESS) {
                alert("좋은 건의사항 감사합니다.");
                $text.val("");
                $("#suggestionTextLimit").html(0 + " / " + textLimit);
            } else {
                let msg = "건의사항 제출에 실패했습니다.";
                if (res.message !== "") {
                    msg = res.message;
                }
                alert(msg);
            }
        })
    });
})