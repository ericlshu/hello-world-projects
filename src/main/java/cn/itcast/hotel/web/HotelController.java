package cn.itcast.hotel.web;

import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.RequestParam;
import cn.itcast.hotel.service.IHotelService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-16 15:38
 * @since jdk-11.0.14
 */
@RestController
@RequestMapping("/hotel")
public class HotelController
{
    @Resource
    private IHotelService hotelService;

    @PostMapping("/list")
    public PageResult search(@RequestBody RequestParam param)
    {
        return hotelService.search(param);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> filters(@RequestBody RequestParam param)
    {
        return hotelService.filters(param);
    }
}
