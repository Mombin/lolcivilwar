function authSave() {
  let saveTarget = [], myAuth = GROUP_AUTH.USER;
  $.each($groupAuthList.children(), function (idx, obj){
    let currentAuth = $(obj).find('td:eq(1)').data('currentAuth')
    if (session.name() === $(obj).find('td:eq(0)').html()) {
      myAuth = currentAuth;
      return;
    }
    let changedAuth = $(obj).find('td:eq(1)').find('select').val();
    if (currentAuth === changedAuth) {
      return;
    }
    let $id = $(obj).find('td:eq(0)'),
      userId = $id.html(),
      userSeq = $id.data('userSeq');
    saveTarget.push({userSeq: userSeq, changedAuth: changedAuth, id: userId, currentAuth: currentAuth})
  });
  if (saveTarget.length === 0) {
    toast.warning("변경 대상이 없습니다.");
    return;
  }
  for (let i = 0; i < saveTarget.length; i++) {
    if (myAuth !== GROUP_AUTH.OWNER
      && (GROUP_AUTH.getOrder(saveTarget[i].changedAuth) >= GROUP_AUTH.getOrder(myAuth)
        || GROUP_AUTH.getOrder(saveTarget[i].currentAuth) >= GROUP_AUTH.getOrder(myAuth))) {
      toast.warning(saveTarget[i].id + "을 변경하기 위한 권한이 부족합니다.")
      return;
    }
    if (myAuth === GROUP_AUTH.OWNER && saveTarget[i].changedAuth === GROUP_AUTH.OWNER) {
      if (!confirm(saveTarget[i].id + "에게 그룹에 대한 소유권을 이전 하시겠습니까?")) {
        return;
      }
    }
  }
  ajaxAuthSave(saveTarget);
}

function ajaxAuthSave(saveTarget) {
  const param = {
    groupSeq: currentGroup.groupSeq,
    targets: saveTarget
  }
  common_ajax.call('/api/group/v1/user/auth', 'PUT', true, param, function (res) {
    if (res.code !== API_RESULT.SUCCESS) {
      toast.error(res.message)
      return;
    }
    toast.success("권한이 수정되었습니다.")
    let authCall = $manageAuthTable.isAuthCall || {};
    authCall[currentGroup.groupSeq] = false;
    $manageAuthTable.isAuthCall = authCall;
    $groupAuthList.empty();
    callGroupAuthList();
  })
}