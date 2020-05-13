package org.javaboy.vhr.converter;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class DateConverterTest {
    DateConverter dateConverter=new DateConverter();
    @Test
    public void convert(){
        //测试用例1，符合模式
        Date convert = dateConverter.convert("2010-01-24");
        Date expect=new Date(110, Calendar.JANUARY,24);
        System.out.println(expect);
        Assert.assertEquals(expect,convert);
        //测试用例2，符合模式，但包含时分秒
        convert=dateConverter.convert("2010-01-24 00:01:02");
        expect=new Date(110, Calendar.JANUARY,24);
        Assert.assertEquals(expect,convert);
        //测试用例3，符合模式，但日期非法
        convert = dateConverter.convert("2010-12-32");
        expect=new Date(111, Calendar.JANUARY,1);
        System.out.println(expect);
        Assert.assertEquals(expect,convert);
        //测试用例4，不符合模式
        convert=dateConverter.convert("2012.1-1");
        assertNull(convert);
        convert=dateConverter.convert("2010-12--1");
        System.out.println(convert);

    }

}
