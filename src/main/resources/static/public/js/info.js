function getInfo(){
	
	var data = {};
	data.username = $("#username").val();
	getData("POST", interfaces.getInfo, false, data, function(result){
		if(result.userName){
			$("#text").html(result.userName);
		}else{
			$("#text").html(result.msg);
		}
	});
}
function getInfo1(){
	
	var data = {};
	data.username = $("#username").val();
	getData("POST", interfaces.getInfo1, false, data, function(result){
		if(result.userName){
			$("#text").html(result.userName);
		}else{
			$("#text").html(result.msg);
		}
	});
}
function getInfo2(){
	
	var data = {};
	data.username = $("#username").val();
	getData("POST", interfaces.getInfo2, false, data, function(result){
		if(result.userName){
			$("#text").html(result.userName);
		}else{
			$("#text").html(result.msg);
		}
	});
}
function getInfo3(){
	
	var data = {};
	data.username = $("#username").val();
	getData("POST", interfaces.getInfo3, false, data, function(result){
		if(result.userName){
			$("#text").html(result.userName);
		}else{
			$("#text").html(result.msg);
		}
	});
}

function logout(){
	getData("POST", interfaces.logout, false, {}, function(result){
		$("#text").html(result.msg);
	});
}
