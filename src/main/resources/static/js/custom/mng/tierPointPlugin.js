let $toggleTierPoint, tierPoints = {};

function initTierPointPlugin() {
  /* variable */
  $toggleTierPoint = $('#toggleTierPoint')

  /* event */
  $toggleTierPoint.on('click', function() {
    let tierToggle = $(this).data('tierToggle');
    if (tierToggle === 'off') {
      $(this).data('tierToggle', 'on')
      $(this).val('티어표 ON')
      $('.tierPointGroup').show()
    } else {
      $(this).data('tierToggle', 'off')
      $(this).val('티어표 OFF')
      $('.tierPointGroup').hide()
    }
    $(this).toggleClass('btn-danger').toggleClass('btn-primary')
    sumTierPoints('a');
    sumTierPoints('b');
  })

  /* function */
  $toggleTierPoint.getToggleVal = function () {
    return $(this).data('tierToggle') === 'on';
  }
}
