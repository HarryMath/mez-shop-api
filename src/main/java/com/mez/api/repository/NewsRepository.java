package com.mez.api.repository;

import com.mez.api.models.DTO.PostPreview;
import com.mez.api.models.Post;
import com.mez.api.tools.DAO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsRepository extends Repository<Post> {

  @Autowired
  NewsRepository(DAO dao) {
    super(dao, "news");
  }

  public List<Post> get(int limit, int offset) {
    return dao.executeListQuery(
        "SELECT * FROM news ORDER BY id LIMIT " + limit + " OFFSET " + offset,
        Post.class);
  }

  public List<PostPreview> getPreviews(int limit, int offset) {
    return dao.executeListQuery(
        "SELECT id, title, date, beforePhotoText, photo FROM news ORDER BY id LIMIT " + limit + " OFFSET " + offset,
        PostPreview.class);
  }

}
