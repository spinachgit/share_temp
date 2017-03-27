package com.spinach.business.info.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spinach.business.info.News;
import com.spinach.business.info.NewsServiceMgr;
import com.spinach.business.info.NewsStatus;
import com.spinach.business.info.NewsType;
import com.spinach.framework.sys.business.AbstractBusinessObjectServiceMgr;
import com.spinach.persistence.BaseConditionVO;
import com.spinach.persistence.beans.InfNews;
import com.spinach.persistence.mapper.InfNewsMapper;

@Transactional(rollbackFor = Exception.class)
@Service(NewsServiceMgr.SERVICE_NAME)
public class NewsServiceMgrImpl extends AbstractBusinessObjectServiceMgr implements NewsServiceMgr {

	@Autowired
	private InfNewsMapper newsMapper;

	public void createAndPublishNews(News news) {
		news.setStatus(NewsStatus.ACTIVE);
		this.createNews(news);
	}

	public void createNews(News news) {
		this.newsMapper.insert(news.getInfNews());
	}

	public void disableNews(Integer id) {
		this.newsMapper.updateAllStatus(id, NewsStatus.DISABLED.toString(), new Date());
	}

	public void publishNews(Integer id) {
		this.newsMapper.updateAllStatus(id, NewsStatus.ACTIVE.toString(), new Date());
	}

	public News getNews(Integer id) {
		InfNews infNews = this.newsMapper.load(id);
		if (infNews != null)
			return new News(infNews);
		return null;
	}

	public News getNewsOnWeb(Integer id) {
		News news = this.getNews(id);
		if (NewsStatus.ACTIVE.equals(news.getStatus()))
			return news;
		return null;
	}

	public void updateNews(News news) {
		InfNews infNews = news.getInfNews();
		infNews.setUpdateDate(new Date());
		this.newsMapper.updateSelective(infNews);
	}

	public void removeNews(Integer id) {
		this.newsMapper.delete(id);
	}

	public List<News> searchNews(BaseConditionVO vo) {
		ArrayList<News> bos = new ArrayList<News>();
		RowBounds rb = new RowBounds(vo.getStartIndex(), vo.getPageSize());
		List<InfNews> pos = newsMapper.findPageBreakByCondition(vo.getKeywords(), vo.getType(), vo.getStatus(), vo.getStartDate(), vo.getEndDate(), rb);
		for (InfNews po : pos) {
			bos.add(new News(po));
		}
		return bos;
	}

	public Integer searchNewsNum(BaseConditionVO vo) {
		Integer count = newsMapper.findNumberByCondition(vo.getKeywords(), vo.getType(), vo.getStatus(), vo.getStartDate(), vo.getEndDate());

		return count;
	}

	public News getNextNews(News news) {
		if (news == null)
			return null;
		InfNews infNews = news.getInfNews();
		InfNews po = this.newsMapper.findNext(infNews.getType(), infNews.getUpdateDate());
		if (po != null){
			return new News(po);
		}
		return null;
	}

	public News getPrevNews(News news) {
		if (news == null)
			return null;
		InfNews infNews = news.getInfNews();
		InfNews po = this.newsMapper.findPrev(infNews.getType(), infNews.getUpdateDate());
		if (po != null){
			return new News(po);
		}
		return null;
	}

	public List<News> listNewsOnWeb(NewsType newsType, Integer startIndex, Integer count) {
		ArrayList<News> bos = new ArrayList<News>();
		RowBounds rb = new RowBounds(startIndex, count);
		List<InfNews> pos = newsMapper.findPageBreakByTypeAndStatus(newsType.toString(), NewsStatus.ACTIVE.toString(), rb);
		for (InfNews po : pos) {
			bos.add(new News(po));
		}
		return bos;
	}

}
