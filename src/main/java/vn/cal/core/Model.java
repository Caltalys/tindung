package vn.cal.core;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.zkoss.util.resource.Labels;

@MappedSuperclass
public class Model<T extends Model<T>> extends CoreObject<T> {

    public final static String TT_DA_XOA = Labels.getLabel("trangthai.daxoa");
    public final static String TT_AP_DUNG = Labels.getLabel("trangthai.apdung");
    public final static String TT_KHONG_AP_DUNG = Labels.getLabel("trangthai.khongapdung");
    
    private Long id;    
    private Date ngayTao;   
    private Date ngaySua;
    private User nguoiTao;
    private User nguoiSua;
    private boolean daXoa;
    private String trangThai = TT_AP_DUNG;
    
    @Id
    @GeneratedValue
    public Long getId() {
        if(id==null){
            id = Long.valueOf(0);
        }
        return id;
    }
    
    public void setId(Long id) {
        if(id!=null && id.longValue()==0l){
            id = null;
        }
        this.id = id;
    }
    
    public Date getNgayTao() {
        return ngayTao;
    }
    
    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }
    
    public Date getNgaySua() {
        return ngaySua;
    }
    
    public void setNgaySua(Date ngaySua) {
        this.ngaySua = ngaySua;
    }
    
    @ManyToOne
    public User getNguoiTao() {
        return nguoiTao;
    }
    
    public void setNguoiTao(User nguoiTao) {
        this.nguoiTao = nguoiTao;
    }
    
    @ManyToOne
    public User getNguoiSua() {
        return nguoiSua;
    }
    
    public void setNguoiSua(User nguoiSua) {
        this.nguoiSua = nguoiSua;
    }
    
    public boolean isDaXoa() {
        return daXoa;
    }
    
    public void setDaXoa(boolean daXoa) {
        this.daXoa = daXoa;
        if(daXoa){
            setTrangThai(TT_DA_XOA);
        }
    }
    
    public String getTrangThai() {
        return trangThai;
    }
    
    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    
    public boolean noId(){
        return getId()==null || getId().equals(0l);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj==null || obj.getClass()!=this.getClass()){
            return false;
        }
        Model<?> other = (Model<?>) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        return new HashCodeBuilder((int) (getId()%2==0?getId()+1:getId()), prime).toHashCode();
    }
    
    @Override
    public String toString() {
        return super.toString() + " : "+ getId();
    }
   
    
}
