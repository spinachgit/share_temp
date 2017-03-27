package com.spinach.persistence.mapper;

import com.spinach.dal.BaseMapper;
import com.spinach.persistence.beans.SysRole;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole,Integer>{
	
	SysRole findBySn(String sn);

}
