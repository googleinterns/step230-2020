 //  Copyright 2019 Google LLC
// 
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
// 
//      https:// www.apache.org / licenses / LICENSE-2.0
// 
//  Unless required by applicable law or agreed to in writing, software
//  difact_idibuted under the License is difact_idibuted on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.


if (document.getElementById('input-text')) {
    document.getElementById('input-text').addEventListener('input', function() {
        let el = document.getElementById('result-user');
        el.innerHTML = "<text>" + document.getElementById('input-text').value.length + "/310" + "</text>";
        }, false);
}

function login() {
  fetch('/login-page').then(response => response.json()).then(login => {loginDomManipulation(login.message, login.status, login.email);});
}

function loginDomManipulation(message, status, email) {
  if (status) {
    document.getElementById('index-content').style.visibility = "visible";

    const menuList = document.getElementById("menu");
    const menuLogoutElement = document.createElement("li");
    const menuEmailElement = document.createElement("li");

    menuLogoutElement.innerHTML = message;
    menuLogoutElement.className = "logout";
    menuEmailElement.innerText = "(" + email + ")";
    menuEmailElement.className = "email"

    menuList.appendChild(menuLogoutElement);
    menuList.appendChild(menuEmailElement);
  } else {
    document.getElementById('index-content').style.visibility = "hidden";

    const loginElement = document.createElement('div');
    const page = document.getElementById('login-page');

    loginElement.className = 'login';
    loginElement.innerHTML = "<h1>Welcome to GPostcard!</h1>" +
                            "<h4>To get to use the app, please login first.</h4>" +
                            "<p><button class='login-button'>" + message + "</button></p>";
    page.appendChild(loginElement);
  }

  const load = document.getElementById('loading');
  load.style.visibility = "hidden";
}

function sendInputPOST(text, location) {
  fetch('/text-input', {
    method: "POST",
    body: "input_text=" + text+"&"+"location_checkbox=" + location,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    credentials: "same-origin"
    }).then(response => response.json()).then(input => {
      localStorage["text"] = input.text; 
      localStorage["link"] = input.link;
      document.getElementById('link').click();});
}

function sendMailPOST(title, message, image, email) {
  fetch('/mail', {
    method: "POST",
    body: "title=" + title + "&message=" + message + "&image=" + image + "&mail=" + email,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    credentials: "same-origin"
    }).then(response => response.text()).then(text => {document.getElementById('sent').innerText = text;});
}


function analyseInput() {
  let text = document.getElementsByName('input-text')[0].value;
  let location = document.getElementsByName('location-checkbox')[0].value;
  sendInputPOST(text, location);
}

function generatePostcard() {
  let loader = document.getElementById("loader-animation");
  loader.className = "loader";
  
  analyseInput();
}

function loadPostcard() {
  displayPostcard("", localStorage["text"], localStorage["link"]);
}

 //  click and send the postcard
function send() {
  const title = "";
  const message = localStorage["text"];
  const imageUrl = localStorage["link"];

  let email = document.getElementsByName('mail')[0].value;
  sendMailPOST(title, message, imageUrl, email);
}

//  code used for adding the location option

let x = document.getElementById('location-checkbox');

function getUserLocation() {
  getLocation();
}


function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(geocodeLatLng);
  } else {
    x.value = "none";
  }
}

function geocodeLatLng(position) {
  const geocoder = new google.maps.Geocoder();
  const latlng = {
    lat: position.coords.latitude,
    lng: position.coords.longitude
  };
 
  geocoder.geocode(
    {
      location: latlng
    },
    (results, status) => {
      if (status === "OK") {
        if (results[0]) {
          x.value = results[0].address_components[2].long_name + " " +
                 results[0].address_components[5].long_name;
        } else {
          window.alert("No results found");
        }
      } else {
        window.alert("Geocoder failed due to: " + status);
      }
    }
  );
}

function record() {
  const recognition = new webkitSpeechRecognition();
  const message = document.getElementById("recording-message");
  const recButton = document.getElementById("recording");
  recognition.lang = "en-GB";

  recButton.style.backgroundColor = "green";

  recognition.onresult = function(event) {
    document.getElementById("input-text").value = event.results[0][0].transcript;
  }

  recognition.addEventListener('nomatch', function() { 
    message.innerText = "SPEECH NOT RECOGNISED";
  });

  recognition.onaudiostart = function() {
    message.innerText = "RECORDING...";
  }

  recognition.onaudioend = function() {
    message.innerText = "DONE RECORDING";
    recButton.style.backgroundColor = "red";
  }

  recognition.onerror = function() {
    message.innerText = "SPEECH NOT RECOGNISED";
  }

  recognition.start();
}
