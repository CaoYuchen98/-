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

//通过注解来设置servlet 组件的名字
@WebServlet("/update.do")
public class UpdateServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 设置编码
        req.setCharacterEncoding("utf-8");
        //2. 获取参数
        //提交了一个表单，调用req.getParameter获取数据
        String fname = req.getParameter("fname");
        String priceStr = req.getParameter("price");
        int price = Integer.parseInt(priceStr);
        String fcountStr = req.getParameter("fcount");
        Integer fcount = Integer.parseInt(fcountStr);
        String remark = req.getParameter("remark");

        String fidStr = req.getParameter("fid");
        Integer fid = Integer.parseInt(fidStr);

        //3. 执行更新
        Fruit fruit = new Fruit(fid,fname,price,fcount, remark);
        fruitDAO.updateFruit(fruit);

        //4. 更新
        //再返回templateName -> index
        //super.processTemplate("index", req, resp);
        //更新session， 刷新页面
        // 需要重定向，目的是重新给index.Servlet 发请求，重新获得FruitList 获得到 Session 中
        resp.sendRedirect("index");
        //? 内部转发无法改变session??
        //req.getRequestDispatcher("index.html").forward(req, resp);
    }
}
