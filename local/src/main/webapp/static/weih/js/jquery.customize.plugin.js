(function($) {
	
	var columns; //需要显示的字段格式、信息
	var url = ''; //请求url
	var param = {}; //请求的参数
	var contentType = "application/x-www-form-urlencoded"; //请求类型
	var obj;
	
	$.fn.layuiDatagrid = function(opt, reloadParam) {
		//验证参数合法性
		if (!this.length) {
			return this;
		}
		
		obj = this;
		
		//设置方法
		if (typeof opt == 'string') {
			if (opt == 'reload') {
				if (typeof(reloadParam) == 'undefined'){
					ajaxLoadColumnsInfo(obj, url, param, columns, contentType);
				} else {
					ajaxLoadColumnsInfo(obj, url, reloadParam, columns, contentType);
				}
			}
		}
		
		if (typeof opt != 'object') {
			return this;
		}
		
		//设置初始值
		opt = $.extend(true, {
		}, opt);
		
		//初始化参数的值
		if (opt.columns!=undefined && opt.columns!="") {
			columns = opt.columns;
		}
		if (opt.url!=undefined && opt.url!="") {
			url = opt.url;
		}
		if (opt.param!=undefined && opt.param!="") {
			param = opt.param;
		}
		if (opt.contentType!=undefined && opt.contentType!="") {
			contentType = opt.contentType;
		}
		
		//加载数据
		if (opt.loadData!=undefined && opt.loadData!="") {
			//加载本地数据
			showColumnsInfo(obj, opt.loadData, columns);
		} else if (opt.url!=undefined && opt.url!="") {
			//加载远程数据
			ajaxLoadColumnsInfo(obj, url, param, columns, contentType);
		} else if (opt.reload!=undefined && opt.reload!="") {
			//reload方法，根据参数重新加载远程数据
			param = opt.reload;
			ajaxLoadColumnsInfo(obj, url, param, columns, contentType);
		}
	};
	
	//ajax加载显示列表信息
	function ajaxLoadColumnsInfo(obj, url, param, columns, contentType) {
		if (typeof param == 'object') {
			param = JSON.stringify(param);
		}
		$.ajax({
			url: url,
			type: 'post',
			data: param,
	    	dataType: 'json',
	    	contentType: contentType,
	    	beforeSend: function() {
	    		loadLayer = parent.layer.load(0, {shade: [0.5, '#fff'] });
	    	},
	    	success: function(data) {
	    		parent.layer.close(loadLayer);
	    		showColumnsInfo(obj, data, columns);
	    	}
		});
	}
	
	//加载显示列表信息
	function showColumnsInfo(obj, optColumnData, columns) {
		//初始化
		$(obj).addClass('layui-table');
		$(obj).empty();
		
		//构建thead
		var thead = '<thead><tr>';
		var fields = [];
		var aligns = [];
		var formatters = [];
		var showalls = [];
		$.each(columns, function(num, col) {
			var width = col.width==undefined || col.width==""?5:col.width;
			var title = col.title==undefined?"":col.title;
			var align = col.align==undefined?"center":col.align;
			var field = col.field==undefined || col.field==""?"noField":col.field;
			var showall = col.showall==undefined || col.showall==""?false:col.showall;
			var formatter = col.formatter==undefined || col.formatter==""?"noformatter":col.formatter;
			
			if (col.checkbox) {
				field = 'checkbox';
				thead += '<th width="10px" style="text-align: center;"><input onclick="layTableDatagridCheckSelectAll(this)" type="checkbox" id="layTableCheckAll"></input></th>';
			} else {
				if (typeof(width) == 'number') {
					thead += '<th width="'+width+'%" style="text-align: center;">'+title+'</th>';
				} else {
					thead += '<th width="'+width+'" style="text-align: center;">'+title+'</th>';
				}
			}
			fields[num] = field;
			aligns[num] = align;
			formatters[num] = formatter;
			showalls[num] = showall;
		});
		thead += '</tr></thead>';
		$(obj).append(thead);
		
		if (typeof(optColumnData) == 'string') {
			optColumnData = $.parseJSON(optColumnData);
		}
		
		//构建tbody
		var tbody = '';
		var layuiDatagrid_dataList;
		if (typeof(optColumnData.dataList) == 'undefined') {
			layuiDatagrid_dataList = optColumnData;
		} else {
			layuiDatagrid_dataList = optColumnData.dataList;
		}
		$.each(layuiDatagrid_dataList, function(num, fil) {
			tbody += '<tr>';
			$.each(fields, function(n, fils) {
				var td = "";
				if (formatters[n] != 'noformatter') {
					// 使用自定义的函数返回显示的td
					td = formatters[n](fil[fils], fil);
				} else {
					td = fil[fils];
				}
				if (fils != 'checkbox' && typeof(td) == 'undefined') {
					td = '';
				}
				if (fils == 'checkbox') {
					tbody += '<td><input onclick="layTableCheck(this)" objId="'+fil.uuid+'" class="laytablecheckbox" type="checkbox" /></td>';
				} else if (showalls[n]) {
					tbody += '<td class="layui-'+aligns[n]+'" title="'+td+'">'+td+'</td>';
				} else {
					tbody += '<td class="layui-'+aligns[n]+'">'+td+'</td>';
				}
			});
			tbody += '</tr>';
		});
		$(obj).append('<tbody>'+tbody+'</tbody>');
		
		//显示分页
		if ($('#pagination').length == 1) {
			if (typeof(optColumnData.rows) != 'undefined') {
				$('#pagination').pagination({
					total: optColumnData.rows,
					pageSize: optColumnData.pageSize,
					pageNumber: optColumnData.pageIndex + 1,
					beforePageText: '第',
					afterPageText: '页，共 {pages} 页',
					displayMsg: '当前显示 {from} 到 {to} ，共 {total} 条记录',
					onSelectPage: function(pageNumber, pageSize) {
						//onSelectPage(pageNumber - 1, pageSize);
						param.pageIndex = pageNumber - 1;
						param.pageSize = pageSize;
						ajaxLoadColumnsInfo(obj, url, param, columns, contentType);
					}
				});
			}
		}
	}
	
	//将form表单属性转成json对象
	$.fn.serializeJsonObject = function(){
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	};
	
	//将form表单属性转成json字符转
	$.fn.serializeJsonStr = function(){
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    if (typeof o == 'object') {
			o = JSON.stringify(o);
		}
	    return o;
	};
	
	//自定义加载显示值
	$.fn.customizeLoadValue = function(opt) {
		//验证参数合法性
		if (!this.length) {
			return this;
		}
		
		obj = this;
		
		if (typeof opt != 'object') {
			return this;
		}
		
		//设置初始值
		opt = $.extend(true, {
		}, opt);
		
		var loadValueParam = opt.param;
		if (typeof loadValueParam != 'object') {
			loadValueParam = $.parseJSON(loadValueParam);
		}
		
		$(obj).find('.loadValue').each(function(){
			var value = loadValueParam[$(this).attr('loadValue')];
			if (value == 'NULL' || typeof(value) == 'undefined') {
				value = '';
			}
			//TODO 还有其他情况
			if ($(this).is("input")) {
				$(this).val(value);
			} else {
				$(this).html(value);
			}
		});
	};
	
})(jQuery);
