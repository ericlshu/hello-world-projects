package cn.itcast.hotel.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-16 15:38
 * @since jdk-11.0.14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult
{
    private Long total;
    private List<HotelDoc> hotels;
}
