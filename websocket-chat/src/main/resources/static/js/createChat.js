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

let chatsLoaded = false;
let currentChatId = null;

createChatBtn.onclick = function () {
  modal.style.display = "block";
};

close1.onclick = function () {
  modal.style.display = "none";
};

closeModalAddUser.onclick = function () {
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
    });
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
}
