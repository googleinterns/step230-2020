describe("Login actions", function() {
  beforeAll(function() {
    jasmine.addMatchers(DOMCustomMatchers); 
    const page = document.createElement('div');
    page.id = 'login_page';
    const content = document.createElement('div');
    content.id = 'index_content';
    const load = document.createElement('div');
    load.id = 'loading';
    var body = document.getElementsByTagName("body")[0];
    body.appendChild(load);
    body.appendChild(page);
    body.appendChild(content);
  });

  it("should append login message to the page", function() {
    loginDomManipulation('lala', true); 
    const elem = document.getElementsByClassName('login')[0];
    expect(elem.innerText).toEqual('lala');
  });

  it("should make content visible", function() {
    loginDomManipulation('lala', true);
    const elem = document.getElementById('index_content');
    expect(elem.style.visibility).toBe('visible');
  });

  it("should make content hidden", function() {
    loginDomManipulation('lala', false);
    const elem = document.getElementById('index_content');
    expect(elem.style.visibility).toBe('hidden');
  });

  it("should make loading sign hidden in case of false status", function() {
    loginDomManipulation('lala', false);
    const elem = document.getElementById('loading');
    expect(elem.style.visibility).toBe('hidden');   
  });

  it("should make loading sign hidden in case of true status", function() {
    loginDomManipulation('lala', true);
    const elem = document.getElementById('loading');
    expect(elem.style.visibility).toBe('hidden');   
  });

  afterAll(function() {
    let load = document.getElementById('loading');
    let page = document.getElementById('login_page');
    let content = document.getElementById('index_content');
    load.remove();
    page.remove();
    content.remove();
  });
});

describe("Input actions", function() {
  beforeAll(function() {
    jasmine.addMatchers(DOMCustomMatchers); 
    const output = document.createElement('div');
    output.id = 'output';
    var body = document.getElementsByTagName("body")[0];
    body.appendChild(output);
    //this.page = document.getElementById('login_page');
  });
  
  it("should show output", function() {
    InputDomManipulations('text', '');
    const elem = document.getElementById('output');
    expect(elem.innerText).toEqual('Now choose your receiver and SEND it!')
  });

  afterAll(function() {
    let output = document.getElementById('output');
    output.remove();
  });
});