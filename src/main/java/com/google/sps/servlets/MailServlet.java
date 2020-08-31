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

  static final String msgBody = "...";
  static final String subject = "You've received a postcard!";
  static final String sender = "GPostcard";
  static final String receiver = "You";

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();

    // Only logged-in users can post messages
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String email = userService.getCurrentUser().getEmail();
    String postcard_link = request.getParameter("link");
    String to = request.getParameter("mail");

    String resp = sendMultipartMail(email, to, postcard_link);

    response.setContentType("text/html;");
    response.getWriter().println(resp);
  }

/**
* This function creates an email and sends it to the user
**/

  private String sendMultipartMail(String from, String to, String link) {
    Properties props = new Properties();
    Session session = Session.getDefaultInstance(props, null);

    try {
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(from, sender));
      msg.addRecipient(Message.RecipientType.TO,
                       new InternetAddress(to, receiver));
      msg.setSubject(subject);
      msg.setText(msgBody);

      String htmlBody = "<img src = " + "\"" + link + "\"" + "style=\"width:400px\">";

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