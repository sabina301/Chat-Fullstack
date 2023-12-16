document.addEventListener("DOMContentLoaded", function () {
  console.log("AAAAAAAAAAAAAA");
  var jwt = localStorage.getItem("token");

  if (!jwt) {
    // Если токен отсутствует, перенаправьте пользователя на страницу "login"
    window.location.href = "http://localhost:8080/login";
  } else {
    // Если токен есть, вы можете использовать его для выполнения других операций на странице "chat"
    // Например, отправка запросов на сервер, требующих авторизации
    console.log("User is authenticated");
  }
});
