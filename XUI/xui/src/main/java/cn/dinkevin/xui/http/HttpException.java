package cn.dinkevin.xui.http;

/**
 * HttpPoster 请求异常 </br>
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public class HttpException extends Exception {

    protected int errorCode;

    public HttpException(Exception e){
        this(e.getMessage(),e.getCause());
    }

    public HttpException(int errorCode,String message){
        this(errorCode,message,null);
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(int errorCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.errorCode = errorCode;
    }

    /**
     * 错误码
     * @return
     */
    public int getErrorCode(){
        return errorCode;
    }
}
