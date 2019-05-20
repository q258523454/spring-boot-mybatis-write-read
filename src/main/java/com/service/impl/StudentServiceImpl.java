package com.service.impl;

import com.dao.StudentMapper;
import com.datasource.annotation.DataSourceAnnotation;
import com.datasource.entity.DataSourceEnum;
import com.entity.Student;
import com.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created By
 *
 * @author :   zhangj
 * @date :   2019-02-22
 */

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper mapper;

    @Override
    public Integer selectAllCount() {
        return mapper.selectAllCount();
    }

    @Override
    //@DataSourceAnnotation(DataSourceEnum.Master)
    // 默认是master数据库
    public List<Student> selectAllStudent() {
        return mapper.selectAllStudent();
    }

    @Override
    @DataSourceAnnotation(DataSourceEnum.MASTER)
    public int insertStudent(Student student) {
        return mapper.insertStudent(student);
    }

}
