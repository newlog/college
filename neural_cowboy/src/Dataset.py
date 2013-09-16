'''
Created on 21/03/2012

@author: newlog
'''
from Parameters import Conf_Params

'''
In the dataset the instances must be defined as:
x1,x2,x3:y1,y2
where x is the input and y is the output.
'''


class Dataset(object):
    __filename = None
    __instances = None

    def open_file(self, fn):
        fd = open(fn, "r")
        return fd

    def get_instances_from_file(self, fd):
        print '[+] Storing instances in memory... [Dataset]'
        d = []
        for line in fd.readlines():
            d.append(line)
        return d

    def process_dataset(self, bad_inst, inp, out, learning_type, phase):
        print "[+] Processing instances... [Dataset]"
        ret_inst = []
        for list1 in bad_inst:
            tmp_instance = list1
            inst = []
            list1 = list1.split(":")
            if len(list1) != 2:
                print "[-] Dataset format is wrong. [Dataset]"
                print "[-] Instances must be defined" \
                " as: x1,x2,x3:y1,y2. [Dataset]"
                print "[*] See you neural cowboy."
                exit()
            else:
                inst = list1[0].split(",")
                if len(inst) != inp:
                    print "[^] Dataset format is wrong. [Dataset]"
                    print "[^] One or more instances do " \
                    "not have the proper inputs. [Dataset]"
                    print "[^] Ignoring instance: ", tmp_instance.strip(), "."\
                    " [Dataset]"
                    inst = []
                    break
                if len(list1[1].split(",")) != out:
                    print "[^] Dataset format is wrong. [Dataset]"
                    print "[^] One or more instances do not have " \
                    "the proper outputs. [Dataset]"
                    print "[^] Ignoring instance: ", tmp_instance.strip(), "."\
                    " [Dataset]"
                    inst = []
                    break
                for output in list1[1].split(","):
                    inst.append(output.strip())
            ret_inst.append(inst)
        return ret_inst

    def get_instances(self):
        return self.__instances

    def __init__(self, conf_params):
        
        npl = conf_params[Conf_Params.NODES_PER_LAYER]
        inp_n = conf_params[Conf_Params.INPUTS_NUMBER]
        pt = conf_params[Conf_Params.TRAIN_FILE_PATH]
        out_n = npl[len(npl) - 1]
        self.__filename = pt
        learning_type = conf_params[Conf_Params.LEARNING_TYPE]
        phase = conf_params[Conf_Params.PHASE]
        
        try:
            fd = self.open_file(self.__filename)
            bad_form_instances = self.get_instances_from_file(fd)
            self.__instances = self.process_dataset(bad_form_instances, inp_n, out_n, learning_type, phase)
            if fd and not fd.closed:
                fd.close()
        except IOError:
            print "[-] Dataset file could not be opened. [Dataset]"
            
        if self.__instances != None and len(self.__instances) != 0:
            print "[+] Instances correctly processed. [Dataset]"
        else:
            print "[-] Something went wrong while " \
            "processing instances. [Dataset]"
            print "[-] Dataset is probably empty. [Dataset]"
            print "[*] See you, Neural Cowboy."
            exit()
        