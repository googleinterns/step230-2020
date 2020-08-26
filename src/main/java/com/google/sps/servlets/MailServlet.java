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


// [START simple_includes]
import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// [END simple_includes]

// [START multipart_includes]
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import javax.activation.DataHandler;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
// [END multipart_includes]

import com.google.sps.data.Postcard;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.util.Map;
import java.util.HashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/mail")
public class MailServlet extends HttpServlet {

  Map<String, Postcard> emails;

  public void init() {
     emails = new HashMap<String, Postcard>();
  }

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    String from = userService.getCurrentUser().getEmail();

    Postcard card = emails.get(from);
    String to = card.to;
    String text = card.text;
    String link = card.link;

    sendMultipartMail(from, to, link, text);

    resp.setContentType("text/html;");
    resp.getWriter().println("Your postcard has been sent!");
  }

  private void sendMultipartMail(String from, String to, String link, String text) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    String msgBody = "...";

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from, "Example.com Admin"));
      msg.addRecipient(Message.RecipientType.TO,
                       new InternetAddress(to, "Mr. User"));
      msg.setSubject("You've received a postcard!");
      msg.setText(msgBody);

      String htmlBody = "<div style=\"display:flex;flex-direction:row;justify-content:center;\">" +
                    "<img src = " + "\"" + link + "\"" + "style=\"width:400px\">" +
                    "<div style=\"width:200px; border:thin solid black;\">" +
                      "<text style=\"display:flex;flex-wrap:wrap;justify-content:center;padding:30px;\">" + text + "</text>" +
                    "</div>" +
                  "</div>";

      // [START multipart_example]
      //String htmlBody = "<img src = " + "\"" + link + "\"" + " style=\"height:200px\"" + ">";
      //String htmlBody = "<img id=\"myImage\" src = \"https://www.w3schools.com/js/pic_bulboff.gif\" style=\"width:500px\">";          // ...
      byte[] attachmentData = null;  // ...
      Multipart mp = new MimeMultipart();

      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(htmlBody, "text/html");
      mp.addBodyPart(htmlPart);

      /*MimeBodyPart attachment = new MimeBodyPart();
      InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
      attachment.setFileName("manual.pdf");
      attachment.setContent(attachmentDataStream, "application/pdf");
      mp.addBodyPart(attachment);*/

      msg.setContent(mp);
      // [END multipart_example]

      Transport.send(msg);

    } catch (AddressException e) {
      // ...
    } catch (MessagingException e) {
      // ...
    } catch (UnsupportedEncodingException e) {
      // ...
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    //String reciever = request.getParameter("mail");
    String email = userService.getCurrentUser().getEmail();
    String text = request.getParameter("card_text");
    String link = request.getParameter("link");
    String to = request.getParameter("mail");

    Postcard card = new Postcard(text, link, to);

    //emails.put(email, reciever);
    emails.put(email, card);

    response.sendRedirect("/postcard.html");
  }
}