package cn.resulte.xunfei;

import com.iflytek.cloud.speech.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.mockito.internal.util.StringUtil;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class XunfeiToVoice {

    private static String[] speecher = {"xiaoyan","aisjiuxu","aisxping","aisjinger","aisbabyxu"};

    public static String changeToVoice(String data, int index) throws IOException {

        SpeechUtility.createUtility("appid=5e86f2a8"); // appid ：官网创建的应用的id

        //合成监听器
        SynthesizeToUriListener synthesizeToUriListener = XunfeiLib.getSynthesize();
        // 默认是生成 pcm 文件，所以后面有一步是转换成 wav 文件才能正常播放
        String fileName= XunfeiLib.getFileName("tts_test.pcm");
        XunfeiLib.delDone(fileName);

        //1.创建SpeechSynthesizer对象
        SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );
        //2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, speecher[index]);//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速，范围0~100
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置语调，范围0~100
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100

        //3.开始合成
        //设置合成音频保存位置（可自定义保存位置），默认保存在“./tts_test.pcm”
        mTts.synthesizeToUri(data,fileName ,synthesizeToUriListener);

        //设置最长时间
        int timeOut=30;
        int star=0;

        //校验文件是否生成
        while(!XunfeiLib.checkDone(fileName)){

            try {
                Thread.sleep(1000);
                star++;
                if(star>timeOut){
                    throw new Exception("合成超过"+timeOut+"秒！");
                }
            } catch (Exception e) {
                // TODO 自动生成的 catch 块
                log.error("Exception: {}",e.getMessage());
                return e.getMessage();
            }

        }

        String wavPath = fileName.replaceAll(".pcm",".wav");
        pcmToWav(fileName,wavPath);

        File file = new File(wavPath);
        FileInputStream  input = new FileInputStream(file);
        MultipartFile multipartFile =new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
        String filePath = saveVoice(multipartFile);
        deleteFile(wavPath);
        deleteFile(fileName);
        return filePath;


    }
    // 保存到服务器上的地址
    private static String saveVoice(MultipartFile  voiceFile) throws IOException {

        Date date = new Date();
        //log.info("wikiVoice: {}", ResourceUtil.wikiVoice());
        String dirPath = ("\\voice"+ new SimpleDateFormat("yyyyMMdd").format(date));
        File dir = new File("D:\\IdeaProjects\\SpeechTransfer\\src\\main\\webapp\\data\\file" + dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //Runtime.getRuntime().exec("chmod -R 777 " + dir);
        String filePath = dirPath + "\\" + UUID.randomUUID().toString().replace("-", "") + ".wav";
        File file = new File("D:\\IdeaProjects\\SpeechTransfer\\src\\main\\webapp\\data\\file"+ filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        //Runtime.getRuntime().exec("chmod -R 777 " + file);
        voiceFile.transferTo(file);
        return "data\\file"+filePath;

    }


    private static void deleteFile(String fileName){
        File file = new File(fileName);
        XunfeiLib.delDone(fileName);
        file.deleteOnExit();
    }
    private static void pcmToWav(String src, String target) throws IOException {

        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(target);

        // 计算长度
        byte[] buf = new byte[1024 * 4];
        int size = fis.read(buf);
        int PCMSize = 0;
        while (size != -1) {
            PCMSize += size;
            size = fis.read(buf);
        }
        fis.close();

        // 填入参数，比特率等等。这里用的是16位单声道 8000 hz
        WaveHeader header = new WaveHeader();
        // 长度字段 = 内容的大小（PCMSize) + 头部字段的大小(不包括前面4字节的标识符RIFF以及fileLength本身的4字节)
        header.fileLength = PCMSize + (44 - 8);
        header.FmtHdrLeth = 16;
        header.BitsPerSample = 16;
        header.Channels = 1;
        header.FormatTag = 0x0001;
        header.SamplesPerSec = 16000;
        header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
        header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
        header.DataHdrLeth = PCMSize;

        byte[] h = header.getHeader();

        assert h.length == 44; // WAV标准，头部应该是44字节
        // write header
        fos.write(h, 0, h.length);
        // write data stream
        fis = new FileInputStream(src);
        size = fis.read(buf);
        while (size != -1) {
            fos.write(buf, 0, size);
            size = fis.read(buf);
        }
        fis.close();
        fos.close();
        log.info("Convert OK!");

    }



}