package org.caesar.common.response;

import lombok.*;
import lombok.experimental.Accessors;
import org.caesar.common.enums.ReturnCode;


/**
 * 公共业务返回
 * @author GuoPeng
 */
@Data
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ApiResponse<T>{
    /**响应编码：0成功；-1系统异常；*/
    private int code;
    /**响应结果描述*/
    private String message;

    /**业务数据*/
    private T data;

    private final long timestamp = System.currentTimeMillis();

    /**
     * 无业务数据的成功响应
     */
    public static ApiResponse<Void> success() {
        return success(null);
    }

    public ApiResponse(int code, String message,  T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 带业务数据的成功响应
     * @param data
     * @return <T>
     */
    public static <F> ApiResponse<F> success(F data) {
        return new ApiResponse<F>(ReturnCode.SUCCESS.getCode(), ReturnCode.SUCCESS.getMsg(), data);
    }

    /**
     *判断请求是否成功
     * @return true 成功  false 失败
     */
    public Boolean isSuccess(){
        return ReturnCode.SUCCESS.getCode().equals(this.code);
    }


    /**
     * 响应失败
     * @return
     */
    public static ApiResponse<Void> fail(String msg) {
        return fail(ReturnCode.FAIL.getCode(), msg);
    }


    /**
     * 响应失败 带参数 描述
     * @param failCode
     * @param msg
     */
    public static ApiResponse<Void> fail(int failCode, String msg) {
        ApiResponse<Void> response = new ApiResponse();
        response.setCode(failCode);
        response.setMessage(msg);
        return response;
    }
    /**
     * 响应失败
     */
    public static ApiResponse<Void> fail() {
        return fail(ReturnCode.FAIL.getCode(), ReturnCode.FAIL.getMsg());
    }

    /**
     * 响应失败 带返回编码
     * @param responseCode
     */
    public static ApiResponse<Void> fail(ReturnCode responseCode) {
        return fail(responseCode.getCode(), responseCode.getMsg());
    }

}
