
fd = open("bad_fibonacci.txt", "r")
for line in fd.readlines():
	print line[line.find("=")+1:line.find(" ")]
