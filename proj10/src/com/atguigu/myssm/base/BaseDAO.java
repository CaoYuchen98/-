package com.atguigu.myssm.base;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//某一个具体的DAO类在继承时需要指明T
public abstract class BaseDAO<T> {
    //如果有订单相关的DAO，也需要这四个属性，用户相关的DAO也是这四个属性
    public final String DRIVER = "com.mysql.jdbc.Driver";
    public final String URL = "jdbc:mysql://localhost:3306/fruitdb?useSSL=false&useUnicode=true&characterEncoding=utf-8";
    public final String USER = "root";
    public final String PWD = "123456";

    //放到成员变量更方便
    protected Connection conn;
    protected PreparedStatement psmt;
    protected ResultSet rs;

    //简化代码量，抽象重复的步骤为方法
    protected Connection getConn() {
        try {
            // 1. 加载驱动
            Class.forName(DRIVER);
            // 2. 通过驱动管理器获取连接对象
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 关闭连接
    protected void close(Connection conn, PreparedStatement psmt, ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
            if (psmt != null)
                psmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //不定参数，把不一样的sql传过来, 对于insert语句，返回自增的id
    protected int executeUpdate(String sql, Object... params) {
        boolean insertFlag = false;
        //判断是否为插入语句
        insertFlag = sql.trim().toUpperCase().startsWith("INSERT");
        try {
            conn = getConn();
            if (insertFlag){
                psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            }
            else{
                psmt = conn.prepareStatement(sql);
            }
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++)
                    //注意set的索引从1开始
                    psmt.setObject(i + 1, params[i]);
            }
            //添加成功返回true
            int count =  psmt.executeUpdate();
            rs = psmt.getGeneratedKeys();
            if (rs.next()){
                return ((Long)rs.getLong(1)).intValue();
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, psmt, rs);
        }
        return 0;
    }

    //设置一个Class, 表示T的Class对象
    private Class entityClass;

    public BaseDAO() {
        //getClass() 获取 子类new FruitDAOImpl 的Class 对象
        //子类构造方法内部会先调用父类的无参构造方法
        //getGenericSuperclass 获取到的是父类的 Class 的 type
        Type genericType = getClass().getGenericSuperclass();
        //参数化类型
        Type[] getActualTypeArguments = ((ParameterizedType) genericType).getActualTypeArguments();
        //只有一个参数化的类型
        Type actualType = getActualTypeArguments[0];
        // getTypeName() 打印出全类名 !!!
        System.out.println(actualType.getTypeName());
        //调用反射创建entityClass对象
        try {
            entityClass = Class.forName(actualType.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //查询操作抽象,//使用泛型
    protected List<T> executeQuery(String sql, Object... params) {
        // 返回的不一定是Fruit
        List<T> list = new ArrayList<>();
        // 1.加载驱动(不抛出，捕获异常)
        try {
            conn = getConn();
            // 4. 编写预处理对象
            psmt = conn.prepareStatement(sql);
            //4.5. 可能需要填充参数
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++)
                    //注意set的索引从1开始
                    psmt.setObject(i + 1, params[i]);
            }
            // 5. 执行查询
            rs = psmt.executeQuery();

            //通过rs 可以获取结果集的元数据，各个列(什么类型)
            ResultSetMetaData rsmd = rs.getMetaData();
            int line = rsmd.getColumnCount();
            // 6. 解析rs
            while (rs.next()) {
                //!. 通过 Class 对象创建实例对象, newInstance() 返回的是Object对象
                T entity = (T) entityClass.newInstance();
                for (int i = 0; i < line; i++) {
                    //获取列名
                    String columnName = rsmd.getColumnName(i + 1);
                    Object columnValue = rs.getObject(i + 1);
                    //把colimnValue 赋给entity 的 columnName 属性
                    //通过反射技术给obj对象的property属性写入value值
                    Class clazz = entity.getClass();
                    Field field = clazz.getDeclaredField(columnName);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(entity, columnValue);
                    }
                }
                list.add(entity);
                //!!!!不知道一共多行列
                //Object fid = rs.getObject(1);
                //String fname = rs.getString(2);
                //int price = rs.getInt(3);
                //int fcount = rs.getInt(4);
                //String remark = rs.getString(5);
                // 如何得到泛型的实例对象!!!
                //Fruit fruit = new Fruit(fid, fname, price, fcount, remark);
                // 添加list
                //dataList.add(fruit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            //!!!!  7. 释放资源
            close(conn, psmt, rs);
        }
        return list;
    }

    //执行复杂查询，返回统计结果
    protected Object[] executeComplexQuery(String sql, Object... params) {
        try {
            conn = getConn();
            // 4. 编写预处理对象
            psmt = conn.prepareStatement(sql);
            //4.5. 可能需要填充参数
            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++)
                    //注意set的索引从1开始
                    psmt.setObject(i + 1, params[i]);
            }
            // 5. 执行查询
            rs = psmt.executeQuery();
            //通过rs 可以获取结果集的元数据，各个列(什么类型)
            ResultSetMetaData rsmd = rs.getMetaData();
            int line = rsmd.getColumnCount();
            Object[] columnValueArr = new Object[line];
            if (rs.next()) {
                for (int i = 0; i < line; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    columnValueArr[i] = columnValue;
                }
                return columnValueArr;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            //!!!!  7. 释放资源
            close(conn, psmt, rs);
        }
        return null;
    }
}
