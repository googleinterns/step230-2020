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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.TextAnalyser;
import com.google.sps.data.Output;

import java.io.IOException;

import java.util.Set;
import java.util.HashSet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* This class gets an input text and location from 
* the client and analyses it with TextAnalyser. Then it
* select an image with ImageSelection class and returns
* an image link and a text to the client. These will be placed
* on a postcard for the user.
**/

@WebServlet("/text-input")
public class InputServlet extends HttpServlet {

  private static final int ANALYSATION_DEPTH = 5;

  private static final int EXTRACTED_IMAGES = 3;

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String input_text = request.getParameter("input_text");
    String user_location = request.getParameter("location_checkbox");
    TextAnalyser textAnalyser = new TextAnalyser(input_text);
    Set<String[]> setsOfKeyWords;

    // If user ticks location, every set of keywords will contain that location
    if (user_location.equals("none") || user_location.equals(null)) {
      user_location = "";
      setsOfKeyWords = textAnalyser.getSetsOfKeyWords();
    } else {
      setsOfKeyWords = new HashSet<String[]>();
      for (String[] keywords : textAnalyser.getSetsOfKeyWords()) {
        setsOfKeyWords.add(new String[] {user_location, keywords[0], keywords[1]});
      }
    }
    
    ImageSelection imageSelect = new ImageSelection(setsOfKeyWords);

    Output output = new Output(input_text, imageSelect.getBestImage(ANALYSATION_DEPTH, EXTRACTED_IMAGES).get(0));

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(output));
  }
}
