package com.mez.api.controllers;

import com.mez.api.models.DTO.EngineUpload;
import com.mez.api.models.DTO.PostPreview;
import com.mez.api.models.Post;
import com.mez.api.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsController {

  private final NewsService newsService;

  @Autowired
  NewsController(NewsService service) {
    this.newsService = service;
  }

  @GetMapping("/news")
  public Object getNews(
      @RequestParam(name = "amount", required = false, defaultValue = "999") int limit,
      @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
      @RequestParam(name = "withDetails", required = false, defaultValue = "false") boolean withDetails) {
    return newsService.get(limit, offset, withDetails);
  }

  @GetMapping("/news/{id}")
  public Post getOne(
      @RequestParam(name = "increaseViews", required = false, defaultValue = "false") boolean increaseViews,
      @PathVariable int id
  ) {
    return newsService.getOne(id, increaseViews);
  }

  @PutMapping("/news/create")
  public int createPost(@RequestBody Post post) {
    return newsService.save(post);
  }

  @PutMapping("/news/update")
  public byte updatePost(@RequestBody Post post) {
    return newsService.update(post);
  }

  @GetMapping("/news/{id}/delete")
  public byte deleteEngine(@PathVariable int id) {
    return newsService.delete(id);
  }

}
