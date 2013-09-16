'''
Created on 09/06/2012

@author: newlog
'''

def cnv_dataset(filename, output):
    if not filename:
        return None
    
    fd = open(filename, 'r')
    fo = open(output, 'w')
    if not (fd and fo):
        return None
    # First two lines are trash
    for _ in xrange(2):
        fd.readline()
    
    for line in fd.readlines():
        if line.find('='):
            tmp = line.split('=')
            if len(tmp) != 2:
                return None
            new_line = ''
            i = 0
            for byte in tmp[1]:
                if i == 0:
                    new_line += str(byte)
                    i += 1
                else:
                    new_line += ',' + str(byte)
            # Erasing \n and the last ,
            new_line = new_line[0:len(new_line) - 2] 
            if len(tmp[0]) != 1:
                return None
            if str(tmp[0]) == 'R':
                new_line += ':1,0\n'
            else:
                new_line += ':0,1\n'
            
            fo.write(new_line)
    
    
if __name__ == '__main__':
    cnv_dataset('pattern_recognition/good_lines_circles_20x20.txt', 'pattern_recognition/r_good_lines_circles_20x20.txt')