package com.atguigu.fruit.sevlets;

import com.atguigu.fruit.dao.FruitDAO;
import com.atguigu.myssm.impl.FruitDAOImpl;
import com.atguigu.myssm.myspringmvc.ViewBaseServlet;
import com.atguigu.myssm.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/del.do")
public class DelServlet extends ViewBaseServlet {
    private FruitDAO  fruitDAO= new FruitDAOImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fidStr = req.getParameter("fid");
        if (!StringUtil.isEmpty(fidStr)){
            int fid = Integer.parseInt(fidStr);
            fruitDAO.delFruitByFid(fid);

            //重定向
            resp.sendRedirect("index");
        }
    }
}
