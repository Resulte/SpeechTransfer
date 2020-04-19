<%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
    <title>在线语音合成</title>
    <script type="text/javascript" src="js/jquery-1.10.2.min.js"></script>
    <script src="iview/vue.min.js"></script>
    <link rel="stylesheet" href="iview/iview.css">
    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">

    <!-- jQuery (Bootstrap 的所有 JavaScript 插件都依赖 jQuery，所以必须放在前边) -->
    <script src="js/jquery-3.2.1.min.js"></script>
    <!-- 加载 Bootstrap 的所有 JavaScript 插件。你也可以根据需要只加载单个插件。 -->
    <script src="js/bootstrap.min.js"></script>

    <script src="iview/iview.min.js"></script>
    <style type="text/css">
        [v-cloak] {
            display: none;
        }

        html, body {
            width: 100%;
            height: 100%
        }

        body {
            background: url(image/2.jpg) no-repeat;
            position:fixed;
            top: 0;
            left: 0;
            background-size: cover;
            -webkit-background-size: cover;
            -o-background-size: cover;
            background-size: 100%;
            text-align: center;
        }

        div {
            width: 778px;
            margin: 0 auto
        }

        .container {
            max-width: 32rem;
            margin-left: auto;
            margin-right: auto;
        }

        .aplayer .aplayer-info .aplayer-music .aplayer-author {
            color: #fff;
        }

        .opt {
            font-size: x-large;
            color: white;
        }

        #select1 {
            padding: auto;
        }

        h1{
            color: white;
            /*font-family: Georgia, serif;*/
            font-weight:bold;
        }
    </style>
    <script>
        $(function () {
            var vm = new Vue({
                el: '#app',
                data: {
                    title: '在线语音合成',
                    message: '享受科技，热爱生活'
                },
                methods: {
                    convert: function () {
                        if (vm.message === "") {
                            vm.open("请输入文字");
                            return
                        }
                        var sel = document.getElementById("select1");
                        var index = sel.selectedIndex; // 选中索引
                        $.ajax({
                            url: "xunfeiConvert",
                            async: false,
                            type: 'post',
                            data: {'message': vm.message, 'index': index},
                            success: function (result) {
                                $("#audio").prop("src", result);
                                document.getElementById("audio").play();
                                //alert(result);
                            },
                            error: function () {
                                alert("网络异常");
                            }
                        });
                    }, open: function (nodesc) {
                        this.$Notice.open({
                            title: '温馨提示',
                            desc: nodesc
                        });
                    }
                }
            });
        });
    </script>
</head>
<body>
<div id="app" v-cloak>
    <div>
        <!--背景图片-->
        <div id="web_bg" style="background-image: url(image/2.jpg);"></div>

        <h1 v-text="title"></h1>
        <div class="opt">输入文本</div>
        <i-input v-model="message" type="textarea" :autosize="{minRows: 8,maxRows: 20}" placeholder="请输入..."></i-input>
    </div>
    <div style="margin-top:10px;" class="opt" >
        选择发音人<select id="select1" class="form-control">
        <option selected>讯飞小燕</option>
        <option>讯飞许久</option>
        <option>讯飞小萍</option>
        <option>讯飞小婧</option>
        <option>讯飞许小宝</option>
    </select>
    </div>
    <div style="margin-top:10px;">
        <i-button @click="convert" type="primary" class="btn btn-info">合成播放</i-button>
    </div>
    <div style="display: none">
        <audio id="audio" controls="controls" src=""></audio>
    </div>
</div>
</body>
</html>
