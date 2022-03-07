package com.imooc.miaosha.result;

/**
 * @Package: com.imooc.miaosha.result
 * @ClassName: CodeMsg
 * @Author: jjt
 * @CreateTime: 2022/2/24 11:32
 * @Description:
 */
public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    //底下这些是写好的CodeMsg类型的常量，已经封装好了内容
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常:%s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg REQUEST_TOMUCH = new CodeMsg(500103, "访问过于频繁");


    //登录模块 5002XX

    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(500213, "手机号错误");
    public static CodeMsg MOBILE_NOTEXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "session错误");

    //商品模块 5003XX

    //订单模块 5004XX
    public static CodeMsg NO_ORDER = new CodeMsg(500400, "订单为空");
    //秒杀模块 5005XX
    public static CodeMsg MIAOSHA_OVER = new CodeMsg(500500, "库存不足，商品已经秒杀完毕");
    public static CodeMsg MIAOSHA_REPEAT = new CodeMsg(500501, "不能重复进行秒杀");
    public static CodeMsg MIAOSHA_FAIL= new CodeMsg(500502, "抱歉秒杀失败");


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }

    public CodeMsg fillArgs(Object... args){//变参数的写法

        int code = this.code;
        String message = String.format(this.msg,args);//原始的msg拼接上传入的参数
        return new CodeMsg(code,message);
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

