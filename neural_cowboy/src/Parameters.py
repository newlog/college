'''
Created on 26/06/2012

@author: newlog
'''

class File_Parameters(object):
    
    learning_type =         "learning-type"
    learning_type_values =  ["supervised", "unsupervised"]
    
    
    phase =                 "phase"
    phase_values =          ["train", "test", "debug"]
    
    
    algorithm =             "algorithm"
    algorithm_values =      ["backpropagation", "hebbian"]
    
    graphics =              "graphics"
    graphics_values =       ["on", "off"]

    sigmoid =               "sigmoid"
    sigmoid_values =        ["none", "output", "hidden", "all"]
    
    learning_rate =         "learning-rate"
    topology =              "topology"    
    iterations =            "iterations"
    dataset =               "dataset"
    weights =               "weights"
    momentum =              "momentum"

class Conf_Params(object):
    '''
    This method returns the parameters needed to compute the neural
    network as a list in where
        1st = Number of iterations
        2nd = A list containing the number of nodes in each layer
        3th = The number of inputs of the neural network
        4th = The path to the training set file
        5th = The path to the weights file
        6th = The algorithm to be used (backpropagation or hebbian)
        7th = The path to the configuration file
        8th = The learning rate
        9th = The learning paradigm (supervised or unsupervised)
        10th = The phase of the neural network (training or testing)
        11th = If graphics are enabled (on, off)
        12th = Where to apply the sigmoid (none, output, hidden [all layers but the last], all)
        13th = The momentum parameter
    def get_NN_parameters(self):
        nn_parameters = [self.__iterations, self.__nodes_per_layer,
                         self.__inputs_number, self.__train_file_path,
                         self.__weights_file, self.__algorithm,
                         self.__conf_file, self.__learning_rate,
                         self.__learning_type, self.__phase,
                         self.__enabled_graphics, self.__sigmoid,
                         self.__momentum]
        return nn_parameters
    '''
    ITERATIONS =        0
    NODES_PER_LAYER =   1
    INPUTS_NUMBER =     2
    TRAIN_FILE_PATH =   3
    WEIGHTS_FILE =      4
    ALGORITHM =         5
    CONF_FILE =         6
    LEARNING_RATE =     7
    LEARNING_TYPE =     8
    PHASE =             9
    ENABLED_GRAPHICS =  10
    SIGMOID =           11
    MOMENTUM =          12
    
    