package framework.qyweixin.util;

import java.util.List;

/**
 * 部门的bean
 * @author Administrator
 * @date2014-12-17
 */
public class PartBean {
	private Integer level;//当前level，因为有权限问题，可能一个人直接从第三级开始查询，那么第三级在此还是 1，第四级是2 ，这个是相对位置
	private String partName;//部门名称
	private Integer id;//id
	private Integer parentId;//父级id
	private List<PartBean> childrenList;//子集
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public List<PartBean> getChildrenList() {
		return childrenList;
	}
	public void setChildrenList(List<PartBean> childrenList) {
		this.childrenList = childrenList;
	}
	
}
