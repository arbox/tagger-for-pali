#NOTE: Input file must contain an empty line at the end. All ouput files contain one, too.

#--initial stuff (importing, opening files)
import random
import codecs #open files in UTF-8
import sys
from optparse import OptionParser #command line options
f_test = codecs.open("test.txt","w", "utf-8")
f_train = codecs.open("train.txt","w", "utf-8")


def writeFiles(corpus_filename, test_size,):
	#reads corpus file (user choice) into an array
	c_open = codecs.open(corpus_filename, "r", "utf-8")
	allLines = c_open.readlines()
	c_open.close()

        #number of lines needed in test corpus: useless since it's the sentences that are important?!
        #c_length = len(codecs.open(corpus_filename, "r", "utf-8").readlines())
        #testLines = (test_size / 100.0) * float(c_length)

        #satzweise einlesen
        sentence = []
        allSentences = []

        for i in allLines:
                if i != '\r\n': #zeile enthält buchstaben --> wie werden satzgrenzen aussehen?
                        sentence.append(i)
                        #print("neu: " + str(sentence))
                else:
                        sentence.append(i) #leerzeile mit rein
                        allSentences.append(sentence)
                        sentence = []
                        #print("satz neu: " + str(allSentences))

        print(len(allSentences))
        print(" ")

        #size for test corpus
        testSize = (float(test_size) / 100.0) * float(len(allSentences)) #not optimal since sentences vary in length
        #--trainLines must be int --> gets rounded
        testSize2INT = int(testSize)
        testSizeRound = 0
        if (testSize < (float(testSize2INT) + 0.5)):
                testSizeRound = testSize2INT
                print("Abgerundeter Input:" + str(testSizeRound)) #debug; number of sentences in future test corpus
        elif (testSize > testSize2INT):
           testSizeRound = testSize2INT+1
           print("Aufgerundeter Input:" + str(testSizeRound)) #debug; number of sentences in future test corpus


        #--create test corpus in a random loop
        rdCounter = testSizeRound
        random.seed() #initialize random function
        while rdCounter != 0:
                rdNum = random.randint(0, len(allSentences) - 1) #pick number between 1 and number of sentences in corpus
                transport = allSentences[rdNum]
                allSentences.pop(rdNum)
                print("random#: " + str(rdNum) + ", Element: " + str(transport)) #debug
                #transport wieder in Zeilen wandeln
                for elem in transport:
                        f_test.write(str(elem))
                rdCounter = rdCounter - 1
        print(str(allSentences))
        #--create train corpus from remaining elements in allLines
        while len(allSentences) != 0:
                transport = allSentences.pop()
                print("Transport-Item: " + str(transport)) #debug
                #transport wieder in Zeilen wandeln
                for elem in transport:
                        f_test.write(str(elem))

        print("Corpus successfully split into train and test corpora.")
        f_train.close()
        f_test.close()

#commandlineoptions
parser = OptionParser()
parser.add_option("-c", "--corpus", dest="c", metavar="FILE",
                  help="choose a corpus you want to split")
parser.add_option("-p", "--test-corpus-size", dest="p", metavar="NUMBER",
                  help="desired size of test corpus in per cent")

(options, args) = parser.parse_args()

if not options.c or not options.p:
        parser.print_help()
        sys.exit(0)
        pass

corpus_filename = options.c
test_size = options.p


writeFiles(corpus_filename, test_size)
