JAVAC = javac
JAVACFLAGS =
SRC= Nodes.java \
     Links.java \
     Cost.java \
     linkstate.java

CLS= $(SRC:.java=.class)

all:  $(CLS)


.SUFFIXES : .class .java

.java.class :
	$(JAVAC) $(JAVACFLAGS) $<
