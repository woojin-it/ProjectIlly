package com.c.illy.product.machine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.c.illy.product.ProductService;
import com.c.illy.product.ProductVO;
import com.c.illy.product.coffee.CoffeeVO;

@Service
public class MachineService extends ProductService {
	
	@Autowired
	private MachineRepository machineRepository;
	
	@Override
	public int setInsertProduct(ProductVO productVO) throws Exception {
		return machineRepository.setInsertProduct(productVO);
	}
	@Override
	public int setUpdateProduct(ProductVO productVO) throws Exception {
		return machineRepository.setUpdateProduct(productVO);
	}

	public MachineVO getSelectMachineOne(ProductVO productVO) throws Exception {
		return machineRepository.getSelectMachineOne(productVO);
	}

}
