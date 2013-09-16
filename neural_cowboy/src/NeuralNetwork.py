'''
Created on 15/03/2012

@author: newlog
'''

from Node import Node
from Layer import Layer
from Dataset import Dataset
from NeuralUtils import NeuralUtils
from Grapher import Grapher
from Parameters import Conf_Params

class NeuralNetwork(object):

    __iterations = -1
    __layers = []
    __input_number = -1
    __input_values = []
    __output_number = -1
    __nodes_per_layer = []
    __path_training = None
    __path_weights = None
    __dataset = None
    __conf_params = None
    __learning_rate = None
    __grapher = None
    __mean_grapher = None
    __algorithm = None
    __phase = None
    __learning_type = None
    __enabled_graphics = None
    __sigmoid = None
    __momentum = None
    PRODUCTION = 2
    DEBUG = 1
    RANDOM = 0

    def get_iterations_number(self):
        return self.__iterations

    def get_layers(self):
        return self.__layers

    def get_input_number(self):
        return self.__input_number

    def get_input_values(self):
        return self.__input_values

    def get_input_layer(self):
        return self.__layers[0]

    def get_output_number(self):
        return self.__output_number

    def get_output_layer(self):
        return self.__layers[len(self.__layers) - 1]

    def get_one_output_node(self, index):
        if index > -1 and index < len(self.__layers[len(self.__layers) - 1]):
            outp = self.__layers[len(self.__layers) - 1]
            return outp[index]
        else:
            print "[-] Wrong index given to output list when working with Neural Network. [NeuralNetwork]\n[*] See you, neural cowboy."
            exit()

    def get_one_input_node(self, index):
        if index > -1 and index < len(self.__layers[0]):
            inp = self.__layers[0]
            return inp[index]
        else:
            print "[-] Wrong index given to input list when working with Neural Network. [NeuralNetwork]\n[*] See you, neural cowboy."
            exit()

    def get_one_layer(self, index):
        if index > -1 and index < len(self.__layers):
            return self.__layers[index]
        else:
            print "[-] Wrong index given to layer list when working with Neural Network. [NeuralNetwork]\n[*] See you, neural cowboy."
            exit()

    def get_dataset(self):
        return self.__dataset

    def get_conf_parameters(self):
        return self.__conf_params

    def get_learning_rate(self):
        return self.__learning_rate
    
    def get_weights_path(self):
        return self.__path_weights
    
    def get_algorithm(self):
        return self.__algorithm
    
    def get_learning_type(self):
        return self.__learning_type
    
    def get_phase(self):
        return self.__phase
    
    def get_grapher(self):
        return self.__grapher
    
    def get_mean_grapher(self):
        return self.__mean_grapher

    def get_momentum(self):
        return self.__momentum
 
    def get_sigmoid(self):
        return self.__sigmoid

    def set_iterations_number(self, layer_num):
        self.__iterations = layer_num

    def set_layers(self, layers_list):
        self.__layers = layers_list

    def set_input_layer(self, inp):
        self.__layers[0] = inp

    def set_input_number(self, inp_num):
        self.__input_number = inp_num

    def set_input_values(self, inp_val):
        self.__input_values = inp_val

    def set_output_number(self, value):
        self.__output_number = value

    def set_output_layer(self, out):
        self.__layers[len(self.__layers) - 1] = out
    
    def set_weights_path(self, path):
        self.__path_weights = path

    def set_momentum(self, value):
        self.__momentum = float(value)

    '''
    This method sets on node from the output layer
    '''

    def set_one_output_node(self, out, index):
        if index > -1 and index < len(self.__output):
            out_layer = self.__layers[len(self.__layers) - 1]
            out_layer[index] = out
            self.__layers[len(self.__layers) - 1] = out_layer
        else:
            print "[-] Wrong index given to output list when working with Neural Network. [NeuralNetwork]"
            print "[*] See you, neural cowboy."
            exit()

    '''
    This method sets on node from the input layer
    '''

    def set_one_input_node(self, inp, index):
        if index > -1 and index < len(self.__input):
            self.__input[index] = inp
            in_layer = self.__layers[0]
            in_layer[index] = inp
            self.__layers[0] = in_layer
        else:
            print "[-] Wrong index given to input list when working with Neural Network. [NeuralNetwork]"
            print "[*] See you, neural cowboy."
            exit()

    def set_one_layer(self, ly, index):
        if index > -1 and index < len(self.__layers):
            self.__layers[index] = ly
        else:
            print "[-] Wrong index given to input list when working with Neural Network. [NeuralNetwork]"
            print "[*] See you, neural cowboy."
            exit()

    def set_dataset(self, da_set):
        self.__dataset = da_set

    def set_learning_rate(self, lr):
        self.__learning_rate = float(lr)
    
    
    
    '''
    To be honest, no one have to set the algorithm, learning type
    or phase from the Neural Network class! Have in mind that if done,
    you have to take into account algorithm_values, phase_values, etc.
    
    def set_algorithm(self, alg):
        return self.__algorithm
    
    def set_learning_type(self, lt):
        return self.__learning_type
    
    def set_phase(self, ph):
        return self.__phase
    
    def set_sigmoid(self, sg):
        return self.__sigmoid
    '''
        
    '''
    This method is the interface to the activation functions
    '''

    def activate(self):
        if self.__learning_type == "supervised":
            if self.__algorithm == "backpropagation":
                print "[+] Back propagation algorithm set."
                self.activate_backpropagation()
            else:
                print   "[-] The learning paradigm is supervised " \
                        "but you have not chosen a correct algorithm."
                print   "[-] I suggest you to read the \'Introduction to the Theory of Neural Computation\' " \
                        "and come back when you are ready."
                print   "[*] Good try, pseudo Neural Cowboy."
        elif self.__learning_type == "unsupervised":
            if self.__algorithm == "hebbian":
                print "[+] Hebbian learning rule algorithm set."
                # We only accept two layer topologies (input + output)
                if len(self.__nodes_per_layer) != 2:
                    print "[-] Hebbian learning implementation only accepts one input layer plus an output layer."
                    print "[-] Bad topology."
                    print "[*] See you, Neural Cowboy." 
                else:
                    self.activate_hebbian()
            else:
                print   "[-] The learning paradigm is unsupervised " \
                        "but you have not chosen a correct algorithm."
                print   "[-] I suggest you to read the \'Introduction to the Theory of Neural Computation\' " \
                        "and come back when you are ready."
                print   "[*] Good try, pseudo Neural Cowboy."

    '''
    This method is the one that activates the neural network for the hebbian learning algorithm.
    '''

    def activate_hebbian(self):
        print "[+] Activating hebbian learning algorithm for the neural network..."
        # Let's start the magic!
        # 0.- The weights have to be initialized
        utils = NeuralUtils(self)   # I should send myself in order to NeuralUtils to have all my data.
        # With unsupervised learning the phase must always be 'train'
        if self.__phase != "train":
            print "[-] With unsupervised learning the phase must always be 'train'."
            print "[-] The weights are set as in 'train'. Proceeding..."
        utils.init_weights(self.RANDOM)
        
        # Let,s iterate through the dataset
        for _ in range(self.__iterations):
            for instance_number in range(len(self.__dataset)):
                # 1.- The ouput have to be computed. V = sum(w*input)
                output = utils.hebbian_calc_output(instance_number)
                if not output:
                    print "[-] Output for instance %s could not be computed. [NeuralNetwork]" % str(instance_number)
                    break
                # 2.- The weights have to be updated.
                utils.hebbian_update_weights()
        
        # Once finished we should write the weights given that in unsupervised learning,
        # the weights give us the Principal Component of the dataset.
        utils.write_weights_to_file("", "unsupervised")

    '''
    This method is the one that activates the neural network for the backpropagation algorithm.
    '''
    
    def activate_backpropagation(self):
        print "[+] Activating backpropagation algorithm for the neural network..."
        
        # This list will be used to draw the graphic of the average of the output error at the end of the iterations
        finalErrorLists = []
        oneIterationErrorList = []
        
        # Let's start the magic!
        # 0.- The weights have to be initialized
        utils = NeuralUtils(self)   # I should send myself in order to NeuralUtils to have all my data.
        # If the phase is test
        # we have to set the weights of the network to the ones specified in the weights file
        # if the phase is train
        # we have to set the weights of the network to random
        if self.__phase == "test":
            utils.init_weights(self.PRODUCTION)
        elif self.__phase == "train":
            utils.init_weights(self.RANDOM)
        elif self.__phase == "debug":
            utils.init_weights(self.DEBUG)
        
        if self.__phase == "test":
            self.__iterations = 1
        for it in range(self.__iterations):                 
            oneIterationErrorList = []
            for instance_number in range(len(self.__dataset)):
                # 1.- Compute output of neurons. We have to know with which instance we are working on. (To know the inputs and outputs)
                output = utils.backp_feed_forward(instance_number) # output will always be a list
                if not output:
                    print "[-] Feed forward algorithm failed for instance %s. [NeuralNetwork]" % str(instance_number)
                    break
                if self.__phase == "test":
                    # The output should be stored in a file
                    utils.write_output(output, "supervised")
                elif self.__phase == "train" or self.__phase == "debug":
                    # 2.- Compute the error
                    error_list = utils.calc_error(output, instance_number)
                    # 2.2.- We set the deltas (errors) of the output layer
                    utils.set_output_deltas(error_list)
                    # For each error (output nodes), it has to be back propagated and the weights updated
                    for error in error_list:
                        # 3.- Back propagate the error through the neural network
                        utils.backp_back_propagate(error)
                        # 4.- Update weights
                        utils.backp_update_weights()
                    # Print the average of the errors of each instance.
                    # This graphic show the error of each instance.
                    # If there is more than one output, the average error of each output is done
                    if self.__enabled_graphics: # Done for each instance
                        avError = float(sum(error_list)/len(error_list)) # average
                        oneIterationErrorList.append(avError) # error of each instance
            # --> All instances processed <-- #
            
            # Done for each iteration
            if self.__phase == "train" or self.__phase == "debug":
                print "[+] Average error in iteration " + str(it) +":"
                print sum(oneIterationErrorList)/len(oneIterationErrorList)
                # We compute the average of the errors of all instances and add the point to the mean_grapher
                if self.__enabled_graphics:
                    # Each point is the average error of all instances in that iteration
                    self.__mean_grapher.add(sum(oneIterationErrorList)/len(oneIterationErrorList))
                    # In finalErrorLists we have all the error list of each iteration
                    finalErrorLists.append(oneIterationErrorList)
            # --> End of Iteration <-- #
            
        # --> End of all Iterations <-- #
        if self.__phase == "train" or self.__phase == "debug":
            # Write the final weights to a file
            utils.write_weights_to_file(str(it), "supervised")
            # Compute and save graphics 
            if self.__enabled_graphics:
                utils.compute_and_save_graphics(finalErrorLists)       





    def __init__(self, nn_conf):
        print "[+] Building neural network... [NeuralNetwork]"

        self.__conf_params = nn_conf.get_NN_parameters()

        self.__iterations = self.__conf_params[Conf_Params.ITERATIONS]
        self.__nodes_per_layer = self.__conf_params[Conf_Params.NODES_PER_LAYER]
        self.__input_number = self.__conf_params[Conf_Params.INPUTS_NUMBER]
        self.__path_training = self.__conf_params[Conf_Params.TRAIN_FILE_PATH]
        self.__output_number = self.__nodes_per_layer[len(self.__nodes_per_layer) - 1]
        self.__path_weights = self.__conf_params[Conf_Params.WEIGHTS_FILE]
        self.__algorithm = self.__conf_params[Conf_Params.ALGORITHM]
        # The self.__conf_params[6] is the conf_file. Here it is not needed.
        self.__learning_rate = self.__conf_params[Conf_Params.LEARNING_RATE]
        self.__learning_type = self.__conf_params[Conf_Params.LEARNING_TYPE]
        self.__phase = self.__conf_params[Conf_Params.PHASE]
        self.__enabled_graphics = self.__conf_params[Conf_Params.ENABLED_GRAPHICS]
        if self.__enabled_graphics == "on":
            # Each point is the average error of all instances in that iteration
            self.__mean_grapher = Grapher("Error evolution", "Iteration", "Error")
            # Average output error of all instances for all iterations
            self.__grapher = Grapher("Average output error of all instances for all iterations", "Instance", "Error")
            # If something goes wrong initializating the graphers
            if not (self.__mean_grapher and self.__grapher): 
                self.__enabled_graphics = "off"
        self.__sigmoid = self.__conf_params[Conf_Params.SIGMOID]
        self.__momentum = self.__conf_params[Conf_Params.MOMENTUM]


        # First of all, we add the input nodes as a layer. We will work with input nodes as nodes in a normal layer
        # Layer 0 will be the one with the inputs.
        '''
        We don't have anymore the input nodes list. That data belongs to the topology parameter
        count = 0
        nodes_list = []
        for i in range(self.__input_number):
            node = Node(i)
            nodes_list.append(node)
        layer = Layer(count, nodes_list)
        self.__layers.append(layer)
        '''
        count = 0
        for n_nodes in self.__nodes_per_layer:
            nodes_list = []
            count += 1
            for i in range(n_nodes):
                node = Node(i)    # pos_in_layer, error, weight list, value. If not specified, getting default values in node constructor.
                nodes_list.append(node)
            layer = Layer(count, nodes_list)
            self.__layers.append(layer)

        if not self.__layers:
            print "[-] There are no layers. Something went wrong. [NeuralNetwork]"
            print "[*] See you, neural cowboy."
            exit()

        if len(self.__layers) == 1:
            print "[-] The is only one layer (in theory, input layer). You need at least inputs and one more layer. [NeuralNetwork]"
            print "[*] See you, neural cowboy."
            exit()

        # Here we have built all the layers with the nodes of the neural network.
        # We have to obtain all the dataset instances. The instances will be stored in a list
        # of lists of n+1 positions where n is the number of inputs and x is the number of outputs
        ds = Dataset(self.__conf_params)
        self.__dataset = ds.get_instances()
        # Once here, we have to return and the activate method must be invoked.
        print "[+] Neural network built. [NeuralNetwork]"
