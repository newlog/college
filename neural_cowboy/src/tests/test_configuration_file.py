'''
Created on 26/06/2012

@author: newlog
'''

from NNConfiguration import NNConfiguration

cnf = NNConfiguration("./src/tests/conf/good_conf.txt", None, None, None, None)
print "1.- With conf file - Parameters: " , cnf.get_NN_parameters(), "\n"

cnf = NNConfiguration("./src/tests/conf/bad_conf_2.txt", None, None, None, None)
print "1.- With conf file - Parameters: " , cnf.get_NN_parameters(), "\n"

cnf = NNConfiguration("./src/tests/conf/bad_conf_1.txt", None, None, None, None)
print "1.- With conf file - Parameters: " , cnf.get_NN_parameters(), "\n"