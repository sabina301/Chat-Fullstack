const modal = document.querySelector("#modal");
const modalAddUser = document.querySelector("#modalAddUser");
const createChatBtn = document.querySelector("#createChat");
const giveChatNameBtn = document.querySelector("#giveName");
const close1 = document.querySelector("#close");
const closeModalAddUser = document.querySelector("#closeModalAddUser");
const chatsArea = document.querySelector("#chats-area");
const area = document.querySelector("#area");
const messageArea = document.querySelector("#messageArea");
const messageForm = document.querySelector("#messageForm");
const addUserBtn = document.querySelector("#addUser");
const writeNameBtn = document.querySelector("#writeName");
const send = document.querySelector("#send");

let chatsLoaded = false;
let currentChatId = null;

// Event handlers

createChatBtn.addEventListener("click", () => openModal(modal));
close1.addEventListener("click", () => closeModal(modal));
closeModalAddUser.addEventListener("click", closeAddUserModal);
window.addEventListener("click", (event) => closeModalOnClick(event, modal));
addUserBtn.addEventListener("click", () => openModal(modalAddUser));
giveChatNameBtn.addEventListener("click", createChatRoom);
writeNameBtn.addEventListener("click", addUserInChat);
chatsArea.addEventListener("click", handleChatAreaClick);
send.addEventListener("click", (event) => sendMessage(event));

// Functions

function openModal(modal) {
  modal.style.display = "block";
}

function closeModal(modal) {
  modal.style.display = "none";
}

function closeAddUserModal() {
  if (document.querySelector("#errorText")) {
    document.querySelector("#errorText").style.display = "none";
  }
  document.getElementById("username").value = "";
  modalAddUser.style.display = "none";
}

function closeModalOnClick(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

function closeModalOnClick(event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
}

function handleChatAreaClick(event) {
  if (event.target.tagName === "P") {
    var liElement = event.target.closest("li");
    var chatId = liElement.id;

    var pElement = event.target;
    var chatName = pElement.textContent.trim();

    handleChatClick(chatId, chatName);
  }
}

function createChatRoom() {
  var roomName = document.getElementById("name").value;
  stompClient.send(
    "/app/chatroom/create",
    {},
    JSON.stringify({ roomName: roomName })
  );
}

function addUserInChat() {
  var username = document.getElementById("username").value;
  stompClient.send(
    "/app/chatroom/user/add",
    {},
    JSON.stringify({ username: username, chatId: currentChatId })
  );
}

function handleChatClick(chatId, chatName) {
  area.style.display = "none";
  messageArea.style.display = "block";
  messageForm.style.display = "block";
  document.querySelector("#welcome").style.display = "none";
  document.querySelector("#itsachat").style.display = "none";
  document.querySelector("#nameChat").textContent = chatName;
  document.querySelector("#addUser").style.display = "block";
  currentChatId = chatId;
  stompClient.send(
    "/app/messages/get",
    {},
    JSON.stringify({ chatId: currentChatId })
  );
}

function sendMessage(event) {
  event.preventDefault();
  var message = document.querySelector("#message").value;
  stompClient.send(
    "/app/messages/send",
    {},
    JSON.stringify({ message: message, chatId: currentChatId })
  );
}

// Work with sockets

var socket = new SockJS("http://localhost:8080/ws");
var stompClient = Stomp.over(socket);

// Connections

stompClient.connect({}, function (frame) {
  var currentUser = frame.headers["user-name"];
  if (chatsLoaded == false && chatsArea.childElementCount == 0) {
    stompClient.send("/app/chatroom/get", {});
    chatsLoaded = true;
  }
  stompClient.subscribe("/user/topic/chats", function (response) {
    var chat = JSON.parse(response.body);
    var liText = document.createElement("li");
    liText.id = chat.id;
    var text = document.createElement("p");
    text.textContent = chat.name;
    liText.appendChild(text);
    chatsArea.appendChild(liText);

    stompClient.subscribe(
      "/topic/messages/send/" + chat.id,
      function (response) {
        var message = JSON.parse(response.body);

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
    );
  });

  stompClient.subscribe("/user/topic/chats/get", function (response) {
    var chats = JSON.parse(response.body);
    var chatsArray = Object.values(chats);
    chatsArray.forEach((chat) => {
      var liText = document.createElement("li");
      liText.id = chat.id;
      var text = document.createElement("p");
      text.textContent = chat.name;
      liText.appendChild(text);
      chatsArea.appendChild(liText);

      stompClient.subscribe(
        "/topic/messages/send/" + chat.id,
        function (response) {
          var message = JSON.parse(response.body);

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
      );
    });

    stompClient.subscribe("/user/topic/messages/get", function (response) {
      while (messageArea.firstChild) {
        messageArea.removeChild(messageArea.firstChild);
      }
      var messages = JSON.parse(response.body);
      var messagesArray = Object.values(messages);
      messagesArray.forEach((message) => {
        let liMessage = document.createElement("li");
        let senderMessage = document.createElement("p");

        let textMessage = document.createElement("p");
        textMessage.textContent = message.content;

        if (message.senderName == currentUser) {
          senderMessage.textContent = "You :)";
        } else {
          senderMessage.textContent = message.senderName;
          liMessage.style.backgroundColor = "grey";
        }
        liMessage.appendChild(senderMessage);
        liMessage.appendChild(textMessage);
        messageArea.appendChild(liMessage);
      });
    });
  });

  stompClient.subscribe("/user/topic/error/add/user", function (response) {
    var error = JSON.parse(response.body);
    var errorText = document.createElement("p");
    errorText.textContent = error.message;
    errorText.id = "errorText";
    modalAddUser.appendChild(errorText);
  });
});
