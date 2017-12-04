package framework.qyweixin.util;

import java.util.List;

/**
 * 部门列表转换工具，原始状态是一个并列的列表，排序后是一个按层级排序的列表
 * 
 * @author Administrator
 * @date2014-12-17
 */
public class QyChangePartType {

	/**
	 * 没写出来暂停！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
	 * 
	 * @param list
	 * @return
	 */
	public QyDepartment changes(List<QyDepartment> list) {
		QyDepartment pb = new QyDepartment();
		int size = list.size();
		QyDepartment getBean = null;

		// 从0开始查 第0位是公司根目录
		boolean isOver = true;
		int count = 0;// 加个保险系数，以防死循环
		while (isOver && count <= 1000) {
			count++;
			for (int i = 0; i < size; i++) {
				getBean = list.get(i);
				if (getBean.getParentid() != null) {

				}

			}

		}

		return pb;
	}

	//
	// {
	// "department":
	// [
	// {
	// "id": 1,
	// "name": "无锡核心信息科技有限公司",
	// "parentid": 0
	// },
	// {
	// "id": 2,
	// "name": "设计",
	// "parentid": 1
	// },
	// {
	// "id": 3,
	// "name": "前端",
	// "parentid": 1
	// },
	// {
	// "id": 4,
	// "name": "客户",
	// "parentid": 1
	// },
	// {
	// "id": 5,
	// "name": "后台",
	// "parentid": 1
	// },
	// {
	// "id": 6,
	// "name": "策划",
	// "parentid": 1
	// },
	// {
	// "id": 7,
	// "name": "管理部",
	// "parentid": 1
	// },
	// {
	// "id": 8,
	// "name": "管理一部",
	// "parentid": 7
	// },
	// {
	// "id": 11,
	// "name": "第一组（一部）",
	// "parentid": 8
	// },
	// {
	// "id": 12,
	// "name": "第二组（一部）",
	// "parentid": 8
	// },
	// {
	// "id": 13,
	// "name": "第三组（一部）",
	// "parentid": 8
	// },
	// {
	// "id": 9,
	// "name": "管理二部",
	// "parentid": 7
	// },
	// {
	// "id": 14,
	// "name": "第一组（二部）",
	// "parentid": 9
	// },
	// {
	// "id": 15,
	// "name": "第二组（二部）",
	// "parentid": 9
	// },
	// {
	// "id": 10,
	// "name": "管理三部",
	// "parentid": 7
	// }],
	// "errcode": 0,
	// "errmsg": "ok"
	// }
}
