package vn.cal.service;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.math.NumberUtils;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;

import com.querydsl.jpa.impl.JPAQuery;

import vn.cal.core.QUser;
import vn.cal.core.Service;
import vn.cal.model.User;

@Init(superclass=true)
public class UserService extends Service<User> {

    /**
     * @param isSave
     * @return User hien tai
     */
    public User getUser(boolean isSave){
        User user = null;
        if(Executions.getCurrent()!=null){
            String key = getClass() + "." + User.class;
            user = (User) Executions.getCurrent().getAttribute(key);
            if(user==null || user.noId()){
                HttpServletRequest req = (HttpServletRequest) Executions.getCurrent().getNativeRequest();
                Object token = null;
                Cookie[] cookies = req.getCookies();
                if(cookies!=null){
                    for (Cookie c : cookies) {
                        if("email".equals(c.getName())){
                            token = c.getName();
                            break;
                        }
                    }
                }
                if(token==null){
                    HttpSession ses = req.getSession();
                    token = ses.getAttribute("email");
                }
                if(token!=null){
                    String[] parts = new String(Base64.decodeBase64(token.toString())).split(":");
                    User userLogin = em().find(User.class, NumberUtils.toLong(parts[0], 0));
                    if(parts.length==3 && userLogin!=null){
                        long expire = NumberUtils.toLong(parts[1], 0);
                        if(expire > System.currentTimeMillis() && token.equals(userLogin.getCookieToken(expire))){
                            user = userLogin;
                        }
                    }
                }
                if(!isSave && (user==null || user.getTrangThai()==TT_DA_XOA)){
                    if(user==null){
                        bootstrapUser();
                    }
                    user = new User();
                    if(token!=null){
                       req.getSession().removeAttribute("email"); 
                    }
                    StringBuilder url = new StringBuilder();
                    url.append(req.getScheme())                                         // http (https)
                        .append("://")                                                  // ://
                        .append(req.getServerName());                                   // localhost (domain name)
                    if(req.getServerPort()!=80 && req.getServerPort()!=443){            
                        url.append(":").append(req.getServerPort());                    // app name
                    }
                    Executions.sendRedirect(url + req.getContextPath() + "/dangnhap");
                }
                Executions.getCurrent().setAttribute(key, user);
            }
        }
        return isSave && user != null && user.noId() ? null : user;
    }

    public void bootstrapUser() {
        JPAQuery<User> q = new JPAQuery<User>(em()).from(QUser.user)
                .where(QUser.user.daXoa.isFalse())
                .where(QUser.user.trangThai.eq(TT_AP_DUNG));
        if(q.fetchCount()==0){
            final User user = new User("test@liferay.com", "Super Addmin");
            user.updatePassword("1");
            user.save();
        }
    }
    
    @Command
    public void login(@BindingParam("email")final String email, @BindingParam("password")final String password){
        User user = new JPAQuery<User>(em()).from(QUser.user)
                .where(QUser.user.daXoa.isFalse())
                .where(QUser.user.trangThai.ne(TT_DA_XOA))
                .where(QUser.user.email.eq(email))
                .fetchFirst();
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        if(user!=null && encryptor.checkPassword(password.trim() + user.getSalkey(), user.getMatKhau())){
            String cookieToken = user.getCookieToken(System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS));
            Session zkSession = Sessions.getCurrent();
            zkSession.setAttribute("email", cookieToken);
            HttpServletResponse res = (HttpServletResponse) Executions.getCurrent().getNativeResponse();
            Cookie cookie = new Cookie("email", cookieToken);
            cookie.setPath("/");
            cookie.setMaxAge(1000000000);
            res.addCookie(cookie);
            Executions.sendRedirect("/");
        } else {
            Clients.showNotification("Đăng nhập không thành công", "error", null, "top_center", 5000);
        }
    }
    
}
