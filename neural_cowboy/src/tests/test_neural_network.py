'''
Created on 24/03/2012

@author: newlog
'''
from NeuralNetwork import NeuralNetwork
from NNConfiguration import NNConfiguration


'''
1st = Not used
2st = Number of iterations of the dataset
2nd = A list containing the number of nodes in each layer
3th = The number of inputs of the neural network
4th = The path to the training set
'''


###############################################################
###############################################################
####            SUN SPOT TRAINING AND TEST START          #####
###############################################################
###############################################################

'''
The Neural Network is invoked to be trained with the sunspots
'''

cnf = NNConfiguration(None, 1, [2, 1], 2, "./src/dataset/DEBUG1/DEBUG_1_DATASET.txt")
nn = NeuralNetwork(cnf)


'''
The Neural Network is invoked to test unclassified instances with the sunspots. (Production)
'''
'''
cnf = NNConfiguration(  None, 
                        1, 
                        [21, 1], 
                        11, 
                        "./src/dataset/good_sunspot_test.dat", 
                        "./WEIGHTS_May_12_19_35.txt")
'''
'''
nn = NeuralNetwork(cnf)
'''

nn.activate(1, 1)

###############################################################
###############################################################
####            SUN SPOT TRAINING AND TEST END            #####
###############################################################
###############################################################



###############################################################
###############################################################
####            POLIO TRAINING AND TEST START           #####
###############################################################
###############################################################

'''
The Neural Network is invoked to be trained with the sunspots
'''
'''
cnf = NNConfiguration(None, 5, [21, 1], 11, "./src/dataset/good_polio.dat")
nn = NeuralNetwork(cnf)
'''

'''
The Neural Network is invoked to test unclassified instances with the sunspots. (Production)
'''
'''
cnf = NNConfiguration(  None, 
                        5, 
                        [21, 1], 
                        11, 
                        "./src/dataset/good_polio.dat.txt", 
                        "./src/dataset/DEBUG1/DEBUG_1_WEIGHTS.txt")

nn = NeuralNetwork(cnf)
'''
'''
nn.activate(11, 1)
'''
###############################################################
###############################################################
####            POLIO TRAINING AND TEST END             #####
###############################################################
###############################################################



###############################################################
###############################################################
####            DEBUG 1 TRAINING AND TEST START           #####
###############################################################
###############################################################

'''
The Neural Network is invoked to be trained with the sunspots
'''
'''
cnf = NNConfiguration(None, 1, [2, 1], 2, "./src/dataset/DEBUG1/DEBUG_1_DATASET.txt")
nn = NeuralNetwork(cnf)
'''

'''
The Neural Network is invoked to test unclassified instances with the sunspots. (Production)
'''
'''
cnf = NNConfiguration(  None, 
                        1, 
                        [2, 1], 
                        2, 
                        "./src/dataset/DEBUG1/DEBUG_1_DATASET.txt", 
                        "./src/dataset/DEBUG1/DEBUG_1_WEIGHTS.txt")

nn = NeuralNetwork(cnf)
'''
'''
nn.activate(2, 1)
'''
###############################################################
###############################################################
####            DEBUG 1 TRAINING AND TEST END             #####
###############################################################
###############################################################








# Now we have to check the neural network
layers = nn.get_layers()
print "[D] Layers: ", layers
i = 1
for layer in layers:
    print "\t[D] Nodes from layer ", i, ": ", layer.get_nodes()
    i += 1
    j = 1
    for node in layer.get_nodes():
        print "\t\t[D] Node ", j, ": "
        print "\t\t\t[D] Value: ", node.get_value()
        print "\t\t\t[D] Error: ", node.get_error()
        print "\t\t\t[D] Position in layer: ", node.get_position_in_layer()
        print "\t\t\t[D] Weights: ", node.get_weights()
        j += 1

