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

import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/sentiment")
public class RequestServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = request.getParameter("message");

    Document doc =
        Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    float score = sentiment.getScore();
    languageService.close();

    String mood = getMood(score);

    // Output the sentiment score as HTML.
    // A real project would probably store the score alongside the content.
    response.setContentType("text/html;");
    response.getWriter().println("<h1>What's your mood?</h1>");
    response.getWriter().println("<p>You entered: " + message + "</p>");
    response.getWriter().println("<p>Sentiment analysis score: " + score + 
                                 " means that you feel " + mood + ".</p>");
    response.getWriter().println("<p><a href=\"/\">Back</a></p>");

    ClassifyTextResponse analysed = analyseText(message);
    ArrayList<String> categories = new ArrayList<String>();

    for (ClassificationCategory category : analysed.getCategoriesList()) {
      String[] listCategories = category.getName().split("/");
      for (int i = 0; i < listCategories.length; i++) {
        categories.add(listCategories[i]);
        response.getWriter().println("<p>" + listCategories[i] + "</p>");
      }
    }
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
      Document document = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();

      ClassifyTextRequest request = ClassifyTextRequest.newBuilder().setDocument(document).build();
      ClassifyTextResponse response = language.classifyText(request);

      return response;
    }
  }
}