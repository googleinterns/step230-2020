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

  addImageStyles(imageElement) {
    imageElement.style.bottom = '150px';
    imageElement.style.height = '200px';
    imageElement.style.left = '50px';
    imageElement.style.position = 'absolute';
    imageElement.style.width = '250px';

    return imageElement;
  }

  addImageElem() {
    let image = document.createElement('img');
    image.className = 'pcard-img';
    image.setAttribute('src', this._imageUrl);
    image = this.addImageStyles(image);
    
    return image;
  }

  addMessageStyles(messageElement) {
    messageElement.style.bottom = '90px';
    messageElement.style.display = 'inline-block';
    messageElement.style.fontFamily = 'Arial, sans-serif';
    messageElement.style.fontSize = '20px';
    messageElement.style.maxWidth = '300px';
    messageElement.style.position = 'absolute';
    messageElement.style.right = '50px';
    messageElement.style.width = '250px';

    return messageElement;
  }

  addMessageElem() {
    let message = document.createElement('div');
    message.className = 'pcard-msg';
    message.appendChild(document.createTextNode(this._message));
    message = this.addMessageStyles(message);

    return message;
  }

  addTitleStyles(titleElement) {
    titleElement.style.bottom = '200px';
    titleElement.style.display = 'inline-block';
    titleElement.style.fontFamily = 'Arial, sans-serif';
    titleElement.style.fontSize = '25px';
    titleElement.style.fontWidth = '300px';
    titleElement.style.position = 'absolute';
    titleElement.style.right = '50px';
    titleElement.style.width = '250px';

    return titleElement;
  }

  addTitleElem() {
    let title = document.createElement('div');
    title.className = 'pcard-title';
    title.appendChild(document.createTextNode(this._title));
    title = this.addTitleStyles(title);
    
    return title;
  }

  addPostcardStyles(postcardElement) {
    // postcardElement.style.backgroundAttachment = 'scroll';
    // postcardElement.style.backgroundImage = "url(" + "https://i.ibb.co/JjqsjjL/postcard.jpg" +")";
    // postcardElement.style.backgroundRepeat = 'no-repeat';
    // postcardElement.style.backgroundSize = '700px 500px';
    // postcardElement.style.color = 'black';
    // postcardElement.style.display = 'block';
    // postcardElement.style.height = '500px';
    // postcardElement.style.marginLeft = 'auto';
    // postcardElement.style.marginRight = 'auto';
    // postcardElement.style.position = 'relative';
    // postcardElement.style.textAlign = 'center';
    // postcardElement.style.width = '700px';
    
    return postcardElement;
  }

  getPostcardHTML() {
    let postcard = document.createElement('div');
    postcard.className = 'pcard-container';
    postcard.id = 'pcard-design';

    postcard.appendChild(this.addTitleElem());
    postcard.appendChild(this.addMessageElem());
    postcard.appendChild(this.addImageElem());
    postcard = this.addPostcardStyles(postcard);
    return postcard;
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

