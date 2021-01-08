package com.li.dao;


import com.li.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * @author liql
 * @date 2021/1/3
 */
@RunWith(SpringJUnit4ClassRunner.class)//声明为spring提供的单元测试环境
@ContextConfiguration(locations = "classpath:applicationContext.xml")//指定spring容易的配置信息
public class SpecTest {
    @Autowired
    private CustomerDao customerDao;

    /**
     * 根据条件 查询单个对象
     */
    @Test
    public void testFindOne(){
        //匿名内部类
        /**
         * 自定义查询条件
         *  1. 实现specification接口 （提供泛型，查询的对象类型）
         *  2. 实现 toPredicate () 来构造查询条件
         *  3.  需要借助方法参数中的两个参数
         *          root：获取需要查询的对象属性
         *          CriteriaBuilder:用来构造查询条件的 ，内部封装了很多的查询条件
         *              ，比如 模糊匹配 精准匹配等等
         *    案例 查询客户名为
         *          查询条件
         *              查询方式 全部存在 CriteriaBuilder对象中
         *           比较的属性名称；全都在 root 对象中
         */
        Specification<Customer> specification=new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                //1. 获取比较属性的名称  需要什么属性 就写什么属性
                Path<Object> custName = root.get("custName");
                //2.构造查询条件  第一个参数是需要比较的属性（path对象），第二个是 属性值
                Predicate predicate = criteriaBuilder.equal(custName, "李秋亮");//equal代表精准匹配
                return predicate;
            }
        };
        Customer customer = customerDao.findOne(specification);
        System.out.println(customer);

    }

    /**
     * root:获取属性
     * 客户名所属行业cb:构造查询
     * 1.构造客户名的精准匹配查询
     * 2.构造所属行业的精准匹配查询
     * 3.将以上两个查询联系起来
     */
    /**
     * 多条件查询
     */
    @Test
    public void spectifitonTest2(){
        Specification<Customer> spec=new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> custName = root.get("custName");
                Path<Object> custId = root.get("custId");

                Predicate p1 = criteriaBuilder.equal(custName, "李秋亮");
                Predicate p2 = criteriaBuilder.equal(custId, "1");
                //把两个条件联系起来
                Predicate and = criteriaBuilder.and(p1, p2);
                return and;
            }
        };
        Customer customer = customerDao.findOne(spec);
        System.out.println(customer);
    }

    /**
     * 模糊查询
     *  对于非equal  需要制定比较属性的 类型
     *      criteriaBuilder(属性.as(类型class)，...)
     */
    @Test
    public void specificationTest3(){
        Specification<Customer> spec=new Specification<Customer>() {
            @Override
            public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Path<Object> custName = root.get("custName");

                Predicate like = criteriaBuilder.like(custName.as(String.class), "李%");

                return like;
            }
        };
        //添加排序 按id倒叙
        Sort sort=new Sort(Sort.Direction.DESC,"custId");

        //因为模糊查询 所以不止一条
        List<Customer> all = customerDao.findAll(spec,sort);
        all.forEach(System.out::println);
    }

    /**
     * 分页查询
     *      specification 查询条件
     *      pageable: 分页参数 含分页的页码和每页查询的条数
     *      findall(spectifiction,pageable) 带有条件的分页查询
     *      findall(pageable) 没有条件的分页
     * 返回 page对象，这个page对象是springdatajpa为我们封装好的pageBean对象。可以获取数据列表和总条数
     */
    @Test
    public void testPage(){
        Specification<Customer> spec=null;
        //PageRequest对象是 pageable接口的一个实现类
        Pageable pageable=new PageRequest(0, 2);//0实际为第一页
        //分页查询
        Page<Customer> all = customerDao.findAll(spec, pageable);
        all.forEach(System.out::println);
    }
}
