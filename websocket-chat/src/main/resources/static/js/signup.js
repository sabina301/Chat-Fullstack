function displayError() {
  if (document.getElementById("error") == null) {
    let form = document.getElementById("usernameForm");
    let error = document.createElement("p");
    error.id = "error";
    error.textContent = "Account with this username exist :(";
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
    xhr.open("POST", "http://localhost:8080/auth/register", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
      if (xhr.readyState === XMLHttpRequest.DONE) {
        var response = JSON.parse(xhr.responseText);
        if (response.username != null) {
          window.location.href = "login.html";
        } else {
          displayError();
        }
      }
    };

    var data = JSON.stringify({ username: username, password: password });
    xhr.send(data);
  });
