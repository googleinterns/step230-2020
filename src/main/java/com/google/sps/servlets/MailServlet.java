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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import java.util.Properties;

import javax.activation.DataHandler;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* This class gets an email and a postcard link from the client
* and then sends a message to it's receiver.
**/

@WebServlet("/mail")
public class MailServlet extends HttpServlet {

  static final String MSG_BODY = "...";
  static final String SUBJECT = "You've received a postcard!";
  static final String SENDER = "GPostcard";
  static final String RECEIVER = "You";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String email = userService.getCurrentUser().getEmail();
    String title = request.getParameter("title");
    String message = request.getParameter("message");
    String image = request.getParameter("image");

    String to = request.getParameter("mail");

    String resp = sendMultipartMail(email, to, title, message, image);

    response.setContentType("text/html;");
    response.getWriter().println(resp);
  }

/**
* This function creates an email and sends it to the user
**/

  private String sendMultipartMail(String from, String to, String title, String message, String image) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);
    final String POSTCARD_CONTAINER = "<div class='pcard-container' id='pcard-design' style='background-attachment: scroll; background-image: url(\"https://i.ibb.co/JjqsjjL/postcard.jpg\"); background-repeat: no-repeat; background-size: 700px 500px; color: black; display: block; height: 500px; margin-left: auto; margin-right: auto; position: relative; text-align: center; width: 700px;'>";
    
    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from, SENDER));
      msg.addRecipient(Message.RecipientType.TO,
                       new InternetAddress(to, RECEIVER));
      msg.setSubject(SUBJECT);
      msg.setText(MSG_BODY);

      /*
       * Problem: Somethimes application/x-www-form-urlencoded does NOT send the entire string.
       * First solution: Use Java hard-coded html elements, insted of sending them from the front-end.
       * TODO: Try to send JSON insted of application/x-www-form-urlencoded.
       **/
      final String htmlBody = POSTCARD_CONTAINER + 
                  "<table cellpadding='0' cellspacing='0' width='640' align='center'><tbody><tr><td><table cellpadding='0' cellspacing='0' width='640' height='150' align='left'></table><table cellpadding='0' cellspacing='0' width='320' height='280' align='left'><td>" +
                  "<img src=" + image +" style='height: 200px; width: 250px;'></td>" +
                  "</table><table cellpadding='0' cellspacing='0' width='320' height='120' align='left'></table><table cellpadding='0' cellspacing='0' width='320' height='30' align='left'><td><div style='display: inline-block; font-family: Arial, sans-serif; font-size: 25px; width: 250px;'>" +
                  title + "</div></td></table><table cellpadding='0' cellspacing='0' width='320' height='120' align='left'>" +
                  "<td><div style='display: inline-block; font-family: &quot;Comic Sans MS&quot;, cursive, sans-serif; font-size: 30px; max-width: 300px; width: 250px;'>" + 
                  message + "</div></td></table></td></tr></tbody></table>" +
                  "</div>";

      byte[] attachmentData = null;  
      Multipart mp = new MimeMultipart();
      
      MimeBodyPart htmlPart = new MimeBodyPart();
      htmlPart.setContent(htmlBody, "text/html");
      mp.addBodyPart(htmlPart);

      msg.setContent(mp);

      Transport.send(msg);

    } catch (AddressException e) {
      System.err.println("Not existing address");
      return "Not existing address";
    } catch (MessagingException e) {
      System.err.println("Something went wrong:(");
      return "Something went wrong:(";
    } catch (UnsupportedEncodingException e) {
      System.err.println("Something went wrong:(");
      return "Something went wrong:(";
    }
    return "Your postcard has been sent!";
  }
}
