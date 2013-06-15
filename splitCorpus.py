#TODO: ask user for input file and where to store output files (and name those)
#NOTE: Input file must contain an empty line at the end. All ouput files contain one, too.

#--initial stuff (importing, opening files)
import random
random.seed(None) #initialize random method as truly random (based on current system time)
f_in = open("dummy.txt", "r") #open corpus file
f_train = open("mug.txt", "w") #create mug file, w = write-Methode
f_test = open("test.txt", "w")

#--calculate needed values/ user input
allLines = f_in.readlines() #create list with all lines in corpus
numberOfLines = len(allLines) #gives our threshold for random function
trainPercent = input("Wie gross soll das Trainingskorpus sein? (Angabe in ganzen Prozent):")
trainLines = (trainPercent / 100.0) * float(numberOfLines)#gives us the number of lines the training corpus needs
print trainLines #debug

#--trainLines must be int --> gets rounded
trainLines2INT = int(trainLines)
trainLinesRound = 0
if (trainLines < (float(trainLines2INT) + 0.5)):
        trainLinesRound = trainLines2INT
        print("Abgerundeter Input:" + str(trainLinesRound)) #debug
elif (trainLines > trainLines2INT):
   trainLinesRound = trainLines2INT+1
   print("Aufgerundeter Input:" + str(trainLinesRound)) #debug

#--create train corpus in a random loop
rdCounter = trainLinesRound
while rdCounter != 0:
        rdNum = random.randint(0, len(allLines) - 1) #pick number between 1 and number of lines in input corpus
        transport = allLines[rdNum]
        allLines.pop(rdNum)
        print("random#: " + str(rdNum) + ", Element: " + transport) #debug
        f_train.write(transport)
        rdCounter = rdCounter - 1

#--create test corpus from remaining elements in allLines
while len(allLines) != 0:
        transport = allLines.pop()
        print("Transport-Item: " + transport)
        f_test.write(transport)

f_in.close()
f_train.close()
f_test.close()
