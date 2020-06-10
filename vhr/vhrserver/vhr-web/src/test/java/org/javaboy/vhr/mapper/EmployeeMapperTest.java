package org.javaboy.vhr.mapper;

import org.javaboy.vhr.converter.DateConverter;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespPageBean;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class EmployeeMapperTest {

    @Autowired
    EmployeeMapper employeeMapper;

    Employee empty = new Employee();
    Employee template = new Employee();
    Employee partEmpty = new Employee();
    int currentSize = -1;

    int ID = 114514;

    @Before
    public void before() {
        currentSize = (int) employeeMapper.getTotal(empty, null).longValue();

        template.setId(ID);
        template.setName("野兽先辈");
        template.setGender("男");
        template.setBirthday(new Date(1145, 1, 4));
        template.setIdCard("114514");
        template.setWedlock("未婚");
        template.setNationId(1);
        template.setNativePlace("上海");
        template.setPoliticId(3);
        template.setEmail("xiabeize@xbz.com");
        template.setPhone("11451490190");
        template.setAddress("下北泽");
        template.setDepartmentId(91);
        template.setJobLevelId(15);
        template.setPosId(33);
        template.setEngageForm("劳务合同");
        template.setTiptopDegree("本科");
        template.setSpecialty("是学生");
        template.setSchool("下北泽综合大学");
        template.setBeginDate(new Date(114, 5, 14));
        template.setWorkState("在职");
        template.setWorkID("114514");
        template.setConversionTime(new Date(114, 5, 14));
        template.setNotWorkDate(null);
        template.setBeginContract(new Date(114, 5, 14));
        template.setEndContract(new Date(114, 5, 14));
        template.setWorkAge(11);

        partEmpty.setId(ID + 1);
        partEmpty.setName("野兽2先辈");
        partEmpty.setGender("男");
        partEmpty.setBirthday(new Date(114, 5, 14));
    }

    @After
    public void after() {
        employeeMapper.deleteByPrimaryKey(ID);
        employeeMapper.deleteByPrimaryKey(ID + 1);
        employeeMapper.deleteByPrimaryKey(ID + 2);
    }

    @Test
    public void deleteByPrimaryKey() {
        employeeMapper.insert(template);
        //1.存在
        Integer re = employeeMapper.deleteByPrimaryKey(ID);
        Assert.assertEquals(1, re.intValue());
        //2.不存在
        re = employeeMapper.deleteByPrimaryKey(ID + 2);
        Assert.assertEquals(0, re.intValue());
    }

    @Test
    public void insert() {
        //1.全空
        Integer re = employeeMapper.insert(empty);
        Assert.assertEquals(1, re.intValue());
        //测试用例2，添加的记录的主键与已有记录重复
        empty.setId(ID + 2);
        Employee emp2 = new Employee();
        emp2.setId(ID + 2);
        try {
            employeeMapper.insert(empty);
            re = employeeMapper.insert(emp2);
        } catch (Exception e) {
            Assert.assertEquals("class " + "org.springframework.dao.DuplicateKeyException", e.getClass().toString());
        }
        //3.正常
        re = employeeMapper.insert(template);
        Assert.assertEquals(1, re.intValue());
        //4.半空
        re = employeeMapper.insert(partEmpty);
    }

    @Test
    public void insertSelective() {
        //
        Integer re = employeeMapper.insertSelective(template);
        Assert.assertEquals(1, re.intValue());
        re = employeeMapper.insertSelective(partEmpty);
        Assert.assertEquals(0, re.intValue());

    }

    @Test
    //不存时间。
    public void selectByPrimaryKey() {
        employeeMapper.insert(template);

        Employee re = employeeMapper.selectByPrimaryKey(ID);

        Assert.assertEquals(re, template);
    }

    @Test
    public void updateByPrimaryKeySelective() {
        employeeMapper.insert(template);
        template.setAddress(null);
        employeeMapper.updateByPrimaryKeySelective(template);
        Employee re = employeeMapper.getEmployeeById(ID);

        template.setAddress("下北泽");
        Assert.assertEquals(template, re);
    }

    @Test
    public void updateByPrimaryKey() {
        employeeMapper.insert(template);
        template.setAddress("上北泽");
        employeeMapper.updateByPrimaryKey(template);
        Employee re = employeeMapper.getEmployeeById(ID);

        Assert.assertEquals("上北泽", re.getAddress());
    }

    @Test
    public void getEmployeeByPage() {
        //测试用例1，页大小为总记录数量，取出第一页，且不设置搜索条件
        Employee nonCondition = new Employee();
        List employeeByPage = employeeMapper.getEmployeeByPage(1, currentSize, nonCondition, null);
        assertEquals(currentSize, employeeByPage.size());
        //测试用例2，页大小为总记录数量，取出第二页，且不设置搜索条件
        employeeByPage = employeeMapper.getEmployeeByPage(2, currentSize, nonCondition, null);
        assertEquals(0, employeeByPage.size());
        //测试用例3，页大小为负数-1
        try {
            employeeByPage = employeeMapper.getEmployeeByPage(2, -1, nonCondition, null);
        } catch (Exception e) {
            assertEquals("class " + "org.springframework.jdbc.BadSqlGrammarException", e.getClass().toString());
        }
        //测试用例4，页大小为10，取第一页，指定姓名为查询条件,且以该名称近似搜索结果不为空
        Employee condition = new Employee();
        condition.setName("江南一点雨");
        employeeByPage = employeeMapper.getEmployeeByPage(1, 10, condition, null);
        assertEquals(10, employeeByPage.size());
        //测试用例5，页大小为20，取最后一页，指定合同类型为劳动合同为查询条件
        condition = new Employee();
        condition.setEngageForm("劳动合同");
        employeeByPage = employeeMapper.getEmployeeByPage(25, 20, condition, null);
        assertEquals(2, employeeByPage.size());
        //测试用例6，页大小为15，取最后一页，指定politicId为3
        condition = new Employee();
        condition.setPoliticId(3);
        employeeByPage = employeeMapper.getEmployeeByPage(12, 15, condition, null);
        assertEquals(3, employeeByPage.size());
        //测试用例7，页大小为10，取最后一页，指定jobLevelId为12
        condition = new Employee();
        condition.setJobLevelId(12);
        employeeByPage = employeeMapper.getEmployeeByPage(18, 10, condition, null);
        assertEquals(4, employeeByPage.size());
        //测试用例8，页大小为8，取最后一页，指定departmentId为82
        condition = new Employee();
        condition.setDepartmentId(82);
        employeeByPage = employeeMapper.getEmployeeByPage(7, 8, condition, null);
        assertEquals(6, employeeByPage.size());
        //测试用例9，页大小为12，取最后一页，指定posId为29
        condition = new Employee();
        condition.setPosId(29);
        employeeByPage = employeeMapper.getEmployeeByPage(13, 12, condition, null);
        assertEquals(2, employeeByPage.size());
        //测试用例10，页大小为15，去最后一页，按合同起始日期来进行筛选
        Date[] beginDateScope = new Date[2];
        DateConverter dateConverter = new DateConverter();
        beginDateScope[0] = dateConverter.convert("2017-12-15");
        beginDateScope[1] = dateConverter.convert("2018-01-05");
        employeeByPage = employeeMapper.getEmployeeByPage(31, 15, nonCondition, beginDateScope);
        assertEquals(3, employeeByPage.size());
    }

    @Test
    public void getTotal() {
        //1.全
        Long re = employeeMapper.getTotal(empty, null);
        assertEquals(currentSize, re.longValue());
        //2.时间段
        Date[] beginDateScope = new Date[2];
        DateConverter dateConverter = new DateConverter();
        beginDateScope[0] = dateConverter.convert("2017-12-15");
        beginDateScope[1] = dateConverter.convert("2018-01-05");
        Long re1 = employeeMapper.getTotal(empty, beginDateScope);
        assertEquals(453, re1.intValue());
        //3.条件
        employeeMapper.insert(template);
        re = employeeMapper.getTotal(partEmpty, null);
        assertEquals(11, re.intValue());
    }

    @Test
    public void addEmps() {
        List list = new ArrayList<Employee>();
        list.add(template);
        list.add(partEmpty);
        employeeMapper.addEmps(list);

        assertEquals(employeeMapper.getTotal(empty, null).intValue(), currentSize + 2);
    }

    @Test
    public void getEmployeeById() {
        //测试用例1，id在表中
        Employee expected = new Employee();
        expected.setId(18);
        expected.setName("王一亭");
        expected.setGender("男");
        expected.setBirthday(new Date(91, Calendar.FEBRUARY, 1));
        expected.setIdCard("610144199102014569");
        expected.setWedlock("已婚");
        expected.setNationId(1);
        expected.setNativePlace("广东");
        expected.setPoliticId(6);
        expected.setEmail("zhangliming@qq.com");
        expected.setPhone("18979994478");
        expected.setAddress("广东珠海");
        expected.setDepartmentId(91);
        expected.setJobLevelId(15);
        expected.setPosId(33);
        expected.setEngageForm("劳动合同");
        expected.setTiptopDegree("高中");
        expected.setSpecialty("考古");
        expected.setSchool("清华大学");
        expected.setBeginDate(new Date(118, Calendar.JANUARY, 1));
        expected.setWorkState("在职");
        expected.setWorkID("00000018");
        expected.setContractTerm((double) 7);
        expected.setConversionTime(new Date(118, Calendar.APRIL, 1));
        expected.setBeginContract(new Date(118, Calendar.JANUARY, 1));
        expected.setEndContract(new Date(125, Calendar.JANUARY, 30));
        Employee result = employeeMapper.getEmployeeById(18);
        assertEquals(expected, result);
        //测试用例2，id不存在表中
        result = employeeMapper.getEmployeeById(2531);
        assertNull(result);
    }

    @Test
    public void getEmployeeByPageWithSalary() {
        //测试用例1，页大小为总记录数量，取出第一页，且不设置搜索条件
        Employee nonCondition = new Employee();
        List employeeByPage = employeeMapper.getEmployeeByPageWithSalary(1, 617);
        assertEquals(617, employeeByPage.size());
        //测试用例2，页大小为总记录数量，取出第二页，且不设置搜索条件
        employeeByPage = employeeMapper.getEmployeeByPageWithSalary(4, 617);
        assertEquals(0, employeeByPage.size());
        //测试用例3，页大小为负数-1
        try {
            employeeByPage = employeeMapper.getEmployeeByPageWithSalary(2, -1);
        } catch (Exception e) {
            assertEquals("class " + "org.springframework.jdbc.BadSqlGrammarException", e.getClass().toString());
        }
    }

    @Test
    public void updateEmployeeSalaryById() {
        //测试用例1，eid存在于empsalary中，并且sid存在于salary中,此时应为更新记录
        Integer result = employeeMapper.updateEmployeeSalaryById(5, 14);
        assertEquals(2, result.intValue());
        //测试用例2，eid不存在empsalary中，但是sid不存在salary中，即违反外键约束
        try {
            employeeMapper.updateEmployeeSalaryById(5, 1);
        } catch (Exception e) {
            assertEquals("class " + "org.springframework.dao.DataIntegrityViolationException", e.getClass().toString());
        }
        //测试用例3，eid不存在empsalary中，sid存在于salary中，此时应为插入
        result = employeeMapper.updateEmployeeSalaryById(48, 14);
        assertEquals(1, result.intValue());
        //测试用例4，eid不存在emplpoyee中，即违反外键约束
        try {
            employeeMapper.updateEmployeeSalaryById(2000, 13);
        } catch (Exception e) {
            assertEquals("class " + "org.springframework.dao.DataIntegrityViolationException", e.getClass().toString());
        }
    }
}
