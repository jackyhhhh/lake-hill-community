var uid = localStorage.getItem("uid")
var host = localStorage.getItem("host")

function onload(){
    checkToken();
        // 添加键盘监听事件ENTER
    document.onkeydown=function(ev){
        var oEvent=ev||event;
        if(oEvent.keyCode==13) { //如果按下enter键也可以提交修改
            modifyInfo();
        }
    }
}

function modifyInfo() {
    var username = document.getElementById("username").value
    var nickname = document.getElementById("nickname").value
    var error_box = document.getElementById("error_box")

    if(isNull(username) && isNull(nickname)){
        error_box.innerHTML = "用户名或昵称, 请至少选填一项!"
        return;
    }
    if(isNull(username)){
        username = localStorage.getItem("username");
    }
    if(isNull(nickname)){
        nickname = localStorage.getItem("nickname");
    }

    const url= host+ "/user/modify";
    const Data={
        "uid": localStorage.getItem("uid"),
        "username": username,
        "nickname": nickname
    }
    postData(url, Data).then(res=>{
        if(res.result == "SUCCESS"){
        　　localStorage.setItem("username", username);
        　　localStorage.setItem("nickname", nickname);
            window.location.assign(host + "/myPage.html");
        }else{
            error_box.innerHTML = res.msg;
            return;
        }
    }).catch(error=>console.log(error));
}