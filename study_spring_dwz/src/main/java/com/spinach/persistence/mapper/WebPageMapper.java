package com.spinach.persistence.mapper;

import java.util.List;

import com.spinach.dal.BaseMapper;
import com.spinach.persistence.beans.WebPage;
import org.springframework.stereotype.Repository;

@Repository
public interface WebPageMapper extends BaseMapper<WebPage, Integer> {

	List<WebPage> findByTarget(String target);

}
