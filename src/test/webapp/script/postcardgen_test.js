
describe('postcard', () => {
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
