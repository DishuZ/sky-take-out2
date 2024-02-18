package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类业务层
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public void save(CategoryDTO categoryDTO) {
        // CategoryDTO -> Category
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category); // 属性拷贝
        // 分类状态默认为禁用状态0
        category.setStatus(0);
        // 设置创建时间、修改时间、创建人、修改人 (x, 由 AutoFill 和 AutoFillAspect 统一自动填充公共字段)
        //category.setCreateTime(LocalDateTime.now());
        //category.setUpdateTime(LocalDateTime.now());
        //category.setCreateUser(BaseContext.getCurrentId());
        //category.setUpdateUser(BaseContext.getCurrentId());
        // 判断该分类名是否已存在
        Category dbCategory = categoryMapper.getByName(category.getName());
        if (dbCategory != null) {
            throw new BaseException(MessageConstant.CATEGORY_EXISTS);
        }

        // 插入 category 表
        categoryMapper.insert(category);
    }

    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        // PageHelper
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        // 查询 category 表
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void deleteById(Long id) {
        // 查询当前分类是否关联了菜品，如果关联了就抛出业务异常
        Integer queryCnt = dishMapper.countByCategoryId(id);
        if (queryCnt > 0) {
            // 当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 查询当前分类是否关联了套餐，如果关联了就抛出业务异常
        queryCnt = setmealMapper.countByCategoryId(id);
        if (queryCnt > 0) {
            // 当前分类下有菜品，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        // 删除 category 表
        categoryMapper.deleteById(id);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //设置修改时间、修改人 (x, 由 AutoFill 和 AutoFillAspect 统一自动填充公共字段)
        //category.setUpdateTime(LocalDateTime.now());
        //category.setUpdateUser(BaseContext.getCurrentId());

        // 判断该分类名是否已存在
        Category dbCategory = categoryMapper.getByName(category.getName());
        if (dbCategory != null) {
            throw new BaseException(MessageConstant.CATEGORY_EXISTS);
        }

        // 更新 category 表
        categoryMapper.updateById(category);
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                // (x, 由 AutoFill 和 AutoFillAspect 统一自动填充公共字段)
                //.updateTime(LocalDateTime.now())
                //.updateUser(BaseContext.getCurrentId())
                .build();

        // 更新 category 表
        categoryMapper.updateById(category);
    }

    @Override
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
