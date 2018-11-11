let model;

$(".progress-bar").hide();
(async function() {
    $(".progress-bar").show();
    model = await tf.loadModel('model/model.json');
    $(".progress-bar").hide();
})();

function argMax(array) {
  return array.map((x, i) => [x, i]).reduce((r, a) => (a[0] > r[0] ? a : r))[1];
}

async function makePrediction() {
    let canvas = $("#canvas").get(0);

    // let tensor = tf.fromPixels(canvas, 1).resizeBilinear([28,28]).toFloat().div(tf.scalar(17.0)).reshape([1, 28, 28]);
    let tensor = tf.fromPixels(canvas, 1).resizeBilinear([28,28]).toFloat().reshape([1, 28, 28, 1]);

    let results = Array.from(await model.predict(tensor).data());
    let prediction = argMax(results);
    $("#textInput").html("Prediction: " + prediction);
    clearCanvas();
}
