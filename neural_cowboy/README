Neural Cowboy
=============

This code implements a generic multilayer ANN.

It is written for Python 2.7 and needs NumPy >= (1.7.0) and matplotlib >= (1.2.x) [pylab] libraries.

Usage
------

    $ python NeuralCowboy.py <config_file_path>
    $ python NeuralCowboy.py  /usr/user/desktop/NN_configuration.txt

Depending of the configuration of the neural network one result or another is expected. For example, if you are training the neural network you will get a file with the stabilized weights of the network, but if you are testing it you will get the output of the network given its inputs.

In the repository you have many examples of how to configure the network and which output it generates.

Configuration
-------------

The configuration file can have all these parameters:

    learning-rate = float_value
    topology = int_value,int_value,...,int_value
    learning-type = unsupervised | supervised
    phase = train | test
    algorithm = backpropagation | hebbian
    iterations = int_value
    dataset = file_path
    weights = file_path
    graphics = on | off
    sigmoid = none | output | hidden | all
    momentum = float_value

+ The learning-rate must be a float value that identifies the learning rate of the NN (yes, a recursive definition).

+ The topology must be a comma separated list of integers. Each integer identifies the number of nodes of each layer.
  The first number identifies the number of inputs of the NN.
  The second one identifies the number of nodes of the second layer of the NN.
  So yes, the inputs are counted as the first layer.

+ The learning-type identifies the learning paradigm of the NN. The algorithm must be set according to this parameter, so:
  supervised --> backpropagation
  unsupervised --> hebbian
  If you don't choose the algorithm according to the learning-type, the NN will fail. You will be warned.

+ The phase parameter identifies if you are training or testing the neural network.
  If you are training it, the `./*folder*/trainig/` directory will be created and the final weights of the network will be written in a file.
  If you set it, two graphic files will be saved in `./*folder*/training/graphics`.
  If you are testing it, the `./*folder*/testing/` directory will be created and the output of each dataset instance will be stored in a file. 
  NOTE: Depending on learning paradigm, `folder` will be `output_supervised` or `output_unsupervised`.

+ The algorithm parameter identifies which algorithm will be used. This parameter must be in accordance with the learning-type. [backpropagation, hebbian]
  When choosing hebbian unsupervised learning, the NN must have two layers. The input and the output layer and the sigmoid will not be applied. 
  With hebbian learning the phase must be set to 'train' or not be set.

+ The iterations parameter identifies the number of times that the neural network will be trained with the same dataset.
  This parameter is used only for training. While testing, the iterations will be set to 1.

+ The dataset parameter identifies the file name and the path in where the instances to train or test the NN are stored.
  The dataset syntanx is: x1,x2,x3:o1,o2 where 'x's are inputs of the NN and 'o's are the outputs.
  `BUG`: When testing the neural network, you don't need the outputs, but you MUST put them and you must put as many outputs as outputs has the NN. Eventually, they will be ignored. 

+ The weights parameter identifies the file name and the path in where the weights to test the NN are stored.
  This parameter is only taken in account when phase is equal to test.
  The weights syntax is a list of weight separated by a new line and the first weight will be set to the first node until the last. Up - down, left - right.
  The weights generated in the training phase are completely suitable for the test phase.

+ The graphics parameter specifies wether some fancy graphics will be generated or not.
  This parameter is only taken in account when the phase is train. Two graphic files will be saved in ./*folder*/training/graphics.
  The graphic called average_error is the average output error of all instances for all iterations.
  From the graphic called error_evolution, each point is the average error of all instances in that iteration. You have to check that this error tends to 0 in order to the NN to converge.
  `NOTE`: Depending on learning paradigm, *folder* will be 'output_supervised' or 'output_unsupervised'.

+ The sigmoid parameter specifies where the sigmoid function will be applied.
  If none, the sigmoid will not be applied to any layer. All node values will be computed without the sigmoid.
  If output, the sigmoid will be applied to the output layer. Just to the output nodes.
  If hidden, the sigmoid function will be applied to all layers (included the input layer) but the output one.
  If all, guess it...
  The sigmoid function is the tanh().
  NOTE: The sigmoid is not applied when using hebbian learning paradigm.

+ The momentum parameter specifies if a momentum should be applied to the ANN or not. Momentum usually allows you to increase your learning rate value without divergent oscillations.
  The momentum parameter must be between 0 and 1, usually 0.9, but this fact is not enforced.
  If you don't want to use it, set it to 0.



**One more thing!** If you end up doing something interesting with this code, I'll be glad to know so tell me :)


See you, Neural Cowboys. Good trip.

Newlog.
