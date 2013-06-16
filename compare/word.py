class Word:

        def __init__(self, word):
                self.word = word
                self.tagCombinations = []

        def add(self, tagCombination):
                self.tagCombinations.append(tagCombination)

        #search the needle (TagCombination) in the haystack (TagCombinations of
        #current word)
        def hasTagCombination(self, needle):
                found = False
                for tagCombination in self.tagCombinations:
                        if(tagCombination == needle):
                                found = True
                                break
                return found
        
        
