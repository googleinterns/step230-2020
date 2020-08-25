
describe('Postcard - check constructor', () => {
  it('should have title', () => {
    const title = 'Happy birthday!';
    let postcard = new Postcard({title});
    expect(postcard.title).toBe(title);
  });

  it('should have message', () => {
    const message = 'Another adventure filled year awaits you.';
    let postcard = new Postcard({message});
    expect(postcard.message).toBe(message);
  });

  it('should have image', () => {
    const imgUrl = 'https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7';
    let postcard = new Postcard({imgUrl});
    expect(postcard.img).toBe(imgUrl);
  });
});

describe('Postcard - generate elements', () => {
  it('should generate title element', () => {
    const title = 'Happy birthday!';
    let postcard = new Postcard({title});
    expect(postcard.addTitleElem().outerHTML).toBe('<div class="pcard-title">' + title + '</div>');
  });

  it('should generate message element', () => {
    const message = 'Another adventure filled year awaits you.';
    let postcard = new Postcard({message});
    expect(postcard.addMessageElem().outerHTML).toBe('<div class="pcard-msg">' + message + '</div>');
  });

  it('should generate image element', () => {
    const imgUrl = 'https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7';
    let postcard = new Postcard({imgUrl});
    expect(postcard.addImageElem().outerHTML).toMatch('^<img class="pcard-img" src="https:\/\/tse2.mm.bing.net\/th\/id\/.*>$');
  });
});

describe('Postcard - add elements togehter', () => {
  it('should generate postcard HTML element', () => {
    const title = 'Happy birthday!';
    const message = 'Another adventure filled year awaits you.';
    const imgUrl = 'https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7';
    let postcard = new Postcard({title, message, imgUrl});
    expect(postcard.getPostcardHTML().outerHTML).toBe('<div class="pcard-container" id="pcard-design">' +
                '<div class="pcard-title">' + title + '</div>' +
                '<div class="pcard-msg">' + message + '</div>' +
                '<img class="pcard-img" src="https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&amp;h=187&amp;c=7&amp;o=5&amp;pid=1.7">' +
                '</div>');
  });
});
