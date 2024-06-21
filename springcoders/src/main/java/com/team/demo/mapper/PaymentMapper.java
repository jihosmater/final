package com.team.demo.mapper;

import com.team.demo.domain.dto.ChargeInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentMapper {
   boolean insertChargeInfo(ChargeInfo chargeInfo);
   boolean insertChargeInfo(String apple, int paymentCoin);
   
   boolean userDeletePayment(String userid);
}