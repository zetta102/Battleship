
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

function postships(evt) {
$.post({
  url: "/api/games/players/" + gpId + "/ships",
  data: JSON.stringify([ { "type": "destroyer", "locations": ["A1", "B1", "C1"] },
                           { "type": "patrol boat", "locations": ["H5", "H6"] }
                         ]),
  dataType: "text",
  contentType: "application/json"
})
.done(function (response, status, jqXHR) {
  alert( "Ships added: " + response );
})
.fail(function (jqXHR, status, httpError) {
  alert("Failed to add ships: " + textStatus + " " + httpError);
})
}