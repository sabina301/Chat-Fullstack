const modal = document.querySelector("#modal");
const modalAddUser = document.querySelector("#modalAddUser");
const createChatBtn = document.querySelector("#createChat");
const giveChatNameBtn = document.querySelector("#giveName");
const close1 = document.querySelector("#close");
const chatsArea = document.querySelector("#chats-area");
const chatsList = document.querySelector("#chats-list");

let chatsLoaded = false;
let currentChatId = null;

// Event handlers

createChatBtn.addEventListener("click", () => openModal(modal));
close1.addEventListener("click", () => closeModal(modal));
window.addEventListener("click", (event) => closeModalOnClick(event, modal));
giveChatNameBtn.addEventListener("click", createChatRoom);
chatsArea.addEventListener("click", handleChatAreaClick);

// Functions

function openModal(modal) {
  modal.style.display = "block";
}

function closeModal(modal) {
  modal.style.display = "none";
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

    handleChatClick(chatId);
  }
}

function handleChatClick(chatId) {
  currentChatId = chatId;
  var xhr = new XMLHttpRequest();
  xhr.open("GET", "/chatroom/" + currentChatId, true);
  xhr.onload = function () {
    if (xhr.status == 200) {
      window.location.href = "/chatroom/" + currentChatId;
    }
  };
  xhr.send();
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

// Work with sockets

var socket = new SockJS("http://localhost:8080/ws");
var stompClient = Stomp.over(socket);

// Connections
function createChatRoom() {
  var roomName = document.getElementById("name").value;
  stompClient.send(
    "/app/chatroom/create",
    {},
    JSON.stringify({ roomName: roomName })
  );
}
stompClient.connect({}, function (frame) {
  var currentUser = frame.headers["user-name"];
  if (chatsLoaded == false) {
    stompClient.send("/app/chatroom/get/all", {});
    chatsLoaded = true;
  }
  stompClient.subscribe("/user/topic/chatroom/create", function (response) {
    var chat = JSON.parse(response.body);
    createChat(chat);
  });

  stompClient.subscribe("/user/topic/chats/get", function (response) {
    var chats = JSON.parse(response.body);
    var chatsArray = Object.values(chats);
    chatsArray.forEach((chat) => {
      createChat(chat);
    });
  });
});
