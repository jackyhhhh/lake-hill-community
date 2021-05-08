var uid = localStorage.getItem("uid")
var host = localStorage.getItem("host")
console.log("uid:"+uid)
console.log("host:"+host)

function onload(){
    checkToken();
        // 添加键盘监听事件ENTER
    document.onkeydown=function(ev){
        var oEvent=ev||event;
        if(oEvent.keyCode==13) { //如果按下enter键也可以提交修改
            modifyPassword();
        }
    }
}

function modifyPassword() {
    var newPwd1 = document.getElementById("newPwd1").value
    var newPwd2 = document.getElementById("newPwd2").value
    var error_box = document.getElementById("error_box")
    if(newPwd1 === newPwd2){
        var oldPwd = document.getElementById("oldPwd").value
        const url= host+ "/user/modifyPassword";
        const Data={
            "uid": uid,
            "oldPwd": md5(oldPwd).substring(0,20),
            "newPwd": md5(newPwd1).substring(0,20)
        }
        postData(url, Data).then(res=>{
            if(res.result == "SUCCESS"){
                window.location.assign(host + "/myPage.html");
            }else{
                error_box.innerHTML = res.msg;
                return;
            }
        }).catch(error=>console.log(error));
    }else{
        error_box.innerHTML = "两次输入的新密码不一样, 请重新输入! ";
        return;
    }
}