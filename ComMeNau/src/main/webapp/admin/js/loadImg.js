

var imageBase64 = "";
var imageName = "";

$("#uploadImage").change(function (event) {
  var reader = new FileReader();
  var file = $(this)[0].files[0];
  reader.onload = function (e) {
    imageBase64 = e.target.result;
    imageName = file.name;
  };
  reader.readAsDataURL(file);
  openImage(this, "viewImage");
});

function openImage(input, imageView) {
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function (e) {
      $("#" + imageView).attr("src", reader.result);
    };
    reader.readAsDataURL(input.files[0]);
  }
}
