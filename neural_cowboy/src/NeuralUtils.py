'''
Created on 23/03/2012

@author: newlog
'''
import random
import numpy as np
import datetime
import os
from Parameters import Conf_Params


class NeuralUtils(object):

    # A reference to the neural network
    __nn = None

    '''
    Interface to initialize weights.
        If 0: Random initialization.
        If 1: Linear initialization.
        If 2: Specific weights initialization.
    '''
    def init_weights(self, init_type):
        if init_type == 0:
            #self.init_random_weights()
            self.easy_random_weights()
        elif init_type == 1:
            self.init_linear_weights()
        elif init_type == 2:
            self.init_specified_weights()
        else:
            self.init_random_weights()
        self.init_last_update_list()
    '''
    This method initialize the last_update list from the Node class (for each node).
    The lists are initialized to 0. This list is used if the momentum is set.
    '''

    def init_last_update_list(self):
        print "[+] Initializing last update list to 0..."
        layers = self.__nn.get_layers()
        i = 0
        for layer in layers:            # For each layer
            if i == len(layers) - 1:    # Not minus 1 because the input layer is not counted as a layer and we have to weight it.
                break
            last_update_list = []       
            last_update_list.append(float(0))        
            next_layer_node_number = len(self.__nn.get_one_layer(i + 1).get_nodes())
            last_update_list *= next_layer_node_number  # [0,0,...0] the same number of positions as number of weights
            for node in layer.get_nodes():              # For each node
                node.set_last_updates(last_update_list)
            i += 1
                    
                    
    def init_random_weights(self):
        '''
        In order to not get stuck in a local minina near to the start point,
        the weight must be of the order of (1 / Ki)^(1/2), where Ki is the number of j's
        which feed forward to i.
        '''

        '''
        Ki will be the number of connections of the node (the number of nodes of the next layer).
        '''
        print "[+] Initializing random weights..."
        layers = self.__nn.get_layers()
        i = 0
        for layer in layers:                        # For each layer
            if i == len(layers) - 1: # Not minus 1 because the input layer is not counted as a layer and we have to weight it.
                break
            j = 0
            for node in layer.get_nodes():          # For each node
                weights = []
                next_layer_node_number = len(self.__nn.get_one_layer(i + 1).get_nodes())
                for _ in range(next_layer_node_number):   # For each node connection
                    rand_int = random.randint(0, 53)
                    # If next_layer_node_number is 1, the module will always be 0,
                    # therefore, the weight will always be 0. We must consider this as
                    # an special case.
                    if next_layer_node_number != 1:
                        weights.append(rand_int % (1 / np.sqrt(next_layer_node_number)))
                    else:
                        new_rand = 0
                        while (new_rand == 0):
                            new_rand = random.randint(0, 29)
                        weights.append(rand_int % (1 / np.sqrt(new_rand)))
                node.set_weights(weights)
                # This is not necessary because when setting weights through node,
                # the weights are modified in the proper node and not in the (possible) copied node
                # obtained by the iterator with get_nodes(). It is not necessary to reinsert node.
                #layer.set_one_node(node, j)   # Minus 1 because the nodes go from 0 to n. And node_number starts with 1.
                j += 1
            i += 1
    
    def easy_random_weights(self):
        '''
        Random number in the range (0, 1]
        '''

        print "[+] Initializing random weights..."
        layers = self.__nn.get_layers()
        i = 0
        for layer in layers:                        # For each layer
            if i == len(layers) - 1: 
                break
            for node in layer.get_nodes():          # For each node
                weights = []
                next_layer_node_number = len(self.__nn.get_one_layer(i + 1).get_nodes())
                for _ in range(next_layer_node_number):   # For each node connection
                    rand_float = random.uniform(0.001, 1)
                    weights.append(rand_float)
                node.set_weights(weights)
                # This is not necessary because when setting weights through node,
                # the weights are modified in the proper node and not in the (possible) copied node
                # obtained by the iterator with get_nodes(). It is not necessary to reinsert node.
                #layer.set_one_node(node, j)   # Minus 1 because the nodes go from 0 to n. And node_number starts with 1.
            i += 1

    
    '''
    This method is only to debug. It initializes the weights in a linear mode
    in order to be able to identify which neurons have them.
    '''
    def init_linear_weights(self):
        print "[+] Initializing linear weights..."
        layers = self.__nn.get_layers()
        i = 0
        node_number = 0
        j = 0
        for layer in layers:                        # For each layer
            #Dj = 0
            for node in layer.get_nodes():          # For each node
                if i >= len(layers) - 1: # Not minus 1 because the input layer is not counted as a layer and we have to weight it.
                    break
                weights = []
                next_layer_node_number = len(self.__nn.get_one_layer(i + 1).get_nodes())
                for _ in range(next_layer_node_number):   # For each node connection
                    #Dweights.append(node_number * 0.1)
                    j = j + 1
                    weights.append(j)
                node.set_weights(weights)
                # This is not necessary because when setting weights through node,
                # the weights are modified in the proper node and not in the (possible) copied node
                # obtained by the iterator with get_nodes(). It is not necessary to reinsert node.
                #layer.set_one_node(node, j)   # Minus 1 because the nodes go from 0 to n. And node_number starts with 1.
                #Dj += 1
                node_number += 1
            i += 1

    '''
    This method is used when the NN is in the production stage. Not when trained.
    The weight are retrieved from a file specified by the user.v 
    '''
    def init_specified_weights(self):
        print "[+] Initializing production weights... [NeuralUtils.init_specified_weights]"
        
        weights_path = self.__nn.get_weights_path()
        if not weights_path:
            print "[-] The weights file is not specified. [NeuralUtils]"
            return None
        fd = open(weights_path, "r")
        if not fd:
            print "[-] The weights file could not be opened. [NeuralUtils]"
            return None
        
        weights = []
        for line in fd.readlines():
            weights.append(float(line))
        
        layers = self.__nn.get_layers()
        i = 0
        node_number = 0
        for layer in layers:                        # For each layer
            j = 0
            for node in layer.get_nodes():          # For each node
                if i >= len(layers) - 1: # Minus 1 because the input layer is counted as a layer and we have to weight it.
                    break
                weights_tmp = []
                next_layer_node_number = len(self.__nn.get_one_layer(i + 1).get_nodes())
                for _ in range(next_layer_node_number):   # For each node connection
                    weights_tmp.append(weights[node_number])
                    node_number += 1
                node.set_weights(weights_tmp)
                # This is not necessary because when setting weights through node,
                # the weights are modified in the proper node and not in the (possible) copied node
                # obtained by the iterator with get_nodes(). It is not necessary to reinsert node.
                #layer.set_one_node(node, j)   # Minus 1 because the nodes go from 0 to n. And node_number starts with 1.
                j += 1
            i += 1  
        if not fd.closed:
            fd.close()

    '''
    This method computes the output of the network when the backpropagation algorithm is set. 
    Therefore, the output of each layer has to be calculated.
    '''

    def backp_feed_forward(self, instance_number):
        '''
        Output expression: [Not expressed mathematically correct]
        out = sum (w_prior_layer . inp_prior_layer)
        Mathematically correct:
        out_j_u = sum_k(w_jk . inp_k_u)
        where: 
            inp_k_u is input (k) for pattern u
            w_jk is the weight of input (k) to the node of which you compute the output (j)
            out_j_u is the output of node j for pattern u
        '''
        conf_params = self.__nn.get_conf_parameters()
        input_number = conf_params[Conf_Params.INPUTS_NUMBER]
        dataset = self.__nn.get_dataset()
        if instance_number >= len(dataset):
            print "[-] The instance to be tested is greater than the number of instances. Ignoring call to backp_feed_forward. [NeuralUtils]"
            return
        instance = dataset[instance_number]
        inputs = instance[:input_number]        # From 0 to number of inputs

        layers = self.__nn.get_layers()
        if not layers:
            print "[-] Layers list empty. Can not traverse neural network. [NeuralUtils]"
            return
        # Setting each node value (output) from input layer to the inputs of the dataset instance
        i = 0
        for node in layers[0].get_nodes():
            if i >= len(inputs):
                print "[^] Warning: For instance ", instance_number, ", there are less inputs than input nodes. Going on. [NeuralUtils]"
                break
            node.set_value(inputs[i])
            i += 1
        # Inputs set for input layer


        # Traversing neural network
        sigmoid = self.__nn.get_sigmoid()
        layers = self.__nn.get_layers()
        num_of_layers = len(layers)
        prior_layer = None
        i = 0
        for layer in layers:                        # For each layer
            # For the input layer we do nothing.
            if i > 0:
                prior_values = prior_layer.get_all_node_values()
                if not prior_values:
                    print "[-] Error obtaining layer values to perform the scalar product. The final neural network might not be valid."
                    break
                sp_weights = self.__build_weights_for_scalar_product(prior_layer)
                if not sp_weights:
                    print "[-] Error building weights to perform the scalar product. The final neural network might not be valid."
                    break
                j = 0
                for node in layer.get_nodes():          # For each node
                    # Not minus 1 because the input layer is not counted as a layer and we have to weight it.
                    # We don't put an = because it would jump and doesn't process the last layer.
                    if i > num_of_layers:
                        break
                    scalar_product = np.dot(prior_values, sp_weights[j])
                    
                    if sigmoid == "none":
                        node.set_value(scalar_product)
                    elif sigmoid == "output":
                        if i != num_of_layers - 1: # If 2 layers and one input layer, then num_of_layers = 3. Therefore we substract 1 to num_of_layers
                            node.set_value(self.__sigmoid_function(scalar_product))
                        else: # If we are processing the output (last layer) we don't use the sigmoid function
                            node.set_value(scalar_product)
                    elif sigmoid == "hidden": # The sigmoid must be applied in all layers but the output
                        if i != num_of_layers - 1: # If 2 layers and one input layer, then num_of_layers = 3. Therefore we substract 1 to num_of_layers
                            node.set_value(scalar_product)
                        else: # If we are processing the output (last layer) we use the sigmoid function
                            node.set_value(self.__sigmoid_function(scalar_product))
                    elif sigmoid == "all":
                        node.set_value(self.__sigmoid_function(scalar_product))
                    
                    j += 1
            i += 1
            prior_layer = layer
        # Neural network traversed

        # Obtaining output
        nn_output = layers[len(layers) - 1].get_all_node_values()
        return nn_output

    '''
    if [[1,2,3], [4,5,6], [7,8,9]], return [[1,4,7], [2,5,8], [3,6,9]]
    '''

    def __build_weights_for_scalar_product(self, layer):
        if not layer or len(layer.get_nodes()) == 0:
            print "[-] Empty layer. Can not build weight list for scalar product. [NeuralUtils]."
            return []
        sp_weights = []
        all_weights_list = layer.get_all_weights()
        if not all_weights_list:
            print "[-] Empty weight list. Can not build weight list for scalar product. [NeuralUtils]."
            return []
        next_layer_total_nodes = len(all_weights_list[0]) # All nodes must have the same number of connections (weights). So we pick up the first number of conenctions/weights
        node_number = 0
        while next_layer_total_nodes > 0:
            pre_weights = []
            for weights in all_weights_list:
                pre_weights.append(weights[node_number])

            sp_weights.append(pre_weights)
            node_number += 1
            next_layer_total_nodes -= 1

        return sp_weights

    '''
    This method computes the output of the network when the hebbian learning algorithm is set.
    '''

    def hebbian_calc_output(self, instance_number):
        '''
        Output expression:
        out_i = sum_j(w_ij . inp_j)
        where:
            out_i is the node (i) of which you want to compute the output
            w_ij is the weight from the input (j) to the node of which you want to compute output (i)
            inp_j is the input node (j)
        NOTE: This is the same as the feed forward algorithm, but without the sigmoid function
        '''
        conf_params = self.__nn.get_conf_parameters()
        input_number = conf_params[Conf_Params.INPUTS_NUMBER]
        dataset = self.__nn.get_dataset()
        if instance_number >= len(dataset):
            print "[-] The instance to be tested is greater than the number of instances. Ignoring call to hebbian_calc_output. [NeuralUtils]"
            return
        instance = dataset[instance_number]
        inputs = instance[:input_number]        # From 0 to number of inputs

        layers = self.__nn.get_layers()
        if not layers:
            print "[-] Layers list empty. Can not traverse neural network. [NeuralUtils]"
            return
        # Setting each node value (output) from input layer to the inputs of the dataset instance
        i = 0
        for node in layers[0].get_nodes():
            if i >= len(inputs):
                print "[^] Warning: For instance ", instance_number, ", there are less inputs than input nodes. Going on. [NeuralUtils]"
                break
            node.set_value(inputs[i])
            i += 1
        # Inputs set for input layer


        # Traversing neural network
        layers = self.__nn.get_layers()
        num_of_layers = len(layers)
        prior_layer = None
        i = 0
        for layer in layers:      # For each layer (with supervised, we only have one layer and input)
            # For the input layer we do nothing.
            if i > 0:
                prior_values = prior_layer.get_all_node_values()
                if not prior_values:
                    print "[-] Error obtaining layer values to perform the scalar product. The final neural network might not be valid. [NeuralUtils]"
                    break
                sp_weights = self.__build_weights_for_scalar_product(prior_layer)
                if not sp_weights:
                    print "[-] Error building weights to perform the scalar product. The final neural network might not be valid. [NeuralUtils]"
                    break
                j = 0
                for node in layer.get_nodes():          # For each node
                    # Not minus 1 because the input layer is not counted as a layer and we have to weight it.
                    # We don't put an = because it would jump and doesn't process the last layer.
                    if i > num_of_layers:
                        break
                    scalar_product = np.dot(prior_values, sp_weights[j])
                    node.set_value(scalar_product)
                    j += 1
            i += 1
            prior_layer = layer
        # Neural network traversed

        # Obtaining output
        nn_output = layers[len(layers) - 1].get_all_node_values()
        return nn_output

    '''
    This function returns the gotten (average, if more that one output) output minus the expected output
    '''

    def calc_error(self, calc_output, instance_number):
        dataset = self.__nn.get_dataset()
        if instance_number >= len(dataset):
            print "[-] The instance to be tested is greater than the number of instances. Ignoring call to backp_feed_forward. [NeuralUtils]"
            return

        conf_params = self.__nn.get_conf_parameters()
        input_number = conf_params[Conf_Params.INPUTS_NUMBER]
        instance = dataset[instance_number]
        real_output = instance[input_number:]   # From number of inputs to n (tested. It is correct to not substract 1)
        if instance_number == len(dataset) - 1:
            print "Computed output: ", calc_output
            print "Real output: " , real_output

        real_float_output = []
        for num in real_output:
            real_float_output.append(float(num))
        '''
        [1,2,3] and [4,5,6] --> zip --> [(1,4), (2,5), (3,6)]
        So the list comprehension returns [-3, -3, -3]
        '''
        return [(x - y) for x, y in zip(real_float_output, calc_output)]

    '''
    We use hyperbolic tangent instead of the typical 1/(1+e^-x).
    '''

    def __sigmoid_function(self, val):
        return np.tanh(val)

    '''
    Our sigmoid derivate
    '''

    def __sigmoid_derivate(self, val):
        return (1.0 - val) * val

    def set_output_deltas(self, errors):
        layers = self.__nn.get_layers()
        nodes = layers[len(layers) - 1].get_nodes()
        for node_num in range(len(nodes)):
            nodes[node_num].set_error(float(errors[node_num]))

    '''
    This functions backpropagates the given error through the neural network
    The error parameter is the error computed in the output of the neural network.
    '''

    def backp_back_propagate(self, error):

        layers = self.__nn.get_layers()
        prior_layer = None
        i = 0
        for layer in reversed(layers):
            if i == 0:                  # We process the output layer
                pass
            else:
                # First, we need the deltas (attribute called error) of the previous layer. (First time here, the previous layer is the output layer)
                deltas_list = prior_layer.get_all_node_errors()
                # Second, we need the weights of the actual layer.
                # For each node in the layer we have a list of weights
                '''
                layer_nodes = layer.get_nodes()
                if len(layer_nodes) < 1:
                    print "[-] Layer without nodes found when backpropagating. [NeuralUtils]"
                    break
                '''
                # Depending of the number of weight we have to built them in a specific way
                #if len(layer_nodes[0].get_weights()) == 1:
                sp_weights = layer.get_all_weights()
                '''
                else:
                    sp_weights = self.__build_weights_for_scalar_product(layer)
                if not sp_weights:
                    print "[-] Error building weights to perform the scalar product. The final neural network might not be valid."
                    break
                '''
                # Third, for each node in the layer, we have to compute the scalar product
                j = 0
                for node in layer.get_nodes():
                    scalar_product = np.dot(deltas_list, sp_weights[j])
                    #delta = scalar_product * self.__sigmoid_derivate(error)
                    #node.set_error(delta)
                    node.set_error(scalar_product)
                    j += 1

            prior_layer = layer
            i += 1

    '''
    This method updates the weights of the neural network when the backpropagation algorithm is set
    and the neural network is executed in the training or debug phase
    '''

    def backp_update_weights(self):
        '''
        Update expression:
        w_ij = w_ij - eta. delta_j . y_j . (1 - y_j) . y_i
        '''

        next_layer = None
        conf_params = self.__nn.get_conf_parameters()
        momentum = conf_params[Conf_Params.MOMENTUM]
        layers = self.__nn.get_layers()
        total_layers = len(layers)
        layer_num = 0
        for layer in layers:
            if layer_num == total_layers - 1:    # The update process has finished
                return
            next_layer = self.__nn.get_one_layer(layer_num + 1)
            next_nodes = next_layer.get_nodes()
            actual_nodes = layer.get_nodes()
            for node in actual_nodes:
                node_weights = node.get_weights()
                if momentum:
                    last_update_list = node.get_last_updates()
                tmp_weights = []
                tmp_update_list = []
                j = 0
                for weight in node_weights:
                    # Here we have the weight of node i (node) to next layer node j (j)
                    y_j = self.__sigmoid_function(float(next_nodes[j].get_value()))
                    #DEBUGy_j = float(next_nodes[j].get_value())
                    der = self.__sigmoid_derivate(y_j)
                    y_i = self.__sigmoid_function(float(node.get_value()))
                    #DEBUGy_i = float(node.get_value())
                    delta_j = float(next_nodes[j].get_error())
                    eta = self.__nn.get_learning_rate()
                    update = eta * delta_j * der * y_i
                    if momentum:
                        last_update = last_update_list[j]
                        print "Weight = ", weight
                        print "Update = ", update
                        weight = weight + update + momentum * last_update
                        print "New weight = ", weight
                    else:
                        print "Weight = ", weight
                        print "Update = ", update
                        weight = weight + update
                        print "New weight = ", weight
                        
                    tmp_weights.append(weight)
                    tmp_update_list.append(update)
                    j += 1
                node.set_weights(tmp_weights)
                node.set_last_updates(tmp_update_list)
            layer_num += 1
            
    
    '''
    This method updates the weights of the neural network when the hebbian algorithm is set
    and the neural network is executed in the training or debug phase
    '''

    def hebbian_update_weights(self):
        '''
        Update expression:
        w_ij = w_ij + eta . out_i . (inp_j - sum_k=1_to_i(out_k . w_kj))
        NOTE: With hebbian learning, the i and j indexs are reversed.
              i for output, j for input
        '''
        next_layer = None
        conf_params = self.__nn.get_conf_parameters()
        momentum = conf_params[Conf_Params.MOMENTUM]
        eta = conf_params[Conf_Params.LEARNING_RATE]
        layers = self.__nn.get_layers()
        total_layers = len(layers)
        layer_num = 0
        for layer in layers:
            if layer_num == total_layers - 1:    # The update process has finished
                return
            next_layer = self.__nn.get_one_layer(layer_num + 1)
            next_node_values = next_layer.get_all_node_values()
            actual_nodes = layer.get_nodes()
            actual_node_values = layer.get_all_node_values()
            # i identifies the node number
            i = 0
            for node in actual_nodes:
                node_weights = node.get_weights()
                if momentum:
                    last_update_list = node.get_last_updates()
                tmp_weights = []
                tmp_update_list = []
                # j identifies the weight number
                j = 0
                summation = 0
                for weight in node_weights:
                    # With hebbian learning, the i and j indexs are reversed.
                    # We need the value of each output node.
                    Vi = next_node_values[j]
                    # We need the value of the ith input node
                    Ij = actual_node_values[i]
                    # We compute the summation : SUM_k=1,i(Vk*Wkj)
                    tmp = next_node_values[j] * weight 
                    summation += tmp
                    # Compute the update
                    update = eta * Vi * (Ij - summation)
                    if momentum:
                        last_update = last_update_list[j]
                        weight = weight + update + momentum * last_update
                    else:
                        weight = weight + update
                        
                    tmp_weights.append(weight)
                    tmp_update_list.append(update)
                    j += 1
                node.set_weights(tmp_weights)
                node.set_last_updates(tmp_update_list)
                i += 1
            layer_num += 1

    def write_weights_to_file(self, it = "", learning_type = ""):
        filename = datetime.datetime.now().strftime("%b_%y_%H_%M")
        folder = "output_%s/training" % learning_type
        filename = "%s/%s_iteration%s_%s%s" % (folder, "train_weights", it, filename, ".txt")
        if not os.path.exists(folder):
            os.makedirs(folder)
        try:
            fd = open(filename, "w")
            
            layers = self.__nn.get_layers()
            total_layers = len(layers)
            layer_num = 0
            for layer in layers:
                if layer_num == total_layers - 1:    # The update process has finished
                    return
                actual_nodes = layer.get_nodes()
                for node in actual_nodes:
                    node_weights = node.get_weights()
                    for weight in node_weights:
                        fd.write(str(weight)+"\n")
                        fd.flush()
                layer_num += 1
            
            if fd and not fd.closed:
                fd.close()
        except IOError:
            print "[-] The weights file could not be generated. [NeuralUtils]"

    '''
    This method stores the output of the NN in a file. It will only
    be executed when the NN is being tested, not training.
    '''
    def write_output(self, outputs, learning_type = ""):
        filename = datetime.datetime.now().strftime("%b_%y_%H_%M")
        folder = "output_%s/testing" % learning_type
        pre_filename = "%s/%s_%s%s" % (folder, "test_out_values", filename, ".txt")
        if not os.path.exists(folder):
            os.makedirs(folder)
        try:
            fd = open(pre_filename, "a")
            for output in outputs:
                fd.write(str(output) + " ")
            fd.write("\n")
            if fd and not fd.closed:
                fd.close()
        except IOError:
            print "[-] The output file could not be generated. [NeuralUtils]"


    '''
    This method finish the computation of the average error and saves the average error
    and the error evolution in two different files
    '''

    def compute_and_save_graphics(self, finalErrorLists):
        
        grapher = self.__nn.get_grapher()
        mean_grapher = self.__nn.get_mean_grapher()
        
        sumList = [0] * len(finalErrorLists[0])
        # error_list is a list of the output error of each instance in a iteration
        for error_list in finalErrorLists:
            i = 0
            # We add the error of each instance, so eventually in each list item
            # we will have the the sum of all the errors of that instance through all iterations
            for error in error_list:
                sumList[i] += float(error)
                i += 1
        i = 0
        # Finally, we get the the average of all the errors of that instance through all iterations
        for error in sumList:
            sumList[i] = float(sumList[i] / len(finalErrorLists)) 
            i += 1
            
        for error in sumList:
            grapher.add(error)
            
        filename_ = datetime.datetime.now().strftime("%b_%y_%H_%M")
        filename = "average_error_%s" % filename_
        grapher.saveGraph(filename, "supervised")        
        filename = "error_evolution_%s" % filename_
        mean_grapher.saveGraph(filename, "supervised") 



    def __init__(self, nn):
        if nn == None:
            print "[-] The neural network is not initialized. [NeuralUtils]"
            print "[-] Neural utils could not be built. [NeuralUtils]"
            print "[*] See you, neural cowboy."
            exit()

        self.__nn = nn

        print "[+] Neural utils built. [NeuralUtils]"
