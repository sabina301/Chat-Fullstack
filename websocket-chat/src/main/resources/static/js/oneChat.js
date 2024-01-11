const messageArea = document.querySelector("#messageArea");
var nameChat = document.querySelector("#nameChat");
const send = document.querySelector("#send");
const writeNameBtn = document.querySelector("#writeName");
const modalAddUser = document.querySelector("#modalAddUser");
const closeModalAddUser = document.querySelector("#closeModalAddUser");
const addUserBtn = document.querySelector("#addUser");
const modalGetInfo = document.querySelector("#modalGetInfo");
const getInfoBtn = document.querySelector("#getInfo");
const closeModalGetInfo = document.querySelector("#closeModalGetInfo");
const attach = document.querySelector("#attach");
const modalAttach = document.querySelector("#modalAttach");
const back = document.querySelector("#back");
const exit = document.querySelector("#exit");

writeNameBtn.addEventListener("click", addUserInChat);
send.addEventListener("click", (event) => sendMessage(event));
addUserBtn.addEventListener("click", () => openModal(modalAddUser));
getInfoBtn.addEventListener("click", () => openModalGetInfo(modalGetInfo));
closeModalAddUser.addEventListener("click", closeAddUserModal);
closeModalGetInfo.addEventListener("click", closeGetInfoModal);
attach.addEventListener("click", (event) => openModalAttach(event));
back.addEventListener("click", (event) => goBack(event));
exit.addEventListener("click", (event) => userExit(event));

var socket = new SockJS("http://localhost:8080/ws");
var stompClient = Stomp.over(socket, {
  websocketLargeMessageSize: 3 * 1024 * 1024,
});

function getChatIdFromUrl() {
  var url = new URL(window.location.href);
  var chatId = url.pathname.split("/").pop();
  return chatId;
}
var chatId = getChatIdFromUrl();

function openFileInput() {
  document.getElementById("imageInput").click();
}

document.getElementById("imageInput").addEventListener("change", function () {
  uploadImage();
});

function uploadImage() {
  var input = document.getElementById("imageInput");
  var image = input.files[0];

  if (image) {
    var reader = new FileReader();

    reader.onloadend = function () {
      var arrayBuffer = reader.result;
      var byteArray = new Uint8Array(arrayBuffer);
      var base64Image = encodeByteArray(byteArray);
      stompClient.send(
        "/app/messages/send/img",
        { chatId: chatId },
        base64Image
      );
    };

    reader.readAsArrayBuffer(image);
  }
}

function encodeByteArray(byteArray) {
  var binaryString = "";
  for (var i = 0; i < byteArray.length; i++) {
    binaryString += String.fromCharCode(byteArray[i]);
  }
  return btoa(binaryString);
}

function userExit(event) {
  event.preventDefault();
  stompClient.send(
    "/app/chatroom/user/exit",
    {},
    JSON.stringify({ chatId: chatId })
  );
  window.location.href = "/chat";
}

function openModal(modal) {
  modal.style.display = "block";
}

function openModalGetInfo(modal) {
  modal.style.display = "block";
  stompClient.send(
    "/app/chatroom/info/get",
    {},
    JSON.stringify({ chatId: chatId })
  );
}

function goBack(event) {
  event.preventDefault();
  window.location.href = "/chat";
}

function openModalAttach(event) {
  event.preventDefault();
  if (modalAttach.style.display == "none") {
    modalAttach.style.display = "block";
  } else {
    modalAttach.style.display = "none";
  }
}

function closeAddUserModal() {
  if (document.querySelector("#errorText")) {
    document.querySelector("#errorText").style.display = "none";
  }
  document.getElementById("username").value = "";
  modalAddUser.style.display = "none";
}

function closeGetInfoModal() {
  modalGetInfo.style.display = "none";
  document.querySelector("#ul-people").innerHTML = "";
}

function sendMessage(event) {
  event.preventDefault();
  var message = document.querySelector("#message").value;
  stompClient.send(
    "/app/messages/send/text",
    {},
    JSON.stringify({
      message: message,
      chatId: chatId,
      byteImg: null,
      type: "TEXT",
    })
  );
}

function loadChatRoom(chatId) {
  stompClient.send(
    "/app/chatroom/get/one",
    {},
    JSON.stringify({ chatId: chatId })
  );

  stompClient.send("/app/messages/get", {}, JSON.stringify({ chatId: chatId }));
}

function addUserInChat() {
  var username = document.getElementById("username").value;
  stompClient.send(
    "/app/chatroom/user/add",
    {},
    JSON.stringify({ username: username, chatId: chatId })
  );
}

function createTextMessage(message, currentUser) {
  let liMessage = document.createElement("li");

  let senderMessage = document.createElement("p");

  let textMessage = document.createElement("p");
  textMessage.textContent = message.messageContent;

  if (message.senderName == currentUser) {
    senderMessage.textContent = "You :)";
    liMessage.style.alignSelf = "flex-end";
  } else {
    senderMessage.textContent = message.senderName;
    liMessage.style.backgroundColor = "grey";
    liMessage.style.alignSelf = "flex-start";
  }

  liMessage.appendChild(senderMessage);
  liMessage.appendChild(textMessage);

  liMessage.className = "liMessageArea";

  messageArea.appendChild(liMessage);
}

function createImageUrl(imageBase64) {
  var byteCharacters = atob(imageBase64);
  var byteNumbers = new Array(byteCharacters.length);

  for (var i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }

  var byteArray = new Uint8Array(byteNumbers);
  var blob = new Blob([byteArray], { type: "image/*" });
  var imageUrl = URL.createObjectURL(blob);
  console.log("Image URL: ", imageUrl);
  return imageUrl;
}

function createImgMessage(message, currentUser) {
  let liMessage = document.createElement("li");
  var imgElement = document.createElement("img");
  let senderMessage = document.createElement("p");

  imgElement.src = createImageUrl(message.byteImg);
  imgElement.alt = "Image message";
  imgElement.className = "imgForChat";
  liMessage.className = "liImgForChat";

  if (message.senderName == currentUser) {
    senderMessage.textContent = "You :)";
    liMessage.style.alignSelf = "flex-end";
  } else {
    senderMessage.textContent = message.senderName;
    liMessage.style.backgroundColor = "grey";
    liMessage.style.alignSelf = "flex-start";
  }
  liMessage.appendChild(senderMessage);
  liMessage.appendChild(imgElement);
  messageArea.appendChild(liMessage);
}

function createChat(chat) {
  var liText = document.createElement("li");
  liText.id = chat.id;
  var text = document.createElement("p");
  text.textContent = chat.name;
  var circle = document.createElement("div");
  circle.classList.add("circle");
  circle.appendChild(text);
  liText.appendChild(circle);
  chatsList.appendChild(liText);
}

function createJoinMessage(messageStatus) {
  let liMessage = document.createElement("li");
  let pMessageStatus = document.createElement("p");
  pMessageStatus.textContent =
    messageStatus.type + " " + messageStatus.username;
  liMessage.appendChild(pMessageStatus);
  liMessage.className = "liJoin";
  messageArea.appendChild(liMessage);
}

stompClient.connect({}, function (frame) {
  var currentUser = frame.headers["user-name"];

  stompClient.subscribe("/user/topic/messages/get", function (response) {
    var messages = JSON.parse(response.body);
    var messagesArray = Object.values(messages);
    messagesArray.forEach((message) => {
      if (message.type == "TEXT") {
        createTextMessage(message, currentUser);
      } else if (message.type == "IMG") {
        createImgMessage(message, currentUser);
      } else {
        createJoinMessage(message);
      }
    });
  });

  stompClient.subscribe("/user/topic/chatroom/get/one", function (response) {
    var chat = JSON.parse(response.body);
    var chatName = chat.name;
    nameChat.textContent = chatName;
  });

  stompClient.subscribe(
    "/topic/messages/send/text/" + chatId,
    function (response) {
      var message = JSON.parse(response.body);
      createTextMessage(message, currentUser);
    }
  );

  stompClient.subscribe(
    "/topic/messages/send/img/" + chatId,
    function (response) {
      var message = JSON.parse(response.body);
      createImgMessage(message, currentUser);
    }
  );

  stompClient.subscribe("/user/topic/chatroom/create", function (response) {
    var chat = JSON.parse(response.body);
    createChat(chat);
  });

  stompClient.subscribe(
    "/topic/messages/status/" + chatId,
    function (response) {
      var messageStatus = JSON.parse(response.body);
      createJoinMessage(messageStatus);
    }
  );

  stompClient.subscribe("/user/topic/error/add/user", function (response) {
    var error = JSON.parse(response.body);
    var errorText = document.createElement("p");
    errorText.textContent = error.message;
    errorText.id = "errorText";
    modalAddUser.appendChild(errorText);
  });

  stompClient.subscribe("/user/topic/chatroom/info/get", function (response) {
    var chat = JSON.parse(response.body);
    console.log(chat);
    document.querySelector("#chatName").textContent = chat.name;
  });

  stompClient.subscribe(
    "/user/topic/chatroom/info/get/users",
    function (response) {
      var users = JSON.parse(response.body);

      users.forEach((user) => {
        var liUser = document.createElement("li");
        liUser.className = "person";
        var imgUser = document.createElement("img");
        imgUser.src = "/img/people.svg";
        var pUser = document.createElement("p");
        pUser.id = "username";
        pUser.textContent = user.username;

        liUser.appendChild(imgUser);
        liUser.appendChild(pUser);

        document.querySelector("#ul-people").appendChild(liUser);
      });
    }
  );

  loadChatRoom(chatId);
});
