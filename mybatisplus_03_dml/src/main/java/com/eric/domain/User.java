package com.eric.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

/**
 * Description :
 * -> @TableName("tbl_user") <--> table-prefix: tbl_
 * -> @TableId(type = IdType.assign_id) <-->  id-type: assign_id
 *
 * @author Eric SHU
 */
@Data
// @TableName("tbl_user")
public class User
{
    // @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    @TableField(value = "pwd", select = false)
    private String password;
    private Integer age;
    private String tel;
    @TableField(exist = false)
    private Integer online;
    // value为正常数据的值，delval为删除数据的值
    // @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    @Version
    private Integer version;
}
