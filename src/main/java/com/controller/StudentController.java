package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.entity.Student;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.service.StudentService;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


@RestController
public class StudentController {

    private Logger log = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/insertStudent", method = RequestMethod.GET)
    public String insertStudent() {
        Student student = new Student();
        student.setUsername("student");
        student.setPassword("student");
        student.setRegTime(new Date());
        return "" + studentService.insertStudent(student);
    }


    /***
     *  分页查询
     * @param pageNum 页数
     * @param pageSize 页面大小
     */
    @RequestMapping(value = "/selectAllStudent", method = RequestMethod.GET)
    public String selectAllStudent(@Param("pageNum") Integer pageNum,
                                   @Param("pageSize") Integer pageSize,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        // PageHelper.startPage(pageNum, pageSize);
        // List<Student> studentList = studentService.selectAllStudent();
        // PageInfo<Student> studentPageInfo = new PageInfo<>(studentList);

        // pageNum 查询第几页, pageSize 每页的大小(会根据select count(1) 计算总页数)
        PageInfo<String> stringPageInfo = null;
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize); // 特别注意: 只对其后的第一个mapper有效, 为保证线程安全PageHelper和dao层必须作为一个整体
            List<Student> studentList = studentService.selectAllStudent();
            stringPageInfo = new PageInfo(studentList);
        }
        return null != stringPageInfo ? JSONObject.toJSONString(stringPageInfo) : "null";
    }

}