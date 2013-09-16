'''
Created on 15/03/2012

@author: newlog
'''


class Layer(object):

    __position = -1
    __nodes = []

    def get_position(self):
        return self.__position

    def get_nodes(self):
        return self.__nodes

    def get_one_node(self, index):
        if index > -1 and index < len(self.__nodes):
            return self.__nodes[index]
        else:
            print "[-] Wrong index given to node list when " \
            "working with Layers. [Layer]"
            print "[*] See you, neural cowboy."
            exit()

    def get_all_node_values(self):
        values = []
        for node in self.__nodes:
            values.append(float(node.get_value()))
        return values

    def get_all_node_errors(self):
        errors = []
        for node in self.__nodes:
            errors.append(float(node.get_error()))
        return errors

    '''
    This method returns a list of lists with
    the weights of all the nodes of a layer
    '''

    def get_all_weights(self):
        weights = []
        for node in self.__nodes:
            weights.append(node.get_weights())
        return weights

    def set_position(self, pos):
        self.__position = pos

    def set_nodes(self, node_list):
        self.__nodes = node_list

    def set_one_node(self, node, index):
        if index > -1 and index < len(self.__nodes):
            self.__nodes[index] = node
        else:
            print "[-] Wrong index given to node list when " \
            "working with Layers. [Layer]"
            print "[*] See you, neural cowboy."
            exit()

    def set_all_node_values(self, values):
        i = 0
        total_nodes = len(self.__nodes)
        total_values = len(values)
        for node in self.__nodes:
            if i < total_nodes and i < total_values:
                node.set_value(float(values[i]))
                i += 1

    def set_all_node_errors(self, errors):
        i = 0
        total_nodes = len(self.__nodes)
        total_errors = len(errors)
        for node in self.__nodes:
            if i < total_nodes and i < total_errors:
                node.set_error(float(errors[i]))
                i += 1

    '''
    This method sets a list of lists with the
    weights of all the nodes of a layer
    '''

    def set_all_weights(self, weights):
        i = 0
        total_nodes = len(self.__nodes)
        total_weights = len(weights)
        for node in self.__nodes:
            if i < total_nodes and i < total_weights:
                node.set_weights(float(weights[i]))
                i += 1

    '''
    __position = -1
    __nodes = []
    '''

    def __init__(self, pos, nodes_list):
        self.__position = pos
        self.__nodes = nodes_list
