package czu.bigdata.flightAnalysis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误编码
 *
 * @author 阿沐 babamu@126.com
*/
@Getter
@AllArgsConstructor
public enum ErrorCode {
    CODE_200(200, "请求成功"),
    CODE_404(404, "请求地址不存在"),
    CODE_500(500, "服务器返回错误，请联系管理员"),
    CODE_600(600, "请求参数错误");

    private final int code;
    private final String msg;
}
