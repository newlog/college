'''
Created on 14/03/2012

@author: newlog
'''

from Parameters import File_Parameters
import Utils

class NNConfiguration(object):
    '''
    classdocs
    '''
    # Number of layers of the neural network
    __iterations = 1
    # First element is the number of nodes of first layer, second element...
    __nodes_per_layer = []
    # Number of inputs
    __inputs_number = -1
    # File path to the training set
    __train_file_path = None
    # Determine if configuration is stored in a file
    __conf_file = None
    # Determine if weights are stored in a file
    __weights_file = None
    # Learning type: Supervised or Unsupervised
    __learning_type = None
    # Phase: training or test
    __phase = None
    # Algorithm: backpropagation or hebbian
    __algorithm = None
    # Learning rate
    __learning_rate = None
    # Graphics enabled? on, off
    __enabled_graphics = None
    # Where to apply the sigmoid: none, output, hidden, all
    __sigmoid = None
    # Momentum parameter. 
    __momentum = 0.0

    def usage(self):
        print "\n[?] You only have to pass the configuration file."
        print "Coded by Newlog, see you console cowboys"

    # This method retrieves all needed parameters from the configuration file
    # Params:
    #        cf: A string with the path to configuration file

    def read_conf_file(self, cf):
        print "[+] Processing configuration file. [NNConfiguration]"
        try:
            fd = open(cf, 'r')
            for line in fd.readlines():
                if line.find('=') != -1:
                    conf_line = line.split('=')
                    if len(conf_line) != 2:
                        if len(conf_line) > 0:
                            print "[-] Bad configuration parameter: " + conf_line[0]
                    else:
                        param = str(conf_line[0]).strip()
                        value = str(conf_line[1]).strip()
                        self.__assign_values(param, value)  
            if fd and not fd.closed:
                fd.close()
        except IOError:
            print "[-] The configuration file could not be opened."
            print "[*] See you, Neural Cowboy."
            exit()
        

    def __assign_values(self, param, value):
        if param == File_Parameters.learning_rate:
            if Utils.is_number(value):
                self.__learning_rate = float(value)
            else:
                print "[-] Bad parameter: " + param
                print "[-] It must be a integer or float value"
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.topology:
            self.build_nodes_per_layer(value)
            if self.__nodes_per_layer and len(self.__nodes_per_layer) < 1:
                print "[-] Counting the inputs layer, the ANN " \
                      " must have at least one layer."
                print "[*] See you, Neural Cowboy"
                exit()
            else:
                if self.__nodes_per_layer and Utils.is_number(self.__nodes_per_layer[0]):
                    self.__inputs_number = int(self.__nodes_per_layer[0])
                else:
                    print "[-] There was a problem with the input layer." \
                          "It seems not to be a digit" 
                    print "[*] See you, Neural Cowboy"
                    exit()  
        elif param == File_Parameters.dataset:
            self.__train_file_path = value
        elif param == File_Parameters.dataset:
            self.__train_file_path = value
        elif param == File_Parameters.weights:
            self.__weights_file = value
        elif param == File_Parameters.algorithm:
            if value in File_Parameters.algorithm_values:
                self.__algorithm = value
            else: 
                print "[-] Bad parameter: " + param
                print "[-] Possible values: " + str(File_Parameters.algorithm_values)
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.learning_type:
            if value in File_Parameters.learning_type_values:
                self.__learning_type = value
            else: 
                print "[-] Bad parameter: " + param
                print "[-] Possible values: " + str(File_Parameters.learning_type_values)
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.phase:
            if value in File_Parameters.phase_values:
                self.__phase = value
            else: 
                print "[-] Bad parameter: " + param
                print "[-] Possible values: " + str(File_Parameters.phase_values)
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.iterations:
            if Utils.is_number(value):
                self.__iterations = int(value)
                if self.__iterations < 1:
                    print "[-] Bad parameter: " + param
                    print "[-] It must be a positive integer value"
                    print "[*] See you, Neural Cowboy"
                    exit()
            else:
                print "[-] Bad parameter: " + param
                print "[-] It must be a positive integer value"
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.graphics:
            if value in File_Parameters.graphics_values:
                self.__enabled_graphics = value
            else: 
                print "[-] Bad parameter: " + param
                print "[-] Possible values: " + str(File_Parameters.graphics_values)
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.sigmoid:
            if value in File_Parameters.sigmoid_values:
                self.__sigmoid = value
            else: 
                print "[-] Bad parameter: " + param
                print "[-] Possible values: " + str(File_Parameters.sigmoid_values)
                print "[*] See you, Neural Cowboy"
                exit()
        elif param == File_Parameters.momentum:
            if Utils.is_number(value):
                self.__momentum = float(value)
            else: 
                print "[-] Bad parameter: " + param
                print "[-] It must be a integer or float value"
                print "[*] See you, Neural Cowboy"
                exit()

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
    '''
    def get_NN_parameters(self):
        nn_parameters = [self.__iterations, self.__nodes_per_layer,
                         self.__inputs_number, self.__train_file_path,
                         self.__weights_file, self.__algorithm,
                         self.__conf_file, self.__learning_rate,
                         self.__learning_type, self.__phase,
                         self.__enabled_graphics, self.__sigmoid,
                         self.__momentum]
        return nn_parameters

    
    def build_nodes_per_layer(self, npl):
        if not self.__nodes_per_layer:
            self.__nodes_per_layer = []
        if npl:
            if not npl.split(','):
                # This condition tests if there is only one layer in the npl parameter
                if str(npl).isdigit():
                    nn = int(float(str(npl)))
                    if nn > 0:
                        self.__nodes_per_layer.append(nn)
                    else:
                        print "[-] Each layer must have, at least, one node."
                        print "[*] See you, Neural Cowboy."
                else:
                    print "[-] The nodes in layer parameter are not a digits."
                    print "[*] See you, Neural Cowboy."
                    exit()
            else:
                for node_l in npl.split(','):
                    # If there is more layers, we split them by the ,
                    if str(node_l).isdigit():
                        nn = int(float(str(node_l)))
                        if nn > 0:
                            self.__nodes_per_layer.append(nn)
                        else:
                            print "[-] Each layer must have, at least, one node."
                            print "[*] See you, Neural Cowboy."
                    else:
                        print "[-] The nodes in layer parameter are not a digits."
                        print "[*] See you, Neural Cowboy."
                        exit()

    '''
    This constructor builds all the data needed to work
    with the neural network
    '''
    def __init__(self, cf=None):
        if not cf:
            self.usage()
            exit()
        else:
            # The configuration file is set.
            # The params should be retrieved from it
            self.__conf_file = cf
            self.read_conf_file(self.__conf_file)
        