
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
    const imageUrl = 'https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7';
    let postcard = new Postcard({imageUrl});
    expect(postcard.imageUrl).toBe(imageUrl);
  });
});

describe('Postcard - generate elements', () => {
  it('should generate title element', () => {
    const title = 'Happy birthday!';
    let actualTitle = new Postcard({title}).addTitleElem().outerHTML;
    
    expect(actualTitle).toContain('pcard-title');
    expect(actualTitle).toContain(title);
  });

  it('should generate message element', () => {
    const message = 'Another adventure filled year awaits you.';
    let actualMessage = new Postcard({message}).addMessageElem().outerHTML;

    expect(actualMessage).toContain('pcard-msg');
    expect(actualMessage).toContain(message);
  });

  it('should generate image element', () => {
    const imageUrl = 'https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7';
    let actualImage = new Postcard({imageUrl}).addImageElem().outerHTML;

    expect(actualImage).toContain('pcard-img');
    expect(actualImage).toContain('https://tse2.mm.bing.net/th/id/');
  });
});

describe('Postcard - add elements together', () => {
  it('should generate postcard HTML element', () => {
    const title = 'Happy birthday!';
    const message = 'Another adventure filled year awaits you.';
    const imageUrl = 'https://tse2.mm.bing.net/th/id/OIP.geCKcqpyVwMD6EJuAT3lVQHaEK?w=333&h=187&c=7&o=5&pid=1.7';
    let actualPostcard = new Postcard({title, message, imageUrl}).getPostcardHTML().outerHTML;

    expect(actualPostcard).toContain('pcard-container');
    expect(actualPostcard).toContain('pcard-design');
    expect(actualPostcard).toContain(title);
    expect(actualPostcard).toContain(message);
    expect(actualPostcard).toContain('https://tse2.mm.bing.net/th/id/');
  });
});
