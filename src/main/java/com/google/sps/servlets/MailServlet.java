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

    sendMultipartMail(email, to, postcard_link);

    response.setContentType("text/html;");
    response.getWriter().println("Your postcard has been sent!");
  }

  private void sendMultipartMail(String from, String to, String link) {
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
    } catch (MessagingException e) {
      System.err.println("Something went wrong:(");
    } catch (UnsupportedEncodingException e) {
      System.err.println("Something went wrong:(");
    }
  }
}