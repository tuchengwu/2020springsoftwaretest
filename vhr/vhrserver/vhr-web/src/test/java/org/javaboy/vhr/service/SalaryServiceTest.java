package org.javaboy.vhr.service;


import org.javaboy.vhr.model.Salary;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@WebAppConfiguration
public class SalaryServiceTest {
    @Autowired
    SalaryService salaryService;

    @Test
    public void getAllSalaries() {
        List<Salary> result = salaryService.getAllSalaries();
        List<Salary> expect = new ArrayList<>();
        Salary ele1=new Salary();
        ele1.setId(9);
        ele1.setBasicSalary(9000);
        ele1.setBonus(4000);
        ele1.setLunchSalary(800);
        ele1.setTrafficSalary(500);
        ele1.setPensionBase(2000);
        ele1.setPensionPer((float) 0.07);
        ele1.setCreateDate(new Date(120, 4,6,0,0,0));
        ele1.setMedicalBase(2000);
        ele1.setMedicalPer((float) 0.07);
        ele1.setAccumulationFundBase(2000);
        ele1.setAccumulationFundPer((float) 0.07);
        ele1.setName("市场部工资账套");
        Salary ele2=new Salary();
        ele2.setId(10);
        ele2.setBasicSalary(2000);
        ele2.setBonus(2000);
        ele2.setLunchSalary(400);
        ele2.setTrafficSalary(1000);
        ele2.setPensionBase(2000);
        ele2.setPensionPer((float) 0.07);
        ele2.setCreateDate(new Date(118, 0,1,0,0,0));
        ele2.setMedicalBase(2000);
        ele2.setMedicalPer((float) 0.07);
        ele2.setAccumulationFundBase(2000);
        ele2.setAccumulationFundPer((float) 0.07);
        ele2.setName("人事部工资账套");
        Salary ele3=new Salary();
        ele3.setId(13);
        ele3.setBasicSalary(20000);
        ele3.setBonus(3000);
        ele3.setLunchSalary(500);
        ele3.setTrafficSalary(500);
        ele3.setPensionBase(4000);
        ele3.setPensionPer((float) 0.07);
        ele3.setCreateDate(new Date(118, 0,25,0,0,0));
        ele3.setMedicalBase(4000);
        ele3.setMedicalPer((float) 0.07);
        ele3.setAccumulationFundBase(4000);
        ele3.setAccumulationFundPer((float) 0.07);
        ele3.setName("运维部工资账套");
        expect.add(ele1);
        expect.add(ele2);
        expect.add(ele3);
        Assert.assertEquals(expect,result);
    }

    @Test
    public void addSalary() {
        //测试用例1，重复的主键
        Salary toInsert = new Salary();
        toInsert.setId(13);
        try{
            salaryService.addSalary(toInsert);
        }
        catch (Exception e){
            Assert.assertEquals(e.getClass().toString(),"class "+"org.springframework.dao.DuplicateKeyException");
        }
        //测试用例2，全空
        toInsert=new Salary();
        Integer result = salaryService.addSalary(toInsert);
        Assert.assertEquals(1,result.intValue());
        //测试用例3，除主键外与现有记录相同
        Salary ele3=new Salary();
        ele3.setId(20);
        ele3.setBasicSalary(20000);
        ele3.setBonus(3000);
        ele3.setLunchSalary(500);
        ele3.setTrafficSalary(500);
        ele3.setPensionBase(4000);
        ele3.setPensionPer((float) 0.07);
        ele3.setCreateDate(new Date(118, 0,25,0,0,0));
        ele3.setMedicalBase(4000);
        ele3.setMedicalPer((float) 0.07);
        ele3.setAccumulationFundBase(4000);
        ele3.setAccumulationFundPer((float) 0.07);
        ele3.setName("运维部工资账套");
        result = salaryService.addSalary(ele3);
        Assert.assertEquals(1,result.intValue());
        //测试用例4，普通有效值
        ele3.setId(200);
        ele3.setBasicSalary(23000);
        ele3.setBonus(30001);
        ele3.setLunchSalary(5100);
        ele3.setTrafficSalary(5000);
        ele3.setPensionBase(40000);
        ele3.setPensionPer((float) 0.09);
        ele3.setCreateDate(new Date(128, 0,25,0,0,0));
        ele3.setMedicalBase(4001);
        ele3.setMedicalPer((float) 0.07);
        ele3.setAccumulationFundBase(40030);
        ele3.setAccumulationFundPer((float) 0.07);
        ele3.setName("运维部工资账套");
        result = salaryService.addSalary(ele3);
        Assert.assertEquals(1,result.intValue());
    }

    @Test
    public void deleteSalaryById() {
        //测试用例1，需要删除的主键不存在于数据库中
        Integer result = salaryService.deleteSalaryById(15);
        Assert.assertEquals(0,result.intValue());
        //测试用例2，需要删除的主键存在于数据库中
        result=salaryService.deleteSalaryById(14);
        Assert.assertEquals(1,result.intValue());
        //测试用例3，需要删除的主键为负数,且存在
        result=salaryService.deleteSalaryById(-1);
        Assert.assertEquals(1,result.intValue());
        //测试用例4，需要删除的主键为负数，且不存在
        result = salaryService.deleteSalaryById(-2);
        Assert.assertEquals(0,result.intValue());
    }

    @Test
    public void updateSalaryById() {
        //测试用例1，待更新的记录的主键存在于数据库中
        Salary ele3 = new Salary();
        ele3.setId(14);
        ele3.setBonus(30001);
        ele3.setLunchSalary(5100);
        ele3.setTrafficSalary(5000);
        ele3.setPensionBase(40000);
        ele3.setPensionPer((float) 0.09);
        ele3.setCreateDate(new Date(128, 0,25,0,0,0));
        ele3.setMedicalBase(4001);
        ele3.setMedicalPer((float) 0.07);
        ele3.setAccumulationFundBase(40030);
        ele3.setAccumulationFundPer((float) 0.07);
        ele3.setName("运维部工资账套");
        Integer result = salaryService.updateSalaryById(ele3);
        Assert.assertEquals(1,result.intValue());
        //测试用例2，待更新的记录存在于数据库中，但除主键外待更新记录全为空字段
        Salary ele4 = new Salary();
        ele4.setId(14);
        try{
            salaryService.updateSalaryById(ele4);
        }
        catch (Exception e){
            Assert.assertEquals(e.getClass().toString(),"class "+"org.springframework.jdbc.BadSqlGrammarException");
        }
        //测试用例3，待更新的记录不存在数据库中，且字段不均为空
        Salary ele5 = new Salary();
        ele5.setId(16);
        ele5.setBonus(30001);
        ele5.setLunchSalary(5100);
        ele5.setTrafficSalary(5000);
        ele5.setPensionBase(40000);
        ele5.setPensionPer((float) 0.09);
        ele5.setCreateDate(new Date(128, 0,25,0,0,0));
        ele5.setMedicalBase(4001);
        ele5.setMedicalPer((float) 0.07);
        ele5.setAccumulationFundBase(40030);
        ele5.setAccumulationFundPer((float) 0.07);
        ele5.setName("运维部工资账套");
        result=salaryService.updateSalaryById(ele5);
        Assert.assertEquals(0,result.intValue());
        //测试用例4，记录主键不存在于数据库，且记录其余字段为空
        Salary ele6=new Salary();
        ele6.setId(18);
        try{
            salaryService.updateSalaryById(ele6);
        }
        catch (Exception e){
            Assert.assertEquals(e.getClass().toString(),"class "+"org.springframework.jdbc.BadSqlGrammarException");
        }

    }
}
