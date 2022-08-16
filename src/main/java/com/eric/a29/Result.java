package com.eric.a29;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result
{
    private int code;
    private String msg;
    private Object data;

    @JsonCreator
    private Result(@JsonProperty("code") int code, @JsonProperty("data") Object data)
    {
        this.code = code;
        this.data = data;
    }

    private Result(int code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public static Result ok(Object data)
    {
        return new Result(200, data);
    }

    public static Result error(String msg)
    {
        return new Result(500, "服务器内部错误:" + msg);
    }
}
