package vn.cal.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
@Controller
public class App extends CoreObject<Object> {

    public static App instance;
    
    public App(){
        super();
        setEnv();
        instance = this;
    }
    
    @RequestMapping(value = "/dangnhap")
    public String dangnhap() {
        return "forward:/WEB-INF/zul/login.zul";
    }
    
    @RequestMapping(value = "/{path:.+$}")
    public String index(@PathVariable String path) {
        System.out.println("path:"+path);
        return "forward:/WEB-INF/zul/home.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path + "/list.zul";
    }
}
