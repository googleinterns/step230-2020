class Postcard {
  constructor({title = '', message = '', imageUrl = ''}) {
    this._title = title;
    this._message = message;
    this._imageUrl = imageUrl;
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
    title = this.addTitleStyles(title);
    
    return title;
  }

  getPostcardHTML() {
    let postcard = document.createElement('div');
    postcard.className = 'pcard-container';
    postcard.id = 'pcard-design';

    postcard.appendChild(this.addTitleElem());
    postcard.appendChild(this.addMessageElem());
    postcard.appendChild(this.addImageElem());
    return postcard;
  }

  addImageGmailStyles(imageElement) {
    imageElement.style.height = '200px';
    imageElement.style.width = '250px';

    return imageElement;
  }

  addMessageGmailStyles(messageElement) {
    messageElement.style.display = 'inline-block';
    messageElement.style.fontFamily = "'Comic Sans MS', cursive, sans-serif";
    messageElement.style.fontSize = '30px';
    messageElement.style.maxWidth = '300px';
    messageElement.style.width = '250px';

    return messageElement;
  }

  addTitleGmailStyles(titleElement) {
    titleElement.style.display = 'inline-block';
    titleElement.style.fontFamily = 'Arial, sans-serif';
    titleElement.style.fontSize = '25px';
    titleElement.style.width = '250px';

    return titleElement;
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

  
}

function displayPostcard(title, message, imageUrl) {
  const bodyElem = document.getElementsByClassName('pcard-container')[0];
  //const title = "Happy birthday";
  //const message = "Another adventure filled year awaits you. I wish you a very happy and full-filled birthday!";
  //const imageUrl = "https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7";
  let postcd = new Postcard({title, message, imageUrl}).getPostcardHTML();
  postcd.id = "postcard";
  bodyElem.appendChild(postcd);
}

