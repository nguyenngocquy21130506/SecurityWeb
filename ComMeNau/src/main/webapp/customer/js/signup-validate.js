$(document).ready(function () {
  $.validator.addMethod(
      "nowhitespace",
      function (value, element) {
        return this.optional(element) || /^\S+$/i.test(value);
      },
      "Không được có khoảng trắng"
  );
  $("#signup-form").validate({
    rules: {
      firstName: "required",
      lastName: "required",
      phoneNumber: {
        digits: true
      },
      email: {
        required: true,
        email: true,
      },
      username: {
        required: true,
        nowhitespace: true
      },
      password: {
        required: true,
        nowhitespace: true
      },
      confirmPassword: {
        required: true,
        nowhitespace: true,
        equalTo: "#password",
      },
    },
    messages: {
      firstName: "Vui lòng điền tên của bạn",
      lastName: "Vui lòng điền họ của bạn",
      phoneNumber: {
        digits: "Chỉ được điền chữ số"
      },
      email: {
        required: "Vui lòng điền địa chỉ email",
        email: "Định dạng email không hợp lệ",
      },
      username: {
        required: "Vui lòng điền tên đăng nhập",
      },
      password: {
        required: "Vui lòng điền mật khẩu",
      },
      confirmPassword: {
        required: "Vui lòng điền lại mật khẩu",
        equalTo: "Vui lòng điền vào mật khẩu giống ở trên",
      },
    },
    submitHandler: function (form) {
      form.submit();
    },
  });
});
