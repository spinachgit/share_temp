package com.tag.framework.service.impl;

import com.tag.framework.core.pool.ThreadPoolFactory;
import com.tag.framework.core.task.MybatisDBTask;
import com.tag.framework.dao.MybatisBaseDao;
import com.tag.framework.page.Page;
import com.tag.framework.service.MybatisBaseService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MybatisBaseServiceImpl implements MybatisBaseService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MybatisBaseServiceImpl.class);

	public static int type = 1;
	public static final int MYSQL = 1;
	public static final int ORACLE = 2;

	public abstract MybatisBaseDao getDao();

	public <T> int insert(T parameter) {
		return getDao().insert(parameter);
	}

	public <T> int update(T parameter) {
		return getDao().update(parameter);
	}

	public <T> int delete(T parameter) {
		return getDao().delete(parameter);
	}

	public <T, P> T selectObject(P parameter) {
		return (T) getDao().selectObject(parameter);
	}

	public <T, P> List<T> selectObjectList(P parameter) {
		return getDao().selectObjectList(parameter);
	}

	public <V, P> Map<String, V> selectMap(P parameter) {
		return getDao().selectMap(parameter);
	}

	public <V, P> List<Map<String, V>> selectMapList(P parameter) {
		return getDao().selectMapList(parameter);
	}

	public <T> Page<T> page(Map<String, Object> map, int pageIndex, int pageSize) {
		int count = getDao().pageCount(map);

		int offset = (pageIndex - 1) * pageSize;

		if (type == 1) {
			map.put("offset", Integer.valueOf(offset));
			map.put("rows", Integer.valueOf(pageSize));
		} else if (type == 2) {
			map.put("begin", Integer.valueOf(offset));
			map.put("end", Integer.valueOf(offset + pageSize));
		}

		List list = getDao().page(map);

		return new Page(pageIndex, pageSize, count, list);
	}

	public <T> Page<T> page(String pageSelectId, String pageCountSelectId, Map<String, Object> map, int pageIndex, int pageSize) {
		MybatisBaseDao dao = getDao();

		int count = 0;

		Class clazz = dao.getClass();
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(pageCountSelectId, new Class[] { Map.class });
		} catch (NoSuchMethodException e1) {
			LOGGER.error("分页查询【记录数】异常，请检查【{}.{}(Map<String,Object> map)】方法是否存在!", new Object[] { dao.getClass().getName(), pageCountSelectId });
		} catch (SecurityException e1) {
			e1.printStackTrace();
		}
		try {
			try {
				count = ((Integer) method.invoke(dao, new Object[] { map })).intValue();
			} catch (ClassCastException e) {
				LOGGER.error("分页查询【记录数】异常，【{}.{}(Map<String,Object> map)】方法的返回值不是int类型!", new Object[] { dao.getClass().getName(), pageCountSelectId });
			}
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			LOGGER.error("分页查询【记录数】异常，请检查Mapper.xml文件中是否有对应 select id 【{}】!", new Object[] { pageCountSelectId });
		}

		int offset = (pageIndex - 1) * pageSize;

		if (type == 1) {
			map.put("offset", Integer.valueOf(offset));
			map.put("rows", Integer.valueOf(pageSize));
		} else if (type == 2) {
			map.put("begin", Integer.valueOf(offset));
			map.put("end", Integer.valueOf(offset + pageSize));
		}

		List list = null;

		Class clazz2 = dao.getClass();
		Method method2 = null;
		try {
			method2 = clazz2.getDeclaredMethod(pageSelectId, new Class[] { Map.class });
		} catch (NoSuchMethodException e) {
			LOGGER.error("分页查询【记录数】异常，请检查【{}.{}(Map<String,Object> map)】方法是否存在!", new Object[] { dao.getClass().getName(), pageSelectId });
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		try {
			try {
				list = (ArrayList) method2.invoke(dao, new Object[] { map });
			} catch (ClassCastException e) {
				LOGGER.error("分页查询【记录数】异常，【{}.{}(Map<String,Object> map)】方法的返回值不是List类型!", new Object[] { dao.getClass().getName(), pageSelectId });
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			LOGGER.error("分页查询【数据集】异常，请检查Mapper.xml文件中是否有对应 select id 【{}】!", new Object[] { pageSelectId });
		}

		return new Page(pageIndex, pageSize, count, list);
	}

	public <P> void asyncInsert(P parameter) {
		MybatisDBTask task = new MybatisDBTask(1, parameter, getDao());
		ThreadPoolFactory.submit(dbThreadName(), task);
	}

	public <P> void asyncUpdate(P parameter) {
		MybatisDBTask task = new MybatisDBTask(2, parameter, getDao());
		ThreadPoolFactory.submit(dbThreadName(), task);
	}

	public <P> void asyncDelete(P parameter) {
		MybatisDBTask task = new MybatisDBTask(3, parameter, getDao());
		ThreadPoolFactory.submit(dbThreadName(), task);
	}

	public String dbThreadName() {
		String threadName = "DB_" + getClass().getSimpleName().replace("ServiceImpl", "").toUpperCase() + "_THREAD";
		return threadName;
	}
}