'''
Created on 15/03/2012

@author: newlog
'''


class Node(object):

    __error = 0
    __position_in_layer = -1        # From [0..n-1]
    __weights = []
    __last_updates = []
    __value = -1

    def get_error(self):
        return self.__error

    def get_position_in_layer(self):
        return self.__position_in_layer

    def get_weights(self):
        return self.__weights
    
    def get_last_updates(self):
        return self.__last_updates

    def get_one_weight(self, index):
        if index > -1 and index < len(self.__weights):
            return self.__weights[index]
        else:
            print "[-] Wrong index given to weight list. [Node]"
            print "[*] See you, neural cowboy."
            exit()
    
    def get_one_update(self, index):
        if index > -1 and index < len(self.__last_updates):
            return self.__last_updates[index]
        else:
            print "[-] Wrong index given to last updates list. [Node]"
            print "[*] See you, neural cowboy."
            exit()

    def get_value(self):
        return self.__value

    def set_error(self, err):
        self.__error = err

    def set_position_in_layer(self, pos):
        self.__position_in_layer = pos

    def set_weights(self, weight_list):
        self.__weights = weight_list
    
    def set_last_updates(self, last_updates_list):
        self.__last_updates = last_updates_list

    def set_one_weight(self, weight, index):
        if index > 0 and index < len(self.__weights):
            self.__weights[index] = weight
        else:
            print "[-] Wrong index given to weight list. [Node]"
            print "[*] See you, neural cowboy."
            exit()
    
    def set_one_update(self, update, index):
        if index > 0 and index < len(self.__last_updates):
            self.__last_updates[index] = update
        else:
            print "[-] Wrong index given to last update list. [Node]"
            print "[*] See you, neural cowboy."
            exit()

    def set_value(self, val):
        self.__value = val

    '''
    __error = 0
    __position_in_layer = -1
    __weights = None
    __last_updates = None
    __value = -1
    When it is initialized, only the position in layer parameter is past.
    '''

    def __init__(self, p_i_l=-1, err=0, w=None, lu=None, v=-1):
        self.__error = err
        self.__position_in_layer = p_i_l
        self.__weights = w
        self.__last_updates = lu
        self.__value = v
