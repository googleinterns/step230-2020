describe("Login actions", function() {
  beforeAll(function() {
    jasmine.addMatchers(DOMCustomMatchers); 
    const page = document.createElement('div');
    page.id = 'login_page';
    const content = document.createElement('div');
    content.id = 'index_content';
    const load = document.createElement('div');
    load.id = 'loading';
    let body = document.getElementsByTagName('body')[0];
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

describe("form actions", function() {

  beforeAll(function() {
    const input = document.createElement('input');
    input.name = "input_text";
    input.value = "test";
    const checkbox = document.createElement('input');
    checkbox.name = "location_checkbox";
    checkbox.value = "me";
    let body = document.getElementsByTagName('body')[0];
    body.appendChild(input);
    body.appendChild(checkbox);
  });

  it("should get the correct text value", function() {
    let text = document.getElementsByName('input_text')[0].value;
    expect(text).toEqual('test');
  });

  it("should get the correct checkbox value", function() {
    let location = document.getElementsByName('location_checkbox')[0].value;
    expect(location).toEqual('me');
  });

  afterAll(function() {
    let input_text = document.getElementsByName('input_text')[0];
    let location = document.getElementsByName('location_checkbox')[0];
    input_text.remove();
    location.remove();
  });
});