class Postcard {
  constructor({title = '', message = '', imgUrl = ''}) {
    this._title = title;
    this._message = message;
    this._img = imgUrl;
  }

  get title() {
    return this._title;
  }

  get message() {
    return this._message;
  }

  get img() {
    return this._img;
  }

  addImageElem() {
    const imgElem = document.createElement('img');
    imgElem.className = 'pcard-img';
    imgElem.setAttribute('src', this._img);
    
    return imgElem;
  }

  addMessageElem() {
    const msgElem = document.createElement('div');
    msgElem.className = 'pcard-msg';
    msgElem.appendChild(document.createTextNode(this._message));

    return msgElem;
  }

  addTitleElem() {
    const titleElem = document.createElement('div');
    titleElem.className = 'pcard-title';
    titleElem.appendChild(document.createTextNode(this._title));

    return titleElem;
  }

  getPostcard() {
    const pcardElem = document.createElement('div');
    pcardElem.className = 'pcard-container';
    pcardElem.id = 'pcard-design';

    pcardElem.appendChild(this.addTitleElem());
    pcardElem.appendChild(this.addMessageElem());
    pcardElem.appendChild(this.addImageElem());
    return pcardElem;
  }
}

function displayPostcard() {
  const bodyElem = document.getElementsByTagName('body')[0];
  const title = "Happy birthday";
  const message = "Another adventure filled year awaits you.";
  const image = "https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7";

  bodyElem.appendChild(new Postcard({title, message, image}).getPostcard());
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
