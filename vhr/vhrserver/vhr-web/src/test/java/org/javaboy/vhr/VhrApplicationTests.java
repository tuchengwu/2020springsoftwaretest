package org.javaboy.vhr;

import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.JobLevel;
import org.javaboy.vhr.service.DepartmentService;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.service.JobLevelService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VhrApplicationTests {
    @Autowired
    HrService hrService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    DepartmentService departmentService;
    @Test
    public void contextLoads() {
//        UserDetails admin = hrService.loadUserByUsername("libai");
//        //注意不能使用assertSame，因为字符串内容相同但地址不同，应该使用assertEquals
//        Assert.assertEquals("用户名不对","libai",admin.getUsername());
//        List<JobLevel> allJobLevels = jobLevelService.getAllJobLevels();
//        System.out.println(allJobLevels);
        Department department = new Department();
        department.setName("测试部门");
        department.setId(1011);
        department.setParentId(-1);
        departmentService.addDep(department);
        departmentService.addDep(department);
    }

}
