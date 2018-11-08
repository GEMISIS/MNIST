let timer;
let predictionTimer = 250;
context = document.getElementById('canvas').getContext("2d");

$('#canvas').mousedown(function(e) {
    var mouseX = e.pageX - this.offsetLeft;
    var mouseY = e.pageY - this.offsetTop;

    paint = true;
    addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop);
    redraw();
    clearTimeout(timer);
});

$("#canvas").mousemove(function(e) {
    if (paint) {
        addClick(e.pageX - this.offsetLeft, e.pageY - this.offsetTop, true);
        redraw();
    }
});

$('#canvas').mouseup(function(e) {
    if (paint) {
        timer = setTimeout(makePrediction, predictionTimer);
    }
    paint = false;
});

$('#canvas').mouseleave(function(e) {
    if (paint) {
        timer = setTimeout(makePrediction, predictionTimer);
    }
    paint = false;
});

var clickX = new Array();
var clickY = new Array();
var clickDrag = new Array();
var paint;

function addClick(x, y, dragging)
{
    clickX.push(x);
    clickY.push(y);
    clickDrag.push(dragging);
}

function clearCanvas() {
    canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
    clickX = [];
    clickY = [];
    clickDrag = [];
}

function redraw() {
    // Clears the canvas
    context.clearRect(0, 0, context.canvas.style.width, context.canvas.style.height);

    // Can't be exactly 0, otherwise the image will just be blank.
    context.strokeStyle = "#111";
    context.lineJoin = "round";
    context.lineWidth = 20;

    for(var i=0; i < clickX.length; i++) {        
        context.beginPath();
        if(clickDrag[i] && i) {
            context.moveTo(clickX[i-1], clickY[i-1]);
        } else {
            context.moveTo(clickX[i]-1, clickY[i]);
        }
        context.lineTo(clickX[i], clickY[i]);
        context.closePath();
        context.stroke();
    }
}

