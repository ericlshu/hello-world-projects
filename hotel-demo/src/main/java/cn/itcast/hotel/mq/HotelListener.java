package cn.itcast.hotel.mq;

import cn.itcast.hotel.service.IHotelService;
import cn.itcast.hotel.util.AppConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Description :
 *
 * @author Eric L SHU
 * @date 2022-04-17 15:09
 * @since jdk-11.0.14
 */
@Component
public class HotelListener
{
    @Resource
    private IHotelService hotelService;

    /**
     * 监听酒店新增或修改的业务
     *
     * @param id hotel id
     */
    @RabbitListener(queues = {AppConstants.HOTEL_INSERT_QUEUE})
    public void ListenHotelInsetOrUpdate(Long id)
    {
        hotelService.insertOrUpdateById(id);
    }


    /**
     * 监听酒店新增或修改的业务
     *
     * @param id hotel id
     */
    @RabbitListener(queues = {AppConstants.HOTEL_DELETE_QUEUE})
    public void ListenHotelDelete(Long id)
    {
        hotelService.deleteById(id);
    }
}
