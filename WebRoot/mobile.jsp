<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>即拍即传-直播</title>
	<style>
		.info-holder {
		    background: #f5f5f5 none repeat scroll 0 0;
		    color: #838383;
		    font-size: 12px;
		    height: 33px;
		    padding: 3px 10px 2px 0;
		    position: relative;
		    text-align: right;
		}
		.info-holder .icon-area {
		    float: left;
		    line-height: 20px;
		    margin-top: 7px;
		    min-width: 67px;
		    position: relative;
		    text-align: left;
		    width: auto !important;
		}
		.info-holder .icon-area.good-area {
		    background: rgba(0, 0, 0, 0) url("images/good-btn.png") no-repeat scroll 0 -101px;
		    cursor: default;
		    height: 23px;
		    line-height: 23px;
		    margin: 5px 0 0 141px;
		}
		.info-holder .icon-area.un-good {
		    background: rgba(0, 0, 0, 0) url("images/good-btn.png") no-repeat scroll 0 -50px;
		    cursor: pointer;
		    height: 23px;
		    line-height: 23px;
		    margin: 5px 0 0 141px;
		}
		.info-holder .icon-area.fav-area {
		    background: rgba(0, 0, 0, 0) url("images/good-btn.png") no-repeat scroll 0 0;
		    cursor: pointer;
		    height: 23px;
		    line-height: 23px;
		    margin-left: 5px;
		    margin-top: 5px;
		}
		.info-holder .icon-area.icon-sta {
		    background: rgba(0, 0, 0, 0) url("images/good-btn.png") no-repeat scroll 0 -149px;
		}
		.info-holder .icon-area.icon-fav {
		    background: rgba(0, 0, 0, 0) url("images/good-btn.png") no-repeat scroll 0 0;
		}
		.info-holder .icon-area .icon {
		    background: rgba(0, 0, 0, 0) url("images/white-icon.png") no-repeat scroll 0 11px;
		    float: left;
		    height: 20px;
		    margin-right: 10px;
		    width: 19px;
		}
		.info-holder .icon-area .icon.icon-visit {
		    background-position: -16px -12px;
		}
		.info-holder .icon-area .icon.icon-say {
		    background-position: -142px -12px;
		    margin: 0 3px 0 7px;
		}
	</style>
</head>

<body style="background-color: #fff;">
	<div style="width: 100%; height: auto;margin-bottom: 10px;text-align:center;">
	<h3>即拍即传视频直播</h3>
	<!-- <img src="images/M1_test11.jpg" /> -->
	</div>
	<%-- 
	<!--html5标签-->
	<video style="margin:-7px -10px 0 0;width: 100%;height: auto;" id="my-video" controls="controls" autoplay="autoplay" 
		poster="images/default_230_130.png">
		<!-- <source src="http://101.95.49.76:8080/WsMobile/plugin/M1_test11.mp4" type='video/mp4'> -->
		<source src="${hlsUrl}" type='video/mp4'>
	</video> --%>
	<!-- flowplayer播放器 -->
	<video controls width="100%" height="auto">
       <source type="application/x-mpegurl"
               src="http://61.152.231.61:8080/dxys001.m3u8">
   </video>
	<div class="info-holder">
		<div class="icon-area" style="margin-left: 20px;">
			<span class="icon icon-visit"></span> 
			<span class="visitNum" id="curVisit">${records }</span>
		</div>
		<div class="icon-area un-good" id="sayGood">
			<span class="icon icon-good"></span> 
			<span class="goodNum" id="curGood">11</span> 
			<span class="tipGood" style="position: absolute; left: 4px; top: -1px; z-index: 9999; color: #4baef4; font-weight: bold; display: none;">+1</span>
		</div>
	</div>
	<div style="width: 100%; min-height: 120px;margin-top:5px;border: 1px;word-break: break-all;">
		<span style="line-height: 1.6;text-indent: 2em;word-break: break-all;">1111111111111111111111111111111111111111111</span>
	</div>
	<div style="width: 100%; height: auto;margin-top:5px;border: 1px ;text-align:center;">
			<!-- <p style="text-align: left;width: 14%;margin: auto;color: red;"> -->
			<p style="color: red;">
				Come With Us~<br />
				客服热线：400-920-8566<br />
				地址：上海杨高南路5788号<br />
				邮编：200122<br />
				传真：021-68733629<br />
				邮箱：spzx@sttri.com.cn
			</p>
		<img src="images/0.jpg" />
	</div>
	<script type="text/javascript" src="js/jquery.js"></script>
	<script type="text/javascript" src="js/flowplayer.min.js"></script>
	<script>
		$('.browser').text(window.navigator.userAgent.toString());
		$('.mime').text(document.createElement('video').canPlayType('application/x-mpegurl') || '(empty string)');
	</script>
	<script type="text/javascript">
		$(function(){
			//判断用户在该IP下是否已经点过赞
			if(${hasGood}){
				$("#sayGood").removeClass("un-good").addClass("good-area").off("click");
			}
			
        });
        
        //万位以上数字转换
	    function numTrans(int){
	        var f = 0;
	        if( int > 9999 ){
	            f =(Math.round(int/10000*10)/10).toFixed(1)+'万'
	        }else{
	            f = int;
	        }
	        return f;
	    }
		
		//点赞
	    $("#sayGood").on("click", function () {
	        var $this = $(this);
	        $this.attr("disabled","disabled");
	        $.ajax({
	            url: "addLike",
	            type: 'post',
	            data: {videoId:${id}},
	            success: function (data) {
	                var resultCode = data.retCode;
	                $this.removeAttr("disabled");
	                if (resultCode == 0) {
	                    var $good = $('#curGood');
	                    $('.tipGood').show();
	                    $('.tipGood').animate({
	                        'top': '-14',
	                        'left': '8'
	                    }, 600, function () {
	                        $('.tipGood').fadeOut(function () {
	                            $('.tipGood').css('top', '-14px')
	                        });
	                        var num = Number(${video.goodcount});
	                        if(num < 10000){
	                            num++;
	                            $good.text(numTrans(num));
	                        }
	                        $('#sayGood').removeClass('un-good').addClass('good-area').off('click');
	                    });
	                }else if(resultCode == 1){
	                	alert("重复点赞");
	                    $('#sayGood').removeClass('un-good').addClass('good-area').off('click');
	                }else{
	                	alert("点赞失败");
	                	$this.removeAttr("disabled");
	                }
	            },
	            error: function (data) {
	                $this.removeAttr("disabled");
	                alert('操作失败，请检查网络连接！');
	            }
	        });
	    });
	    
	</script>
</body>
</html>
