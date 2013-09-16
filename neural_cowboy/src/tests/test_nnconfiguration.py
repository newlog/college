'''
Created on 24/03/2012

@author: newlog
'''
from NNConfiguration import NNConfiguration

cnf = NNConfiguration(None, 2, [3, 1], 3, "./src/dataset/data.txt")
print "1.- Without conf file - Parameters: " , cnf.get_NN_parameters(), "\n"

cnf = NNConfiguration("./nonexistentfile", None, None, None, None)
print "2.- With conf file - Parameters: " , cnf.get_NN_parameters(), "\n"

cnf = NNConfiguration("./nonexistentfile",  2, [3, 1], 3, "./src/dataset/data.txt")
print "3.- Incorrect initialization - Parameters: " , cnf.get_NN_parameters(), "\n"

cnf = NNConfiguration(None,  None, [3, 1], 3, "./src/dataset/data.txt", "./src/dataset/weights.txt")
print "4.- Test initialization - Parameters: " , cnf.get_NN_parameters(), "\n"