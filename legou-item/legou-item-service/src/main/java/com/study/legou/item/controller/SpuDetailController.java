package com.study.legou.item.controller;

import com.legou.core.controller.BaseController;
import com.study.legou.item.po.SpuDetail;
import com.study.legou.item.service.SpuDetailService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title:
 */
@RestController
@RequestMapping(value = "/item/spu-detail")
@CrossOrigin
public class SpuDetailController extends BaseController<SpuDetailService, SpuDetail> {

}