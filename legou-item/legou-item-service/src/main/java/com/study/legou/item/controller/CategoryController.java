package com.study.legou.item.controller;

import com.legou.core.controller.BaseController;
import com.study.legou.item.po.Category;
import com.study.legou.item.service.CategoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@RestController
@RequestMapping("/item/category")
@CrossOrigin
public class CategoryController extends BaseController<CategoryService, Category> {

    @ApiOperation(value="根据ids查询names", notes = "根据分类id查询名称列表")
    @GetMapping("/names")
    public ResponseEntity<List<String>> queryNameByIds(@RequestParam("ids") List<Long> ids) {
        List<String> names = service.selectNamesByIds(ids);

        if (null == names || names.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok(names);
    }
}
