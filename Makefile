# file: Makefile
#
#	You must NOT change this Makefile. 
#		It includes another file "make-include", which you MAY change.
#		I provide a sample "make-include" to show 
#			how I customize my configuration, namely,
#				(Cygwin installed on a Windows 10 laptop).
#			You should customize it for own environment
#				(Mac, Linux, Windows).
#		If you are in Windows, then I suggest you install the Cygwin
#		to give you a Unix/Linux like operating system inside Windows.
#		You can freely move between Windows and Cygwin, and share
#		each other's files. Talk to our Tutor Metarya for other OS.
# 
#	HINTS:
#		-- This file is best viewed with an editor
#			that understands Makefile syntax and provides syntax colors
#		-- Note the following "standard" variables:
#
#				p: program to run/compile
#				ss: seed for random generator (ss=0 is special)
#				nn: int value used by many programs
#				mm: int value if we need a second int 
#
#############################################
#  CS 301, Basic Algorithms, Fall 2021
#	Prof. Chee Yap
#	TAs: Bingran Shen and Zihan Feng
#############################################

# ================================================
# ENVIRONMENT CUSTOMIZATION:
# ================================================
# Put your customizations in make-include file.
# 	To ensure that the default target is NOT over-ridden
#	by your make-include, we put the default here:
default: compileAll

-include make-include

# program 
# ================================================
# Hello should be always available for small Java experiments
p=Hello
p=Hello1 		# think of Hello1 as a variant of Hello
p=Tree23

# standard command line arguments:
# ================================================
# the first 3 arguments for command lines are three integers:
ss=111
nn=10
mm=0
# Other arguments are optional:
a3=true
a4=abc
# We assemble them into a single argument:
args=$(ss) $(nn) $(mm) $(a3) $(a4) 

# ================================================
# TARGETS
# ================================================
h help:
	-@echo "HELP:"
	-echo "    >make                  -- compile everything" 
	-@echo "    >make c               -- compile \$$(p)"
	-@echo "    >make r               -- run \$$(p)" 
	-@echo "    >make cr              -- compile and run \$$(p)" 
	-@echo "    >make t1 nn=12 ss=0   -- test1" 
	-@echo "    >make t2 mm=2 nn=321  -- test2 (etc)"

# default is to compile all *java programs
ca compileAll:
	test -d bin || mkdir bin
	$(javac) $(cflags) -d bin *.java 

# for doing only ONE program (great in debugging):
cr compileRun: c r

c compile javac compileOne:
	test -d bin || mkdir bin
	$(javac) $(cflags) -d bin $(p).java

r run java: 
	$(java) $(rflags) -classpath bin $(p) $(args)

r0 run0 java0: 
	$(java) $(rflags) -classpath bin $(p) 

# running a variant of $(p):
r1 run1 java1: 
	$(java) $(rflags) -classpath bin $(p)1 $(args) 

s showargs:
	@printf "ss= $(ss), nn=$(nn), mm=$(mm)\n"
	@printf "args= $(args)\n"

hello:
	@echo "Hello program is always available for testing!"
	$(java) -classpath bin Hello $(ss) $(nn)

hw currentHw :
	@echo "hw3 :"
	$(java) $(rflags) -classpath bin Tree23 111 15 0

# ================================================
# HOUSEKEEPING
# ================================================
e edit g gvim:
	$(gvim) $(p).java &

m makefile:
	$(gvim) Makefile &

clean:
	-rm -f bin/* .*~ *.class *~ src/*~  src/.*

v vclean: clean
	-rm -r bin

# Create zip file "hwXXX-YYY.zip" (e.g., "hw0-yap.zip") one level up:
zip:	vclean
	-test -f ../hw$(hwNumber)-$(myName).zip && \
			rm ../hw$(hwNumber)-$(myName).zip
	zip ../hw$(hwNumber)-$(myName).zip \
	 ../hw$(hwNumber)-$(myName)/Hello.java \
	 ../hw$(hwNumber)-$(myName)/Hello1.java \
	 ../hw$(hwNumber)-$(myName)/README \
	 ../hw$(hwNumber)-$(myName)/Makefile \
	 ../hw$(hwNumber)-$(myName)/make-include \
	 ../hw$(hwNumber)-$(myName)/TESTOUTPUT \
	 ../hw$(hwNumber)-$(myName)/Homework3 \
	 ../hw$(hwNumber)-$(myName)/Util.java \
	 ../hw$(hwNumber)-$(myName)/Tree23.java \

# ================================================
# END
# ================================================
