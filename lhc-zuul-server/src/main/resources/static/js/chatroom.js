var uid = localStorage.getItem("uid")
var host = localStorage.getItem("host")
var username = localStorage.getItem("username");
//声明一个定时任务 int
var int = null;

console.log("uid:"+uid)
console.log("host:"+host)
function refreshUserList(){
    var user_box = document.getElementById("user_list");
    user_box.innerHTML = "";
    getData(host + "/user/online").then(online=>{
        for(var i=0;i<online.obj.length;i++){
            var li = document.createElement("li");
            setUserData(online.obj[i], li, true);
            user_box.appendChild(li);
        }
        if(online.msg==="OK"){
            getData(host + "/user/offline").then(offline=>{
                for(var i=0;i<offline.obj.length;i++){
                    var li = document.createElement("li");
                    setUserData(offline.obj[i], li, false);
                    user_box.appendChild(li);
                }
            }).catch(error=>console.log(error));
        }
    }).catch(error=>console.log(error));
}

function setUserData(user, li, isOnline){
    // 给li标签创建img子标签(头像)
    var img = document.createElement("img");
    img.setAttribute("style", "border-radius: 20px; vertical-align: top;");
    // 给li标签创建span1子标签(用户名)
    var span1 = document.createElement("span");
    span1.setAttribute("style", "margin-left: 10px;");
    if(isNotNull(user.nickname)){
        span1.innerHTML = user.nickname;
    }else{
        span1.innerHTML = user.username;
    }
    // 给li标签创建span2子标签(用户状态)
    var span2 = document.createElement("span");
    // 根据在线状态设置的属性
    if(isOnline == false){
        img.setAttribute("src", "images/default.png");
        span2.setAttribute("style", "margin-left: 5px; color: brown");
        span2.innerHTML = " (离线)";
    }else{
        img.setAttribute("src", "images/favicon.ico");
        span2.setAttribute("style", "margin-left: 5px; color: green");
        span2.innerHTML = " (在线)";
    }
    // 将创建的标签添加到li的子节点
    li.appendChild(img);
    li.appendChild(span1);
    li.appendChild(span2);
}

function send(){
    var input_box = document.getElementById("input_box");
    var content = input_box.value;
    var error_box = document.getElementById("error_box");
    if(isNull(content)){
        error_box.innerHTML = "不能发送空消息!";
        return;
    }else{
        error_box.innerHTML = "";
    }
    // 点击发送后, 输入框清空
    document.getElementById("input_box").value="";
    const url = host + "/msg/saveMsg";
    const params = {
        "content": content
    }
    postData(url, params).then(res=>{
        if(res.result === "FAILED"){
            console.log("发送失败:"+res.msg);
        }
    })
}


// 页面加载完成时执行
function onload(){
    checkToken();
    // 刷新用户列表
    refreshUserList();
    // 刷新消息窗
    refreshMsg();
    // 定时任务int => 定时检测是否有新消息, 有则刷新消息窗口
    int = self.setInterval("clock()", 3000);
    // 添加键盘监听事件CTRL+ENTER
    var input_box = document.getElementById("input_box");
    input_box.onkeydown=function(ev){
        var oEvent=ev||event;
        if(oEvent.keyCode==13&&oEvent.ctrlKey) { //如果按下ctrl+enter键也可以提交留言
            send();
        }
    }
}

function clock(){
    var url = host + "/msg/describeLastMsg";
    getData(url).then(res=>{
        checkTokenInRes(res);
        if(isNotNull(res.obj)){
            var maxMid_got = res.obj.mid;
            var maxMid_storage = localStorage.getItem("maxMid_storage");
            console.log("midgot:"+maxMid_got+",midstorage:"+maxMid_storage+",g>s:"+(maxMid_got>maxMid_storage));
            if(maxMid_got > maxMid_storage){
                console.log("lastMsg:");
                console.log(res.obj);
                refreshMsg();
            }
        }
    }).catch(error=>console.log(error))
}

function refreshMsg(){
    var msg_box = document.getElementById("msg_box");
    // 重置窗口消息
    msg_box.innerHTML = "";

    var url = host + "/msg/describeMsgData?pageNum=0&pageSize=50";
    getData(url).then(res=>{
        console.log("describeMsgData:");
        console.log(res)
        var content = res.obj.content;
        if(res.msg == "OK" && res.obj != 1){
            if(content.length > 0){
                localStorage.setItem("maxMid_storage", content[0].mid);
                for(var i=content.length-1; i>=0; i--){
                    var record = content[i];
                    var li = document.createElement("li");
                    setRecordData(record, li);
                    msg_box.appendChild(li);
                }

                var box = document.getElementById("msg_container");
                msg_container.scrollTop = msg_container.scrollHeight;
            }
        }else{
            window.alert(res.msg);
            return;
        }
    }).catch(error=>console.log(error))
}

function setRecordData(message, li){
    // 给li标签添加i子标签(昵称)
    var i_name = document.createElement("a")
    if(isNull(message.nickname)){
        var display_name = "&nbsp;&nbsp;" + message.username + "&nbsp;&nbsp; ";
    }else{
        var display_name = "&nbsp;&nbsp;" + message.nickname + "&nbsp;&nbsp;";
    }
    if(message.username === username){
        i_name.setAttribute("style", "display: block; margin:1px; border-radius: 3 px; font-size:10px; vertical-align: top; color: green;");
        display_name += "(我)&nbsp;&nbsp;";
    }else {
        i_name.setAttribute("style", "display: block; margin:1px; border-radius: 3 px; font-size:10px; vertical-align: top; color: grey;");
    }
    i_name.innerHTML = display_name + message.sendTime;
    li.appendChild(i_name);
    // 给li标签添加p子标签
    var p = document.createElement("p");
    p.setAttribute("class", "msgcard");
    p.innerHTML = message.content.replace(/\n/g, "<br>");
    li.appendChild(p);
}
