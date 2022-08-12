package com.atguigu.fruit.dao;

import com.atguigu.fruit.pojo.Fruit;

import java.util.List;

//dao 与数据库交互
public interface FruitDAO {

    //获取库存列表,默认public abstract
    List<Fruit> getFruitList();
    //重载为新的方法，获取指定页码，每页显示五条
    List<Fruit> getFruitList(Integer pageNo, Integer pageNum);
    //重载为新的方法，添加关键字
    List<Fruit> getFruitList(String keyword, Integer pageNo, Integer pageNum);
    //新增库存
    boolean addFruit(Fruit fruit);
    //修改库存
    boolean updateFruit(Fruit fruit);
    //根据名称查询指定库存
    Fruit getFruitByFname(String fname);
    //根据id 查询指定库存
    Fruit getFruitByFid(Integer fid);
    //删除特定库存记录.根据名字删除
    boolean delFruit(String fname);
    //根据指定的id删除库存记录

    boolean delFruitByFid(Integer fid);

    int getFruitCount();
    int getFruitCount(String keyword);
}
