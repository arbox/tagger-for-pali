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
                correct = True
                if other.pos != self.pos: #pos is required
                        return False
                elif self.case is not None and other.case is not None and other.case != self.case:
                        return False
                elif self.number is not None and other.number is not None and other.number != self.number:
                        return False
                elif self.gender is not None and other.gender is not None and other.gender != self.gender:
                        return False
                elif self.person is not None and other.person is not None and other.person != self.person:
                        return False
                elif self.time is not None and other.time is not None and other.time != self.time:
                        return False
                elif self.mode is not None and other.mode is not None and other.mode != self.mode:
                        return False
                elif self.genusVerbi is not None and other.genusVerbi is not None and other.genusVerbi != self.genusVerbi:
                        return False
                else:
                        return True
                
                #return self.__dict__ == other.__dict__

        
