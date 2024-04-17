package org.codecrafterslab.unity.exception.core;

import org.codecrafterslab.unity.exception.api.Module;
import org.codecrafterslab.unity.exception.api.ModuleStatus;

/**
 * @author Wu Yujie
 */
public enum BizStatus implements ModuleStatus {

    /**
     * 操作成功返回值
     */
    OK("SUCCESS", 200) {
        @Override
        public int getCode() {
            return 0;
        }
    },

    /* 基础定义 */
    UN_IMPLEMENTED_METHOD("未实现的方法"),
    OUT_OF_MODULE_CODE_RANGE("%s 超出模块编码范围 %s"),
    OUT_OF_ERROR_CODE_RANGE("%s 超出错误编码范围 %s"),

    /* 认证相关 */
    UNAUTHORIZED("未经认证的用户", 401),
    INVALID_BEARER_TOKEN("无效的令牌", 412),
    ACCOUNT_EXPIRED("账户密钥已过期"),
    ACCOUNT_DISABLED("账户已被禁用"),
    ACCOUNT_LOCKED("账户已被锁定"),
    ACCOUNT_EXISTS("用户已存在"),
    USERNAME_NOT_FOUND("用户不存在"),
    BAD_CREDENTIALS("密钥凭证错误", 400),

    INVALID_USERNAME_OR_PASSWORD("无效的用户名或密码", 400),
    OAUTH2_AUTHENTICATION_EXCEPTION("OAuth2 认证异常", 400),
    AUTHENTICATION_PROVIDER_NOT_FOUND("未找到相应的认证处理器"),
    UNBOUND_USER("未绑定系统用户"),
    ALREADY_BOUND_USER("已绑定其他系统用户，请解绑后重新绑定"),
    INCORRECT_TEMPORARY_AUTHORIZATION_CODE("错误的临时授权码", 400),
    FORBIDDEN("没有访问权限", 403),

    NOT_FOUND("请求接口地址不存在", 404),
    BAD_REQUEST("错误的请求", 400),
    UN_SUPPORTED_METHOD("不支持的请求方法", 405),
    INTERNAL_SERVER_ERROR("服务器内部错误"),
    JSON_SERIALIZE_EXCEPTION("序列化异常"),
    JSON_DESERIALIZE_EXCEPTION("反序列化异常"),
    
    ENTITY_VALIDATED_ERROR("实体属性校验错误", 400),
    REQUEST_ADDRESS_NOT_MATCH("请求地址与实体ID不一致"),
    INSERT_DATA_ERROR("新增数据异常"),
    UPDATE_DATA_ERROR("更新数据异常"),
    DELETE_DATA_ERROR("删除数据异常"),

    RECORD_EXISTS("已存在该条记录相关数据"),
    RECORD_NOT_EXISTS("指定记录不存在"),

    ;
    private String message;

    private final int status;

    BizStatus(String message, int status) {
        this.message = message;
        this.status = status;
    }

    BizStatus(String message) {
        this(message, 500);
    }

    @Override
    public Module getModule() {
        return new Module() {
            @Override
            public boolean internal() {
                return true;
            }

            @Override
            public int getValue() {
                return 0;
            }
        };
    }

    @Override
    public int getMask() {
        return this.ordinal();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public BizStatus message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public int getHttpStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "[" + this.getCode() + "]" + this.message;
    }

}
