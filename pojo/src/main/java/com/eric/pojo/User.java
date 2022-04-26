package com.eric.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-26 22:37
 * @since jdk-11.0.14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable
{
    private int id;
    private String username;
    private String password;
}
