// 页面初始化
$(function() {
	// 关闭加载层
	parent.layer.close(parent.loadLayer);
	parent.parent.layer.close(parent.parent.loadLayer);
	
})

//刷新操作
function refresh() {
	parent.loadLayer = parent.layer.load(0, {shade: [0.5, '#fff'] });
	window.location.reload();
}

//全选操作
function layTableDatagridCheckSelectAll(cobj) {
	if (cobj.checked) {
		$('.laytablecheckbox').prop('checked', true);
	} else {
		$('.laytablecheckbox').prop('checked', false);
	}
}

//勾选某一项的checkbox的响应函数
function layTableCheck(cobj) {
	if (cobj.checked == false) {
		$('#layTableCheckAll').prop('checked', false);
	}
}

//打开新TAB页面
openTab = function (id, title, url, tab) {
	var t;
	if (null != tab) {
		t = $('#' + tab);
	} else {
		t = $('#tabs');
	}
	if(t.tabs('exists', id)) {
		t.tabs('select', id);
		var f = document.getElementById(t.tabs('getSelected').panel('body').find('iframe').attr('id'));
		if (null != f) {
			var c = f.contentWindow;
			if (c.refresh) c.refresh();
		}
	} else {
		loadLayer = layer.load(0, {shade: [0.5, '#fff'] });
		t.tabs('add', {
			id : id,
			title: title,
			iconCls: 'origin',
			fit: true,
			content: '<iframe frameborder="0" marginheight="0" marginwidth="0" width="100%" height="100%" src="' + url + '" id="main_tabs_iframe_' + id + '"></iframe>',
			closable : true,
			onOpen : function () {
				var f = t.tabs('getSelected').panel('body').find('iframe');
				if (f.attr('src') == '#') {
					f.attr('src', url);
				}
			}
		});
	}
};
//关闭TAB页面
closeTab = function (id, isRefresh, tab) {
	if (null != tab) {
		t = $('#' + tab);
	} else {
		t = $('#tabs');
	}
	if (typeof(id) == "undefined" || id == "") {
		title = t.tabs('getSelected').panel('options').title;
	}
	t.tabs('close', id);
	var f = document.getElementById(t.tabs('getSelected').panel('body').find('iframe').attr('id'));
	if (null != f && isRefresh == 'refresh') {
		var c = f.contentWindow;
		if (c.refresh) c.refresh();
	}
};

var ne_wf_requestSubmitted=false;
//检测页面是否已经提交过
function ne_wf_checkSubmit() {
	if (ne_wf_requestSubmitted == true) {
		alert("您已经提交了请求，请耐心等候服务器应答……");
		return false;
	} else {
		ne_wf_requestSubmitted=true;
		return true;
	}
}
//表单提交验证
function ne_wf_WebInputForm_onSubmit(form, isCheck, mode) {
	if(isCheck == null)
		isCheck = true;
	if(isCheck){
		if(!ne_wf_Validator.Validate(form,mode)){
			return false;
		}
	}
	if(ne_wf_checkSubmit()){
		return true;
	}
	return false;
}
/**
 * json对象转字符串形式
 */
function json2str(o) {
	var arr = [];
	var fmt = function(s) {
		if (typeof s == 'object' && s != null) return json2str(s);
		return /^(string|number)$/.test(typeof s) ? '"' + s + '"' : s;
   	};
	for (var i in o) arr.push('"' + i + '":' + fmt(o[i]));
   	return '{' + arr.join(',') + '}';
}

/**
 * json数组转字符串
 */
function jsonArray2str(jsonArray) {
	if (jsonArray.length == 0) return "[]";
	var jsonArrayString = "[";
	for (var i = 0; i < jsonArray.length; i++) {
		jsonArrayString = jsonArrayString + json2str(jsonArray[i]) + ",";
	}
	jsonArrayString = jsonArrayString.substring(0, jsonArrayString.length - 1) + "]";
	return jsonArrayString;
}

/**
 * 获取服务器时间
 */
function getServerTime() {
	//因程序执行耗费时间,所以时间并不十分准确,误差大约在2000毫秒以下 
	var xmlHttp = false;
	//获取服务器时间 
	try {
		xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
	} catch (e) {
		try {
			xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
		} catch (e2) {
			xmlHttp = false;
		}
	}
	if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
		xmlHttp = new XMLHttpRequest();
	}
	xmlHttp.open("GET", "null.txt?n=" + new Date().getTime(), false);
	xmlHttp.setRequestHeader("Range", "bytes=-1");
	xmlHttp.send(null);
	return new Date(xmlHttp.getResponseHeader("Date"));
}

function FileInfo(id, name) {
	this.id = id;
	this.name = name;
}

/*****************************************************************************/
zy_DateFormat = {
	aDay: new Array("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"),
	aMonth: new Array("January","February","March","April","May","June","July","August","September","October","November","December"),
	formatDate: function(xdate){
		if(xdate == null || typeof(xdate) != "object")
			return "";
		var con = xdate.constructor;
		if (con != Date){
			try{
				return this.formatDate(xdate.value);
			}catch(e){
				return "";
			}
		}
		var result = "";
		result += xdate.getFullYear();
		result += "-";
		if(xdate.getMonth() < 9)
			result += "0";
		result += xdate.getMonth()+1;
		result += "-";
		if(xdate.getDate() < 10)
			result += "0";
		result += xdate.getDate();
		return result;
	},
	formatDateTime: function(xdate, sec){
		if(xdate == null || typeof(xdate) != "object")
			return "";
		var con = xdate.constructor;
		if (con != Date){
			try{
				return this.formatDateTime(xdate.value);
			}catch(e){
				return "";
			}
		}
		var result = "";
		result += xdate.getFullYear();
		result += "-";
		if(xdate.getMonth() < 9)
			result += "0";
		result += xdate.getMonth()+1;
		result += "-";
		if(xdate.getDate() < 10)
			result += "0";
		result += xdate.getDate();
		result += " ";
		if(xdate.getHours() < 10)
			result += "0";
		result += xdate.getHours();
		result += ":";
		if(xdate.getMinutes() < 10)
			result += "0";
		result += xdate.getMinutes();
		if (sec) {
			result += ":";
			if(xdate.getSeconds() < 10)
				result += "0";
			result += xdate.getSeconds();
		}
		return result;
	},
	formatDateWeek: function(xdate) {
		if(xdate == null || typeof(xdate) != "object")
		return "";
		var con = xdate.constructor;
		if (con != Date){
			try {
				return this.formatDate(xdate.value);
			} catch (e) {
				return "";
			}
		}
		var result = "";
		result += xdate.getFullYear();
		result += "年";
		if(xdate.getMonth() < 9)
			result += "0";
		result += xdate.getMonth()+1;
		result += "月";
		if(xdate.getDate() < 10)
			result += "0";
		result += xdate.getDate();
		result += "日";
		
		result += "  星期" + "天一二三四五六".charAt(xdate.getDay());
		return result;
	},
	parseDate: function(source){
		if(source == null || source.length < 7)
			return null;
		var year = parseInt(source.substr(0,4));
		var month = parseInt(source.substr(5,2));
		var day = parseInt(source.substr(8,2));
		var result = new Date(year, month-1, day);
		return result;
	},
	parseDateTime: function(source){
		if(source == null || source.length < 13)
			return null;
		var year = parseInt(source.substr(0,4));
		var month = parseInt(source.substr(5,2));
		var day = parseInt(source.substr(8,2));
		var hour = parseInt(source.substr(11,2));
		var minute = parseInt(source.substr(14,2));
		var result = new Date(year, month-1, day, hour, minute);
		return result;
	}
};

//获得日期yyyy-MM-dd
function getDateStr(AddDayCount) {
	var dd = new Date();
	dd.setDate(AddDayCount == 0 ? dd.getDate() : dd.getDate() + AddDayCount);
	var y = (dd.getFullYear() < 1900) ? (1900 + dd.getFullYear()) : dd.getFullYear();
	var m = dd.getMonth() + 1;
	var d = dd.getDate();
	return y + "-" + m + "-" + d; 
}
//替换字符（换行）
function strReplace(str){
	var t = "";
	t = str.replace(/\r\n/g,"\\n");
	return t;
}

//计算宽度
function countWidth(num) {
	return $(window).width() * num;
}
