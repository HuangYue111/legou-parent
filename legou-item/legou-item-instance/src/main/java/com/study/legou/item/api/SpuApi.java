package com.study.legou.item.api;


import com.study.legou.item.po.Spu;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(value = "/item/spu")
public interface SpuApi {

    @ApiOperation(value="查询所有", notes="查询所有spu")
    @GetMapping("/list-all")
    public List<Spu> selectAll();

    @GetMapping("/edit/{id}")
    public Spu edit(@PathVariable Long id);
}