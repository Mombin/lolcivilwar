let $toggleGameData, ingameApiWorker, encryptIds = {};

function initGameDataPlugin() {
  /* variable */
  $toggleGameData = $('#toggleGameData')

  /* event */
  $toggleGameData.on('click', function() {
    let gameToggle = $(this).data('gameToggle');
    if (gameToggle === 'off') {
      $(this).data('gameToggle', 'on')
      $(this).val('게임데이터 ON')
      $('.gameDataGroup').show()
      runIngameApiWorker();
    } else {
      $(this).data('gameToggle', 'off')
      $(this).val('게임데이터 OFF')
      $('.gameDataGroup').hide()
      ingameApiWorker.terminate();
    }
    $(this).toggleClass('btn-danger').toggleClass('btn-primary')
  })

  /* function */
  $toggleGameData.getToggleVal = function () {
    return $(this).data('gameToggle') === 'on';
  }
}

function runIngameApiWorker() {
  if (ingameApiWorker) {
    ingameApiWorker.terminate();
  }
  ingameApiWorker = new Worker('/static/js/custom/mng/ingameApiWorker.js');
  ingameApiWorker.onmessage = function (evt) {
    const message = evt.data;
    toast.success(message);
  }
  ingameApiWorker.postMessage({ids: getCurrentEncryptIds()})
}

function getCurrentEncryptIds() {
  let ids = [];
  $.each($('#team .team-position input'),function (idx, obj) {
    if ($(obj).val().trim() !== '') {
      let encryptId = encryptIds[$(obj).val().trim()] || '';
      if (encryptId) {
        ids.push(encryptId)
      }
    }
  });
  return ids;
}