package vn.cal.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.zkoss.util.resource.Labels;

import com.querydsl.core.annotations.QueryInit;

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
    private Quyen quyen;
    private Set<String> quyens = new HashSet<>();
    private Set<VaiTro> vaiTros = new HashSet<>();
    
    private Set<String> tatCaQuyens = new HashSet<>();
    
    public User() {
        super();
    }
    
    public User(final String tenDangNhap, final String hoTen) {
        super();
        setTenDangNhap(tenDangNhap);
        setEmail(tenDangNhap);
        setHoVaTen(hoTen);
    }

    public String getHoVaTen() {
        return hoVaTen;
    }
    
    public void setHoVaTen(final String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }
    
    public String getTenDangNhap() {
        return tenDangNhap;
    }
    
    public void setTenDangNhap(final String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }
    
    public String getMatKhau() {
        return matKhau;
    }
    
    public void setMatKhau(final String matKhau) {
        this.matKhau = matKhau;
    }
    
    public String getSalkey() {
        return salkey;
    }
    
    public void setSalkey(final String salkey) {
        this.salkey = salkey;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @ElementCollection
    @CollectionTable(name="user_quyens")
    public Set<String> getQuyens() {
		return quyens;
	}

	public void setQuyens(final Set<String> quyens) {
		this.quyens = quyens;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@QueryInit("*.*.*.*")
	public Set<VaiTro> getVaiTros(){
		return vaiTros;
	}
	
	public void setVaiTros(final Set<VaiTro> vaiTros1) {
		vaiTros = vaiTros1;
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

    @Transient
	public Quyen getTaCaQuyen() {
    	if(quyen==null){
    		SimpleAccountRealm realm = new SimpleAccountRealm() {
				@Override
				protected AuthorizationInfo getAuthorizationInfo(final PrincipalCollection principals) {
					final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
					info.setStringPermissions(getTatCaQuyens());
					return info;
				}
    		};
    		quyen = new Quyen(realm);
    	}
    	return quyen;
	}

    @Transient
	public Set<String> getTatCaQuyens() {
		if(tatCaQuyens.isEmpty()){
			tatCaQuyens.addAll(quyens);
			for (VaiTro vt : vaiTros) {
				if(vt.getAlias() != null && !vt.getAlias().isEmpty()){
					tatCaQuyens.add("vt"+vt.getAlias());
				}
				tatCaQuyens.addAll(vt.getQuyens());
			}
			if(Labels.getLabel("email.superuser").equals(tenDangNhap)){
				tatCaQuyens.add("*");
			}
		}
		return tatCaQuyens;
	}

	
    
    
    
}
