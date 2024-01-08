package czu.bigdata.flightAnalysis.controller;

import com.alibaba.fastjson.JSONArray;
import czu.bigdata.flightAnalysis.service.WordCountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {
    @Resource
    private final WordCountService wordCountService;

    public TestController( WordCountService wordCountService) {
        this.wordCountService = wordCountService;
    }

    @GetMapping("/wordCount")
    public JSONArray wordCount(@RequestParam String filePath) {
        return wordCountService.wordCount(filePath);
    }
}
