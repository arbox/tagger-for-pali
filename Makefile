ANT := ant

SRC_DIR 	:= "src/"
TAGGER_DIR 	:= "tagger/"
COMPARE_DIR := "compare/"

.DEFAULT:
	make-all


make-all:
	make-tagger
	make-compare

make-tagger:
	cd $(SRC_DIR)$(TAGGER_DIR)
	ant build.xml
