let $toggleGameData, ingameApiWorker

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
      terminateApiWorker()
    }
    $(this).toggleClass('btn-danger').toggleClass('btn-primary')
  })

  /* function */
  $toggleGameData.getToggleVal = function () {
    return $(this).data('gameToggle') === 'on';
  }
}

function runIngameApiWorker() {
  ingameApiWorker = new Worker('/static/js/custom/mng/ingameApiWorker.js');
  ingameApiWorker.onmessage = function (evt) {
    const message = evt.data;
    toast.success(message);
  }
  ingameApiWorker.postMessage({flag: true})
}

function terminateApiWorker() {
  ingameApiWorker.postMessage({flag: false})
  ingameApiWorker.terminate();
}