package com.tag.framework.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class RateValidator<T> {
	private List<T> objList = new ArrayList();

	private List<Integer> weightList = new ArrayList();

	public void put(T obj, int weight) {
		this.objList.add(obj);
		this.weightList.add(Integer.valueOf(weight));
	}

	public T getResult() {
		int index = RandomUtil.getRandomResult(this.weightList);
		if ((index < 0) || (index > this.objList.size() - 1)) {
			System.out.println("概率验证器中取值错误！索引=【" + index + "】，容器大小=【" + this.objList.size() + "】");
			return null;
		}
		return this.objList.get(index);
	}

	public T getResult(long seed) {
		int index = RandomUtil.getRandomResult(this.weightList, seed);
		if ((index < 0) || (index > this.objList.size() - 1)) {
			System.out.println("概率验证器中取值错误！索引=【" + index + "】，容器大小=【" + this.objList.size() + "】");
			return null;
		}
		return this.objList.get(index);
	}

	public T getResultAndRemove() {
		T obj = getResult();
		remove(obj);
		return obj;
	}

	public T getResultAndRemove(long seed) {
		T obj = getResult(seed);
		remove(obj);
		return obj;
	}

	public void clear() {
		this.objList.clear();
		this.weightList.clear();
	}

	public void remove(T result) {
		if (this.objList.size() == 0) {
			return;
		}
		int index = this.objList.indexOf(result);
		this.objList.remove(index);
		this.weightList.remove(index);
	}

	public int size() {
		return this.objList.size();
	}
}