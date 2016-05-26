package vn.cal.core;

import javax.persistence.EntityManager;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.WebApps;

import vn.cal.service.UserService;

public class CoreObject<T> implements ApplicationContextAware {

    public final static String TT_DA_XOA = Labels.getLabel("trangthai.daxoa");
    public final static String TT_AP_DUNG = Labels.getLabel("trangthai.apdung");
    public final static String TT_KHONG_AP_DUNG = Labels.getLabel("trangthai.khongapdung");
    
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
    
    // SERVICES
    
    public UserService getUserService(){
        return new UserService();
    }
    
    
    // END SERVICES
}
