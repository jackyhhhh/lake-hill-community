var host = localStorage.getItem("host")
function onload(){
    // 添加键盘监听事件ENTER
    document.onkeydown=function(ev){
        var oEvent=ev||event;
        if(oEvent.keyCode==13) { //如果按下enter键也可以提交注册
            register();
        }
    }
}

function checkUsername(){
    var username = document.getElementById("username_reg").value
    var error_box1 = document.getElementById("checkUsernameLabel1")
    var error_box2 = document.getElementById("checkUsernameLabel2")
    var url = host + "/user/checkUsername?username=" + username;

    if(isNull(username)){
        error_box1.innerHTML = "";
        error_box2.innerHTML = "";
        return;
    }
    if (username.length > 20 || username.length < 3) {
        error_box2.innerHTML = '<font color="red">&times;</font>'
        error_box1.innerHTML = '<font color="red">用户名请输入3-20位字符</font>';
        return;
    }else if((username.charCodeAt(0)>=48) && (username.charCodeAt(0)<=57)){
        error_box2.innerHTML = '<font color="red">&times;</font>'
        error_box1.innerHTML = '<font color="red">用户名首字符必须为字母</font>';
        return;
    }else for(var i=0;i<username.charCodeAt(i);i++){
        var code = username.charCodeAt(i)
        if(code<48 || (code>57 && code<65) || (code > 90 && code < 97) || code>122){
            error_box2.innerHTML = '<font color="red">&times;</font>'
            error_box1.innerHTML = '<font color="red">用户名必须为大小写字母跟数字组成</font>';
            return;
        }
    }

    getData(url).then(res=>{
        if(res.msg == "OK" && res.result == "SUCCESS"){
            error_box1.innerHTML = ''
            if(! isNull(username)){
                error_box2.innerHTML = '<font color="green">&radic;</font>'
            }
        }else{
            error_box2.innerHTML = '<font color="red">&times;</font>'
            error_box1.innerHTML = '<font color="red">' + res.msg + '</font>'
        }
    })
    .catch(error=>console.log(error));
}

function register() {
    var username = document.getElementById("username_reg")
    var password = document.getElementById("password")
    var nickname = document.getElementById("nickname")
    var error_box = document.getElementById("error_box")
    var error_box1 = document.getElementById("checkUsernameLabel1")
    var error_box2 = document.getElementById("checkUsernameLabel2")
    if (username.value.length > 20 || username.value.length < 3) {
        error_box2.innerHTML = '<font color="red">&times;</font>'
        error_box1.innerHTML = '<font color="red">用户名请输入3-20位字符</font>';
        return;
    }else if((username.value.charCodeAt(0)>=48) && (username.value.charCodeAt(0)<=57)){
        error_box2.innerHTML = '<font color="red">&times;</font>'
        error_box1.innerHTML = '<font color="red">用户名首字符必须为字母</font>';
        return;
    }else for(var i=0;i<username.value.charCodeAt(i);i++){
        var code = username.value.charCodeAt(i)
        if(code<48 || (code>57 && code<65) || (code > 90 && code < 97) || code>122){
            error_box2.innerHTML = '<font color="red">&times;</font>'
            error_box1.innerHTML = '<font color="red">用户名必须为大小写字母跟数字组成</font>';
            return;
        }
    }
    error_box.innerHTML = "";
    if (password.value.length > 20 || password.value.length < 1) {
        error_box.innerHTML = "密码请输入1-20位字符"
        return;
    }

    const url= host+ "/user/reg";
    const Data={
        "username": username.value,
        "password": password.value,
        "nickname": nickname.value
    }
    postData(url, Data).then(res=>{
        if(res.result == "FAILED"){
            window.alert("注册失败! " + res.msg);
            return;
        }else{
            window.location.assign(host + "/reg_successful.html");
        }
    }).catch(error=>console.log(error));
}