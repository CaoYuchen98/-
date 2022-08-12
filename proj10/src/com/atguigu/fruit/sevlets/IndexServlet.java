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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


//Servlet 从3.0版本开始支持注解方式的注册
@WebServlet("/index")
public class IndexServlet extends ViewBaseServlet {
    //直接在网址上访问 index.html
    int pageNum = 5;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //一定要编码
        req.setCharacterEncoding("UTF-8");
        //
        HttpSession session = req.getSession();
        FruitDAO fruitDAO = new FruitDAOImpl();
        Integer pageNo = 1;
        //-----防止错用
        String keyword = null;
        String oper = req.getParameter("oper");
        if (!StringUtil.isEmpty(oper) && "search".equals(oper)) {
            //说明是点击表单查询发送过来的
            //此时 pageCount 应该为1
            pageNo = 1;
            keyword = req.getParameter("keyword");
            if (StringUtil.isEmpty(keyword))
                keyword="";
            session.setAttribute("keyword", keyword);
        }
        else{
            //说明此处不是点击表单查询发送过来的请求，比如点击上一页，下一页
            // 此时keyword 应该从session作用域内获取
            /*
            String pageNoStr = req.getParameter("pageNo");
            if (!StringUtil.isEmpty(pageNoStr))
                pageNo = Integer.parseInt(pageNoStr);
            */
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null)
                keyword = (String)keywordObj;
            else
                keyword="";
        }
        //------分页功能
        //0. 计算总页数
        int fruitCount = fruitDAO.getFruitCount(keyword);
        Integer pageCount = (fruitCount + pageNum - 1) / pageNum;
        //int pageCount = 4;

        session.setAttribute("pageCount", pageCount);
        //1. 获取页码
        //如果不是search，不会传 pageNo 参数，自动跳过
        String pageNoStr = req.getParameter("pageNo");
        if (!StringUtil.isEmpty(pageNoStr)) {
            pageNo = Integer.parseInt(pageNoStr);
            if (pageNo <= 0)
                pageNo = 1;
            else if (pageNo > pageCount)
                pageNo = pageCount;
        }
        //2. 获取session 作用域(这样页面页直到当前页是多少了)
        session.setAttribute("pageNo", pageNo);

        //------

        //测试一下分页的效果
        List<Fruit> fruitList = fruitDAO.getFruitList(keyword, pageNo, pageNum);
        // 保存到 session 作用域
        //HttpSession session = req.getSession();
        // 将查询的结果写入一个map
        session.setAttribute("fruitList", fruitList);
        // 下一步将这个结果展示到页面上
        // 调用父类的processTemplate, thmeleaf 会将逻辑视图名称对应到 物理视图名称上去
        // 逻辑视图名称：index
        // 物理视图名称: view-prefix + 逻辑视图名称 + view-suffix
        // 所以真实的视图名称是:  /        index        .html
        super.processTemplate("index", req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
