package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入单员工
     * @param employee
     */
    @Insert("INSERT INTO employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user, status) VALUES (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser}, #{status})")
    @AutoFill(value = OperationType.INSERT)
    void insert(Employee employee);

    /**
     * 分页查询
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 根据id更新员工
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateById(Employee employee);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("SELECT * from employee WHERE id = #{id}")
    Employee getById(Long id);
}
