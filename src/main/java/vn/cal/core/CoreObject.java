package vn.cal.core;

import javax.persistence.EntityManager;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.WebApps;

@Configuration
@Controller
public class CoreObject<T> implements ApplicationContextAware {

    private static ApplicationContext appContext;
    private static CoreObject<?> env;
    
    
    public ApplicationContext ctx(){
        System.out.println("get ctx");
        if(appContext==null){
            System.out.println("ctx null, get from current");
            appContext = WebApplicationContextUtils.getWebApplicationContext(WebApps.getCurrent().getServletContext());
        }
        return appContext;
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(appContext==null){
            appContext = applicationContext;
        }
    }

    public CoreObject<?> env() {
        assert env != null;
        return env;
    }

    public void setEnv() {
        if(env==null){
            CoreObject.env = this;
        }
    }
    
    public EntityManager em(){
        return ctx().getBean(EntityManager.class);
    }
    
    public PlatformTransactionManager transactionManager(){
        return ctx().getBean(PlatformTransactionManager.class);
    }
    
    public TransactionTemplate transactionTemplate(){
        return new TransactionTemplate(transactionManager());
    }

    @RequestMapping(value = "/dangnhap")
    public String dangnhap() {
        return "forward:/WEB-INF/zul/login.zul";
    }
    
    
    
}
