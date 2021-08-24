package com.mez.api.repository;

import com.mez.api.models.DTO.PostPreview;
import com.mez.api.models.Post;
import com.mez.api.tools.DAO;
import java.sql.SQLException;
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
        "SELECT * FROM news ORDER BY id DESC LIMIT " + limit + " OFFSET " + offset,
        Post.class);
  }

  public List<PostPreview> getPreviews(int limit, int offset) {
    return dao.executeListQuery(
        "SELECT id, title, date, beforePhotoText, photo, views FROM news ORDER BY id DESC LIMIT " + limit + " OFFSET " + offset,
        PostPreview.class);
  }

  public void increaseViews(int id) {
    try {
      dao.executeUpdate("UPDATE news SET views = views + 1 WHERE id = " + id);
    } catch (SQLException ignore) { }
  }

}
