package vn.cal.core;

import javax.persistence.EntityManager;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.zkoss.zk.ui.WebApps;

import vn.cal.model.Quyen;
import vn.cal.model.User;
import vn.cal.service.UserService;

@Configuration
@Controller
public class CoreObject<T> implements ApplicationContextAware {

	public static final String TT_DA_XOA = "da_xoa";
	public static final String TT_AP_DUNG = "ap_dung";
	public static final String TT_KHONG_AP_DUNG = "khong_ap_dung";
	
    private ApplicationContext appContext;
    private static CoreObject<?> env;
    
    public ApplicationContext ctx(){
        if(appContext==null){
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

    public static CoreObject<?> env() {
        if(env == null){
        	env = new CoreObject<>();
        }
        return env;
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
    
    public ServletRegistrationBean dispatcherServlet(String path){
        ServletRegistrationBean srb = new ServletRegistrationBean(new DispatcherServlet(), path);
        srb.setLoadOnStartup(1);
        return srb;
    }

    public FilterRegistrationBean characterEncodingFilter() {
        FilterRegistrationBean rs = new FilterRegistrationBean(new CharacterEncodingFilter());
        rs.setMatchAfter(true);
        rs.addInitParameter("encoding", "UTF-8");
        rs.addInitParameter("forceEncoding", "true");
        return rs;
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
    
    // SERVICES
    
    public UserService getUserService(){
        return new UserService();
    }
    
    
    // END SERVICES
    
    public User getUser(){
    	return getUserService().getUser(false);
    }
    
    public Quyen getQuyen(){
    	return getUser().getTaCaQuyen();
    }
}
