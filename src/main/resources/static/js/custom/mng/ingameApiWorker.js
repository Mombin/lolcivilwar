importScripts("/static/js/common/common-util.js");

let data = {};
onmessage = function (evt) {
  data = evt.data;
  while(data.flag) {
    sleep(1000);
    console.log("apiShoot")
    if (Math.round(Math.random()*10) > 8) {
      data.flag = false;
      postMessage("apiSuccess")
    } else {
      console.log("apiFail")
    }
  }
}