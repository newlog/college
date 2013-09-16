'''
Created on 30/03/2012

@author: newlog
'''
import sys

def usage():
    print "\n\nThis script creates a dataset with instance format as:"
    print "x1,x2,...,xn:y1,y2,...,yk"
    print "From a file with all the instances."
    print "Usage: "
    print sys.argv[0], " <inputs_number> <outputs_number> <instances_file> <output_file>\n\n"
    
def open_file(fn, mode):
    fd = open(fn, mode)
    return fd

def create_output_file(fd, inp, outs, out_file):
    fd_out = open(out_file, "w")
    lines = fd.readlines()
    lines =  [line.strip() for line in lines]
    ap = 0
    rip = 0
    rop = 0
    while ( ap < len(lines) - inp - outs + 1 ):
        while ( rip != inp ):
            fd_out.write(lines[ap + rip])
            if (rip < inp - 1):
                fd_out.write(",")
            rip += 1
        
        fd_out.write(":")
        fd_out.flush()
        
        while ( rop != outs):
            fd_out.write(lines[ap + rip + rop])
            if (rop < outs - 1):
                fd_out.write(",")
            rop += 1
        ap += 1
        rop = 0
        rip = 0  
        fd_out.write("\n")
        fd_out.flush()
    
    return fd_out


if __name__ == '__main__':
    if len(sys.argv) != 5:
        # The program has been invoked incorrectly
        usage()
        exit()
    fd_in = open_file(sys.argv[3], "r")
    fd_out = create_output_file(fd_in, int(sys.argv[1]), int(sys.argv[2]), sys.argv[4] )
    
    if (not fd_in.closed):
        fd_in.close()
    if (not fd_out.closed):
        fd_out.close()