'''
Created on 26/06/2012

@author: newlog
'''


def is_number(s):
    try:
        float(s)
        return True
    except ValueError:
        return False
