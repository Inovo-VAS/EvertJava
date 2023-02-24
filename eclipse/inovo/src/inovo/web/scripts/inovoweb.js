var mainformid="";

function inovo_action(actionelem){
	
	var urlparams="";
	
	if(actionelem.attr("urlparams")!=undefined){
		urlparams=actionelem.attr("urlparams");
	}
	
	var enableblockui=true;
	if(actionelem.attr("enableblockui")!=undefined){
		if(actionelem.attr("enableblockui")=="true"){
			enableblockui=true;
		} else {
			enableblockui=false;
		}
	}
	
	var callbackurl="?";
	
	var command="";	
	if(actionelem.attr("command")!=undefined){
		command=actionelem.attr("command");
	}
	
	if(command!=""){
		callbackurl+="command="+command+"&";
	}
	
	var formid=mainformid;	
	if(actionelem.attr("formid")!=undefined){
		if(actionelem.attr("formid")!="") formid=actionelem.attr("formid");
	}
	
	var widget="";	
	if(actionelem.attr("widget")!=undefined){
		widget=actionelem.attr("widget");
	}
	
	if(widget!=""){
		callbackurl+="widget="+widget+"&";
	}
	
	if(urlparams!=""){
		callbackurl+=urlparams;
	}
	
	var actiontarget="";	
	if(actionelem.attr("actiontarget")!=undefined){
		actiontarget=actionelem.attr("actiontarget");
		callbackurl+="&actiontarget="+actiontarget+"&";
	}
	webActionRequest(callbackurl, formid, actiontarget,enableblockui);
}

function webActionRequest(callbackurl,formid,actiontarget,enableblockui){
	if(enableblockui==undefined){
		enableblockui=true;
	}
	if(enableblockui) {
		$.blockUI({ 
			message : '<span style="font-size:1.2em" id="showprogress">Please wait ...</span>',
			css: { 
			border: 'none', 
	        padding: '15px', 
	        backgroundColor: '#000', 
	        '-webkit-border-radius': '10px', 
	        '-moz-border-radius': '10px', 
	        opacity: .7, 
	        color: '#fff'
	    } });
	}
	if(formid=="mainform") formid=mainformid;
	if(formid!=""){
		formid="#"+formid;
		
		$(formid).attr("method","POST");
		$(formid).attr("action",callbackurl);
		$(formid).attr("enctype","multipart/form-data");
		
		var percentageelem=$("#showprogress");
		
		$(formid).ajaxSubmit({
			beforeSubmit: function(formData, jqForm, options) {
				return true;
			},
			success: function(responseText, statusText, xhr, $form){
				if(enableblockui) {
					$.unblockUI();
				}
				if(xhr.getResponseHeader("Content-Disposition")==null){
					processActionResponse(responseText,actiontarget);
				} else {
					var contentdisposition=(""+xhr.getResponseHeader("Content-Disposition")).trim();
					if (contentdisposition.indexOf("attachment;")>-1) {
						contentdisposition=contentdisposition.substr(contentdisposition.indexOf("attachment;")+"attachment;".length).trim();
						if (contentdisposition.indexOf("filename=")>-1) {
							contentdisposition=contentdisposition.substr(contentdisposition.indexOf("filename=")+"filename=".length).trim();
							contentdisposition=contentdisposition.replace(/"/i,"")
							contentdisposition=contentdisposition.replace(/"/i,"")
						}
						safeData(responseText,contentdisposition,"text/csv");
					}
				}
			},
			uploadProgress: function(event,position,total,percentComplete){
				if(enableblockui) {
					percentageelem.html("Please wait... [ "+percentComplete+"% ]");
				}
			},
			error: function(){
				if(enableblockui){
					$.unblockUI();
				}
			}
		});		
	}
	else{
		
		var jqxhr = $.ajax({
			  type: "POST",
			  url: callbackurl,
			  dataType: "html"
			});
		
		jqxhr.done(function(data, textStatus, jqXHR) {
			if(enableblockui) {
				$.unblockUI();
			}
			processActionResponse(data,actiontarget);
		});
		jqxhr.fail(function(jqXHR, textStatus, errorThrown) { $.unblockUI(); });
		jqxhr.always(function(jqXHR) {  });
	}
}

function processActionResponse(data,actiontarget){
	if(actiontarget!=""){
		$("#"+actiontarget).html("");
		$("#"+actiontarget).html(data);
		data="";
	}
	while(data.length>0){
		if(data.indexOf("replacecomponent||")>-1){
			data=data.slice(data.indexOf("replacecomponent||")+"replacecomponent||".length);
		
			if(data.indexOf("||")>-1){
				replacewidgetid="#"+data.substring(0,data.indexOf("||"));
				data=data.slice(data.indexOf("||")+"||".length);
				if(data.indexOf("||replacecomponent")>-1){
					$(replacewidgetid).html("");
					$(replacewidgetid).html(data.substring(0,data.indexOf("||replacecomponent")));
					data=data.slice(data.indexOf("||replacecomponent")+"||replacecomponent".length);
				}
				else{
					break;
				}
			}
			else{
				break;
			}
		}
	}
}

function safeData(data, fileName,contentType) {
    var a = document.createElement("a");
    document.body.appendChild(a);
    a.style = "display: none";
   
            blob = new Blob([data], {type: contentType}),
            url = window.URL.createObjectURL(blob);
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
    
}

function checkCheckBoxes(selectnamespace){$(selectnamespace).prop('checked', true);}

function uncheckCheckBoxes(selectnamespace){$(selectnamespace).prop('checked', false);}

$.fn.inovoActionTabs=function(){
	$(this).tabs({
		activate: function(event,ui){ inovo_action(ui.newTab); }
	});
}

$.fn.inovoAction=function(){	
	var nextactionelem=$(this);
	
	var hasstarticon=false;
	
	if(nextactionelem.attr("starticon")==undefined){
		nextactionelem.attr("starticon","");
	}
	else{
		hasstarticon=true;
	}
	
	var hasendicon=false;
	
	if(nextactionelem.attr("endicon")==undefined){
		nextactionelem.attr("endicon","");
	}
	else{
		hasendicon=true;
	}
	
	if(hasstarticon&&!hasendicon){
		nextactionelem.button({
			text: (nextactionelem.html()!=""),
			icons: {
				primary: nextactionelem.attr("starticon")
				//,secondary: nextactionelem.attr("endicon")
			}
		});
	}
	else if(!hasstarticon&&hasendicon){
		nextactionelem.button({
			text: (nextactionelem.html()!=""),
			icons: {
				//primary: nextactionelem.attr("starticon")
				//,
				secondary: nextactionelem.attr("endicon")
			}
		});
	}
	else if(hasstarticon&&hasendicon){
		nextactionelem.button({
			text: (nextactionelem.html()!=""),
		
			icons: {
				primary: nextactionelem.attr("starticon")
				,secondary: nextactionelem.attr("endicon")
			}
		});
	}
	else if(!hasstarticon&&!hasendicon){
		nextactionelem.button();
	}
	
	nextactionelem.click(function(event){
		inovo_action(nextactionelem);
	});
};

$( document ).ready(function() {
	
});
