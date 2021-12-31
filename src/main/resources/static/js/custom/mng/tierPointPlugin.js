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

function sumTierPoints(team) {
  if (team === undefined) {
    return;
  }
  let sum = 0;
  $.each($(`#team .team-position input[name="team_${team}"]`),function (idx, obj) {
    if ($(obj).val().trim() !== '') {
      let tierPoint = tierPoints[$(obj).val().trim()] || 0;
      sum += tierPoint;
    }
  });
  $(`#team [name="tearPointSum"][data-team="${team}"]`).val(sum);
}