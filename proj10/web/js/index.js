function delFruit(fid){
    //如果确认
    if(confirm('是否确认删除?')){
        //当前的网页是window
        //location 指的是地址栏对象
        //href 表示给地址栏属性附上后面的值
        // 给del.do 的Servlet 发请求
        //不是通过表单发送的，所以为 get 请求
        window.location.href='del.do?fid='+fid;
    }
}

function page(pageNo){
    window.location.href="index?pageNo="+pageNo;
}