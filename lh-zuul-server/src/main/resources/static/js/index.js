var host = localStorage.getItem("host");
function onload(){
    var label = document.getElementById("checkStatus");
    getData(host + "/user/checkToken")
    .then(res=>{
        if(isNotNull(res) && res.msg.indexOf("ACCESS_DENIED") != -1){
            console.log(res);
            label.innerHTML = '<a href="login.html"><h3>立即登录</h3></a>';
        }else{
            label.innerHTML = '<a href="myPage.html"><h3>我的主页</h3></a>';
        }
    })

}