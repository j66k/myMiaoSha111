package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;
import com.imooc.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @Package: com.imooc.miaosha.exception
 * @ClassName: GlobleExceptionHandler
 * @Author: jjt
 * @CreateTime: 2022/2/25 21:20
 * @Description:
 */

@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

    @ExceptionHandler(value = Exception.class)//拦截所有的异常
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){

        if(e instanceof BindException){
            //如果传入的异常是绑定类型的异常
            //进行强转
            BindException ex = (BindException)e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);//获取第一个错误信息
            String msg = error.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else{
            //如果不是绑定异常
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
