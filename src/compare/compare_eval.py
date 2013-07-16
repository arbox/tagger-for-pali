#Part 1: read gs, create dictionary (hashtable)
def compare(tagger_output, test):
        taggerFile = codecs.open(tagger_output, "r", "utf-8")
        taggerLines = taggerFile.readlines()

        testFile = codecs.open(test, "r", "utf-8")
        testLines = testFile.readlines()

        tagNotFoundCounter = 0
        combinationFoundCounter = 0
        #combinationNotFoundCounter = 0
        
        for line in taggerLines:
                splitValues = line.split("\t")
                                
                #filter waste lines (should be superfluous by now)
                if splitValues[0] == 'text' or len(splitValues) < gold_columns: 
                        #print splitValues
                        continue
                comp = testLines[line]
                if splitValues[1] == comp[1]
					combinationFoundCounter = combinationFoundCounter + 1
                else tagNotFoundCounter = tagNotFoundCounter + 1 		

		precision = float(combinationFoundCounter) / float((combinationFoundCounter + tagNotFoundCounter))
		recall = combinationFoundCounter
		FScore = 2 * ((precision * recall) / (precision + recall))


        print "Lines equals gold standard: " + str(combinationFoundCounter)
        print "Tags/Words not found: " + str(tagNotFoundCounter)
        print "Precision: " + str(precision)
        print "Recall: " + str(recall)
        print "F score: "  + str(FScore)
#end of definition


# Command Line Options
parser = OptionParser()
parser.add_option("-o", "--output", dest="o",
                  metavar="FILE", help="tagger output filename")
parser.add_option("-t", "--test-file", dest="t",
                  metavar="FILE", help="test corpus filename")

(options, args) = parser.parse_args()

if not options.t or not options.o:
        parser.print_help()
        sys.exit(0)
        pass

#user input: Name gold standard and tagger output to compare
test_filename = options.t
tagger_output_filename = options.o

compare(tagger_output_filename, test_filename)

