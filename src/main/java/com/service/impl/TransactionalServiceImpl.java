package com.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dao.StudentMapper;
import com.dao.TeacherMapper;
import com.datasource.annotation.DataSourceAnnotation;
import com.datasource.config.DataSourceConfig;
import com.datasource.entity.DataSourceEnum;
import com.datasource.util.MultiByZeroException;
import com.entity.Student;
import com.entity.Teacher;
import com.service.TransactionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */

@Service
public class TransactionalServiceImpl implements TransactionalService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;


    // 注意下面的事务只能对一个数据源,保持一个service中操作一个数据源的原则,不同源的数据库是不能同时进行事务操作的
    @Override
    @DataSourceAnnotation(DataSourceEnum.SLAVE) // @DataSourceAnnotation可以不写, 默认master数据源
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public String transactionOrg() throws Exception {
        Student student1 = new Student("student_transactionOrg1", "123", new Date());
        Student student2 = new Student("student_transactionOrg2", "123", new Date());
        System.out.println(JSONObject.toJSONString(student1));
        System.out.println(JSONObject.toJSONString(student2));

        Teacher teacher1 = new Teacher("teacher_transactionOrg1", "abc", new Date());
        Teacher teacher2 = new Teacher("teacher_transactionOrg2", "abc", new Date());
        System.out.println(JSONObject.toJSONString(teacher1));
        System.out.println(JSONObject.toJSONString(teacher2));

//        studentService.insertStudent(student1); // ①
        teacherMapper.insertTeacher(teacher1); // ①
        MultiByZeroException.multiByZero();  // 出现ArithmeticException异常, ①不会插入,②不会插入，因为有@Transactional
//        studentService.insertStudent(student2); // ②
        teacherMapper.insertTeacher(teacher2); // ②
        return "1";
    }

}
