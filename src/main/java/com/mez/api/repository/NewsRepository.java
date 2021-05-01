package com.mez.api.repository;

import com.mez.api.models.DTO.PostPreview;
import com.mez.api.models.Post;
import com.mez.api.tools.DAO;
import com.mez.api.tools.ResponseCodes;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NewsRepository {

  DAO dao;
  @Autowired
  NewsRepository(DAO dao) {
    this.dao = dao;
    try {
      this.dao.openConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int save(Post post) throws SQLException {
    dao.executeUpdate(
        "INSERT INTO news (title, date, beforePhotoText, photo, afterPhotoText, tags) " +
            "values ( \"" +
            post.getTitle() + "\" , \"" +
            post.getDate() + "\", \"" +
            post.getBeforePhotoText() + "\", \"" +
            post.getPhoto() + "\", \"" +
            post.getAfterPhotoText() + "\", \"" +
            post.getTags() + "\");"
    ); // return id of saved post
    return (int) dao.countQuery("SELECT max(id) FROM news");
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

  public byte update(Post post) {
    try {
      dao.executeUpdate( "UPDATE news SET " +
          "title = \"" + post.getTitle() + "\", " +
          "beforePhotoText = \"" + post.getBeforePhotoText() + "\", " +
          "photo = \"" + post.getPhoto() + "\" " +
          "afterPhotoText = \"" + post.getAfterPhotoText() + "\" " +
          "WHERE id = " + post.getId() + ""
      );
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public byte delete(int id) {
    try {
      dao.executeUpdate("DELETE FROM photos WHERE engineId = " + id);
      dao.executeUpdate("DELETE FROM characteristics WHERE engineId = " + id);
      dao.executeUpdate("DELETE FROM engines WHERE id = " + id);
      return ResponseCodes.SUCCESS;
    } catch (SQLException e) {
      e.printStackTrace();
      return ResponseCodes.DATABASE_ERROR;
    }
  }

  public Post getById(int id) {
    return dao.executeQuery("SELECT * FROM posts WHERE id = " + id, Post.class);
  }

}
