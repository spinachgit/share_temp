package com.tag.framework.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomUtil {
	private static final double PRECISION = 1000000.0D;

	public static int getRandomInt(int n) {
		if (n == 0) {
			return 0;
		}
		Random random = new Random();
		return random.nextInt(n);
	}

	public static int getRandomInt(int n, long seed) {
		if (n == 0) {
			return 0;
		}
		Random randomWithSeed = new Random();
		randomWithSeed.setSeed(seed);
		return randomWithSeed.nextInt(n);
	}
	
	public static int getRandomInt(int min, int max) {
		if (min == max) {
			return min;
		}
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		Random random = new Random();
		return random.nextInt(max - min + 1) + min;
	}
	public static void main(String[] args) {
		Random random = new Random();
		for(int i=0;i<100;i++){
			System.out.println(random.nextInt(100));
		}
	}
	public static int getRandomInt(int min, int max, long seed) {
		if (min == max) {
			return min;
		}
		if (min > max) {
			int temp = min;
			min = max;
			max = temp;
		}
		Random randomWithSeed = new Random();
		randomWithSeed.setSeed(seed);
		return randomWithSeed.nextInt(max - min + 1) + min;
	}

	public static double getRandomDouble(double min, double max) {
		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}
		if (max * 1000000.0D > 2147483647.0D) {
			throw new RuntimeException("the number mast be less than 2147.");
		}
		int intMin = (int) (min * 1000000.0D);
		int intMax = (int) (max * 1000000.0D);
		double result = getRandomInt(intMin, intMax) / 1000000.0D;
		return result;
	}

	public static double getRandomDouble(double min, double max, long seed) {
		if (min > max) {
			double temp = min;
			min = max;
			max = temp;
		}
		if (max * 1000000.0D > 2147483647.0D) {
			throw new RuntimeException("the number mast be less than 2147.");
		}
		int intMin = (int) (min * 1000000.0D);
		int intMax = (int) (max * 1000000.0D);
		double result = getRandomInt(intMin, intMax, seed) / 1000000.0D;
		return result;
	}

	public static double getRandomDouble(double min, double max, int n) {
		/*if (min > max)
			throw new RuntimeException("min number must be less than or equal than max number");
		double precision;
		double precision;
		if (n == 1) {
			precision = 10.0D;
		} else {
			double precision;
			if (n == 2) {
				precision = 100.0D;
			} else {
				double precision;
				if (n == 3) {
					precision = 1000.0D;
				} else {
					double precision;
					if (n == 4) {
						precision = 1000.0D;
					} else {
						double precision;
						if (n == 5) {
							precision = 10000.0D;
						} else {
							double precision;
							if (n == 6) {
								precision = 100000.0D;
							} else {
								double precision;
								if (n == 7) {
									precision = 1000000.0D;
								} else {
									double precision;
									if (n == 8) {
										precision = 10000000.0D;
									} else {
										double precision;
										if (n == 9) {
											precision = 100000000.0D;
										} else {
											double precision;
											if (n == 10)
												precision = 1000000000.0D;
											else
												precision = 1.0D;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		*/
//		if (max * precision > 2147483647.0D) {
//			throw new RuntimeException("the number max and n is too large");
//		}
//		int intMin = (int) (min * precision);
//		int intMax = (int) (max * precision);
		double result = 0.234f ; // getRandomInt(intMin, intMax) / precision;
		return result;
	}

	public static boolean checkIsTrue(double rate) {
		if (rate >= 1.0D)
			return true;
		if (rate <= 0.0D) {
			return false;
		}

		int denominator = 1000000;

		int probability = (int) (rate * denominator);
		Random random = new Random();
		int n = random.nextInt(denominator) + 1;
		if (n <= probability) {
			return true;
		}
		return false;
	}

	public static boolean checkIsTrue(double rate, long seed) {
		if (rate >= 1.0D)
			return true;
		if (rate <= 0.0D) {
			return false;
		}
		Random randomWithSeed = new Random();
		randomWithSeed.setSeed(seed);

		int denominator = 1000000;

		int probability = (int) (rate * denominator);
		int n = randomWithSeed.nextInt(denominator) + 1;
		if (n <= probability) {
			return true;
		}
		return false;
	}

	public static List<Integer> getUnlikeRandomInt(int num, int min, int max) {
		if (num > max - min + 1) {
			throw new RuntimeException("the number num is too large");
		}
		List list = new ArrayList();
		for (int i = 0; i < num; i++) {
			int randomPoint = getRandomInt(min, max);
			if (!list.contains(Integer.valueOf(randomPoint)))
				list.add(Integer.valueOf(randomPoint));
			else {
				i--;
			}
		}
		return list;
	}

	public static int getRandomResult(List<Integer> weightList) {
		int index = 0;
		int all = 0;
		for (int i = 0; i < weightList.size(); i++) {
			int rate = ((Integer) weightList.get(i)).intValue();
			all += rate;
		}
		int randomNum = getRandomInt(1, all);
		int tempNum = 0;
		for (int i = 0; i < weightList.size(); i++) {
			int rate = ((Integer) weightList.get(i)).intValue();
			tempNum += rate;
			if (tempNum >= randomNum) {
				index = i;
				break;
			}
		}
		return index;
	}

	public static int getRandomResult(List<Integer> weightList, long seed) {
		int index = 0;
		int all = 0;
		for (int i = 0; i < weightList.size(); i++) {
			int rate = ((Integer) weightList.get(i)).intValue();
			all += rate;
		}
		int randomNum = getRandomInt(1, all, seed);
		int tempNum = 0;
		for (int i = 0; i < weightList.size(); i++) {
			int rate = ((Integer) weightList.get(i)).intValue();
			tempNum += rate;
			if (tempNum >= randomNum) {
				index = i;
				break;
			}
		}
		return index;
	}

}