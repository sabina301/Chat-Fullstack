const messageArea = document.querySelector("#messageArea");
var nameChat = document.querySelector("#nameChat");
const send = document.querySelector("#send");
const writeNameBtn = document.querySelector("#writeName");
const modalAddUser = document.querySelector("#modalAddUser");
const closeModalAddUser = document.querySelector("#closeModalAddUser");
const addUserBtn = document.querySelector("#addUser");

writeNameBtn.addEventListener("click", addUserInChat);
send.addEventListener("click", (event) => sendMessage(event));
addUserBtn.addEventListener("click", () => openModal(modalAddUser));
closeModalAddUser.addEventListener("click", closeAddUserModal);

var socket = new SockJS("http://localhost:8080/ws");
var stompClient = Stomp.over(socket);

function getChatIdFromUrl() {
  var url = new URL(window.location.href);
  var chatId = url.pathname.split("/").pop();
  return chatId;
}
var chatId = getChatIdFromUrl();

function openModal(modal) {
  modal.style.display = "block";
}

function closeAddUserModal() {
  if (document.querySelector("#errorText")) {
    document.querySelector("#errorText").style.display = "none";
  }
  document.getElementById("username").value = "";
  modalAddUser.style.display = "none";
}

function sendMessage(event) {
  event.preventDefault();
  var message = document.querySelector("#message").value;
  stompClient.send(
    "/app/messages/send",
    {},
    JSON.stringify({ message: message, chatId: chatId })
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

function createMessage(message, currentUser) {
  let liMessage = document.createElement("li");
  let senderMessage = document.createElement("p");

  let textMessage = document.createElement("p");
  textMessage.textContent = message.messageContent;

  if (message.senderName == currentUser) {
    senderMessage.textContent = "You :)";
  } else {
    senderMessage.textContent = message.senderName;
    liMessage.style.backgroundColor = "grey";
  }
  liMessage.appendChild(senderMessage);
  liMessage.appendChild(textMessage);
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

stompClient.connect({}, function (frame) {
  var currentUser = frame.headers["user-name"];
  stompClient.subscribe("/user/topic/messages/get", function (response) {
    var messages = JSON.parse(response.body);
    var messagesArray = Object.values(messages);
    messagesArray.forEach((message) => {
      createMessage(message, currentUser);
    });
  });

  stompClient.subscribe("/user/topic/chatroom/get/one", function (response) {
    var chat = JSON.parse(response.body);
    var chatName = chat.name;
    nameChat.textContent = chatName;
  });

  stompClient.subscribe("/topic/messages/send/" + chatId, function (response) {
    var message = JSON.parse(response.body);
    createMessage(message, currentUser);
  });

  stompClient.subscribe("/user/topic/chatroom/create", function (response) {
    var chat = JSON.parse(response.body);
    createChat(chat);
  });

  stompClient.subscribe("/user/topic/error/add/user", function (response) {
    var error = JSON.parse(response.body);
    var errorText = document.createElement("p");
    errorText.textContent = error.message;
    errorText.id = "errorText";
    modalAddUser.appendChild(errorText);
  });

  loadChatRoom(chatId);
});
