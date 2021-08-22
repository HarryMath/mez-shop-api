package com.mez.api.service;

import com.mez.api.models.Post;
import com.mez.api.repository.NewsRepository;
import com.mez.api.tools.DateFormatter;
import com.mez.api.tools.ImageRepository;
import com.mez.api.tools.ResponseCodes;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

  private final NewsRepository newsRepository;
  private final ImageRepository imageRepository;

  @Autowired
  NewsService(NewsRepository repository, ImageRepository imageRepository) {
    this.newsRepository = repository;
    this.imageRepository = imageRepository;
  }

  public Object get(int limit, int offset, boolean withDetails) {
    return withDetails ?
        newsRepository.get(limit, offset) :
        newsRepository.getPreviews(limit, offset);
  }

  public Post getOne(int id) {
    return newsRepository.getById(String.valueOf(id));
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
    Post post = newsRepository.getById(String.valueOf(id));
    if (post == null) {
      return ResponseCodes.SUCCESS;
    }
    if (post.getPhoto() != null) {
      imageRepository.delete(post.getPhoto());
    }
    return newsRepository.delete(String.valueOf(id));
  }

  public byte update(Post post) {
    try {
      newsRepository.update(post);
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      return ResponseCodes.DATABASE_ERROR;
    }
  }
}
