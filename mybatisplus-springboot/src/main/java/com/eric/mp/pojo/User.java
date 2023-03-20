package com.eric.mp.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-03-15 20:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_user")
public class User extends Model<User> {

    /**
     * 指定id类型为自增长
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标准驼峰标识可以自动识别
     */
    private String userName;

    /**
     * 指定字段不加入查询
     */
    @TableField(select = false)
    private String password;

    private String name;
    private Integer age;

    /**
     * 字段名和属性名不一致时使用
     */
    @TableField("email")
    private String mail;
    /**
     * 属性在数据表中不存在
     */
    @TableField(exist = false)
    private String address;
}
