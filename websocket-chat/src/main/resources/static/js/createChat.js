const modal = document.querySelector("#modal");
const createChatBtn = document.querySelector("#createChat");
const giveChatNameBtn = document.querySelector("#giveName");
const close = document.querySelector(".close");
const chatsArea = document.querySelector("#chats-area");
let chatsLoaded = false;

createChatBtn.onclick = function () {
  modal.style.display = "block";
};

close.onclick = function () {
  modal.style.display = "none";
};

window.onclick = function (event) {
  if (event.target == modal) {
    modal.style.display = "none";
  }
};

var socket = new SockJS("http://localhost:8080/ws");
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
  console.log("COUNT = ", chatsArea.childElementCount);
  if (chatsLoaded == false && chatsArea.childElementCount == 0) {
    stompClient.send("/app/chatroom/get", {});
    chatsLoaded = true;
  }
  stompClient.subscribe("/topic/chats", function (response) {
    var chat = JSON.parse(response.body);
    var liText = document.createElement("li");
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
