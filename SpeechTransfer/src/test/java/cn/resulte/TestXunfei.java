package cn.resulte;

import cn.resulte.xunfei.XunfeiToVoice;
import org.junit.Test;

import java.io.IOException;

public class TestXunfei {
    @Test
    public void WavTest() throws IOException {
        String data = "你好吗？我还好哦。";
        String path = XunfeiToVoice.changeToVoice(data,1);
        System.out.println(path);
    }
}
