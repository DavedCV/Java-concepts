package com.davidcv.springboot.firstwebapp.hello;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SayHelloController {
    @RequestMapping(path = "sayHello")
    @ResponseBody
    public String sayHello() {
        return "Hello! What are you learning?";
    }

    @RequestMapping(path = "sayHelloHtml")
    @ResponseBody
    public String sayHelloHtml() {
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>My First HTML Page</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("My First <strong>HTML</strong> page with body");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    @RequestMapping(path = "sayHelloJsp")
    public String sayHelloJsp() {
        return "sayHello";
    }
}
