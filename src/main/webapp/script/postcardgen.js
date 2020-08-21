function genPostcard() {
  const node = document.getElementById('pcard-design');

  domtoimage.toPng(node).then(function (dataUrl) {
    const img = new Image();
    img.src = dataUrl;
    document.body.appendChild(img);
  }).catch(function (error) {
    console.error('oops, something went wrong!', error);
  });
}
