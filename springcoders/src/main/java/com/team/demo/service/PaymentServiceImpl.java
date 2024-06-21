package com.team.demo.service;

import com.team.demo.domain.dto.ChargeInfo;
import com.team.demo.mapper.PaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
   
   @Autowired
   private PaymentMapper pmapper;

	@Override
	public boolean insertChargeInfo(ChargeInfo chargeInfo) {
		return pmapper.insertChargeInfo(chargeInfo);
	}
}
