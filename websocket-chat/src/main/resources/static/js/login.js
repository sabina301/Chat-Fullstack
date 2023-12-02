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
          let jwt = response.jwt;
          goToChat(jwt);
        }
      } else {
        console.log("Ошибка");
      }
    };
  });

function goToChat(jwt) {
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:8080/chat", true);
  xhr.setRequestHeader("Authorization", "Bearer " + jwt);
  xhr.setRequestHeader("Content-Type", "application/json");
  xhr.onload = function () {
    if (xhr.status == 200) {
      window.location.href = "http://localhost:8080/chat";
    } else {
      console.log("Error", xhr.status);
    }
  };
  xhr.send();
}

document
  .getElementById("enter-question")
  .addEventListener("click", function (event) {
    event.preventDefault();

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "http://localhost:8080/signup", true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onload = function () {
      if (xhr.status == 200) {
        window.location.href = "http://localhost:8080/signup";
      } else {
        console.log("Error ", xhr.status);
      }
    };
    xhr.send();
  });
