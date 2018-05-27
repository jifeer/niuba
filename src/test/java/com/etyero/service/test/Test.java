package com.etyero.service.test;

import java.text.ParseException;

import com.etyero.tool.TimeUtil;

public class Test {
public static void main(String[] args) throws ParseException {
//	System.out.println(TimeUtil.getDate("yyyy-MM-dd HH"));
//	long nowDate = TimeUtil.strToDate(TimeUtil.getDate("yyyyMMdd"), "yyyyMMdd").getTime()/1000;//当前日期0点时间戳
//    long beforeYesToday = nowDate - 48 * 60 *60;//两天前的0点时间戳
//    System.out.println(nowDate + 60 * 60);
//    System.out.println(beforeYesToday);
    
    System.out.println(new Test().mod_inverse(2,5));
}
int extgcd(int a, int b, int x, int y)
{
    int d = a;
    if(b != 0){
        d = extgcd(b, a % b, y, x);
        y -= (a / b) * x;
    }else {
        x = 1;
        y = 0;
    }
    return d;
}
int mod_inverse(int a, int m)
{
    int x=0, y=0;
    extgcd(a, m, x, y);
    return (m + x % m) % m;
}
}
