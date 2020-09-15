class Postcard {
  constructor({title = '', message = '', imageUrl = ''}) {
    this._title = title;
    this._message = message;
    this._imageUrl = imageUrl;
    this._stampImage = "../image/logo.png";
  }

  get title() {
    return this._title;
  }

  get message() {
    return this._message;
  }

  get imageUrl() {
    return this._imageUrl;
  }

  addImageElem() {
    let image = document.createElement('img');
    image.className = 'pcard-img';
    image.setAttribute('src', this._imageUrl);
    
    return image;
  }

  addMessageElem() {
    let message = document.createElement('div');
    message.className = 'pcard-msg';
    message.appendChild(document.createTextNode(this._message));

    return message;
  }

  addTitleElem() {
    let title = document.createElement('div');
    title.className = 'pcard-title';
    title.appendChild(document.createTextNode(this._title));
    
    return title;
  }

  addStampElem() {
    let stamp = document.createElement('img');
    stamp.className = 'pcard-stamp';
    stamp.setAttribute('src', this._stampImage);

    return stamp;
  }

  getPostcardHTML() {
    let postcard = document.createElement('div');
    postcard.className = 'pcard-container';
    postcard.id = 'pcard-design';

    postcard.appendChild(this.addStampElem());
    postcard.appendChild(this.addTitleElem());
    postcard.appendChild(this.addMessageElem());
    postcard.appendChild(this.addImageElem());
    return postcard;
  }

  addImageEmailStyle() {
    let image = document.createElement('img');
    image.setAttribute('src', this._imageUrl);
    image.style.height = '200px';
    image.style.width = '250px';

    return image;
  }

  addMessageEmailStyle() {
    let message = document.createElement('div');
    message.style.display = 'inline-block';
    message.style.fontFamily = "'Comic Sans MS', cursive, sans-serif";
    message.style.fontSize = '30px';
    message.style.maxWidth = '300px';
    message.style.width = '250px';
    message.appendChild(document.createTextNode(this._message));

    return message;
  }

  addTitleEmailStyle() {
    let title = document.createElement('div');
    title.style.display = 'inline-block';
    title.style.fontFamily = 'Arial, sans-serif';
    title.style.fontSize = '25px';
    title.style.width = '250px';
    title.appendChild(document.createTextNode(this._title));

    return title;
  }

  // The postcard needs to be already on the page.
  // Getting the postcard directly from getPostcardHTML() does not work.
  getPostcardImage() {
    let node = document.getElementById('pcard-design');

    return domtoimage.toPng(node).then(function (encodedImg) {
      return encodedImg;
    }).catch(function (error) {
      console.error('Something went wrong! Try again!', error);

      // Set default image to notify user on the error.
      image = 'https://live.staticflickr.com/4034/4543895219_8d78eba86f_c.jpg';
      return image;
    });
  }

  createTableElement(width, height) {
      let blank = document.createElement('table');
      blank.setAttribute("cellpadding", "0");
      blank.setAttribute("cellspacing", "0");
      blank.setAttribute("width", width.toString());
      blank.setAttribute("height", height.toString());
      blank.setAttribute("align", "left");

      return blank;
    }

  addBlankTable(width, height) {
    let blank = this.createTableElement(width, height);
    return blank;
  }

  addImageTable() {
    let image = this.createTableElement(320, 280);
    let imageInnerTd = document.createElement('td');

    imageInnerTd.appendChild(this.addImageEmailStyle());
    image.appendChild(imageInnerTd);
    return image;
  }

  addTitleTable() {
    let title = this.createTableElement(320, 30);
    let titleInnerTd = document.createElement('td');

    titleInnerTd.appendChild(this.addTitleEmailStyle());
    title.appendChild(titleInnerTd);
    return title;
  }

  addMessageTable() {
    let message = this.createTableElement(320, 120);
    let messageInnerTd = document.createElement('td');

    messageInnerTd.appendChild(this.addMessageEmailStyle());
    message.appendChild(messageInnerTd);
    return message;
  }

  getPostcardGmailHTML() {
    let postcard = document.createElement('table');
    postcard.setAttribute("cellpadding", "0");
    postcard.setAttribute("cellspacing", "0");
    postcard.setAttribute("width", "640");
    postcard.setAttribute("align", "center");

    let postcardInnerTbody = document.createElement('tbody');
    let postcardInnerTr = document.createElement('tr');
    let postcardInnerTd = document.createElement('td');

    postcardInnerTd.appendChild(this.addBlankTable(640, 150));
    postcardInnerTd.appendChild(this.addImageTable());
    postcardInnerTd.appendChild(this.addBlankTable(320, 120));
    postcardInnerTd.appendChild(this.addTitleTable());
    postcardInnerTd.appendChild(this.addMessageTable());

    postcardInnerTr.appendChild(postcardInnerTd);
    postcardInnerTbody.appendChild(postcardInnerTr);
    postcard.appendChild(postcardInnerTbody);

    return postcard;
  }
}

function displayPostcard(title, message, imageUrl) {
  const bodyElem = document.getElementsByClassName('pcard-container')[0];
  let postcd = new Postcard({title, message, imageUrl}).getPostcardHTML();
  
  postcd.id = "postcard";
  bodyElem.appendChild(postcd);
}
