package com.study.legou.item.controller;

import com.legou.core.controller.BaseController;
import com.study.legou.item.po.Sku;
import com.study.legou.item.service.SkuService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Title:
 */
@RestController
@RequestMapping(value = "/item/sku")
@CrossOrigin
public class SkuController extends BaseController<SkuService, Sku> {


}