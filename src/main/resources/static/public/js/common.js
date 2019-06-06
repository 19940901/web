var timeoutCount = 0; //登录超时，请求次数  0未弹出提示
var token = $.cookie('token');
//var dev_domain="http://"+window.location.host+"/";
var dev_domain="http://"+window.location.hostname+":8088/";

var domain=dev_domain;

var interfaces={
		register:domain+"registerController/register",
		login:domain+"registerController/login",
		logout:domain+"registerController/logout",
		getInfo:domain+"infoController/getInfo",
		getInfo1:domain+"infoController/getInfo1",
		getInfo2:domain+"infoController/getInfo2",
		getInfo3:domain+"infoController/getInfo3"
};

function getData(typed, url, async, data, callbacktype) {
	var Autho = $.cookie('token');
	$.ajax({
		type : typed,
		url : url,
		data : data,
		async:async,
		cache:false,
		scriptCharset:'utf-8',
		beforeSend: function (xhr) { xhr.setRequestHeader ("Authorization", Autho); },
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			if(timeoutCount==0){
				timeoutCount ++;	//超时后 多次异步请求，多次响应，第一次跳转即可
				if(XMLHttpRequest.status=='404'){
					//window.location= "/public/error/404.html";
				}else{
					if(0 != XMLHttpRequest.status&&200 != XMLHttpRequest.status){
						alert("系统繁忙-"+XMLHttpRequest.status);
					}
				}
			}
		},
		success : function(resultData) {
			callbacktype(resultData);
		}
	});
}


//获取url中的参数
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); //匹配目标参数
	if (r != null)
		return unescape(r[2]);
	return null; //返回参数值
}
/**
 * format 数据库时间格式代码
 * @returns {YY-mm-dd}
 */
function format_date(tObj) {
	if(tObj==null||tObj==""){
		return "";
	}else{
		var tim =  new Date(tObj);
		var year = tim.getFullYear(); //年
		var month = tim.getMonth() + 1; //月
		month = month < 10 ? '0' + month : month;
		var day = tim.getDate();//日
		day = day < 10 ? '0' + day : day;
		return year + '-' + month + '-' + day;
	}
}

/**
 * 数据为null时,返回""
 */
function returnN(data) {
	if ( data == undefined || data == null || data == "undefined" || data=="null") {
		data = "";
		return data;
	}
	return $.trim(data);
}

/**
*去掉空格
*/
function trim(str){ 
    return str.replace(/(^\s*)|(\s*$)/g, ""); 
}