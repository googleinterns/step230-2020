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
    const image = document.createElement('img');
    image.className = 'pcard-img';
    image.setAttribute('src', this._imageUrl);
    
    return image;
  }

  addMessageElem() {
    const message = document.createElement('div');
    message.className = 'pcard-msg';
    message.appendChild(document.createTextNode(this._message));

    return message;
  }

  addTitleElem() {
    const title = document.createElement('div');
    title.className = 'pcard-title';
    title.appendChild(document.createTextNode(this._title));

    return title;
  }

  getPostcardHTML() {
    const postcard = document.createElement('div');
    postcard.className = 'pcard-container';
    postcard.id = 'pcard-design';

    postcard.appendChild(this.addTitleElem());
    postcard.appendChild(this.addMessageElem());
    postcard.appendChild(this.addImageElem());
    return postcard;
  }

  // The postcard needs to be already on the page.
  // Getting the postcard directly from getPostcardHTML() does not work.
  getPostcardImage() {
    let node = document.getElementById('pcard-design');
    let image = document.createElement('img');

    domtoimage.toPng(node).then(function (encodedImg) {
      image.setAttribute('src', encodedImg);
    }).catch(function (error) {
      console.error('Something went wrong! Try again!', error);

      // Set default image to notify user on the error.
      image.setAttribute('src', 'https://live.staticflickr.com/4034/4543895219_8d78eba86f_c.jpg');
    });
    return image;
  }
}

function displayPostcard() {
  const bodyElem = document.getElementsByTagName('body')[0];
  const title = "Happy birthday";
  const message = "Another adventure filled year awaits you.";
  const imageUrl = "https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7";

  bodyElem.appendChild(new Postcard({title, message, imageUrl}).getPostcardHTML());
  document.body.appendChild(new Postcard({title, message, imageUrl}).getPostcardImage());
}
