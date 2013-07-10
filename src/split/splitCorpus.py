#NOTE: Input file must contain an empty line at the end. All ouput files contain one, too.
import random
import os
import os.path
import argparse

def generateSplit(corpus, test_size):
	allSentences = list()
	testSentences = list()
	sentence = list()
	
	#satzweise einlesen
	for line in corpus:
		if line != os.linesep: #zeile enthält buchstaben --> wie werden satzgrenzen aussehen?
			sentence.append(line)
		else:
			sentence.append(line)
			allSentences.append(sentence)
			sentence = list()
			
	sentence.append(os.linesep)
	allSentences.append(sentence)
	
	print("Corpus length: %d sentences" % len(allSentences))
	
	#size for test corpus
	testSize = (float(test_size) / 100.0) * float(len(allSentences)) #not optimal since sentences vary in length
	testSize = int(round(testSize))

	#--create test corpus from random sentences
	random.seed()
	for i in range(testSize):
		rdNum = random.randint(0, len(allSentences) - 1) #pick number between 1 and number of sentences in corpus
		testSentences.append(allSentences.pop(rdNum))

	print("Corpus successfully split into train and test corpora.")
	
	return (testSentences, allSentences)

def writeSentences(sentences, outfile):
		#Write first sentence without seperator
		del sentences[-1][-1]
			
		for sentence in sentences[1:]:
			for elem in sentence:
				outfile.write(elem)

def main():
	#commandlineoptions
	parser = argparse.ArgumentParser(description='Splits a corpus into a test and a training corpus')
	parser.add_argument("infile", metavar="FILE", help="The corpus you want to split", nargs=1, type=str)
	parser.add_argument("-p", "--test-corpus-size",  metavar="NUMBER", default=10, type=int, help="Desired size of test corpus in per cent (default: 10)")
	parser.add_argument("-o", "--outdir",  metavar="DIR", default=".", type=str, help="Output directory for the test and the training corpus")
	parser.add_argument("--test-name",  metavar="FILE", default="test.csv", type=str, help="Name of the file for the test corpus (default: test.csv")
	parser.add_argument("--train-name",  metavar="FILE", default="train.csv", type=str, help="Name of the file for the training corpus (default: train.csv")

	args = parser.parse_args()
	
	testCorpus = list()
	trainCorpus = list()

	with open(args.infile[0], "r", encoding="utf-8") as corpus:
		(testCorpus, trainCorpus) = generateSplit(corpus, args.test_corpus_size)
		
	with open(os.path.join(args.outdir, args.test_name),"w", encoding="utf-8") as test:
		writeSentences(testCorpus, test)
		
	with open(os.path.join(args.outdir, args.train_name),"w", encoding="utf-8") as train:
		writeSentences(trainCorpus, train)
		

if __name__ == "__main__":
	main()