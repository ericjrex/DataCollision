package com.borosoft.ypfx;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.borosoft.middleware.holder.EntityMappingCache;
import com.borosoft.ypfx.dnaCollision.entity.DnaInfo;

@Component
public class Listener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent context) {
		if (context.getApplicationContext().getParent() == null) {
			// 初始化目录映射
			EntityMappingCache entityMappingCache = EntityMappingCache.getInstance();
			entityMappingCache.initEntityMappingByWare(DnaInfo.configCode,DnaInfo.class);
			/*entityMappingCache.initEntityMappingByWare(DnaTaskInfo.configCode,DnaTaskInfo.class);
			entityMappingCache.initEntityMappingByWare(DnaTaskDetailedInfo.configCode,DnaTaskDetailedInfo.class);
			
			entityMappingCache.initEntityMappingByWare(PersonInfoMian.configCode,PersonInfoMian.class);
			entityMappingCache.initEntityMappingByWare(PersonInfoDetailed.configCode,PersonInfoDetailed.class);
			
			entityMappingCache.initEntityMappingByWare(PersonCollisonInfo.configCode,PersonCollisonInfo.class);*/
		}
	}

}
