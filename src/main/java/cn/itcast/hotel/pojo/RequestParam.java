package cn.itcast.hotel.pojo;

import lombok.Data;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-16 15:36
 * @since jdk-11.0.14
 */
@Data
public class RequestParam
{
    private String key;
    private Integer page;
    private Integer size;
    private String sortBy;
}
