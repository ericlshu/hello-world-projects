package cn.itcast.hotel;

import cn.itcast.hotel.service.IHotelService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
class HotelDemoApplicationTests
{
    @Resource
    private IHotelService hotelService;

    @Test
    void contextLoads()
    {
        Map<String, List<String>> filters = hotelService.filters();
        System.out.println(filters);
        //{
        //  品牌=[7天酒店, 如家, 皇冠假日, 速8, 万怡, 华美达, 和颐, 万豪, 喜来登, 希尔顿, 汉庭, 凯悦, 维也纳, 豪生, 君悦, 万丽, 丽笙],
        //  星级=[二钻, 五钻, 四钻, 五星级, 三钻, 四星级],
        //  城市=[上海, 北京, 深圳]
        //}
    }
}
