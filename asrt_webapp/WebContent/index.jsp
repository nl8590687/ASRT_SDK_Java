<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>ASRT Web Client Demo</title>
</head>
<body>
<div id="mainpage">

	<h1><center>ASRT语音识别WebApp演示页面</center></h1>
	<div id="page_text" style="
    padding-left: 5%;
    padding-right: 5%;
    padding-top: 10px;
    padding-bottom: 10px;
    min-height: 600px;
">
		<textarea id="textbox" style="max-width: 100%;min-width: 100%;min-height:580px;font-size: 84px;font-family: inherit;"></textarea>
	</div>
	<div id="ctrl_btns">
		<center>
		<button id="btn_start_record">开始录制</button>

		<button id="btn_end_record">停止录制</button>

		<button id="btn_upload">上传识别</button>
		</center>
	</div>
	
	<div id="wav_pannel">
	</div>

</div>


<!-- jQuery -->
<script src="plugins/jQuery/jquery.min.js"></script>
<script src="plugins/Recorder/recorder-core.js"></script> <!--必须引入的录音核心，CDN: https://cdn.jsdelivr.net/gh/xiangyuecn/Recorder@latest/dist/recorder-core.js-->

<script src="plugins/Recorder/engine/wav.js"></script> <!--相应格式支持文件；如果需要多个格式支持，把这些格式的编码引擎js文件放到后面统统加载进来即可-->

<script src="plugins/Recorder/extensions/waveview.js"></script>  <!--可选的扩展支持项-->
<script>
var blob_wav_current;
//简单控制台直接测试方法：在任意(无CSP限制)页面内加载Recorder，加载成功后再执行一次本代码立即会有效果，import("https://xiangyuecn.github.io/Recorder/recorder.mp3.min.js").then(function(s){console.log("import ok")}).catch(function(e){console.error("import fail",e)})

var rec;
/**调用open打开录音请求好录音权限**/
var recOpen=function(success){//一般在显示出录音按钮或相关的录音界面时进行此方法调用，后面用户点击开始录音时就能畅通无阻了
    rec=Recorder({
        type:"wav",sampleRate:16000,bitRate:16 //wav格式，指定采样率hz、比特率kbps，其他参数使用默认配置；注意：是数字的参数必须提供数字，不要用字符串；需要使用的type类型，需提前把格式支持文件加载进来，比如使用wav格式需要提前加载wav.js编码引擎
        ,onProcess:function(buffers,powerLevel,bufferDuration,bufferSampleRate,newBufferIdx,asyncEnd){
            //录音实时回调，大约1秒调用12次本回调
            //可利用extensions/waveview.js扩展实时绘制波形
            //可利用extensions/sonic.js扩展实时变速变调，此扩展计算量巨大，onProcess需要返回true开启异步模式
        }
    });

    //var dialog=createDelayDialog(); 我们可以选择性的弹一个对话框：为了防止移动端浏览器存在第三种情况：用户忽略，并且（或者国产系统UC系）浏览器没有任何回调，此处demo省略了弹窗的代码
    rec.open(function(){//打开麦克风授权获得相关资源
        //dialog&&dialog.Cancel(); 如果开启了弹框，此处需要取消
        //rec.start() 此处可以立即开始录音，但不建议这样编写，因为open是一个延迟漫长的操作，通过两次用户操作来分别调用open和start是推荐的最佳流程
        
        success&&success();
    },function(msg,isUserNotAllow){//用户拒绝未授权或不支持
        //dialog&&dialog.Cancel(); 如果开启了弹框，此处需要取消
        console.log((isUserNotAllow?"UserNotAllow，":"")+"无法录音:"+msg);
    });
};

/**开始录音**/
function recStart(){//打开了录音后才能进行start、stop调用
    rec.start();
};

/**结束录音**/
function recStop(){
    rec.stop(function(blob,duration){
        console.log(blob,(window.URL||webkitURL).createObjectURL(blob),"时长:"+duration+"ms");
        rec.close();//释放录音资源，当然可以不释放，后面可以连续调用start；但不释放时系统或浏览器会一直提示在录音，最佳操作是录完就close掉
        rec=null;
        
        blob_wav_current = blob;
        //已经拿到blob文件对象想干嘛就干嘛：立即播放、上传
        
        /*** 【立即播放例子】 ***/
        //var audio=document.createElement("audio");
        //audio.controls=true;
        //document.body.appendChild(audio);
        //简单利用URL生成播放地址，注意不用了时需要revokeObjectURL，否则霸占内存
        //audio.src=(window.URL||webkitURL).createObjectURL(blob);
        //audio.play();
    },function(msg){
        console.log("录音失败:"+msg);
        rec.close();//可以通过stop方法的第3个参数来自动调用close
        rec=null;
    });
};


//我们可以选择性的弹一个对话框：为了防止移动端浏览器存在第三种情况：用户忽略，并且（或者国产系统UC系）浏览器没有任何回调
/*伪代码：
function createDelayDialog(){
    if(Is Mobile){//只针对移动端
        return new Alert Dialog Component
            .Message("录音功能需要麦克风权限，请允许；如果未看到任何请求，请点击忽略~")
            .Button("忽略")
            .OnClick(function(){//明确是用户点击的按钮，此时代表浏览器没有发起任何权限请求
                //此处执行fail逻辑
                console.log("无法录音：权限请求被忽略");
            })
            .OnCancel(NOOP)//自动取消的对话框不需要任何处理
            .Delay(8000); //延迟8秒显示，这么久还没有操作基本可以判定浏览器有毛病
    };
};
*/

$("#btn_start_record").click(function(event){
	//这里假设立即运行，只录6秒，录完后立即播放，本段代码copy到控制台内可直接运行
	recOpen(function(){
	    recStart();
	    setTimeout(recStop,6000);
	});
})

$("#btn_end_record").click(function(event){
	recStop();
})

$("#btn_upload").click(function(event){
	//alert('upload');
	var blob = blob_wav_current;
	var TestApi="upload";//用来在控制台network中能看到请求数据，测试的请求结果无关紧要
	
	//使用jQuery封装的请求方式，实际使用中自行调整为自己的请求方式
	//录音结束时拿到了blob文件对象，可以用FileReader读取出内容，或者用FormData上传
	var api=TestApi;

	/***将blob文件转成base64纯文本编码，使用普通application/x-www-form-urlencoded表单上传***/
	var reader=new FileReader();
	reader.onloadend=function(){
	    $.ajax({
	        url:api //上传接口地址
	        ,type:"POST"
	        ,data:{
	            mime:blob.type //告诉后端，这个录音是什么格式的，可能前后端都固定的mp3可以不用写
	            ,upfile_b64:(/.+;\s*base64\s*,\s*(.+)$/i.exec(reader.result)||[])[1] //录音文件内容，后端进行base64解码成二进制
	            //...其他表单参数
	        }
	        ,success:function(v){
	            console.log("上传成功",v);
	            document.getElementById("textbox").innerHTML += v;
	        }
	        ,error:function(s){
	            console.error("上传失败",s);
	        }
	    });
	};
	reader.readAsDataURL(blob);

})

</script>
</body>
</html>