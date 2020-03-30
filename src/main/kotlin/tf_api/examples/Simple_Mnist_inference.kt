package tf_api.examples

import org.tensorflow.Tensor
import tf_api.Input
import tf_api.Metrics
import tf_api.Output
import tf_api.TFModel
import util.MnistUtils
import java.util.*


private const val PATH_TO_MODEL = "src/main/resources/model1"
private const val IMAGE_PATH = "src/main/resources/datasets/test/t10k-images-idx3-ubyte"
private const val LABEL_PATH = "src/main/resources/datasets/test/t10k-labels-idx1-ubyte"

fun main() {
    val images = MnistUtils.mnistAsList(IMAGE_PATH, LABEL_PATH, Random(0), 10000)
    TFModel().use {
        it.loadModel(PATH_TO_MODEL) // TODO: need to combine with Reciever to avoid it. Create method with scope with name "inference or predict or tf"
        println(it)

        // TODO: extract reshape function, input and output tensors
        it.reshape(::reshape)
        it.input(Input.PLACEHOLDER) // TODO: add STRINGs too
        it.output(Output.ARGMAX)    // TODO: add STRINGs too

        val prediction = it.predict(images[0])
        println("Predicted Label is: " + prediction[0].toInt())
        println("Correct Label is: " + images[0].label)

        val predictions = it.predictAll(images)
        println(predictions.toString())

        println("Accuracy is : ${it.evaluateTFModel(images, Metrics.ACCURACY)}")

    }
}

private fun reshape(doubles: DoubleArray): Tensor<*>? {
    val reshaped = Array(
        1
    ) { Array(28) { FloatArray(28) } }
    for (i in doubles.indices) reshaped[0][i / 28][i % 28] = doubles[i].toFloat()
    return Tensor.create(reshaped)
}