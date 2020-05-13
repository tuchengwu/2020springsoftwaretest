package org.javaboy.vhr.service;


import org.javaboy.vhr.converter.DateConverter;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespPageBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class EmployeeServiceTest {
    @Autowired
    EmployeeService employeeService;

    @Test
    public void getEmployeeByPage() {
        //测试用例1，页大小为总记录数量，取出第一页，且不设置搜索条件
        Employee nonCondition = new Employee();
        RespPageBean employeeByPage = employeeService.getEmployeeByPage(1, 617, nonCondition, null);
        Assert.assertEquals(617,employeeByPage.getData().size());
        //测试用例2，页大小为总记录数量，取出第二页，且不设置搜索条件
        employeeByPage = employeeService.getEmployeeByPage(2, 617, nonCondition, null);
        Assert.assertEquals(0,employeeByPage.getData().size());
        //测试用例3，页大小为负数-1
        try{
            employeeByPage = employeeService.getEmployeeByPage(2, -1, nonCondition, null);
        }catch (Exception e){
            Assert.assertEquals("class "+"org.springframework.jdbc.BadSqlGrammarException",e.getClass().toString());
        }
        //测试用例4，页大小为10，取第一页，指定姓名为查询条件,且以该名称近似搜索结果不为空
        Employee condition = new Employee();
        condition.setName("江南一点雨");
        employeeByPage = employeeService.getEmployeeByPage(1, 10, condition, null);
        Assert.assertEquals(10,employeeByPage.getData().size());
        //测试用例5，页大小为20，取最后一页，指定合同类型为劳动合同为查询条件
        condition=new Employee();
        condition.setEngageForm("劳动合同");
        employeeByPage = employeeService.getEmployeeByPage(25, 20, condition, null);
        Assert.assertEquals(2,employeeByPage.getData().size());
        //测试用例6，页大小为15，取最后一页，指定politicId为3
        condition=new Employee();
        condition.setPoliticId(3);
        employeeByPage = employeeService.getEmployeeByPage(12, 15, condition, null);
        Assert.assertEquals(3,employeeByPage.getData().size());
        //测试用例7，页大小为10，取最后一页，指定jobLevelId为12
        condition=new Employee();
        condition.setJobLevelId(12);
        employeeByPage = employeeService.getEmployeeByPage(18, 10, condition, null);
        Assert.assertEquals(4,employeeByPage.getData().size());
        //测试用例8，页大小为8，取最后一页，指定departmentId为82
        condition=new Employee();
        condition.setDepartmentId(82);
        employeeByPage = employeeService.getEmployeeByPage(7, 8, condition, null);
        Assert.assertEquals(6,employeeByPage.getData().size());
        //测试用例9，页大小为12，取最后一页，指定posId为29
        condition=new Employee();
        condition.setPosId(29);
        employeeByPage = employeeService.getEmployeeByPage(13, 12, condition, null);
        Assert.assertEquals(2,employeeByPage.getData().size());
        //测试用例10，页大小为15，去最后一页，按合同起始日期来进行筛选
        Date[] beginDateScope = new Date[2];
        DateConverter dateConverter=new DateConverter();
        beginDateScope[0]=dateConverter.convert("2017-12-15");
        beginDateScope[1]=dateConverter.convert("2018-01-05");
        employeeByPage = employeeService.getEmployeeByPage(31, 15, nonCondition, beginDateScope);
        Assert.assertEquals(3,employeeByPage.getData().size());
    }

    @Test
    //必须在getEmpByPage之后
    public void addEmp() {
        //测试用例1，添加的记录均为空字段
        Employee toAdd = new Employee();
        Integer result = employeeService.addEmp(toAdd);
        Assert.assertEquals(1,result.intValue());
        //测试用例2，添加的记录的主键与已有记录重复
        toAdd.setId(1941);
        try{
            result = employeeService.addEmp(toAdd);
        }catch (Exception e){
            Assert.assertEquals("class "+"org.springframework.dao.DuplicateKeyException",e.getClass().toString());
        }
        //测试用例3，添加的记录需要的所有字段均不为空
        toAdd.setId(2000);
        toAdd.setName("涂成武");
        toAdd.setGender("男");
        toAdd.setBirthday(new Date());
        toAdd.setIdCard("4986151658149");
        toAdd.setWedlock("未婚");
        toAdd.setNationId(1);
        toAdd.setNativePlace("上海");
        toAdd.setPoliticId(3);
        toAdd.setEmail("tuchengwu19991218@qq.com");
        toAdd.setPhone("17621777283");
        toAdd.setAddress("龙水北路960弄12号404");
        toAdd.setDepartmentId(78);
        toAdd.setJobLevelId(15);
        toAdd.setPosId(33);
        toAdd.setEngageForm("劳务合同");
        toAdd.setTiptopDegree("本科");
        toAdd.setSpecialty("软件工程");
        toAdd.setSchool("同济大学");
        toAdd.setBeginDate(new Date());
        toAdd.setWorkState("在职");
        toAdd.setWorkID("00000065");
        toAdd.setConversionTime(new Date());
        toAdd.setNotWorkDate(null);
        toAdd.setBeginContract(new Date(110,0,24));
        toAdd.setEndContract(new Date(111,0,24));
        toAdd.setWorkAge(1);
        result=employeeService.addEmp(toAdd);
        Assert.assertEquals(1,result.intValue());

    }

    @Test
    public void maxWorkID() {
        int maxId= employeeService.maxWorkID();
        Assert.assertEquals(65,maxId);
    }

    @Test
    //必须在addEmp之后
    public void deleteEmpByEid() {
        //测试用例1，删除主键在记录中
        Integer result = employeeService.deleteEmpByEid(2000);
        Assert.assertEquals(1,result.intValue());
        //测试用例2，删除主键不在记录中
        result=employeeService.deleteEmpByEid(2001);
        Assert.assertEquals(0,result.intValue());
    }

    @Test
    public void updateEmp() {
        //测试用例1，更新主键在记录中
        Employee toUpdate=employeeService.getEmployeeById(1941);
        toUpdate.setWedlock("未婚");
        Integer result = employeeService.updateEmp(toUpdate);
        Assert.assertEquals(1,result.intValue());
        //测试用例2，更新主键不在记录中
        toUpdate.setId(2500);
        result = employeeService.updateEmp(toUpdate);
        Assert.assertEquals(0,result.intValue());
    }

    @Test
    public void addEmps() {
        //测试用例1，传入列表为空表
        List<Employee> toAdd = new ArrayList<Employee>();
        try{
            employeeService.addEmps(toAdd);
        }
        catch (Exception e){
            Assert.assertEquals("class "+"org.springframework.jdbc.BadSqlGrammarException",e.getClass().toString());
        }
        //
    }

    @Test
    public void getEmployeeByPageWithSalary() {
    }

    @Test
    public void updateEmployeeSalaryById() {
    }

    @Test
    public void getEmployeeById() {
    }
}
