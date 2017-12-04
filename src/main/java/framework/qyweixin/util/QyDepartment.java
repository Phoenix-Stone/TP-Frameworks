package framework.qyweixin.util;

import java.util.List;

/**
 * 部门
 * @author Administrator
 * @date2014-12-17
 */
public class QyDepartment {
	private Long id;//id
	private String partName;//部门名称
	private Integer parentid;//父级id
	private Integer order; //在父部门中的次序值。order值小的排序靠前。 
	private List<QyDepartment> childrenList;//子集

	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getParentid() {
		return parentid;
	}
	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	public List<QyDepartment> getChildrenList() {
		return childrenList;
	}
	public void setChildrenList(List<QyDepartment> childrenList) {
		this.childrenList = childrenList;
	}
}
