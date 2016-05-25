
package vn.cal.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.zkoss.bind.BindUtils;
import org.zkoss.zul.TreeNode;

import com.google.common.base.Strings;

import vn.cal.core.Model;

@Entity
@Table(name = "vaitro", uniqueConstraints = @UniqueConstraint(columnNames = {"tenVaiTro"}),
        indexes = {@Index(columnList = "alias"), @Index(columnList = "tenVaiTro")})
public class VaiTro extends Model<VaiTro> {

    public static transient final Logger log = Logger.getLogger(VaiTro.class.getName());
    
    public static final String QUANTRIVIEN = "quantrivien";
    public static final String NGUOIDUNG = "nguoidung";
    
    public static final String[] VAITRO_DEFAULTS = { QUANTRIVIEN, NGUOIDUNG };
    
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
        this.tenVaiTro = Strings.nullToEmpty(tenVaiTro);
    }
    
    public String getAlias() {
        return alias;
    }
    
    public void setAlias(String alias) {
        this.alias = Strings.nullToEmpty(alias);
    }
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="vaitro_quyens")
    public Set<String> getQuyens() {
    	if(quyens.isEmpty()){
    		quyens.addAll(getQuyenMacDinhs());
    	}
		return quyens;
	}
	
	public void setQuyens(Set<String> quyens) {
		this.quyens = quyens;
	}

    @Transient
	private Set<String> getQuyenMacDinhs() {
		return getQuyenMacDinhs(getAlias());
	}

    @Transient
	private Set<String> getQuyenMacDinhs(String alias) {
		Set<String> quyens = new HashSet<>();
		if(!alias.isEmpty()){
			Quyen q = new Quyen();
			
			if(NGUOIDUNG.equals(alias)){
				
				quyens.add(q.NGUOIDUNGLIST);
				quyens.add(q.NGUOIDUNGXEM);
				quyens.add(q.NGUOIDUNGSUA);
				quyens.add(q.NGUOIDUNGXOA);
				quyens.add(q.NGUOIDUNGTHEM);
				
			} else if(QUANTRIVIEN.equals(alias)){
				
				quyens.add(q.VAITROLIST);
				quyens.add(q.VAITROXEM);
				quyens.add(q.VAITROSUA);
				quyens.add(q.VAITROXOA);
				quyens.add(q.VAITROTHEM);
				
				quyens.add(q.NGUOIDUNGLIST);
				quyens.add(q.NGUOIDUNGXEM);
				quyens.add(q.NGUOIDUNGSUA);
				quyens.add(q.NGUOIDUNGXOA);
				quyens.add(q.NGUOIDUNGTHEM);
			}
		}
    	return quyens;
	}

    private Set<TreeNode<String[]>> selectedItems = new HashSet<>();
    
    @Transient
    public Set<TreeNode<String[]>> getSelectedItems(){
    	return selectedItems;
    }
    
    public void setSelectedItems(Set<TreeNode<String[]>> selectedItems_){
    	Set<TreeNode<String[]>> selectedItemsTmp = new HashSet<>();
    	selectedItemsTmp.addAll(selectedItems);
    	
    	for (final TreeNode<String[]> node : selectedItems) {
			if(!selectedItems_.contains(node)){
				quyenEdits.remove(node.getData()[1]);
				selectedItemsTmp.remove(node);
				
				//Remove parent
				TreeNode<String[]> pNode = node.getParent();
				if(pNode!=null && selectedItems_.contains(pNode)){
					quyenEdits.remove(pNode.getData()[1]);
					selectedItemsTmp.remove(pNode);
				}
				
				//Remove all children
				if(node.getChildCount() > 0){
					for (TreeNode<String[]> n : node.getChildren()) {
						quyenEdits.remove(n.getData()[1]);
						selectedItemsTmp.remove(n);
					}
				}
			}
		}
    	
    	for (TreeNode<String[]> node : selectedItems_) {
    		if (!selectedItems.contains(node)) {
				quyenEdits.add(node.getData()[1]);
				selectedItemsTmp.add(node);
				if (node.getChildCount() > 0) {
					for (TreeNode<String[]> n : node.getChildren()) {
						quyenEdits.add(n.getData()[1]);
						selectedItemsTmp.add(n);
					}
				}
			}
		}
    	
    	selectedItems = selectedItemsTmp;
		BindUtils.postNotifyChange(null, null, this, "quyenEdits");
		BindUtils.postNotifyChange(null, null, this, "selectedItems");
    }
    
    
}
