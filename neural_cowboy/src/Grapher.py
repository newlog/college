'''
Created on 19/mag/2012

@author: pablosproject
'''
import pylab as pl #@UnresolvedImport
import os

class Grapher(object):
    
    '''
    THIS CLASS REPRESENT A GRAPH OBJECT. DURING THE EXECUTION OF THE PROGRAM
    IT STORE ALL THE ERROR FOR EVERY EXECUTION.
    WHEN CALL THE PRINT() METHOD IT SHOW THE GRAPH OF THE CURRENT EXECUTION
    '''
    '''STRUCTURE OF THE OBJECT
        errorList    =    Structure that contain all the error sort by iteration
        name         =    Name of the graph
        x-axis       =    Label of the x-axis
        y-axis       =    Label of y-axis
    '''
   
    '''Setting general paramenter of the plot'''
    pl.rcParams['axes.grid'] = True
        
    ##PRE-CONDITION: I do not control if the error in input is a float or
    ##                a string. I assume that is a number
    def add(self, newError):
        self.__errorList.append(newError)
       
    ''' 
    This method must not be used because it shows and
    image everytime it is called (stoping the algorithm, for example).
             
    def printGraph(self):
        x_value=range(1,len(self.__errorList)+1)
        pl.plot(x_value,self.__errorList)
       
        pl.plot(x_value,self.__errorList)
        pl.xlabel(self.__x_axis)
        pl.ylabel(self.__y_axis)
        pl.title(self.__graphName)
        pl.show()
        pl.clf()
    '''
        
    def saveGraph(self, filename, learning_type = ""):
        extension = ".png"
        x_value=range(1,len(self.__errorList)+1)
        pl.plot(x_value,self.__errorList)
        pl.xlabel(self.__x_axis)
        pl.ylabel(self.__y_axis)
        pl.title(self.__graphName)
        folder = "output_%s/training/graphics" % learning_type
        if not os.path.exists(folder):
            os.makedirs(folder)
        final_filename = "%s/%s%s" % (folder, filename, extension)
        pl.savefig(final_filename,format="png")
        pl.clf()
        
    def meanGraph(self):    
        return sum(self.__errorList)/len(self.__errorList)
    
    def clear(self):
        self.__errorList = []
        if pl:
            pl.clf()
        
    def __init__(self, name ="",x_axis="",y_axis=""):         
        self.__errorList=[]  #The constructor create a new list
        self.__graphName=name
        self.__x_axis=x_axis
        self.__y_axis=y_axis
        