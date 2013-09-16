'''
Created on 14/03/2012

@author: newlog
'''

import sys
from NNConfiguration import NNConfiguration
from NeuralNetwork import NeuralNetwork
import time


def usage():
    print "\n[?] You only have to pass the configuration file."
    print "Coded by Newlog, see you console cowboys\n"

if __name__ == '__main__':
    if len(sys.argv) != 2:
        # The program has been invoked incorrectly
        usage()
        exit()
    
    # The program has been invoked with a configuration file
    nn_conf = NNConfiguration(sys.argv[1])    
    # Now we can build the neural network
    nn = NeuralNetwork(nn_conf)
    # Now we can activate the neural network
    init = time.time()
    nn.activate()
    final = time.time()
    print "[+] Execution time: %s seconds" % str(final - init)
    print "[*] Well done, Neural Cowboy."
