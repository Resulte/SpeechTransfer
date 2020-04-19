package cn.resulte.controller;

import cn.resulte.xunfei.XunfeiToVoice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String index()  {
        return "index";
    }

    @ResponseBody
    @PostMapping("/xunfeiConvert")
    public String xunfeiConvert(HttpServletRequest request, HttpServletResponse response){

        String message = request.getParameter("message");
        String index = request.getParameter("index");
        System.out.println(index);

        String path=null;
        try {
            path = XunfeiToVoice.changeToVoice(message,Integer.parseInt(index));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(path);
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        /*try {
            PrintWriter out = response.getWriter();
            out.println(path);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return path;

    }

}
