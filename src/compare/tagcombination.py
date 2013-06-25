class TagCombination:

        def __init__(self, pos, case = None,
                     number = None, gender= None,
                     person = None, time = None,
                     mode = None, genusVerbi = None):
                self.pos = pos
                self.case = case
                self.number = number
                self.gender = gender
                self.person = person
                self.time = time
                self.mode = mode
                self.genusVerbi = genusVerbi

        def __eq__(self, other):
                #self.__dict___ --> all attributes of object
                #as dict (associative array)
                return self.__dict__ == other.__dict__

        
