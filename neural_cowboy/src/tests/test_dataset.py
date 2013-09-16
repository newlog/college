'''
Created on 24/03/2012

@author: newlog
'''
from Dataset import Dataset
##################################
# CODE TO KNOW CURRENT DIRECTORY #
##################################
import os

curpath = os.path.abspath(os.curdir)
packet_file = "%s/%s.txt" % ("dataset", "dataset")
print "Current path is: %s" % (curpath)
print "Trying to open: %s" % (os.path.join(curpath, packet_file))

#########################################
# END OF CODE TO KNOW CURRENT DIRECTORY #
#########################################

print "##############################"
print "##     TESTING DATASET      ##"
print "##############################"


ds = Dataset("./src/dataset/prove.txt", 5, 1)
data = ds.get_instances()
print "Data.txt - Printing instances: " , data

ds = Dataset("./src/dataset/data2.txt", 5, 1)
data = ds.get_instances()
print "Data2.txt - Printing instances: " , data

ds = Dataset("./src/dataset/data3.txt", 1, 1)
data = ds.get_instances()
print "Data3.txt - Printing instances: " , data

ds = Dataset("./src/dataset/data4.txt", 1, 1)
data = ds.get_instances()
print "Data4.txt - Printing instances: " , data

ds = Dataset("./src/dataset/data5.txt", 1, 1)
data = ds.get_instances()
print "Data5.txt - Printing instances: " , data