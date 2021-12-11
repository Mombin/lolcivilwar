const AUTH_MENU = {
  MANAGE_AUTH: 'manage-auth',
  INVITE: 'invite'
}

function switchMenu(changeMenu) {
  $switchGroup.hide();
  $switchGroup.filter('.' + changeMenu).show();
}