package com.atguigu.demo;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//演示application 保存作用域
@WebServlet("/demo05")
public class Demo05Servlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 向application 保存作用域, servlet 上下文
        ServletContext application = req.getServletContext();
        application.setAttribute("uname", "lili");
        // 2. 客户端重定向
        resp.sendRedirect("demo06");
        // 3. 内部转发
        //req.getRequestDispatcher("demo02").forward(req, resp);
    }
}
