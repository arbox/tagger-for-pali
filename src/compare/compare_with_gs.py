from tagcombination import TagCombination
from word import Word
import codecs #open files in UTF-8
import sys
from optparse import OptionParser #command line options


# creates dictionary from gold standard (Part 1)
# compares it with tagger output (Part 2)
def goldstandard(tagger_output_filename, gold_standard_filename, gold_columns, tagger_columns):

        #Part 1: read gs, create dictionary (hashtable) 
        gs_long = codecs.open(gold_standard_filename, "r", "utf-8")
        allLines = gs_long.readlines()

        words = {}

        for line in allLines:
                splitValues = line.split("\t")

                #filter waste lines
                if splitValues[0] == 'text' or len(splitValues) < gold_columns: 
                        #print splitValues
                        continue
                
                if splitValues[0] in words:
                        word = words[splitValues[0]]
                else:
                        word = Word(splitValues[0]) #create Word object
                        words[splitValues[0]] = word 


                default = [None,None,None,None,None,None,None,None,None] #max. number of columns is 9
                for i in range(1,len(splitValues)):
                        if i > 9:
                                break  
                        default[i] = splitValues[i].strip('\n\r')
                        
                        
                tagComb = TagCombination(default[1], default[2], default[3],
                                         default[4], default[5], default[6],
                                         default[7], default[8])

                word.add(tagComb) #create connection between word and tag combination

        gs_long.close()

        #Part 2: compare tagger output file with gs
        tagger_output = codecs.open(tagger_output_filename, "r", "utf-8")
        allLines = tagger_output.readlines()
        wordNotFoundCounter = 0
        combinationFoundCounter = 0
        combinationNotFoundCounter = 0

        linenumber = 1
        for line in allLines:
                splitValues = line.split("\t")
                #print splitValues
                        
                if splitValues[0] in words:
                        word = words[splitValues[0]]

                        default = [None,None,None,None,None,None,None,None,None]
                        for i in range(1,len(splitValues)):
                                if i > 9:
                                      break
                                
                                default[i] = splitValues[i].strip('\n\r')
                        
                

                        tagComb = TagCombination(default[1], default[2], default[3],
                                         default[4], default[5], default[6],
                                         default[7], default[8])

                        #print tagComb.__dict__

                        if word.hasTagCombination(tagComb):
                                combinationFoundCounter += 1
                        else:
                                combinationNotFoundCounter += 1
                                print "Combination not found in line "+ str(linenumber) +": " + splitValues[0].encode(sys.stdout.encoding, errors='replace')
                                #print str(tagComb.__dict__).encode(sys.stdout.encoding, errors='replace')
                        
                else:
                        wordNotFoundCounter += 1
                        print "Word not found in line "+ str(linenumber) +": " + splitValues[0].encode(sys.stdout.encoding, errors='replace')

                word.add(tagComb)
                linenumber  += 1
                precision = float(combinationFoundCounter) / float((linenumber - wordNotFoundCounter))
                recall = float(combinationFoundCounter) / float(linenumber)
                FScore = 2 * ((precision * recall) / (precision + recall))
        tagger_output.close()

        print "Words not found in gold standard: " + str(wordNotFoundCounter)
        print "Wrong tagged words: " + str(combinationNotFoundCounter) #Unknown tag combinations (known word)
        print "Correctly tagged words: " + str(combinationFoundCounter) #Known tag combinations
        print "Precision: " + str(precision)
        print "Recall: " + str(recall)
        print "F score: "  + str(FScore)
#end of definition



# Command Line Options
parser = OptionParser()
parser.add_option("-t", "--tagger-output", dest="t",
                  metavar="FILE", help="tagger output filename")
parser.add_option("-g", "--gold-standard", dest="g",
                  metavar="FILE", help="gold-standard filename")

(options, args) = parser.parse_args()

if not options.g or not options.t:
        parser.print_help()
        sys.exit(0)
        pass

#user input: Name gold standard and tagger output to compare
gold_standard_filename = options.g
tagger_output_filename = options.t
gs_columns = len(codecs.open(gold_standard_filename, "r", "utf-8").readline().split("\t"))
tagger_columns = len(codecs.open(tagger_output_filename, "r", "utf-8").readline().split("\t"))

goldstandard(tagger_output_filename, gold_standard_filename, gs_columns, tagger_columns)
