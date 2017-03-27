package com.tag.framework.constant;

public final class Symbol {
	public static final String fenHao = ";|；";
	public static final String maoHao = ":|：";
	public static final String douHao = ",|，";
	public static final String shuXian = "\\|";
	public static final String xiaHuaXian = "_";
	public static final String jianHao = "-";
	public static final String jingHao = "\\#";
	public static final String zuoZhongKuoHao = "\\[";
	public static final String youZhongKuoHao = "\\]";
	public static final String zuoDaKuoHao = "\\{";
	public static final String youDaKuoHao = "\\}";
	public static final String at = "@";

	public static void main(String[] args) {
		String s = "aaaaaa;bbbbb";
		System.out.println(s.split(";|；")[0]);

		s = "aaaaaa[bbbbb";
		System.out.println(s.split("\\[")[0]);
	}
}