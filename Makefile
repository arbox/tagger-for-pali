ANT := ant
MAKE := make

BASE_DIR        := $(abspath .)

BIN_DIR         := $(addprefix $(BASE_DIR)/, bin)
SRC_DIR         := $(addprefix $(BASE_DIR)/, src)
TAGGER_DIR      := $(addprefix $(SRC_DIR)/, tagger)
COMPARE_DIR     := $(addprefix $(SRC_DIR)/, compare)

.DEFAULT:
	$(MAKE) all
all:
	$(MAKE) clean
	$(MAKE) prepare
	$(MAKE) tagger
	$(MAKE) compare

prepare:
	mkdir -p $(BIN_DIR)
	
tagger:
	$(ANT) -f $(TAGGER_DIR)/build.xml
	cp $(TAGGER_DIR)/build/tagger.jar $(BIN_DIR)/tagger.jar

compare:
	echo "To be implemented"
        
clean:
	$(ANT) -f $(TAGGER_DIR)/build.xml clean
	rm -Rf $(BIN_DIR)