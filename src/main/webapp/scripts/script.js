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

// click and send the postcard
function send() {
  const title = "Hello";
  const message = localStorage["text"];
  const imageUrl = localStorage["link"];

  let email = document.getElementsByName('mail')[0].value;
  sendMailPOST(title, message, imageUrl, email);
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

var body_colors=new Array("#E8E8E8", "#CFCFCF", "#E6E4C1", "#DEBDBD", "#B4D1B4",
                          "#FFEABF", "#FFD7A3", "#D6C8E0", "#B9D8ED");
var hex_col = new Array('0','1','2','3','4','5','6','7','8','9',
                        'A','B','C','D','E','F')
var pause = 2000;
var speed = 20;
var steps = 40;
var step = 1;

var red_col_1, red_col_2, red_col_1_b, red_col_2_b;
var green_col_1, green_col_2, green_col_1_b, green_col_2_b;
var blue_col_1, blue_col_2, blue_col_1_b, blue_col_2_b;

var rgb_red_from, rgb_green_from, rgb_blue_from;
var rgb_red_to, rgb_green_to, rgb_blue_to;
var rgb_red_now, rgb_green_now, rgb_blue_now;

var rgb_red_from_b, rgb_green_from_b, rgb_blue_from_b;
var rgb_red_to_b, rgb_green_to_b, rgb_blue_to_b;
var rgb_red_now_b, rgb_green_now_b, rgb_blue_now_b;

var color_A = 0;
var color_B = 1;
var color_C = 1;
var color_D = 2;

var browser_infos = navigator.userAgent;
var ie_4 = document.all&&!document.getElementById;
var ie_5 = document.all&&document.getElementById&&!browser_infos.match(/Opera/);
var ns_4 = document.layers;
var ns_6 = document.getElementById&&!document.all;
var Opera = browser_infos.match(/Opera/);
var browser_ok = ie_4||ie_5||ns_4||ns_6||Opera;

function translate_rgb() {
    var hexa = body_colors[color_A];
    var hexa_red = hexa.substring(1,3);
    var hexa_green = hexa.substring(3,5);
    var hexa_blue = hexa.substring(5,7);
    rgb_red_from = parseInt("0x" + hexa_red);
    rgb_green_from = parseInt("0x" + hexa_green);
    rgb_blue_from = parseInt("0x" + hexa_blue);
    rgb_red_now = rgb_red_from;
    rgb_green_now = rgb_green_from;
    rgb_blue_now = rgb_blue_from;

    var hexa = body_colors[color_B];
    var hexa_red = hexa.substring(1,3);
    var hexa_green = hexa.substring(3,5);
    var hexa_blue = hexa.substring(5,7);
    rgb_red_to = parseInt("0x" + hexa_red);
    rgb_green_to = parseInt("0x" + hexa_green);
    rgb_blue_to = parseInt("0x" + hexa_blue);

    var hexa = body_colors[color_C];
    var hexa_red = hexa.substring(1,3);
    var hexa_green = hexa.substring(3,5);
    var hexa_blue = hexa.substring(5,7);
    rgb_red_from_b = parseInt("0x" + hexa_red);
    rgb_green_from_b = parseInt("0x" + hexa_green);
    rgb_blue_from_b = parseInt("0x" + hexa_blue);
    rgb_red_now_b = rgb_red_from_b;
    rgb_green_now_b = rgb_green_from_b;
    rgb_blue_now_b = rgb_blue_from_b;

    var hexa = body_colors[color_D];
    var hexa_red = hexa.substring(1,3);
    var hexa_green = hexa.substring(3,5);
    var hexa_blue = hexa.substring(5,7);
    rgb_red_to_b = parseInt("0x" + hexa_red);
    rgb_green_to_b = parseInt("0x" + hexa_green);
    rgb_blue_to_b = parseInt("0x" + hexa_blue);

    color_A++;
    color_B++;
    color_C++;
    color_D++;

    if (color_A>=body_colors.length) color_A = 0;
    if (color_B>=body_colors.length) color_B = 0;
    if (color_C>=body_colors.length) color_C = 0;
    if (color_D>=body_colors.length) color_D = 0;

    change();
}

function change() {
    rgb_red_now = rgb_red_now - ((rgb_red_from-rgb_red_to)/speed);
    rgb_green_now = rgb_green_now - ((rgb_green_from-rgb_green_to)/speed);
    rgb_blue_now = rgb_blue_now - ((rgb_blue_from-rgb_blue_to)/speed);

    rgb_red_now_b = rgb_red_now_b - ((rgb_red_from_b-rgb_red_to_b)/speed);
    rgb_green_now_b = rgb_green_now_b - ((rgb_green_from_b-rgb_green_to_b)/speed);
    rgb_blue_now_b = rgb_blue_now_b - ((rgb_blue_from_b-rgb_blue_to_b)/speed);

    if (rgb_red_now>255) rgb_red_now = 255;
    if (rgb_red_now<0) rgb_red_now = 0;
    if (rgb_green_now>255) rgb_green_now = 255;
    if (rgb_green_now<0) rgb_green_now = 0;
    if (rgb_blue_now>255) rgb_blue_now = 255;
    if (rgb_blue_now<0) rgb_blue_now = 0;

    if (rgb_red_now_b>255) rgb_red_now_b = 255;
    if (rgb_red_now_b<0) rgb_red_now_b = 0;
    if (rgb_green_now_b>255) rgb_green_now_b = 255;
    if (rgb_green_now_b<0) rgb_green_now_b = 0;
    if (rgb_blue_now_b>255) rgb_blue_now_b = 255;
    if (rgb_blue_now_b<0) rgb_blue_now_b = 0;

    if (step<=speed) {
        red_col_1 = hex_col[Math.floor(rgb_red_now/16)];
         red_col_2 = hex_col[Math.floor(rgb_red_now)%16];
        green_col_1 = hex_col[Math.floor(rgb_green_now/16)];
        green_col_2 = hex_col[Math.floor(rgb_green_now)%16];
        blue_col_1 = hex_col[Math.floor(rgb_blue_now/16)];
        blue_col_2 = hex_col[Math.floor(rgb_blue_now)%16];

        red_col_1_b = hex_col[Math.floor(rgb_red_now_b/16)];
         red_col_2_b = hex_col[Math.floor(rgb_red_now_b)%16];
         green_col_1_b = hex_col[Math.floor(rgb_green_now_b/16)];
        green_col_2_b = hex_col[Math.floor(rgb_green_now_b)%16];
        blue_col_1_b = hex_col[Math.floor(rgb_blue_now_b/16)];
        blue_col_2_b = hex_col[Math.floor(rgb_blue_now_b)%16];
        var back_color = "#" + red_col_1 + red_col_2 + green_col_1 +
                        green_col_2 + blue_col_1 + blue_col_2;
        var back_color_b = "#" + red_col_1_b + red_col_2_b + green_col_1_b +
                        green_col_2_b + blue_col_1_b + blue_col_2_b;
        if (ie_5) document.body.style.filter =
            "progid:DXImageTransform.Microsoft.Gradient(startColorstr=" +
            back_color + ", endColorstr=" + back_color_b + ")"
        else document.body.style.background = back_color;

        step++;
        var timer = setTimeout("change()", steps);
    }
    else {
        clearTimeout(timer);
        step = 1;
        var timer=setTimeout("translate_rgb()", pause);
    }
}

if (browser_ok) translate_rgb();
