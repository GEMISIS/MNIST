let model;

(async function() {
    model = await tf.loadModel('modal/model.json');
    $(".progress-bar").hide();
})();

function argMax(array) {
  return array.map((x, i) => [x, i]).reduce((r, a) => (a[0] > r[0] ? a : r))[1];
}

async function makePrediction() {
    let canvas = $("#canvas").get(0);

    // let tensor = tf.fromPixels(canvas, 1).resizeBilinear([28,28]).toFloat().div(tf.scalar(17.0)).reshape([1, 28, 28]);
    let tensor = tf.fromPixels(canvas, 1).resizeBilinear([28,28]).toFloat().reshape([1, 28, 28]);

    let prediction = argMax(Array.from(await model.predict(tensor).data()));
    $("#textInput").html(prediction);
    clearCanvas();
}
