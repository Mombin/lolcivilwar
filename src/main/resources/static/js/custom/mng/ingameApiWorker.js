importScripts("/static/js/common/common-util.js");

let data = {}, flag = false, encryptIds = [];
onmessage = function (evt) {
  data = evt.data;
  encryptIds = data.ids;
  flag = encryptIds.length >= 2;
  while(flag) {
    sleep(1000);
    console.log(encryptIds)
    if (Math.round(Math.random()*10) > 8) {
      flag = false;
      postMessage("apiSuccess")
      break
    } else {
      console.log("apiFail")
    }
  }
}