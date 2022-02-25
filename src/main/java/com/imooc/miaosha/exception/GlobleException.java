package com.imooc.miaosha.exception;

import com.imooc.miaosha.result.CodeMsg;

/**
 * @Package: com.imooc.miaosha.exception
 * @ClassName: ClobleException
 * @Author: jjt
 * @CreateTime: 2022/2/25 21:49
 * @Description:
 */
//全局的异常
public class GlobleException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private  CodeMsg cm;
    public GlobleException(CodeMsg cm) {

        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }

    public void setCm(CodeMsg cm) {
        this.cm = cm;
    }
}
