package cn.bugstack.xfg.dev.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
@RestController()
@RequestMapping("/api/")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * http://localhost:8091/api/test
     * http://117.72.37.243:8091/api/test
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseBodyEmitter test(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        String[] words = new String[]{"嗨，臭宝。\r\n", "恭喜💐 ", "你的", " Jenkins ", "部", "署", "测", "试", "成", "功", "了啦🌶！", "\r\nBy 小傅哥 https://bugstack.cn"};
        new Thread(() -> {
            for (String word : words) {
                try {
                    emitter.send(word);
                    Thread.sleep(250);
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        return emitter;
    }

    /**
     * http://localhost:8091/api/api2
     * @return
     */
    @RequestMapping(value = "/api2", method = RequestMethod.GET)
    public String api2() {
        return "hi";
    }

}
