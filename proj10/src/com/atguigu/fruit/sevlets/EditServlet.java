package com.atguigu.fruit.sevlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.fruit.pojo.Fruit;
import com.atguigu.myssm.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/edit.do")
public class EditServlet extends ViewBaseServlet {
    private FruitDAO fruitDAO = new FruitDAOImpl();
    //需要得到发送过来的数据
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 得到发来的数据
        String fidStr = req.getParameter("fid");
        /* 判断字符串为空的方式写成一个方法，放在工具包中
        if (findStr != null && !findStr.equals(""))
         */
        if (!StringUtil.isEmpty(fidStr)){
            int fid = Integer.parseInt(fidStr);
            Fruit fruit = fruitDAO.getFruitByFid(fid);
            //设置请求的作用域：(str, Obj)
            req.setAttribute("fruit", fruit);
            //调用themeleaf去刷新页面,页面名为 edit.html
            super.processTemplate("edit", req, resp);

        }
    }
}
