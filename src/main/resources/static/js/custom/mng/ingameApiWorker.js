importScripts("/static/js/common/common-util.js");

let data = {}, flag = false, encryptIds = [];
onmessage = function (evt) {
  data = evt.data;
  encryptIds = data.ids;
  flag = encryptIds.length >= 2;
  while(flag) {
    let ids = encryptIds.join(",")
    common_ajax.pure_call('/api/custom/ingame-info?encryptIdList='+ ids, 'GET', false, '', function (res) {
      if (res.code !== API_RESULT.SUCCESS) {
        flag = false;
        return;
      }
      console.log(res);
    })
    sleep(20000)
  }
}