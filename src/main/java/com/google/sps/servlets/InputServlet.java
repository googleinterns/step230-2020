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
 
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
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
 
    float score = getSentimentScore(input);
    String mood = getMood(score);
    ClassifyTextResponse analysed = analyseText(input);
 
    // list of all the categories that text is about
    // check https://cloud.google.com/natural-language/docs/categories
    ArrayList<String> categories = getCategories(analysed); //Classifying Content
 
    Gson gson = new Gson();
 
    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(mood));
  }
 
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
 
    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }
 
    String input = request.getParameter("message");
    String email = userService.getCurrentUser().getEmail();
 
    requests.put(email, input);
 
    response.sendRedirect("/postcard.html");
  }
 
  public String getMood(float score) {
    if(score == 1) {
      return "super super happy";
    }
 
    if(score == -1) {
        return "so pessimistic";
    }
 
    int position = 0;
    String[] moods = new String[]{"neutral", "calm", "relaxed", "serene", "contented",
                                "joyful", "happy", "elated", "excited", "alert",
                                "tense", "nervous", "stressed", "upset", "sad", 
                                "depressed", "bored", "fatigued", "pessimisctic"};
 
    score = score * 10;
    position = (int) score;
 
    if(position < 0) {
        position = position * (-1) + 9;
    }
 
    return moods[position];
  }
 
  public static ClassifyTextResponse analyseText(String text) throws IOException {
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document document = 
          Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
 
      ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(document).build();
      ClassifyTextResponse response = language.classifyText(request);
 
      return response;
    }
  }
 
  public static float getSentimentScore(String message) throws IOException {
    try (LanguageServiceClient languageService = LanguageServiceClient.create()) {
      Document doc =
          Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
 
      Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
      float score = sentiment.getScore();
 
      return score;
    }
  }
 
  public static ArrayList<String> getCategories(ClassifyTextResponse response) {
    ArrayList<String> categories = new ArrayList<String>();
    for (ClassificationCategory category : response.getCategoriesList()) {
      String[] listCategories = category.getName().split("/");
      for (int i = 0; i < listCategories.length; i++) {
        categories.add(listCategories[i]);
      }
    }
    return categories;
  }
}