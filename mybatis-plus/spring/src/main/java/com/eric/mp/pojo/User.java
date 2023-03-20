package com.eric.mp.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 12:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_user")
public class User {
    private Long id;
    private String userName;
    private String password;
    private String name;
    private Integer age;
    private String email;
}
