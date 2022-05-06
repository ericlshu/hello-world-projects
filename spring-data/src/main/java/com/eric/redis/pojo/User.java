package com.eric.redis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-05-06 23:56
 * @since jdk-11.0.14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User
{
    private String name;
    private Integer age;
}
