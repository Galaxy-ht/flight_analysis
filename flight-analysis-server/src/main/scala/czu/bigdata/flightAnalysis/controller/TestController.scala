//package czu.bigdata.flightAnalysis.controller
//
//import com.alibaba.fastjson.JSONArray
//import czu.bigdata.flightAnalysis.service.WordCountService
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestParam
//import org.springframework.web.bind.annotation.RestController
//import javax.annotation.Resource
//
//
//@RestController class TestController(@Resource private val wordCountService: WordCountService) {
//  @GetMapping(Array("/wordCount")) def wordCount(@RequestParam filePath: String): JSONArray = wordCountService.wordCount(filePath)
//}
