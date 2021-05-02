package com.mez.api.service;

import com.mez.api.models.Post;
import com.mez.api.repository.NewsRepository;
import com.mez.api.tools.DateFormatter;
import com.mez.api.tools.ResponseCodes;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

  private final NewsRepository newsRepository;

  @Autowired
  NewsService(NewsRepository repository) {
    this.newsRepository = repository;
  }

  public Object get(int limit, int offset, boolean withDetails) {
    return withDetails ?
        newsRepository.get(limit, offset) :
        newsRepository.getPreviews(limit, offset);
  }

  public Post getOne(int id) {
    return newsRepository.getById(id);
  }

  public int save(Post post) {
    try {
      post.setDate(DateFormatter.nowDateString());
      return newsRepository.save(post);
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public byte delete(int id) {
    return newsRepository.delete(id);
  }

  public byte update(Post post) {
    return newsRepository.update(post);
  }
}
