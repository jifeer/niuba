package com.etyero.service.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 生成身份证号
 * @author lijialong
 **/
public class MakeIdno {
	public static void main(String[] args) {
		System.out.println(getCardId("19900302"));
	}

	/**
	 * 省、直辖市代码表： 11 : 北京 12 : 天津 13 : 河北 14 : 山西 15 : 内蒙古 21 : 辽宁 22 : 吉林 23 :
	 * 黑龙江 31 : 上海 32 : 江苏 33 : 浙江 34 : 安徽 35 : 福建 36 : 江西 37 : 山东 41 : 河南 42 :
	 * 湖北 43 : 湖南 44 : 广东 45 : 广西 46 : 海南 50 : 重庆 51 : 四川 52 : 贵州 53 : 云南 54 :
	 * 西藏 61 : 陕西 62 : 甘肃 63 : 青海 64 : 宁夏 65 : 新疆 71 : 台湾 81 : 香港 82 : 澳门 91 :
	 * 国外
	 */
	private static String cityCode[] = { "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35",
			"36", "37", "41", "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65",
			"71", "81", "82", "91" };

	/**
	 * 每位加权因子
	 */
	private static int power[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

	public static String getCardId(String birthday) {
		if (birthday.length() != 8) {
			return "出生年月日有误";
		}
		// 校验出生日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			Date birthDate = sdf.parse(birthday);
			String tmpDate = sdf.format(birthDate);
			if (!tmpDate.equals(birthday)) {// 出生年月日不正确
				return "出生年月日有误";
			}
		} catch (ParseException e1) {
			return "出生年月日有误";
		}

		return validate18Idcard(birthday);
	}

	/**
	 * @param birthday
	 *            出生年月日 格式yyyyMMdd
	 * @return String idno
	 */
	public static String validate18Idcard(String birthday) {
		StringBuilder idcard17 = new StringBuilder();

		// 获取省份
		String provinceid = getProvinceid();
		idcard17.append(provinceid);
		int a = new Random().nextInt(8999) + 1000;
		idcard17.append(a);
		idcard17.append(birthday);
		int b = new Random().nextInt(899) + 100;
		idcard17.append(b);
		char c[] = idcard17.toString().toCharArray();

		int bit[] = converCharToInt(c);

		int sum17 = 0;

		sum17 = getPowerSum(bit);

		// 将和值与11取模得到余数进行校验码拼接
		String checkCode = getCheckCodeBySum(sum17);
		return idcard17 + checkCode;
	}

	/**
	 * 获取省份
	 *
	 * @return String
	 */
	private static String getProvinceid() {
		int random = new Random().nextInt(cityCode.length);
		return cityCode[random];
	}

	/**
	 * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
	 *
	 * @param bit
	 * @return
	 */
	private static int getPowerSum(int[] bit) {

		int sum = 0;

		if (power.length != bit.length) {
			return sum;
		}

		for (int i = 0; i < bit.length; i++) {
			for (int j = 0; j < power.length; j++) {
				if (i == j) {
					sum = sum + bit[i] * power[j];
				}
			}
		}
		return sum;
	}

	/**
	 * 将和值与11取模得到余数进行校验码判断
	 *
	 * @param sum17
	 * @return 校验位
	 */
	private static String getCheckCodeBySum(int sum17) {
		String checkCode = null;
		switch (sum17 % 11) {
		case 10:
			checkCode = "2";
			break;
		case 9:
			checkCode = "3";
			break;
		case 8:
			checkCode = "4";
			break;
		case 7:
			checkCode = "5";
			break;
		case 6:
			checkCode = "6";
			break;
		case 5:
			checkCode = "7";
			break;
		case 4:
			checkCode = "8";
			break;
		case 3:
			checkCode = "9";
			break;
		case 2:
			checkCode = "x";
			break;
		case 1:
			checkCode = "0";
			break;
		case 0:
			checkCode = "1";
			break;
		}
		return checkCode;
	}

	/**
	 * 将字符数组转为整型数组
	 *
	 * @param c
	 * @return
	 * @throws NumberFormatException
	 */
	private static int[] converCharToInt(char[] c) throws NumberFormatException {
		int[] a = new int[c.length];
		int k = 0;
		for (char temp : c) {
			a[k++] = Integer.parseInt(String.valueOf(temp));
		}
		return a;
	}
}
