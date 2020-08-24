function addImage(imgLink) {
  const imgElem = document.createElement('img');
  imgElem.className = 'pcard-img';
  imgElem.setAttribute('src', imgLink);
  
  return imgElem;
}

function addMessage(messageTxt) {
  const msgElem = document.createElement('div');
  msgElem.className = 'pcard-msg';
  msgElem.appendChild(document.createTextNode(messageTxt));

  return msgElem;
}

function addTitle(titleTxt) {
  const titleElem = document.createElement('div');
  titleElem.className = 'pcard-title';
  titleElem.appendChild(document.createTextNode(titleTxt));

  return titleElem;
}

function addPcardElement(titleTxt, msgTxt, imgLink) {
  const pcardElem = document.createElement('div');
  pcardElem.className = 'pcard-container';
  pcardElem.id = 'pcard-design';

  pcardElem.appendChild(addTitle(titleTxt));
  pcardElem.appendChild(addMessage(msgTxt));
  pcardElem.appendChild(addImage(imgLink));
  return pcardElem;
}

function getPostcard() {
  const bodyElem = document.getElementsByTagName('body')[0];
  const title = "Happy birthday";
  const message = "Another adventure filled year awaits you.";
  const image = "https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7";
  bodyElem.appendChild(addPcardElement(title, message, image));
}

function genPostcard() {
  const node = document.getElementById('pcard-design');

  domtoimage.toPng(node).then(function (dataUrl) {
    const img = new Image();
    img.src = dataUrl;
    document.body.appendChild(img);
  }).catch(function (error) {
    console.error('oops, something went wrong!', error);
  });
}
