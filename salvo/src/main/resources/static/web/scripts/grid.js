


//main function that shoots the gridstack.js framework and loads the grid with the ships
const loadGrid = function () {
    var options = {
        //10 x 10 grid
        width: 10,
        height: 10,
        //space between elements (widgets)
        verticalMargin: 0,
        //height of cells
        cellHeight: 45,
        //disables resizing of widgets
        disableResize: true,
        //floating widgets
		float: true,
        //removeTimeout: 100,
        //allows the widget to occupy more than one column
        disableOneColumnMode: true,
        //false allows widget dragging, true denies it
        staticGrid: false,
        //activates animations
        animate: true
    }
    //grid initialization
    $('.grid-stack').gridstack(options);

    grid = $('#grid').data('gridstack');




    setShips()



    createGrid(11, $(".grid-ships"), 'ships')


    rotateShips("carrier", 5)
    rotateShips("battleship", 4)
    rotateShips("submarine",3)
    rotateShips("destroyer", 3)
    rotateShips("patrol_boat",2)

    listenBusyCells('ships')
    $('.grid-stack').on('change', function(){ listenBusyCells('ships')})


    //all the functionalities are explained in the gridstack github
    //https://github.com/gridstack/gridstack.js/tree/develop/doc
    
}


//creates the grid structure
const createGrid = function(size, element, id){

    let wrapper = document.createElement('DIV')
    wrapper.classList.add('grid-wrapper')

    for(let i = 0; i < size; i++){
        let row = document.createElement('DIV')
        row.classList.add('grid-row')
        row.id =id+`grid-row${i}`
        wrapper.appendChild(row)

        for(let j = 0; j < size; j++){
            let cell = document.createElement('DIV')
            cell.classList.add('grid-cell')
            if(i > 0 && j > 0)
            cell.id = id+`${i - 1}${ j - 1}`

            if(j===0 && i > 0){
                let textNode = document.createElement('SPAN')
                textNode.innerText = String.fromCharCode(i+64)
                cell.appendChild(textNode)
            }
            if(i === 0 && j > 0){
                let textNode = document.createElement('SPAN')
                textNode.innerText = j
                cell.appendChild(textNode)
            }
            row.appendChild(cell)
        }
    }

    element.append(wrapper)
}

//adds a listener to the ships, wich shoots its rotation when clicked
const rotateShips = function(shipType, cells){

        $(`#${shipType}`).click(function(){
        	document.getElementById("alert-text").innerHTML = ""
            let x = +($(this).attr('data-gs-x'))
            let y = +($(this).attr('data-gs-y'))
        if($(this).children().hasClass(`${shipType}Horizontal`)){
        	if(grid.isAreaEmpty(x,y+1,1,cells) || y + cells < 10){
	            if(y + cells - 1 < 10){
	                grid.resize($(this),1,cells);
	                $(this).children().removeClass(`${shipType}Horizontal`);
	                $(this).children().addClass(`${shipType}Vertical`);
	            } else{
	            	
	            		grid.update($(this), null, 10 - cells)
	                	grid.resize($(this),1,cells);
	                	$(this).children().removeClass(`${shipType}Horizontal`);
	                	$(this).children().addClass(`${shipType}Vertical`);
	            }
                
                
            }else{
            		document.getElementById("alert-text").innerHTML = "A ship is blocking the way!"
            }
            
        }else{
            if(x + cells - 1  < 10){
                grid.resize($(this),cells,1);
                $(this).children().addClass(`${shipType}Horizontal`);
                $(this).children().removeClass(`${shipType}Vertical`);
            } else{
                grid.update($(this), 10 - cells)
                grid.resize($(this),cells,1);
                $(this).children().addClass(`${shipType}Horizontal`);
                $(this).children().removeClass(`${shipType}Vertical`);
            }
            
        }
    });

}

//loops over all the grid cells, verifying if they are empty or busy
const listenBusyCells = function(id){
    for(let i = 0; i < 10; i++){
        for(let j = 0; j < 10; j++){
            if(!grid.isAreaEmpty(i,j)){
                $(`#${id}${j}${i}`).addClass('busy-cell').removeClass('empty-cell')
            } else{
                $(`#${id}${j}${i}`).removeClass('busy-cell').addClass('empty-cell')
            }
        }
    }
}

const setShips = function(){

    for(i = 0; i < gamesData.ships.length; i++){
        let shipType = gamesData.ships[i].type
        let x = +(gamesData.ships[i].locations[0][1]) - 1
        let y = stringToInt(gamesData.ships[i].locations[0][0].toUpperCase())
        let w
        let h
        let orientation

        if(gamesData.ships[i].locations[0][0] == gamesData.ships[i].locations[1][0]){
            w = gamesData.ships[i].locations.length
            h = 1
            orientation = "Horizontal"
        } else{
            h = gamesData.ships[i].locations.length
            w = 1
            orientation = "Vertical"
        }

        grid.addWidget($('<div id="'+shipType+'"><div class="grid-stack-item-content '+shipType+orientation+'"></div><div/>'),
        x, y, w, h);



    }
}

const setSalvoes = function(){

    for(i = 0; i < gamesData.salvos.length; i++){
        for(j = 0; j<gamesData.salvos[i].location.length; j++) {
            let turnNum = gamesData.salvos[i].turnNumber
            let x = +(gamesData.salvos[i].location[j][1]) - 1
            let y = stringToInt(gamesData.salvos[i].location[j][0].toUpperCase())

            document.getElementById("salvoes"+y+x).classList.add("salvo")
             document.getElementById("salvoes"+y+x).innerHTML=turnNum
        }
    }
}

const stringToInt = function(str){
    switch(str){
        case "A": 
            return 0;
        case "B": 
            return 1;
        case "C": 
            return 2;
        case "D": 
            return 3;
        case "E": 
            return 4;
        case "F": 
            return 5;
        case "G": 
            return 6;
        case "H": 
            return 7;
        case "I": 
            return 8;
        case "J": 
            return 9;
    }
}