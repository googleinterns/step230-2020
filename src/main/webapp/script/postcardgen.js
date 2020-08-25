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

  getPostcardHTML() {
    const pcardElem = document.createElement('div');
    pcardElem.className = 'pcard-container';
    pcardElem.id = 'pcard-design';

    pcardElem.appendChild(this.addTitleElem());
    pcardElem.appendChild(this.addMessageElem());
    pcardElem.appendChild(this.addImageElem());
    return pcardElem;
  }

  // The postcard needs to be already on the page.
  // Getting the postcard directly from getPostcardHTML() does not work.
  getPostcardImage() {
    let node = document.getElementById('pcard-design');
    let img = document.createElement('img');
    domtoimage.toPng(node).then(function (dataUrl) {
      img.setAttribute('src', dataUrl);
    }).catch(function (error) {
      console.error('oops, something went wrong!', error);
      img.setAttribute('src', '');
    });
    return img;
  }
}

function displayPostcard() {
  const bodyElem = document.getElementsByTagName('body')[0];
  const title = "Happy birthday";
  const message = "Another adventure filled year awaits you.";
  const imgUrl = "https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7";

  bodyElem.appendChild(new Postcard({title, message, imgUrl}).getPostcardHTML());
  document.body.appendChild(new Postcard({title, message, imgUrl}).getPostcardImage());
}
