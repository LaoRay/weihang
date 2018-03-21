$(document).on('input propertychange', '#telInput', function (e) {
    if (e.type === "input" || e.orignalEvent.propertyName === "value") {
        if (/^1[3-9]{1}[0-9]{9}$/.test(this.value)) {
//          var myreg = /^1\d{10}$/;
//          if (!myreg.test(this.value)) {
//              return;
//          }
			$('#yzm').attr("disabled", false); 
            $('#yzm').addClass('yanzm-usable').removeClass('yanzm-disable');
        } else {
            $('#yzm').addClass('yanzm-disable').removeClass('yanzm-usable');
            $('#yzm').attr("disabled", true); 
        }
    }
})

$(document).ready(function(){
$('#yzm').attr("disabled", true); 
});