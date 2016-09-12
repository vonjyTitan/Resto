var canvas, ctx, width, height;
var x = new Number();
var y = new Number();
var moovemode=true;
var addmode=false;
var selectmode=false;
var mousePress=false;
var selectedTable=undefined;

var inputStates = {};

// array of tables to animate
var tableArray = [];

function initGestionTable(idDiv,tableListe,
addmodeVal,
selectmodeVal,
moovemodeVal) 
{
	addmode=addmodeVal;
	selectmode=selectmodeVal;
	moovemode=moovemodeVal;
	tableArray=tableListe;
	
	var canvas = document.createElement('canvas');
        div = $("#"+idDiv); 
        canvas.id     = "CursorLayer";
        canvas.width  = 500;
        canvas.height = 175;
        canvas.style.zIndex   = 8;
       // canvas.style.position = "absolute";
        canvas.style.border   = "1px solid";
        div.append(canvas)
	
  //canvas = document.querySelector("#myCanvas");
  ctx = canvas.getContext('2d');
  width = canvas.width;
  height = canvas.height;	
  
   canvas.addEventListener('mousedown', function(event){
	   mousePress=true;

	   canoffset = $(canvas).offset();
	   x = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft - Math.floor(canoffset.left);
	   y = event.clientY + document.body.scrollTop + document.documentElement.scrollTop - Math.floor(canoffset.top) + 1;

		selectTable(x,y);
		if(selectedTable!=undefined){
			if(selectmode){
				//selected table to commande
			}
			else if(addmode)
			{
				
			}
		}
}, false); 
   canvas.addEventListener('mouseleave', function(event){
	   selectedTable=null;
   });
 canvas.addEventListener('mouseup', function(event){ 
 mousePress=false;
 if(selectedTable!=undefined && moovemode){
	
 }
 selectedTable=undefined;
 }, false);
  canvas.addEventListener('mousemove', function(event){ 
	  canoffset = $(canvas).offset();
	   x = event.clientX + document.body.scrollLeft + document.documentElement.scrollLeft - Math.floor(canoffset.left);
	   y = event.clientY + document.body.scrollTop + document.documentElement.scrollTop - Math.floor(canoffset.top) + 1;

	if(mousePress && selectedTable!=undefined && moovemode){
		selectedTable.move(x,y);
	}
 }, false);
  
  requestAnimationFrame(mainLoop);
}
function tableInpoint(valx,valy){
	for(var i=0; i < tableArray.length; i++) {
      var table = tableArray[i];
	var IsInCity=table.isClicked(valx,valy);
	if(IsInCity){
		return table;
	}
  }
  return undefined;
}
function selectTable(x,y){
	var selected=tableInpoint(x,y);
			if(selected!=undefined){
				selectedTable=selected;
			}
			else{
			}
}
function mainLoop() {
    // clear the canvas
    ctx.clearRect(0, 0, width, height);
  
    // for each table in the array
    for(var i=0; i < tableArray.length; i++) {
      var table = tableArray[i];
     // table.move(x,y);
      table.draw();
  }
    // ask for a new frame of animation at 60f/s
     window.requestAnimationFrame(mainLoop);
}

function Table(x, y, diameter,nom,id) {
  this.x = x;
  this.y = y;
  this.radius = diameter/2;
  this.nom=nom;
  this.id=id;
  
  this.isClicked=function(valx,valy){
      var dx = (this.x)-valx;
		var dy = (this.y)-valy;
		var radius=this.radius;
		var IsInCity = (dx*dx+dy*dy)<(radius*radius);
		return IsInCity;
  };
  this.draw = function() {
      ctx.beginPath();
	  ctx.fillStyle = "blue";
      ctx.arc(this.x, this.y, this.radius, 0, 2*Math.PI);
      ctx.fill();
	  ctx.font="20px Georgia";
	  ctx.fillStyle = "white";
	  ctx.fillText(nom,this.x-16,this.y+5);
	  //ctx.fill();
  };
  
  this.move = function(mx,my) {
	
	this.x = mx;
    this.y = my;
   
  };
}
