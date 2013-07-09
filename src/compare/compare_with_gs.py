from tagcombination import TagCombination
from word import Word
import codecs #open files in UTF-8

#Part 1: read gs, create dictionary (hashtable) 
gs_long = codecs.open("pali-goldstandard2.csv", "r", "utf-8")
allLines = gs_long.readlines()

words = {}

for line in allLines:
        splitValues = line.split("\t")

        #filter waste lines
        if splitValues[0] == 'text' or len(splitValues) < 9: 
                #print splitValues
                continue
        
        if splitValues[0] in words:
                word = words[splitValues[0]]
        else:
                word = Word(splitValues[0]) #create Word object
                words[splitValues[0]] = word 

        tagComb = TagCombination(splitValues[1], splitValues[2], splitValues[3],
                                 splitValues[4], splitValues[5], splitValues[6],
                                 splitValues[7], splitValues[8])

        word.add(tagComb) #create connection between word and tag combination

gs_long.close()

#Part 2: compare tagger output file with gs
tagger_output = codecs.open("dummy_compare.csv", "r", "utf-8")
allLines = tagger_output.readlines()
wordNotFoundCounter = 0
combinationFoundCounter = 0
combinationNotFoundCounter = 0

for line in allLines:
        splitValues = line.split("\t")
        #print splitValues
                
        if splitValues[0] in words:
                word = words[splitValues[0]]
                tagComb = TagCombination(splitValues[1], splitValues[2], splitValues[3], splitValues[4],
                                         splitValues[5], splitValues[6], splitValues[7], splitValues[8])
                if word.hasTagCombination(tagComb):
                        combinationFoundCounter += 1
                else:
                        combinationNotFoundCounter += 1
                
        else:
                wordNotFoundCounter += 1
                print splitValues[0] 

        word.add(tagComb)

tagger_output.close()

print "Words not found in gold standard: " + str(wordNotFoundCounter)
print "Wrong tagged words: " + str(combinationNotFoundCounter) #Unknown tag combinations (known word)
print "Correctly tagged words: " + str(combinationFoundCounter) #Known tag combinations

