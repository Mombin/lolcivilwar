let $unreadMessage;
const ALARM_TYPE = {
  MESSAGE: 'MESSAGE'
}

// 읽지 않은 알림 불러오기
function getUnreadMessage() {
    common_ajax.call('/api/user/alarm', 'GET', true, {}, function(res) {
        if (res.code !== API_RESULT.SUCCESS) {
            toast.error("메시지를 불러오는데 에러가 발생하였습니다.\n관리자에게 문의부탁드립니다.");
            return;
        }
        $unreadMessage.empty();
        if (res.data.length === 0) {
            $unreadMessage.append(
                $('<div>').addClass('card text-center').append(
                    $('<div>').addClass('card-body')
                        .append($('<p>').addClass('card-text').html('더이상 읽을 알림이 없습니다.'))
                )
            )
            return;
        }
        $.each(res.data, function (index, item) {
          let alarm = '';
          if (item.alarmType === ALARM_TYPE.MESSAGE) {
            alarm = typeMessage(item);
          } else {
            alarm = typeInvite(item);
          }
          $unreadMessage.append(alarm)
        });
    });
}

function typeMessage(item) {
  return $('<div>').addClass('card')
    .append(
      $('<div>').addClass('card-body')
        .append($('<p>').addClass('card-text').html(item.message))
        .append(
          $('<a>').attr('href', '#')
            .addClass('btn btn-primary')
            .data('alarmSeq', item.alarmSeq)
            .on('click', readMessage)
            .html('확인')
        )
    )
    .append(
      $('<div>').addClass('card-footer text-muted').html(item.viewTime)
    )
}

function typeInvite(item) {
  return $('<div>').addClass('card')
    .append(
      $('<div>').addClass('card-body')
        .append($('<p>').addClass('card-text').html(item.message))
        .append(
          $('<a>').attr('href', '#')
            .addClass('btn btn-primary mr10')
            .data('alarmSeq', item.alarmSeq)
            .data('inviteSeq', item.inviteSeq)
            .data('result', 'Y')
            .on('click', replyInvite)
            .html('승인')
        )
        .append(
          $('<a>').attr('href', '#')
            .addClass('btn btn-danger')
            .data('alarmSeq', item.alarmSeq)
            .data('inviteSeq', item.inviteSeq)
            .data('result', 'N')
            .on('click', replyInvite)
            .html('취소')
        )
    )
    .append(
      $('<div>').addClass('card-footer text-muted').html(item.viewTime)
    )
}