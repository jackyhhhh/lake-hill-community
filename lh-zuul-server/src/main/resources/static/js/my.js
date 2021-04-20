var uid = localStorage.getItem("uid")
var host = localStorage.getItem("host")
console.log("uid is null:"+isNull(uid))
console.log("host:"+host)

function showDeleteElement(){
    var deleteElements = document.getElementById("deleteElements");
    deleteElements.removeAttribute("hidden")
    var show_del = document.getElementById("show_del");
    show_del.setAttribute("disabled", "disabled")
}

function removeUser(){
    var del_pwd = document.getElementById("del_pwd").value;
    var username = localStorage.getItem("username");
    var error_box = document.getElementById("error_box");
    var Data = {
        "username": username,
        "password": del_pwd
    }
    url = host + "/user/removeUser"
    postData(url, Data).then(res=>{
        if(res.result == "FAILED" || res.result == "ERROR"){
            error_box.innerHTML == res.msg;
        }else {
            window.location.assign(host + "/index.html");
        }
    }).catch(console.log(error));

}

function cancelRemove(){
    var deleteElements = document.getElementById("deleteElements")
    deleteElements.setAttribute("hidden", "hidden")
    var show_del = document.getElementById("show_del");
    show_del.removeAttribute("disabled")

}

function onload(){
    checkToken();
    var name = localStorage.getItem("nickname");
    if(! isNull(name)){
        user.innerHTML = name+" ";
    }else{
        user.innerHTML = localStorage.getItem("username")+" ";
    }
}

function goChatting(){
    var chatId = localStorage.getItem("chatId");
    console.log("chatId: "+chatId);
    if(isNull(chatId)){
        chatId = randomString(8);
        localStorage.setItem("chatId", chatId);
    }
    window.location.assign(host+"/chatroom.html");
}
