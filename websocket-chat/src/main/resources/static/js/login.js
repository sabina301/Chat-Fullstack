function displayError() {
  if (document.getElementById("error") == null) {
    let form = document.getElementById("usernameForm");
    let error = document.createElement("p");
    error.id = "error";
    error.textContent = "Error!";
    form.appendChild(error);
  }
}

document
  .getElementById("usernameForm")
  .addEventListener("submit", function (event) {
    event.preventDefault();

    var username = document.getElementById("name").value;
    var password = document.getElementById("password").value;

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://localhost:8080/auth/login", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    var data = JSON.stringify({ username: username, password: password });
    xhr.send(data);
    xhr.onload = function () {
      if (xhr.status === 200) {
        var response = JSON.parse(xhr.responseText);
        if (response.user == null) {
          displayError();
        } else {
          window.location.href = "chat.html";
        }
      } else {
        console.log("Ошибка");
      }
    };
  });
