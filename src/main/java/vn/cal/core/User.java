package vn.cal.core;

import java.util.logging.Logger;

import javax.persistence.Entity;

@Entity
public class User extends Model<User> {
    public static transient final Logger log = Logger.getLogger(User.class.getName());
    
    private String hoVaTen;
    private String tenDangNhap;
    private String matKhau;
    private String salkey;
    
    public String getHoVaTen() {
        return hoVaTen;
    }
    
    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }
    
    public String getTenDangNhap() {
        return tenDangNhap;
    }
    
    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }
    
    public String getMatKhau() {
        return matKhau;
    }
    
    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
    
    public String getSalkey() {
        return salkey;
    }
    
    public void setSalkey(String salkey) {
        this.salkey = salkey;
    }
    
    public static Logger getLog() {
        return log;
    }
    
    
    
}
