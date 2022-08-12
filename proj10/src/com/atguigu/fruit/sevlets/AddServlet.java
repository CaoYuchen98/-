package com.atguigu.fruit.sevlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/add.do")
public class AddServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 编码
        req.setCharacterEncoding("UTF-8");
        //2. 获取数据
        String fname = req.getParameter("fname");
        Integer price = Integer.parseInt(req.getParameter("price"));
        Integer fcount = Integer.parseInt(req.getParameter("fcount"));
        String remark = req.getParameter("remark");
        //3. 构筑Fruit对象
        Fruit fruit = new Fruit(0, fname, price, fcount, remark);
        //4. 调用fruitDAO 的add 方法
        fruitDAO.addFruit(fruit);
        //5. 重定向
        resp.sendRedirect("index");

    }
}
