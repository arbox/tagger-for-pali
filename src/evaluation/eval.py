#!/usr/bin/env python3

import csv
import argparse

def main():
    parser = argparse.ArgumentParser(description='Splits a corpus into a test and a training corpus')
    parser.add_argument("infile1", metavar="FILE", help="The the reference test corpus", nargs=1, type=str)
    parser.add_argument("infile2", metavar="FILE", help="The the tagged test corpus", nargs=1, type=str)
    args = parser.parse_args()
    fg = open(args.infile1[0], "r", newline='', encoding="utf-8")
    gs = csv.reader(fg, delimiter='\t')

    fo = open(args.infile2[0], "r", newline='', encoding="utf-8")
    out = csv.reader(fo, delimiter='\t')

    lines = 0
    correct = 0
    tagged = 0
    gsr = next(gs)
    outr = next(out)
    while True:
        try:
            gsr = next(gs)
            outr = next(out)
        except:
            break
        if gsr == []:
            continue
        lines += 1
        if gsr[0] == outr[0] and gsr[1] == outr[1]:
            correct += 1
        if outr[1] != 'NoTagFound':
            tagged += 1

    wrong = tagged - correct
    recall = correct/lines
    precission = correct/tagged
    fallout = wrong/tagged
    f1measure = ((recall*precission)+(recall*precission))/(recall+precission)

    print("Lines:   %d" % lines)
    print("Tagged:  %d" % tagged)
    print("Correct: %d" % correct)
    print("Wrong:   %d" % wrong)

    print("--------------------------")

    print("Recall:     %.5f (%3.0f%%)" % (recall, recall*100))
    print("Precission: %.5f (%3.0f%%)" % (precission, precission*100))
    print("Fallout:    %.5f (%3.0f%%)" % (fallout, fallout*100))
    print("F1Measure:  %.5f (%3.0f%%)" % (f1measure, f1measure*100))

if __name__ == "__main__":
    main()

