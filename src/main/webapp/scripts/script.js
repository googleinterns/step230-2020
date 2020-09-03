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

function empty() {
  let elem = document.getElementById('input_text');
  elem.value = "";
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
  displayPostcard("Hello!", localStorage["text"], localStorage["link"]);
}

/*function ShowPostcard(title, text, link) {
  
  const actions = document.getElementById('output');
  actions.innerText = "Now choose your receiver and SEND it!";
  //TODO(Andrei): type in an actual code 
}*/

// click and send the postcard
function send() {
  const title = "";
  const message = localStorage["text"];
  const imageUrl = localStorage["link"];

  let postcard = new Postcard({title, message, imageUrl}).getPostcardHTML().outerHTML;
  console.log(postcard);

  let email = document.getElementsByName('mail')[0].value;
  sendMailPOST(postcard, email);
}

// code used for adding the location option

let x = document.getElementById('location_checkbox');

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