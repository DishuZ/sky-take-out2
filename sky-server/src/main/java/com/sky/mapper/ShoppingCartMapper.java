package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    /**
     * 条件查询购物车数据
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新购物车商品数量
     * @param shoppingCart
     */
    @Update("UPDATE shopping_cart SET number = #{number} WHERE id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     * @param shoppingCart
     */
    @Insert("INSERT INTO shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) VALUES (#{name}, #{image}, #{userId}, #{dishId}, #{setmealId}, #{dishFlavor}, #{amount}, #{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     * @param currentId
     */
    @Delete("DELETE FROM shopping_cart WHERE user_id = #{userId}")
    void deleteByUserId(Long currentId);

    void delete(ShoppingCart shoppingCart);

    /**
     * 批量插入购物车数据
     * @param shoppingCartList
     */
    void insertBatch(List<ShoppingCart> shoppingCartList);
}
