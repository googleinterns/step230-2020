package com.google.sps.servlets;

import com.google.sps.image.ImageSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/images")
public class ImageScrapperServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    List<String> keywords = new ArrayList<>();

    // Temporal hard code
    keywords.add("happy");
    keywords.add("birthday");
    ImageSelection imageSelect = new ImageSelection(keywords);
    
    response.setContentType("text/html;");
    response.getWriter().println(imageSelect.getBestImage());
  }
}
