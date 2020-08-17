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
    fetch('/login_page').then(response => response.json()).then((login) => {
        const page = document.getElementById('log');
        const loginElement = document.createElement('div');
        loginElement.className = 'login';
        loginElement.innerHTML = login.message;
        page.appendChild(loginElement);
        if (login.status) {
            document.getElementById("content").style.visibility = "visible";
            loginElement.style.marginLeft = "1300px";
        } else {
            document.getElementById("content").style.visibility = "hidden";
            loginElement.style.marginLeft = "auto";
            loginElement.style.marginTop = "200px";
        }
        const load = document.getElementById('login_page');
        load.style.visibility = "hidden";
    });
}

function loadPostcard() {
  fetch('/text-input').then(response => response.json()).then((input) => {

    const actions = document.getElementById("output");
    actions.innerText = "Now choose your receiver and SEND it!";
    document.getElementById('myImage').src = String(input.link);
    document.forms["input_email"].elements["link"].value = String(input.link);
    document.forms["input_email"].elements["card_text"].value = String(input.text);
    document.getElementById('greating').innerText = String(input.text);
  });
}

function send() {
  fetch('/mail').then(response => response.text()).then((output) => {
      document.getElementById('sent').innerText = String(output);
  });
}