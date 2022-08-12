package com.atguigu.myssm.impl;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.base.BaseDAO;

import java.util.List;

//特别地，子类重写的方法不能抛出比父类更大的异常
//访问修饰符不允许缩小，异常不允许扩大
public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {

    @Override
    public int getFruitCount(String keyword) {
        return ((Long)super.executeComplexQuery("select count(*) from t_fruit where fname like ? or remark like ?","%"+keyword+"%", "%"+keyword+"%")[0]).intValue();
    }

    @Override
    public int getFruitCount() {
        return ((Long)super.executeComplexQuery("select count(*) from t_fruit")[0]).intValue();
        //return 1;
    }

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo, Integer pageNum) {
        String sql = "select * from t_fruit where fname like ? or remark like ? limit ?, ?";
        Integer idx = (pageNo-1)*pageNum;
        return super.executeQuery(sql, "%"+keyword+"%", "%"+keyword+"%", idx, pageNum);
    }

    @Override
    public List<Fruit> getFruitList(Integer pageNo, Integer pageNum) {
        // (pageNo-1)*pageNum, pageNum
        String sql = "select * from t_fruit limit ?, ?";
        Integer idx = (pageNo-1)*pageNum;
        //返回一个 List<Fruit>
        return super.executeQuery(sql, idx, pageNum);
    }

    @Override
    public List<Fruit> getFruitList() {
        //System.out.println("主键值是dwa");
        //return null;
        return super.executeQuery("select * from t_fruit");
    }
    @Override
    public boolean addFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(0, ?, ?, ?, ?)";
        int key = super.executeUpdate(sql, fruit.getFname(),fruit.getPrice(),fruit.getFcount(),fruit.getRemark());
        //System.out.println("主键值是" + key);
        //insert语句返回的是自增列的id值而不是影响行数
        return key > 0;
    }

    @Override
    public boolean updateFruit(Fruit fruit) {
        String sql = "update t_fruit set fname=?, price=?, fcount = ?,remark=? where fid = ?";
        //Str和Integer可以混杂，因为接受的是Object... params
        return super.executeUpdate(sql,fruit.getFname(),fruit.getPrice(), fruit.getFcount(), fruit.getRemark(),fruit.getFid()) > 0;
    }

    @Override
    public Fruit getFruitByFname(String fname){
        List<Fruit> a;
        a = super.executeQuery("select * from t_fruit where fname like ?", fname);
        if (a == null || a.size() == 0)
            return null;
        return a.get(0);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        List<Fruit> a;
        //一定要写一个 ? 作为占位符
        a = super.executeQuery("select * from t_fruit where fid = ?", fid);
        if (a == null || a.size() == 0)
            return null;
        return a.get(0);
    }

    @Override
    public boolean delFruit(String fname) {
        String sql = "delete from t_fruit where fname = ?";
        return super.executeUpdate(sql, fname) > 0;
    }

    @Override
    public boolean delFruitByFid(Integer fid) {
        String sql = "delete from t_fruit where fid = ?";
        return super.executeUpdate(sql, fid) > 0;
    }
}
