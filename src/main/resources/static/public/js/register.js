function submit() {

    var data = {};
    data.username = $("#username").val();
    data.password = $("#password").val();

    if ($.isEmptyObject(data.username) || $.isEmptyObject(data.password)) {
        //alert("null")
        //消息提示全局配置
        toastr.options = {
            "closeButton": false,//是否配置关闭按钮
            "debug": false,//是否开启debug模式
            "newestOnTop": false,//新消息是否排在最上层
            "progressBar": false,//是否显示进度条
            "positionClass": "toast-top-center",//消息框的显示位置
            "preventDuplicates": false,//是否阻止弹出多个消息框
            "onclick": null,//点击回调函数
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "1500",//1.5s后关闭消息框
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }
        toastr.warning("username or password is null")
    } else {
        getData("POST", interfaces.register, false, data, function (result) {
            //alert(result.username+result.password)

            if (result != null)
                window.location = "/public/view/login.html"
        });
    }
}
