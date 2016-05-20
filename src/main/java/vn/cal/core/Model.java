package vn.cal.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import vn.cal.model.User;

@MappedSuperclass
public class Model<T extends Model<T>> extends CoreObject<T> {
    
    private transient long instanceTime;
    
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
    
    @PostLoad
    private void loaded() {
        if (instanceTime == 0) {
            instanceTime = System.currentTimeMillis();
        }
    }
    
    @Transient
    public boolean isLoaded() {
        return instanceTime != 0;
    }
    
    @Transient
    public long getInstanceTime() {
        return instanceTime;
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
   
    public void save(){
        setNgaySua(new Date());
        setNguoiSua(getUserService().getUser(true));
        if(noId()){
            setNgayTao(getNgaySua());
            setNguoiTao(getNguoiSua());
        }
        loaded();
        saveModel(this);
    }

    public void saveModel(final Model<T> obj) {
        if(obj.getClass().isAnnotationPresent(Entity.class)){
            transactionTemplate().execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    if(obj.noId()){
                        em().persist(obj);
                    } else {
                        em().merge(obj);
                    }
                }
            });
        }
    }
}
