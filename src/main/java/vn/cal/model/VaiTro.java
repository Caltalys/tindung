
package vn.cal.model;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import vn.cal.core.Model;

@Entity
@Table(name = "vaitro", uniqueConstraints = @UniqueConstraint(columnNames = {"tenVaiTro"}),
        indexes = {@Index(columnList = "alias"), @Index(columnList = "tenVaiTro")})
public class VaiTro extends Model<VaiTro> {

    public static transient final Logger log = Logger.getLogger(VaiTro.class.getName());
    
    public static final String QUANTRIVIEN = "quantrivien";
    
    public static final String[] VAITRO_DEFAULTS = { QUANTRIVIEN };
    
    private String tenVaiTro = "";
    private String alias = "";
    private Set<String> quyens = new HashSet<>();
    private Set<String> quyenEdits = quyens;
    
    public VaiTro(){
        super();
    }
    
    public VaiTro(final String ten,final String quyen){
        super();
        setTenVaiTro(ten.trim());
        setAlias(quyen.trim());
    }
    
    public String getTenVaiTro() {
        return tenVaiTro;
    }
    
    public void setTenVaiTro(String tenVaiTro) {
        this.tenVaiTro = tenVaiTro;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = alias;
    }
    
    
    
}
