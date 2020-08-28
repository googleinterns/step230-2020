// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// difact_idibuted under the License is difact_idibuted on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


function login() {
  fetch('/login_page').then(response => response.json()).then(login => {loginDomManipulation(login.message, login.status);});
}

function loginDomManipulation(message, status) {
  const loginElement = document.createElement('div');
  loginElement.className = 'login';
  loginElement.innerHTML = message;

  const page = document.getElementById('login_page');
  page.appendChild(loginElement);

  if (status) {
    document.getElementById('index_content').style.visibility = "visible";
    loginElement.style.marginLeft = "1300px";
  } else {
    document.getElementById('index_content').style.visibility = "hidden";
    loginElement.style.marginLeft = "auto";
    loginElement.style.marginTop = "200px";
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

function sendMailPOST(link, email) {
  fetch('/mail', {
    method: "POST",
    body: "link=" + link + "&" + "mail=" + email,
    headers: {
      "Content-Type": "application/x-www-form-urlencoded"
    },
    credentials: "same-origin"
    }).then(response => response.text()).then(text => {document.getElementById('sent').innerText = text;});
}


function analyseInput() {
  let text = document.getElementsByName('input_text')[0].value;
  let location = document.getElementsByName('location_checkbox')[0].value;
  sendInputPOST(text, location);
}

function generatePostcard() {
  analyseInput();
}


function loadPostcard() {
  AndreiFunction(localStorage["text"], localStorage["link"]);
}

function AndreiFunction(text, link) {
  const actions = document.getElementById('output');
  actions.innerText = "Now choose your receiver and SEND it!";
  //Andrei places the postcard into img with id="postcard"
}

// click and send the postcard
function send() {
  let link = document.getElementById('postcard').src;
  let email = document.getElementsByName('mail')[0].value;
  sendMailPOST(link, email);
}

// code used for adding the location option

let x = document.getElementById('location_checkbox');

function getUserLocation() {
  getLocation();
}


function getLocation() {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(showPosition);
  } else {
    x.value = "none";
  }
}


function showPosition(position) {
  x.value = "Latitude: " + (position.coords.latitude).toString() + "\n" + "Longitude: " + (position.coords.longitude).toString();
}