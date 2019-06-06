function submit() {

    var data = {};
    data.username = $("#username").val();
    data.password = $("#password").val();
    data.rememberme = "true";
    getData("POST", interfaces.login, false, data, function (result) {
        if (result != null && result.msg == "登录成功") {
            $.cookie("token", result.token);
            $.cookie("REMEMBERME", $.cookie("REMEMBERME"));
         //   window.location = "/public/view/info.html";
        }
    });
}
