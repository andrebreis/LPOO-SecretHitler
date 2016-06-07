var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var playerIndex = 0;
var players = [];
var noActivePlayers;

var LIBERAL = 0;
var FASCIST = 1;
var HITLER = 2;

var presidentIndex = 0;
var chancellorIndex = -1;
var lastPresident = -1;
var lastChancellor = -1;
var noFascistLaws = 0;
var noLiberalLaws = 0;
var policyDeck = [];

var chancellorVotes = {};

var gameStarted = false;

server.listen(8080, function(){
  console.log("Server is now running...");
});

io.on('connection', function(socket){
  if(gameStarted || playerIndex == 9)
    socket.disconnect('unauthorized');
  else{
  console.log("Player Connected!");
  socket.emit('getPlayers', players);
  players.push(new player(socket.id, playerIndex++));
  socket.emit('idAndPosition', { id : socket.id, position : playerIndex - 1});
  socket.on('disconnect', function(){
    console.log("Player Disconnected!");
    for(var i = 0; i < players.length; i++){
      if(players[i].id == socket.id){
        socket.broadcast.emit('playerDisconnected', { position : players[i].position});
        if(gameStarted){
          players[i].playing = false;
          noActivePlayers--;
        }
        else {
          players.splice(i, 1);
          playerIndex--;
          fixPlayerPositions();
        }
      }
    }
    //socket.disconnect('Ended');
    console.log(players);
  });
  socket.on('playerName', function(name){
    console.log("ping");
    for(var i = 0; i < players.length; i++){
      if(players[i].id == socket.id){
        players[i].name = name;
        //socket.emit('playerPosition', { position : players[i].position });
        socket.broadcast.emit('newPlayer', { id : socket.id, name : players[i].name, position: players[i].position});
        console.log(players);
      }
    }
  });
  socket.on('gameStarted', function(){
    console.log("Game Started");
    noActivePlayers = players.length;
    gameStarted = true;
    assignRoles();
    policyDeck = shuffleDeck();
    socket.emit('setPlayers', { players : players });
    socket.broadcast.emit('setPlayers', { players : players });
  });
  socket.on('pickedChancellor', function(index){
    socket.emit('initiateChancellorVote', { position : index });
    socket.broadcast.emit('initiateChancellorVote', { position : index });
  });
  socket.on('voteForChancellor', function(vote){
    chancellorVotes[socket.id] = vote;
    if(chancellorVotes.length == noActivePlayers){
      var voteSum = 0;
      for(var key in chancellorVotes){
        voteSum += chancellorVotes[key];
      }
      socket.emit('chancellorVoteResult', {votes : chancellorVotes, verdict : voteSum > chancellorVotes.length/2}); //TODO: socket.on for this on java
      socket.broadcast.emit('chancellorVoteResult', {votes : chancellorVotes, verdict : voteSum > chancellorVotes.length/2});
    }
  });
//players.push(new player(socket.id, playerIndex++));
}
});

function player(id, position){
  this.id = id;
  this.position = position;
  this.playing = true;
  this.name = "";
  this.party = -1;
  this.role = -1;
}

function shuffleDeck(){

  var numLiberalCards = 6;
  var numFascistCards = 11;
  var noRandomNumbers = 1000;

  while(numFascistCards + numLiberalCards > 0){
    var card = Math.floor(Math.random() * noRandomNumbers);
    if(card < noRandomNumbers/2 && numFascistCards > 0){
      numFascistCards--;
      policyDeck.push(FASCIST);
    }
    else if(numLiberalCards > 0) {
      numLiberalCards--;
      policyDeck.push(LIBERAL);
    }
  }
  return policyDeck;
}

function assignRoles(){
  var noLiberals;
  var noFascists;

  if(players.length == 5 || players.length == 6){
    noFascists = 2;
  }
  else if(players.length == 7 || players.length == 8){
    noFascists = 3;
  }
  else{ //(players.size() == 9 || players.size() == 10)
    noFascists = 4;
  }

  noLiberals = players.length - noFascists;

  var hitlerIndex = Math.floor(Math.random() * players.length);
  players[hitlerIndex].role = HITLER;
  players[hitlerIndex].party = FASCIST;
  noFascists--;

  while(noFascists > 0){
    var fascistIndex = Math.floor(Math.random() * players.length);
    if(players[fascistIndex].role == -1){
      players[fascistIndex].role = FASCIST;
      noFascists--;
    }
  }

  while(noLiberals > 0){
    var liberalIndex = Math.floor(Math.random() * players.length);
    if(players[liberalIndex].role == -1){
      players[liberalIndex].role = LIBERAL;
      noLiberals--;
    }
  }
}

function fixPlayerPositions(){
  for(var i = 0; i < players.length ; i++){
    players[i].position = i;
  }
}
