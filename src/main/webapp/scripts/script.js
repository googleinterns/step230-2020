function record() {
  const recognition = new webkitSpeechRecognition();
  const message = document.getElementById("recording");

  recognition.lang = "en-GB";

  recognition.onresult = function(event) {
    document.getElementById("message").value = event.results[0][0].transcript;
  }

  recognition.addEventListener('nomatch', function() { 
    message.innerText = "SPEECH NOT RECOGNISED";
  });

  recognition.onaudiostart = function() {
    message.innerText = "RECORDING...";
  }

  recognition.onaudioend = function() {
    message.innerText = "DONE RECORDING";
  }

  recognition.onerror = function() {
    message.innerText = "SPEECH NOT RECOGNISED";
  }

  recognition.start();
}