
var gamesData


var gpId = paramObj(window.location.href).gp

fetch("/api/game_view/"+gpId)
.then(function(response){
	return response.json()
})
.then(function(json){
	gamesData = json
	loadGrid()
})
.catch(function(error){
	console.log(error)
})



function paramObj(search) {
  var obj = {};
  var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

  search.replace(reg, function(match, param, val) {
    obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
  });

  return obj;
}