JAVAC = javac
JAVACFLAGS =
SRC= Nodes.java \
     DistanceVector_UDP.java

CLS= $(SRC:.java=.class)

all:  $(CLS)


.SUFFIXES : .class .java

.java.class :
	$(JAVAC) $(JAVACFLAGS) $<
