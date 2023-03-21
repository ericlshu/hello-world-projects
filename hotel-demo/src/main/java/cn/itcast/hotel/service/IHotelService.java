package cn.itcast.hotel.service;

import cn.itcast.hotel.pojo.Hotel;
import cn.itcast.hotel.pojo.PageResult;
import cn.itcast.hotel.pojo.ReqParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IHotelService extends IService<Hotel>
{
    PageResult search(ReqParam param);
    Map<String, List<String>> filters(ReqParam param);
    List<String> getSuggestion(String prefix);
    void insertOrUpdateById(Long id);
    void deleteById(Long id);
}
