GRAMMARS := $(shell ls *.g4)

MAIN_CLASS := $(subst .g4,Main.class,$(GRAMMARS))
LEXER_CLASS := $(subst .g4,Lexer.class,$(GRAMMARS))
PARSER_CLASS := $(subst .g4,Parser.class,$(GRAMMARS))
LISTENER_CLASS := $(subst .g4,Listener.class,$(GRAMMARS))
BASELISTENER_CLASS := $(subst .g4,BaseListener.class,$(GRAMMARS))

AUTO_CLASSES := \
	$(LEXER_CLASS) \
	$(PARSER_CLASS) \
	$(BASELISTENER_CLASS)

.SECONDARY: $(subst .class,.java,$(AUTO_CLASSES) $(MAIN_CLASS))

%Parser.java %Lexer.java %Listener.java %BaseListener.java: %.g4
	antlr4 $^

%Main.java:
	@echo "Generate file \"$@\""
	@read -p "Grammar entry rule? " rule; \
	while true; do \
	    read -p "Use listeners? " yn; \
	    case $$yn in \
		[Yy]* ) read -p "Listeners's class? " class; \
			rule="$$rule -l $$class"; \
			warning="Don't forget to change the Makefile to include a dependency from class $$class\n"; \
			break;; \
		[Nn]* ) break;; \
		* ) echo "Please answer yes or no";; \
	    esac; \
	done; \
	while true; do \
	    read -p "Use visitors? " yn; \
	    case $$yn in \
		[Yy]* ) read -p "Visitor's class? " class; \
			rule="$$rule -v $$class"; \
			warning="Don't forget to change the Makefile to include a dependency from class $$class\n"; \
			break;; \
		[Nn]* ) break;; \
		* ) echo "Please answer yes or no";; \
	    esac; \
	done; \
	antlr4-main $* $$rule \

%.class: %.java
	javac $*.java

main:  $(AUTO_CLASSES) $(MAIN_CLASS)

clean:
	antlr4-clean

deep-clean:
	-antlr4-clean
	-rm $(subst .class,.java,$(AUTO_CLASSES))
