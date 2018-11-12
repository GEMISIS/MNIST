let timer;
let predictionTimer = 2500;
let canvas = document.getElementById('canvas');
context = canvas.getContext("2d");

function drawStart(e) {
    var x = (e.pageX ? e.pageX : e.touches[0].clientX) - this.offsetLeft;
    var y = (e.pageY ? e.pageY : e.touches[0].clientY) - this.offsetTop;

    paint = true;
    addClick(x, y);
    redraw();
    clearTimeout(timer);
}

function drawMove(e) {
    if (paint) {
        var x = (e.pageX ? e.pageX : e.touches[0].clientX) - this.offsetLeft;
        var y = (e.pageY ? e.pageY : e.touches[0].clientY) - this.offsetTop;
        addClick(x, y, true);
        redraw();
    }
}

function drawEnd(e) {
    if (paint) {
        timer = setTimeout(makePrediction, predictionTimer);
    }
    paint = false;
}

// Because FireFox on Android doesn't support pointer events
// we need to keep the touch events in for the time being.
// See https://bugzilla.mozilla.org/show_bug.cgi?id=1426786
// for more details.
canvas.addEventListener("touchstart", drawStart);
canvas.addEventListener("touchmove", drawMove);
canvas.addEventListener("touchend", drawEnd);

// Also not supported on Safari, meaning no apple devices if
// not used: https://caniuse.com/#feat=pointer
// Unfortunately they also don't support the above touch events.
// See https://caniuse.com/#feat=touch
canvas.addEventListener("mousedown", drawStart);
canvas.addEventListener("mousemove", drawMove);
canvas.addEventListener("mouseup", drawEnd);

// Pointer - Disabled for now due to lack of suport :/
// canvas.addEventListener("pointerdown", drawStart);
// canvas.addEventListener("pointermove", drawMove);
// canvas.addEventListener("pointerup", drawEnd);

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
    // Note that we reset this here as otherwise it'll reset each time
    // for some reason.
    context.strokeStyle = "#111";
    context.lineJoin = "round";

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

function resize() {
    // We really only want it to take up 80% of the screen, otherwise there
    // won't be room for our prediction results + it'll add a scrollbar.
    var length = Math.round(window.innerHeight * 0.75);
    if (length > window.innerWidth * 0.75) {
        length = window.innerWidth;
    }
    
    // Update both the and canvas width and height so
    // we can continue to draw on it properly. We always
    // want square canvases
    canvas.width = canvas.height = length;
    canvas.style.width = canvas.style.height = length + "px";
    context.lineWidth = (length / 360) * 20;
}

// Resize on the initial loading and when the user resizes the window.
window.addEventListener('load', resize, false);
window.addEventListener('resize', resize, false);