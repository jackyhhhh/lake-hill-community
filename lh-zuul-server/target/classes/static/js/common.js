var host="http://"+window.location.host;
localStorage.setItem("host", host);


// 重新定义空值判定
function isNull(value) {
    if (value === null || value === "null" || value === "" || value === "undefined") {
        return true;
    } else {
        return false;
    }
}

function isNotNull(value) {
    return !isNull(value);
}

// 获取指定长度的随机字符串
function randomString(len) {
　　len = len || 32;
　　var $chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';    /****默认去掉了容易混淆的字符oOLl,9gq,Vv,Uu,I1****/
　　var maxPos = $chars.length;
　　var pwd = '';
　　for (i = 0; i < len; i++) {
　　　　pwd += $chars.charAt(Math.floor(Math.random() * maxPos));
　　}
　　return pwd;
}

// 设置本站业务相关的localStorage Item
function setLocalStorageItem(uid, username, nickname, token){
　　localStorage.setItem("uid", uid);
　　localStorage.setItem("username", username);
　　localStorage.setItem("nickname", nickname);
   localStorage.setItem("token", token);
}

// 清空本站业务相关的localStorage Item
function logout(){
　　 localStorage.clear();
    getData(host + "/user/logout").then(res=>{
        if(isNotNull(res) && res.result=="FAILED"){
            window.alert("发生未知错误");
        }
        console.log(res);
    })
    window.alert("您已退出登录, 即将返回首页...")
    window.location.href=host+"/index.html"
}

// post请求并返回数据
function postData(url, params) {
  // Default options are marked with *
  return fetch(url, {
    body: JSON.stringify(params), // must match 'Content-Type' header
    cache: 'no-cache', // *default, no-cache, reload, force-cache, only-if-cached
    credentials: 'same-origin', // include, same-origin, *omit
    headers: {
      'user-agent': 'Mozilla/4.0 MDN Example',
      'content-type': 'application/json'
    },
    method: 'POST', // *GET, POST, PUT, DELETE, etc.
    mode: 'cors', // no-cors, cors, *same-origin
    redirect: 'follow', // manual, *follow, error
    referrer: 'no-referrer', // *client, no-referrer
  })
  .then(response => response.json()) // parses response to JSON
}

// get请求并返回数据
function getData(url) {
  // Default options are marked with *
  return fetch(url,{
     method: "GET",
     mode: "cors",
     headers: {
         "Accept": "application/json",
     }
  })
  .then(response => response.json()) // parses response to JSON
}

///*========================================================================
//    ************   退出浏览器时清空本站相关的localStorage Item   ************
// ========================================================================*/
//初始化应用缓存
(function(){
    if(!getCookie('eks_cache_keys')){//每次进入页面初始化缓存
       logout();
    　　setCookie('eks_cache_keys',true);
    }
})();

/*======================================================
    ************   cookie操作   ************
 ======================================================*/
//取得cookie
function getCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');    //把cookie分割成组
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];                      //取得字符串
        while (c.charAt(0)==' ') {          //判断一下字符串有没有前导空格
            c = c.substring(1,c.length);      //有的话，从第二位开始取
        }
        if (c.indexOf(nameEQ) == 0) {       //如果含有我们要的name
            return unescape(c.substring(nameEQ.length,c.length));    //解码并截取我们要值
        }
    }
    return false;
}

//清除cookie
function clearCookie(name) {
 setCookie(name, "", -1);
}

//设置cookie
function setCookie(name, value, seconds) {
    seconds = seconds || 0;   //seconds有值就直接赋值，没有为0，这个根php不一样。
    var expires = "";
    if (seconds != 0 ) {      //设置cookie生存时间
        var date = new Date();
        date.setTime(date.getTime()+(seconds*1000));
        expires = "; expires="+date.toGMTString();
    }
    document.cookie = name+"="+escape(value)+expires+"; path=/";   //转码并赋值
}

//检查response中是否返回token失效
function checkTokenInRes(res){
    if(isNotNull(res) && res.msg.indexOf("ACCESS_DENIED") != -1){
        console.log(res);
        window.alert(res.msg);
        window.location.assign(host + "/login.html");
    }
}

//检查token是否有效
function checkToken(){
    getData(host + "/user/checkToken").then(res=>{
        checkTokenInRes(res);
    }).catch(error=>console.log(error))
}

