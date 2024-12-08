package cn.bugstack.xfg.dev.tech.infrastructure.dao;

import cn.bugstack.xfg.dev.tech.infrastructure.po.UserOrderPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserOrderDao {

    void insert(UserOrderPO userOrderPO);

    void updateOrderStatusByUserId(String userId);

    void updateOrderStatusByUserMobile(String userMobile);

    void updateOrderStatusByOrderId(String orderId);

    UserOrderPO selectById(Long id);

    List<UserOrderPO> selectByUserId(String userId);

    List<UserOrderPO> selectByUserMobile(String userMobile);

    UserOrderPO selectByOrderId(String orderId);

    UserOrderPO selectByOrderIdAndUserId(UserOrderPO userOrderPO);

    UserOrderPO selectByUserIdAndOrderId(UserOrderPO userOrderPO);

    Long queryMaxId();

    List<UserOrderPO> queryPage();

}
