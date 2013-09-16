'''
Created on 03/06/2012

@author: newlog
'''
import numpy as np

def create_input_array():
    inputs = []
    for angle in xrange(360):
        inputs.append(angle * np.pi/180) # We convert the degrees to radians
    return inputs

def generate_serie(inputs):
    elements = np.array(inputs)
    output = np.sin(elements)
    return output
        
def write_in_file(data):
    fd = open("sinus_data.txt", 'w')
    if fd:
        for out in output:
            fd.write("{0:.4f}".format(out) + '\n')
            
    if not fd.closed:
        fd.close()

if __name__ == '__main__':
    inputs = create_input_array()
    output = generate_serie(inputs)
    write_in_file(output)
        