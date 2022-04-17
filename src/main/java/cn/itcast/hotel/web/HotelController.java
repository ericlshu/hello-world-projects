package cn.itcast.hotel.web;

import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.ReqParam;
import cn.itcast.hotel.service.IHotelService;
import org.springframework.web.bind.annotation.*;

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
    public PageResult search(@RequestBody ReqParam param)
    {
        return hotelService.search(param);
    }

    @PostMapping("/filters")
    public Map<String, List<String>> filters(@RequestBody ReqParam param)
    {
        return hotelService.filters(param);
    }

    @GetMapping("/suggestion")
    public List<String> getSuggestion(@RequestParam("key") String prefix)
    {
        return hotelService.getSuggestion(prefix);
    }
}
