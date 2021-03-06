# SpeechTransfer
基于SpringBoot+讯飞SDK的简易在线语音合成系统

### 效果展示

![image](https://github.com/Resulte/SpeechTransfer/blob/master/img/show.jpg)

### 功能展示

文本输入框中输入文本，选择不同发音人，点击合成播放后即可自动播放合成语音。

发音人的选择有5种，具有不同的语音效果。

### 技术概要

点击合成播放后，将输入文本和发音人作为参数信息，发送一个异步请求到服务端，服务端收到后，使用讯飞提供的语音合成SDK将文本转换为PCM文件，然后再将PCM文件转为WAV文件，在服务器后端播放，数据流传回客户端。

朋友们在自己测试时，需要去讯飞申请一个语音合成SDK，放入自己的项目中，并且将appid改成自己的。

