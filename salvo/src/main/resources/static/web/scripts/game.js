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



function paramObj(search) {
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });

    return obj;
}

function postships(evt) {
    $.post({
            url: "/api/games/players/" + gpId + "/ships",
            data: JSON.stringify([{
                    "type": "destroyer",
                    "locations": ["A1", "B1", "C1"]
                },
                {
                    "type": "patrol boat",
                    "locations": ["H5", "H6"]
                }
                         ]),
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

function getLocation(y) {
    switch (y) {
        case 1:
            return "A";
        case 2:
            return "B";
        case 3:
            return "C";
        case 4:
            return "D";
        case 5:
            return "E";
        case 6:
            return "F";
        case 7:
            return "G";
        case 8:
            return "H";
        case 9:
            return "I";
        case 10:
            return "J";
    }
}

function sendShips() {
    let shipsData = []
    var info = document.querySelectorAll(".grid-stack-item")
    var ships = Array.from(info)
    ships.forEach(ship => {
        let shipData = {};
        let shipLoc = [];
        let height = ship.dataset.gsHeight;
        let width = ship.dataset.gsWidth;
        let x = parseInt(ship.dataset.gsX);
        let y = parseInt(ship.dataset.gsY);
        shipLoc.push(ship.id);
        if (width > height) {
            for (let i = 0; i < width; i++) {
                shipLoc.push(getLocation(y) + (x + i + 1));
                shipData.push(shipLoc);
            }
        } else {
            for (let i = 0; i < height; i++) {
                shipLoc.push(getLocation(y + i) + (x + 1));
                shipData.push(shipLoc);
            }
        }
        shipsData.push(shipData);
    })
    console.log(ships)
}
