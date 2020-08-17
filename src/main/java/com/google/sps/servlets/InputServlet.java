// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.sps.image.ImageSelection;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.image.ImageSelection;
import com.google.sps.data.TextAnalyser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/text-input")
public class InputServlet extends HttpServlet {

  Map<String, String> requests;

  public void init() {
     requests = new HashMap<String, String>();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    String email = userService.getCurrentUser().getEmail();
    
    String input = requests.get(email);

    TextAnalyser textAnalyser = new TextAnalyser(input);

    ImageSelection imageSelect = new ImageSelection(textAnalyser.getKeyWords());

    response.setContentType("text/html;");
    response.getWriter().println(imageSelect.getBestImage());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String input = request.getParameter("word");
    String email = userService.getCurrentUser().getEmail();

    requests.put(email, input);

    response.sendRedirect("/postcard.html");
  }
}