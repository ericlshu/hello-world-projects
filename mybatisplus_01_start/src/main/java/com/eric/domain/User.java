package com.eric.domain;

import lombok.Data;

/**
 * Description :
 *
 * @author Eric SHU
 */
@Data
public class User
{
    private Long id;
    private String name;
    private String password;
    private Integer age;
    private String tel;
}
