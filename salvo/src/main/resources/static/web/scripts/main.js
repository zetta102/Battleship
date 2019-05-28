
var gamesData


var gpId = paramObj(window.location.href).gp

fetch("/api/game_view/"+gpId)
.then(function(response){
	return response.json()
	console.log(gamesData)
})
.then(function(json){
	gamesData = json
	loadGrid()
	 createGrid(11, $(".grid-salvoes"), 'salvoes')
	 setSalvoes()
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