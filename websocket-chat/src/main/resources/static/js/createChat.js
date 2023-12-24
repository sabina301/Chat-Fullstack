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

createChatBtn.onclick = function () {
  modal.style.display = "block";
};

close1.onclick = function () {
  modal.style.display = "none";
};

closeModalAddUser.onclick = function () {
  if (document.querySelector("#errorText")) {
    document.querySelector("#errorText").style.display = "none";
  }
  document.getElementById("username").value = "";
  modalAddUser.style.display = "none";
};

window.onclick = function (event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
};

addUserBtn.onclick = function () {
  modalAddUser.style.display = "block";
};

var socket = new SockJS("http://localhost:8080/ws");
var stompClient = Stomp.over(socket);

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

    stompClient.subscribe("/topic/sendmessage/" + chat.id, function (response) {
      var message = JSON.parse(response.body);

      let liMessage = document.createElement("li");
      let senderMessage = document.createElement("p");

      let textMessage = document.createElement("p");
      textMessage.textContent = message.messageContent;

      console.log("СООБЩЕНИЕ ПОЛУЧЕНО! " + message.messageContent);

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

  stompClient.subscribe("/user/topic/getchats", function (response) {
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
        "/topic/sendmessage/" + chat.id,
        function (response) {
          var message = JSON.parse(response.body);

          let liMessage = document.createElement("li");
          let senderMessage = document.createElement("p");

          let textMessage = document.createElement("p");
          textMessage.textContent = message.messageContent;

          console.log("СООБЩЕНИЕ ПОЛУЧЕНО! " + message.messageContent);

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

    stompClient.subscribe("/user/topic/getmessages", function (response) {
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

  stompClient.subscribe("/user/topic/erroradduser", function (response) {
    var error = JSON.parse(response.body);
    var errorText = document.createElement("p");
    errorText.textContent = error.message;
    errorText.id = "errorText";
    modalAddUser.appendChild(errorText);
  });
});

function createChatRoom() {
  var roomName = document.getElementById("name").value;
  stompClient.send(
    "/app/chatroom/create",
    {},
    JSON.stringify({ roomName: roomName })
  );
}

giveChatNameBtn.addEventListener("click", function () {
  createChatRoom();
});

function addUserInChat() {
  var username = document.getElementById("username").value;
  stompClient.send(
    "/app/chatroom/adduser",
    {},
    JSON.stringify({ username: username, chatId: currentChatId })
  );
}

writeNameBtn.addEventListener("click", function () {
  addUserInChat();
});

chatsArea.addEventListener("click", function (event) {
  if (event.target.tagName === "P") {
    var liElement = event.target.closest("li");
    var chatId = liElement.id;

    var pElement = event.target;
    var chatName = pElement.textContent.trim();

    handleChatClick(chatId, chatName);
  }
});

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

function sendMessage() {
  var message = document.querySelector("#message").value;
  console.log("We get message: " + message);
  stompClient.send(
    "/app/messages/sendmessage",
    {},
    JSON.stringify({ message: message, chatId: currentChatId })
  );
}

send.addEventListener("click", function (event) {
  event.preventDefault();
  sendMessage();
});
