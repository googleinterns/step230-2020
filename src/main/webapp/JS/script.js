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

//import domtoimage from 'dom-to-image';


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

function loadPostcard() {
    fetch('/text-input').then(response => response.json()).then((input) => {AndreiFunction(input.text, input.link);});
}

// approach designed to share the greting and the image link with the mail servlet
function AndreiFunction(text, link) {
    const actions = document.getElementById('output');
    actions.innerText = "Now choose your receiver and SEND it!";
}

// click and send the postcard
function send() {
  fetch('/mail').then(response => response.text()).then((output) => {
      document.getElementById('sent').innerText = String(output);
  });
}

// code which will be used for adding the location option

/*let x = document.getElementById('geolocation_demo');

function getUserLocation() {
    getLocation();
}


function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        // ...
        //x.innerHTML = "Geolocation is not supported by this browser.";
    }
}


function showPosition(position) {
    // ...
    //x.innerHTML = "Latitude: " + position.coords.latitude + "<br>Longitude: " + position.coords.longitude;
}*/
