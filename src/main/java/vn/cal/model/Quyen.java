package vn.cal.model;

import java.util.HashMap;
import java.util.logging.Logger;

import org.apache.shiro.realm.AuthorizingRealm;
import org.springframework.beans.factory.annotation.Value;

import vn.cal.core.CoreObject;

public class Quyen extends HashMap<String, Boolean>{

    public static transient final Logger log = Logger.getLogger(Quyen.class.getName());
    
    private static final long serialVersionUID = 8664211408151370739L;
    
    @Value("${action.xem}")
    public String XEM = "";
    @Value("${action.lietke}")
	public String LIST = ""; // duoc vao trang list search url
	@Value("${action.sua}")
	public String SUA = ""; // duoc sua bat ky
	@Value("${action.xoa}")
	public String XOA = ""; // duoc xoa bat ky
	@Value("${action.them}")
	public String THEM = ""; // duoc them
	
	public char CHAR_CACH = ':';
	public String CACH = CHAR_CACH + "";
	
	@Value("${url.vaitro}")
	public String VAITRO = "";
	@Value("${url.vaitro}" + ":" + "${action.xem}")	
	public String VAITROXEM;
	@Value("${url.vaitro}" + ":" + "${action.them}")	
	public String VAITROTHEM = "";
	@Value("${url.vaitro}" + ":" + "${action.list}")	
	public String VAITROLIST = "";
	@Value("${url.vaitro}" + ":" + "${action.xoa}")	
	public String VAITROXOA = "";
	@Value("${url.vaitro}" + ":" + "${action.sua}")	
	public String VAITROSUA = "";	
	
	@Value("${url.nguoidung}")
	public String NGUOIDUNG = "";
	@Value("${url.nguoidung}" + ":" + "${action.xem}")	
	public String NGUOIDUNGXEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.them}")
	public String NGUOIDUNGTHEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.list}")
	public String NGUOIDUNGLIST = "";
	@Value("${url.nguoidung}" + ":" + "${action.xoa}")
	public String NGUOIDUNGXOA = "";
	@Value("${url.nguoidung}" + ":" + "${action.sua}")
	public String NGUOIDUNGSUA = "";
	
	public String[] getResources(){
		return new String[] {VAITRO, NGUOIDUNG};
	}
	
	public String[] getActions(){
		return new String[] {LIST, XEM, THEM, SUA, XOA};
	}
	
	private String resource = "";
	private long id;
	private User nguoiTao;
	private transient AuthorizingRealm realm;
	
	public Quyen(AuthorizingRealm realm) {
		this.realm = realm;
	}
	
	public Quyen(AuthorizingRealm realm, String resource){
		this(realm);
		this.resource = resource;
	}

	public Quyen(AuthorizingRealm realm, String resource, long id, User nguoiTao) {
		this(realm, resource);
		this.id = id;
		this.nguoiTao = nguoiTao;
	}
	
	public Quyen() {
		super();
	}

	@Override
	public Boolean get(Object key_){
		if(key_ == null){
			return false;
		}
		
		if (id != 0 && nguoiTao != null && nguoiTao.equals(CoreObject.env().getUser())) {
			return true;
		}
		
		String key = key_.toString();
		if(!key.isEmpty() && key.charAt(0) == '_'){
			key = resource + key;
		}
		if(id!=0){
			key += CACH + id;
		}
		log.info(key);
		boolean result = realm.isPermitted(null, key.replace('_', CHAR_CACH));
		return result;
	}
	
	
}
