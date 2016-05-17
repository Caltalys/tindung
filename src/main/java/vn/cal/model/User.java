package vn.cal.model;

import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.jasypt.util.password.BasicPasswordEncryptor;

import vn.cal.core.Model;

@Entity
@Table(name="user", uniqueConstraints = @UniqueConstraint(columnNames = {"tenDangNhap"}), indexes = @Index(columnList = "tenDangNhap"))
public class User extends Model<User> {
    public static transient final Logger log = Logger.getLogger(User.class.getName());
    
    private String hoVaTen;
    private String tenDangNhap;
    private String matKhau;
    private String salkey;
    private String email;
    
    public User() {
        super();
    }
    
    public User(String tenDangNhap, String hoTen) {
        super();
        setTenDangNhap(tenDangNhap);
        setEmail(tenDangNhap);
        setHoVaTen(hoTen);
    }

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
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCookieToken(long expire) {
        String token = getId() + ":" + expire +":";
        return Base64.encodeBase64String(token.concat(DigestUtils.md5Hex(token + matKhau + ":" + salkey)).getBytes());
    }

    public void updatePassword(String pass){
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String salkey = getSalkey();
        if(salkey==null || salkey.equals("")){
            salkey = encryptor.encryptPassword((new Date()).toString());
        }
        String passNoHash = pass + salkey;
        String passHash = encryptor.encryptPassword(passNoHash);
        setSalkey(salkey);
        setMatKhau(passHash);
    }
    
    
    
}
