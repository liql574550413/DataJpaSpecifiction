package com.li.dao;

/**
 * @author liql
 * @date 2021/1/3
 */

import com.li.entity.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author liql
 * @date 2021/1/3
 * 符合SpringDataJpa的dao层接口规范
 * JpaRepository<操作的实体类类型， 实体类中主键属性的类型>
 *     封装了crud操作
 * JpaSpecificationExecutor<操作的实体类类型>
 *     封装了复杂查询的操作（分页）
 */
  //按住ctrl+f12  有惊喜
public interface CustomerDao extends JpaRepository<Customer,Long>, JpaSpecificationExecutor<Customer> {
    /*写到这个层次就已经具备增删改查功能了*/



}
