package com.study.legou.search.client;
import com.study.legou.item.api.SpuApi;
import com.study.legou.item.po.Spu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "item-service", fallback = SpuClient.SpuClientFallback.class)
public interface SpuClient extends SpuApi {

    @Component
    @RequestMapping("/spu-fallback") //这个可以避免容器中requestMapping重复
    class SpuClientFallback implements SpuClient {

        private static final Logger LOGGER = LoggerFactory.getLogger(SpuClientFallback.class);

        @Override
        public List<Spu> selectAll() {
            LOGGER.error("异常发生，进入fallback方法");
            return null;
        }

        @Override
        public Spu edit(Long id) {
            return null;
        }
    }
}