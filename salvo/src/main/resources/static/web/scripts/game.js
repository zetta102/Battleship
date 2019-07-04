var gamesData


var gpId = paramObj(window.location.href).gp

fetch("/api/game_view/" + gpId)
    .then(function (response) {
        return response.json()
        console.log(gamesData)
    })
    .then(function (json) {
        gamesData = json
        if (gamesData.ships.length > 0) {
            //if true, the grid is initialized in static mode, that is, the ships can't be moved
            loadGrid(true)
        } else {
            //On the contrary, the grid is initialized in dynamic mode, allowing the user to move the ships
            loadGrid(false)
        }
        createGrid(11, $(".grid-salvoes"), 'salvoes')
        setSalvos()
    })
    .catch(function (error) {
        console.log(error)
    })


function fetchInfo() {
    $(".grid-salvoes").empty();
    $(".grid-ships").empty();
    fetch("/api/game_view/" + gpId)
    .then(function (response) {
        return response.json()
        console.log(gamesData)
    })
    .then(function (json) {
        gamesData = json
        if (gamesData.ships.length > 0) {
            //if true, the grid is initialized in static mode, that is, the ships can't be moved
            loadGrid(true)
        } else {
            //On the contrary, the grid is initialized in dynamic mode, allowing the user to move the ships
            loadGrid(false)
        }
        createGrid(11, $(".grid-salvoes"), 'salvoes')
        setSalvos()
    })
    .catch(function (error) {
        console.log(error)
    })
}

function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });

    return obj;
}



function getLocation(y) {
    switch (y) {
        case 0:
            return "A";
        case 1:
            return "B";
        case 2:
            return "C";
        case 3:
            return "D";
        case 4:
            return "E";
        case 5:
            return "F";
        case 6:
            return "G";
        case 7:
            return "H";
        case 8:
            return "I";
        case 9:
            return "J";
    }
}

function sendShips() {
    let shipsData = []
    var info = document.querySelectorAll(".grid-stack-item")
    var ships = Array.from(info)
    var data = []
    ships.forEach(ship => {
        let shipData = {};
        let shipLoc = [];
        let height = ship.dataset.gsHeight;
        let width = ship.dataset.gsWidth;
        let x = parseInt(ship.dataset.gsX);
        let y = parseInt(ship.dataset.gsY);
        shipData.shipType = ship.id.toUpperCase();
        if (width > height) {
            for (let i = 0; i < width; i++) {
                shipLoc.push(getLocation(y) + (x + i + 1));
            }
        } else {
            for (let i = 0; i < height; i++) {
                shipLoc.push(getLocation(y + i) + (x + 1));
            }
        }
        shipData.locations = shipLoc
        data.push(shipData);
    })

    $.post({
            url: "/api/games/players/" + gpId + "/ships",
            data: JSON.stringify(data),
            dataType: "text",
            contentType: "application/json"
        })
        .done(function (response, status, jqXHR) {
            alert("Ships added: " + response);

        })
        .fail(function (jqXHR, status, httpError) {
            alert("Failed to add ships: " + textStatus + " " + httpError);
        })
    fetchInfo()
}

function sendSalvoes() {
    var info = document.querySelectorAll(".salvoes-cell")
    var salvoes = Array.from(info)
    var data = []
    salvoes.forEach(salvo => {
        let salvoData = {};
        let salvoLoc = [];
        let height = 1;
        let width = 1;
        let x = parseInt(salvo.id.slice(-1));
        let y = parseInt(salvo.id.slice(7, 8));
        salvoLoc.push(getLocation(y) + (x));
        salvoData.locations = salvoLoc
        data.push(shipData);
    })

    $.post({
            url: "/api/games/players/" + gpId + "/ships",
            data: JSON.stringify(data),
            dataType: "text",
            contentType: "application/json"
        })
        .done(function (response, status, jqXHR) {
            alert("Ships added: " + response);

        })
        .fail(function (jqXHR, status, httpError) {
            alert("Failed to add ships: " + textStatus + " " + httpError);
        })
}
